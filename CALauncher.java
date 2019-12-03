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

	// Initialize UI Components and other parameters
	protected static JPanel mainPanel,sideMenuPanel,bottomStatusDisplayPanel,eastPanel;
	protected static JLabel title,noOfRows,noOfColumns,rulesLabel,sleepTime,generations,rulesDescription,statusText,genCount,statusDisplayText;
	protected static JComboBox<RuleNames> rulesDropDown;
	protected static JComboBox<Integer> sleepTimeDropDown,generationsDropDown;
	protected static JButton startBtn,pauseBtn,resetBtn,rewindBtn,createButton,viewRulesDescription;
	protected static JRadioButton lightMode,darkMode,grid,noGrid;
	protected static boolean isLightMode = true;
	protected static boolean isGridMode = true;
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 600;

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

	public static void main(String[] args) {
		new CALauncher();
		log.info("Cellular Automata Crystal Growth App has been launched!!");

	}

	//Implements getMainPanel() abstract method from CAApp and loads all the panels
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

	/*
	 * Returns the side menu panel which initializes all the UI components required for simulation
	 * Used the open source DesignGridLayout for the creation of grid for better look and feel
	 */
	public JPanel getSideMenuPanel() {
		sideMenuPanel = new JPanel();
		sideMenuLayout = new DesignGridLayout(sideMenuPanel);
		sideMenuPanel.setBackground(Color.white);
		sidePanelEmptySpaceBetweenRows(7);
		title = new JLabel("Crystal Automata App");
		title.setForeground(Color.black);
		sideMenuLayout.row().center().add(title);
		loadLightDarkRadioButtons();
		loadRules();
		loadSleepTimeAndGenerations();
		loadGridRadioButtons();
		loadCreateButton();
		loadSimulationButtons();
		return sideMenuPanel;
	}

	//Returns bottom menu panel which displays status information
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

	private void loadRules(){
		sidePanelEmptySpaceBetweenRows(3);
		rulesLabel = new JLabel("Select Rules");
		rulesLabel.setForeground(Color.BLACK);
		rulesDescription = new JLabel("Rules Description");
		rulesDescription.setForeground(Color.BLACK);
		sideMenuLayout.row().center().add(rulesLabel,rulesDescription);
		final RuleNames rulesNames[] = { RuleNames.SingleSnowflake, RuleNames.RandomSnowFlakes,RuleNames.rule3, RuleNames.rule4};
		rulesDropDown = new JComboBox<RuleNames>(rulesNames);
		rulesDropDown.setMaximumRowCount(3);
		rulesDropDown.setEditable(false);
		rulesDropDown.addActionListener(this);

		viewRulesDescription = new JButton("View");
		viewRulesDescription.addActionListener(this);
		sideMenuLayout.row().center().add(rulesDropDown,viewRulesDescription);
	}

	public void loadLightDarkRadioButtons() {

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

	private void loadSleepTimeAndGenerations() {
		sidePanelEmptySpaceBetweenRows(3);
		sleepTime = new JLabel("Sleep Time(Secs)");
		sleepTime.setForeground(Color.BLACK);
		generations = new JLabel("Generations");
		generations.setForeground(Color.BLACK);
		sideMenuLayout.row().center().add(sleepTime,generations);
		final Integer rows[] = { 1000 };
		sleepTimeDropDown = new JComboBox<Integer>(rows);
		sleepTimeDropDown.setMaximumRowCount(3);
		sleepTimeDropDown.setEditable(false);
		sleepTimeDropDown.addActionListener(this);

		final Integer cols[] = { 10, 20, 30, 40, 60};
		generationsDropDown = new JComboBox<Integer>(cols);
		generationsDropDown.setMaximumRowCount(5);
		generationsDropDown.setEditable(false);
		generationsDropDown.addActionListener(this);
		sideMenuLayout.row().center().add(sleepTimeDropDown,generationsDropDown);
	}

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

	private void loadCreateButton() {
		sidePanelEmptySpaceBetweenRows(3);
		createButton = new JButton("Create");
		createButton.addActionListener(this);
		sideMenuLayout.row().center().add(createButton);

	}
	private void loadSimulationButtons() {
		sidePanelEmptySpaceBetweenRows(7);
		Image play,pause,rewind,stop;
		try {
			play = ImageIO.read(getClass().getResource("/icons/Play.png"));
			pause = ImageIO.read(getClass().getResource("/icons/Pause.png"));
			rewind = ImageIO.read(getClass().getResource("/icons/Rewind.png"));
			stop = ImageIO.read(getClass().getResource("/icons/Stop.png"));
			startBtn = new JButton(new ImageIcon(play));
			startBtn.setToolTipText("Start");
			startBtn.setActionCommand("Start");
			startBtn.addActionListener(this);
			pauseBtn = new JButton(new ImageIcon(pause)); 
			pauseBtn.setToolTipText("Pause");
			pauseBtn.setActionCommand("Pause");
			pauseBtn.addActionListener(this);
			resetBtn = new JButton(new ImageIcon(stop));
			resetBtn.setToolTipText("Stop");
			resetBtn.setActionCommand("Reset");
			resetBtn.addActionListener(this);
			rewindBtn = new JButton(new ImageIcon(rewind));
			rewindBtn.setToolTipText("Rewind");
			rewindBtn.setActionCommand("Rewind");
			rewindBtn.addActionListener(this);
			startBtn.setEnabled(false);
			pauseBtn.setEnabled(false);
			resetBtn.setEnabled(false);
			rewindBtn.setEnabled(false);

			sideMenuLayout.row().center().add(startBtn);
			sidePanelEmptySpaceBetweenRows(2);
			sideMenuLayout.row().center().add(pauseBtn,rewindBtn);
			sidePanelEmptySpaceBetweenRows(2);
			sideMenuLayout.row().center().add(resetBtn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent ae) {

		if ("Create".equals(ae.getActionCommand())) { // Create event handler

			// Initialization

			if (Arrays.asList(mainPanel.getComponents()).contains(caCrystalSet))
				mainPanel.remove(caCrystalSet);

			int rows = 94;
			int cols = 125;
			RuleNames rule = (RuleNames) rulesDropDown.getSelectedItem();
			int maximumGen = (int) generationsDropDown.getSelectedItem();
			int sleepTime = (int) sleepTimeDropDown.getSelectedItem();

			int middleCell = Math.round((rows * cols) / 2);
			int initAliveCell = middleCell - Math.round(cols / 2);

			if (rule.compareTo(RuleNames.SingleSnowflake) == 0) {
				initAliveCell = Math.round(cols / 2);
			}
			//mainPanel.setBackground(Color.white);
			CACrystal caCrystal = new CACrystal(rule, rows, cols, initAliveCell);
			caCrystalSet = new CACrystalSet(caCrystal, maximumGen, sleepTime);
			caCrystalSet.setLayout(new BorderLayout());
			startBtn.setEnabled(true);
			resetBtn.setEnabled(true);
			createButton.setEnabled(false);
			generationsDropDown.setEnabled(false);
			sleepTimeDropDown.setEnabled(false);
			rulesDropDown.setEnabled(false);
			lightMode.setEnabled(false);
			darkMode.setEnabled(false);
			grid.setEnabled(false);
			noGrid.setEnabled(false);
			mainPanel.setBackground(isLightMode? Color.white : Color.black);
			mainPanel.add(BorderLayout.CENTER, caCrystalSet);
			frame.setVisible(true);
			statusDisplayText.setText("Crystal Autamata region created successfully!!");
		} else if ("Start".equals(ae.getActionCommand())) { 
			startBtn.setEnabled(false);
			pauseBtn.setEnabled(true);
			resetBtn.setEnabled(true);
			caCrystalSet.nextCrystal();
			statusDisplayText.setText("Crystal growth has begun!!");

		} else if ("Pause".equals(ae.getActionCommand())) { // Pause button event

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

			// Inform the thread to rewind the simulation
			caCrystalSet.rewindCrystal();
			statusDisplayText.setText("Crystal growth is being rewinded!!");

		} else if ("Reset".equals(ae.getActionCommand())) { // Stop button event

			caCrystalSet.stopThread();
			caCrystalSet.setVisible(false);
			startBtn.setEnabled(false);
			pauseBtn.setEnabled(false);
			resetBtn.setEnabled(false);
			rewindBtn.setEnabled(false);
			createButton.setEnabled(true);
			generationsDropDown.setEnabled(true);
			sleepTimeDropDown.setEnabled(true);
			rulesDropDown.setEnabled(true);
			grid.setEnabled(true);
			noGrid.setEnabled(true);
			lightMode.setEnabled(true);
			darkMode.setEnabled(true);
			statusDisplayText.setText("Crystal growth is stopped!! Hope you enjoyed it");
		}
		else if ("Light Mode".equals(ae.getActionCommand())) { // Stop button event
			isLightMode = true;
			mainPanel.setBackground(Color.white);
			sideMenuPanel.setBackground(Color.white);
			bottomStatusDisplayPanel.setBackground(Color.white);
			eastPanel.setBackground(Color.white);
			title.setForeground(Color.black);
			rulesLabel.setForeground(Color.black);
			rulesDescription.setForeground(Color.black);
			sleepTime.setForeground(Color.black);
			generations.setForeground(Color.black);
			frame.setVisible(true);
		}
		else if ("Dark Mode".equals(ae.getActionCommand())) { // Stop button event
			isLightMode = false;
			mainPanel.setBackground(Color.black);
			sideMenuPanel.setBackground(Color.black);
			bottomStatusDisplayPanel.setBackground(Color.black);
			eastPanel.setBackground(Color.black);
			title.setForeground(Color.white);
			rulesLabel.setForeground(Color.white);
			rulesDescription.setForeground(Color.white);
			sleepTime.setForeground(Color.white);
			generations.setForeground(Color.white);
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

	private void viewDescription() {
		CARuleDescription.getInstance();
	}

	private void sidePanelEmptySpaceBetweenRows(int count)
	{
		for (int i = 0; i < count; i++) {
			sideMenuLayout.row().center().add(new JLabel(""));
		}
	}
}
