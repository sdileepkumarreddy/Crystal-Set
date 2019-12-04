package edu.neu.csye6200.ca;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import net.java.dev.designgridlayout.*;

/*
 * @author: Dileep Reddy 
 * NUID: 001063317
 * Class Description: CALauncher is the starting point to launch the application.
 * 					  It extends from CAApp to demonstrate the property of abstract class
 * 					  It also initializes all the UI components and other necessary UI parameters
 * 					  After the frame is loaded with all UI components and its event listeners it then uses CACrystal 
 * 					  and CACrystalSet to render Crystals and display in the UI
 */

public class CALauncher extends CAApp {

	//Initialize UI Components and other parameters
	protected static JPanel mainPanel,sideMenuPanel,bottomStatusDisplayPanel,eastPanel;
	protected static JLabel title,noOfRows,noOfColumns,rulesLabel,sleepTime,generations,rulesDescription,statusText,genCount,statusDisplayText,genCountLabel,generationText;
	protected static JComboBox<RuleNames> rulesDropDown;
	protected static JComboBox<Integer> sleepTimeDropDown,generationsDropDown;
	protected static JButton startBtn,pauseBtn,resetBtn,rewindBtn,createButton,viewRulesDescription;
	protected static JRadioButton lightMode,darkMode,grid,noGrid;
	protected static boolean isLightMode = true;
	protected static boolean isGridMode = true;
	private static final int FRAME_WIDTH = 1000,FRAME_HEIGHT = 600, rows = 94, cols = 125;

	private CACrystalSet caCrystalSet;
	private static Logger log;
	private DesignGridLayout sideMenuLayout;

	public CALauncher() {
		CustomLogger cl = new CustomLogger(CALauncher.class.getName());  //Custom logger that returns logger along with file handler which is common for all classes
		log = cl.getLoggerAndFileHandler();
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT); // Setting up frame size
		frame.setTitle("Cellular Automata Crystal Growth");
		frame.setVisible(true);
		statusDisplayText.setText("Cellular Automata Crystal Growth App has been launched!! Winter is here!!");
	}

	/*start point of application*/
	public static void main(String[] args) {
		new CALauncher();
		log.info("Cellular Automata Crystal Growth App has been launched!!");

	}

	/*Implements getMainPanel() abstract method from CAApp and loads all the panels*/
	@Override
	public JPanel getMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.white);
		mainPanel.add(BorderLayout.WEST, getSideMenuPanel());
		mainPanel.add(BorderLayout.SOUTH, getBottomMenuPanel());
		mainPanel.add(BorderLayout.EAST, getEastMenuPanel());
		mainPanel.setVisible(true);
		return mainPanel;
	}

	/*Calls every time any action performed on UI and all actions are taken care of individually*/
	public void actionPerformed(ActionEvent ae) {

		if ("Create".equals(ae.getActionCommand())) {
			if (Arrays.asList(mainPanel.getComponents()).contains(caCrystalSet)) // remove crystal sets from older simulation if any
				mainPanel.remove(caCrystalSet);
			handleCreateActionPerformed();
			frame.setVisible(true);
			statusDisplayText.setText("Crystal Autamata region created successfully!!");
		} else if ("Start".equals(ae.getActionCommand())) { 
			startBtn.setEnabled(false);
			pauseBtn.setEnabled(true);
			resetBtn.setEnabled(true);
			caCrystalSet.nextCrystal();
			statusDisplayText.setText("Crystal growth has begun!!");
		} else if ("Pause".equals(ae.getActionCommand())) {
			pauseBtn.setEnabled(false);
			rewindBtn.setEnabled(true);
			startBtn.setEnabled(true);
			resetBtn.setEnabled(true);
			caCrystalSet.pauseThread();
			statusDisplayText.setText("Crystal growth is paused!!");
		} else if ("Rewind".equals(ae.getActionCommand())) {
			rewindBtn.setEnabled(false);
			startBtn.setEnabled(false);
			pauseBtn.setEnabled(true);
			resetBtn.setEnabled(true);
			caCrystalSet.rewindCrystal();
			statusDisplayText.setText("Crystal growth is being rewinded!!");

		} else if ("Stop".equals(ae.getActionCommand())) {
			caCrystalSet.stopThread();
			caCrystalSet.setVisible(false);
			enableOrDisableSimulationButtons(false);
			pauseBtn.setEnabled(false);
			rewindBtn.setEnabled(false);
			generationText.setText("0");
			statusDisplayText.setText("Crystal growth is stopped!! Hope you enjoyed it");
		}
		else if ("Light Mode".equals(ae.getActionCommand())) {
			handleLightAndDarkMode("Light");
			frame.setVisible(true);
		}
		else if ("Dark Mode".equals(ae.getActionCommand())) { // Stop button event
			handleLightAndDarkMode("Dark");
			frame.setVisible(true);
		}
		else if ("View".equals(ae.getActionCommand())) {			
			viewDescription();
		}
		else if ("Grid".equals(ae.getActionCommand())) {			
			isGridMode = true;
		}
		else if ("No Grid".equals(ae.getActionCommand())) {			
			isGridMode = false;
		}

	}

	/*Following are the methods that has to be implemented for extending CAApp which implements windows lister interface*/
	@Override
	public void windowOpened(WindowEvent e) {
		log.info("Window Opened!! Let the Winter Games begin!!");
	}

	@Override
	public void windowClosing(WindowEvent e) {
		log.info("Window Closing!!");
	}

	@Override
	public void windowClosed(WindowEvent e) {
		log.info("Window Closed!! Hope you enjoyed crystal growth!!");
	}

	@Override
	public void windowIconified(WindowEvent e) {
		log.info("Window Iconified!!");
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		log.info("Window DeIconified!!");
	}

	@Override
	public void windowActivated(WindowEvent e) {
		log.info("Window Activated!!");
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		log.info("Window Deactivated!!");
	}

	/*
	 * Returns the side menu panel which initializes all the UI components required for simulation
	 * Used the open source DesignGridLayout for the creation of grid for better look and feel
	 */
	private JPanel getSideMenuPanel() {
		sideMenuPanel = new JPanel();
		sideMenuLayout = new DesignGridLayout(sideMenuPanel);
		sideMenuPanel.setBackground(Color.white);
		sidePanelEmptySpaceBetweenRows(3);
		title = new JLabel("Crystal Automata App");
		title.setForeground(Color.black);
		sideMenuLayout.row().center().add(title);
		loadLightDarkRadioButtons();
		loadRules();
		loadSleepTimeAndGenerations();
		loadGridRadioButtons();
		loadCreateButton();
		loadSimulationButtons();
		sidePanelEmptySpaceBetweenRows(3);
		genCountLabel = new JLabel("Generations : ", 4);
		generationText = new JLabel("0");
		sideMenuLayout.row().center().add(genCountLabel,generationText);
		return sideMenuPanel;
	}
	
	/*Handles creation of crystal and crystalsets after click on create button*/
	private void handleCreateActionPerformed() {
		RuleNames rule = (RuleNames) rulesDropDown.getSelectedItem();
		int maximumGen = (int) generationsDropDown.getSelectedItem();
		int sleepTime = (int) sleepTimeDropDown.getSelectedItem();
		CACrystal caCrystal = new CACrystal(rule, rows, cols);
		caCrystalSet = new CACrystalSet(caCrystal, maximumGen, sleepTime);
		caCrystalSet.setLayout(new BorderLayout());
		enableOrDisableSimulationButtons(true);
		mainPanel.setBackground(isLightMode? Color.white : Color.black);
		mainPanel.add(BorderLayout.CENTER, caCrystalSet);
	}

	/*Displays the description of all the rules implemented*/ 
	private void viewDescription() {
		CARuleDescription.getInstance();
	}

	/*To add customized spaces between each row in side panel*/ 
	private void sidePanelEmptySpaceBetweenRows(int count)
	{
		for (int i = 0; i < count; i++) {
			sideMenuLayout.row().center().add(new JLabel(""));
		}
	}

	/*Return the bottom menu panel which displays the status of the application*/ 
	private JPanel getBottomMenuPanel() {
		bottomStatusDisplayPanel = new JPanel();
		bottomStatusDisplayPanel.setBackground(Color.white);
		statusDisplayText = new JLabel("", 10);
		bottomStatusDisplayPanel.add(statusDisplayText);
		return bottomStatusDisplayPanel;
	}

	
	private JPanel getEastMenuPanel() {
		eastPanel = new JPanel();
		eastPanel.setBackground(Color.white);
		return eastPanel;
	}

	/*loading the rules drop down*/ 
	private void loadRules(){
		sidePanelEmptySpaceBetweenRows(3);
		rulesLabel = new JLabel("Select Rules");
		rulesLabel.setForeground(Color.BLACK);
		rulesDescription = new JLabel("Rules Description");
		rulesDescription.setForeground(Color.BLACK);
		sideMenuLayout.row().center().add(rulesLabel,rulesDescription);
		final RuleNames rulesNames[] = { RuleNames.SingleCrystal, RuleNames.RandomCrystals,RuleNames.Butterfly, RuleNames.Highway, RuleNames.Garden};
		rulesDropDown = new JComboBox<RuleNames>(rulesNames);
		rulesDropDown.setMaximumRowCount(3);
		rulesDropDown.setEditable(false);
		rulesDropDown.addActionListener(this);
		viewRulesDescription = new JButton("View");
		viewRulesDescription.addActionListener(this);
		sideMenuLayout.row().center().add(rulesDropDown,viewRulesDescription);
	}

	/*loading the light and dark mode radio buttons*/ 
	private void loadLightDarkRadioButtons() {
		sidePanelEmptySpaceBetweenRows(3);
		lightMode = new JRadioButton("Light Mode");
		lightMode.addActionListener(this);
		lightMode.setSelected(true);
		darkMode = new JRadioButton("Dark Mode");
		darkMode.addActionListener(this);
		ButtonGroup group = new ButtonGroup();
		group.add(lightMode);
		group.add(darkMode);
		sideMenuLayout.row().center().add(lightMode,darkMode);
	}

	/*loads sleep time and generation count*/ 
	private void loadSleepTimeAndGenerations() {
		sidePanelEmptySpaceBetweenRows(3);
		sleepTime = new JLabel("Sleep Time(msec)");
		sleepTime.setForeground(Color.BLACK);
		generations = new JLabel("Generations");
		generations.setForeground(Color.BLACK);
		sideMenuLayout.row().center().add(sleepTime,generations);
		final Integer sleepTime[] = { 500,750,1000,1200,1500 };
		sleepTimeDropDown = new JComboBox<Integer>(sleepTime);
		sleepTimeDropDown.setMaximumRowCount(3);
		sleepTimeDropDown.setEditable(false);
		sleepTimeDropDown.addActionListener(this);
		final Integer generations[] = { 10, 20, 30, 40, 60};
		generationsDropDown = new JComboBox<Integer>(generations);
		generationsDropDown.setMaximumRowCount(5);
		generationsDropDown.setEditable(false);
		generationsDropDown.addActionListener(this);
		sideMenuLayout.row().center().add(sleepTimeDropDown,generationsDropDown);
	}

	/*loads grid and no grid radio buttons*/ 
	private void loadGridRadioButtons() {
		sidePanelEmptySpaceBetweenRows(3);
		grid = new JRadioButton("Grid");
		grid.addActionListener(this);
		grid.setSelected(true);
		noGrid = new JRadioButton("No Grid");
		noGrid.addActionListener(this);
		ButtonGroup gridGroup = new ButtonGroup();
		gridGroup.add(grid);
		gridGroup.add(noGrid);
		sideMenuLayout.row().center().add(grid,noGrid);	
	}

	/*loads create button*/ 
	private void loadCreateButton() {
		sidePanelEmptySpaceBetweenRows(3);
		createButton = new JButton("Create");
		createButton.addActionListener(this);
		sideMenuLayout.row().center().add(createButton);
	}

	/* loads start pause rewind and stop simulation buttons*/
	private void loadSimulationButtons() {
		sidePanelEmptySpaceBetweenRows(7);
		startBtn = initializeButtons(startBtn ,"Start");
		pauseBtn = initializeButtons(pauseBtn ,"Pause");
		rewindBtn = initializeButtons(rewindBtn ,"Rewind");
		resetBtn = initializeButtons(startBtn ,"Stop");
		sideMenuLayout.row().center().add(startBtn);
		sidePanelEmptySpaceBetweenRows(2);
		sideMenuLayout.row().center().add(pauseBtn,rewindBtn);
		sidePanelEmptySpaceBetweenRows(2);
		sideMenuLayout.row().center().add(resetBtn);
	}

	/*common logic to initialize all the buttons*/ 
	private JButton initializeButtons(JButton btn, String buttonName) {
		String iconPath = "/icons/"+buttonName +".png";
		Image image;
		try {
			image = ImageIO.read(getClass().getResource(iconPath));
			btn = new JButton(new ImageIcon(image));
			btn.setToolTipText(buttonName);
			btn.setActionCommand(buttonName);
			btn.addActionListener(this);
			btn.setEnabled(false);
		} catch (IOException e) {
			log.severe("Exception occured whil loading Images!");
		}
		return btn;
	}

	/*enable or disable simulation buttons for start and stop of simulation*/ 
	private void enableOrDisableSimulationButtons(boolean enable) {
		startBtn.setEnabled(enable);
		resetBtn.setEnabled(enable);
		createButton.setEnabled(!enable);
		generationsDropDown.setEnabled(!enable);
		sleepTimeDropDown.setEnabled(!enable);
		rulesDropDown.setEnabled(!enable);
		grid.setEnabled(!enable);
		noGrid.setEnabled(!enable);
		lightMode.setEnabled(!enable);
		darkMode.setEnabled(!enable);		
	}

	/*select black and white colors according for dark and light mode*/ 
	private void handleLightAndDarkMode(String mode) {
		Color foreColor,backColor;
		if(mode == "Light"){
			isLightMode = true;
			foreColor = Color.black;
			backColor = Color.white;
		}
		else {
			isLightMode = false;
			foreColor = Color.white;
			backColor = Color.black;
		}
		mainPanel.setBackground(backColor);
		sideMenuPanel.setBackground(backColor);
		bottomStatusDisplayPanel.setBackground(backColor);
		eastPanel.setBackground(backColor);
		title.setForeground(foreColor);
		rulesLabel.setForeground(foreColor);
		rulesDescription.setForeground(foreColor);
		sleepTime.setForeground(foreColor);
		generations.setForeground(foreColor);
		genCountLabel.setForeground(foreColor);
		generationText.setForeground(foreColor);
		statusDisplayText.setForeground(foreColor);
	}
}
