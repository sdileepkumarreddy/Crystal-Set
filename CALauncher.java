package edu.neu.csye6200.ca;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

import net.java.dev.designgridlayout.*;



public class CALauncher extends CAApp {

	private CACrystalSet caCrystalSet;
	private static Logger log;
	protected static JPanel mainPanel,sideMenuPanel,bottomStatusDisplayPanel,eastPanel;
	private DesignGridLayout sideMenuLayout;

	//Components
	protected static JLabel title = null;
	protected static JLabel noOfRows = null;
	protected static JLabel noOfColumns = null;
	protected static JLabel rulesLabel = null;
	protected static JLabel sleepTime = null;
	protected static JLabel generations = null;
	protected static JLabel rulesDescription = null;
	protected static JLabel statusText = null;
	protected static JComboBox<RuleNames> rulesDropDown = null;
	protected static JComboBox<Integer> sleepTimeDropDown = null;
	protected static JComboBox<Integer> generationsDropDown = null;
	protected static JButton startBtn = null;
	protected static JButton pauseBtn = null;
	protected static JButton resetBtn = null;
	protected static JButton rewindBtn = null;
	protected static JButton createButton = null;
	protected static JButton viewRulesDescription = null;
	protected static JLabel genCount = null;
	protected static JLabel lblStatus = null;
	protected static JRadioButton lightMode,darkMode,grid,noGrid;
	protected static boolean isLightMode = true;
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 600;
	private static final int BUTTONS_HEIGHT = 80;

	public CALauncher() {
		CustomLogger cl = new CustomLogger(CALauncher.class.getName());
		log = cl.getLoggerAndFileHandler();
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT); // initial Frame size
		frame.setTitle("Crystal Automata");
		frame.setVisible(true);
		
		lblStatus.setText("Cellular Automata Crystal growth has been launched!!");
	}

	public static void main(String[] args) {
		new CALauncher();
		log.info("Crystal Automata Simulation has been launched");

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public JPanel getMainPanel() {

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		/*
		 * call to initialize and get the north Panel which contains all the user
		 * interaction items
		 */
		mainPanel.setBackground(Color.white);
		mainPanel.add(BorderLayout.WEST, getSideMenuPanel());
		mainPanel.add(BorderLayout.SOUTH, getBottomMenuPanel());
		mainPanel.add(BorderLayout.EAST, getEastMenuPanel());
		mainPanel.setVisible(true);
		return mainPanel;
	}

	public JPanel getSideMenuPanel() {
		sideMenuPanel = new JPanel();
		sideMenuLayout = new DesignGridLayout(sideMenuPanel);
		sideMenuPanel.setBackground(Color.white);

		sidePanelEmptySpaceBetweenRows(7);
		title = new JLabel("Crystal Automata App"); //title
		title.setForeground(Color.black);
		sideMenuLayout.row().center().add(title);
		//loadInputRowsAndColumns();
		loadLightDarkRadioButtons();
		loadRules();
		loadSleepTimeAndGenerations();
		loadGridRadioButtons();
		loadCreateButton();
		loadSimulationButtons();
		return sideMenuPanel;
	}

	private JPanel getBottomMenuPanel() {
		bottomStatusDisplayPanel = new JPanel();
		bottomStatusDisplayPanel.setBackground(Color.white);
		lblStatus = new JLabel("", 10);
		bottomStatusDisplayPanel.add(lblStatus);
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
		final RuleNames rulesNames[] = { RuleNames.rule1, RuleNames.rule2,RuleNames.rule3};
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

			if (rule.compareTo(RuleNames.rule1) == 0) {
				initAliveCell = Math.round(cols / 2);
			}
			//mainPanel.setBackground(Color.white);
			CACrystal caCrystal = new CACrystal(rule, rows, cols, initAliveCell);
			caCrystalSet = new CACrystalSet(caCrystal, maximumGen, sleepTime);

			// maRegionSet.setLayout(new BoxLayout(maRegionSet, BoxLayout.Y_AXIS));
			caCrystalSet.setLayout(new BorderLayout());
			// Setting preferred Sizes
			caCrystalSet.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));
			caCrystalSet.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));
			startBtn.setEnabled(true);
			resetBtn.setEnabled(true);
			createButton.setEnabled(false);
			//			rowsDropDown.setEnabled(false);
			//			columnDropDown.setEnabled(false);
			generationsDropDown.setEnabled(false);
			sleepTimeDropDown.setEnabled(false);
			rulesDropDown.setEnabled(false);
			mainPanel.setBackground(isLightMode? Color.white : Color.black);
			mainPanel.add(BorderLayout.CENTER, caCrystalSet);
			frame.setVisible(true);
		} else if ("Start".equals(ae.getActionCommand())) { 
			startBtn.setEnabled(false);
			pauseBtn.setEnabled(true);
			resetBtn.setEnabled(true);
			caCrystalSet.nextCrystal();

		} else if ("Pause".equals(ae.getActionCommand())) { // Pause button event

			pauseBtn.setEnabled(false);

			rewindBtn.setEnabled(true);
			startBtn.setEnabled(true);
			resetBtn.setEnabled(true);
			caCrystalSet.pauseThread();
		} else if ("Rewind".equals(ae.getActionCommand())) {

			rewindBtn.setEnabled(false);
			startBtn.setEnabled(false);
			pauseBtn.setEnabled(true);
			resetBtn.setEnabled(true);

			// Inform the thread to rewind the simulation
			caCrystalSet.rewindCrystal();

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
		}
		else if ("Light Mode".equals(ae.getActionCommand())) { // Stop button event
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

	}
	
	private void sidePanelEmptySpaceBetweenRows(int count)
	{
		for (int i = 0; i < count; i++) {
			sideMenuLayout.row().center().add(new JLabel(""));
		}
	}
}
