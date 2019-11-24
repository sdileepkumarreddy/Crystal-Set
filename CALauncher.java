package edu.neu.csye6200.ca;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import net.java.dev.designgridlayout.*;



public class CALauncher extends CAApp {
	
	private CACrystalSet caCrystalSet;
	private static Logger log;
	private JPanel mainPanel;
	private JPanel sideMenuPanel;
	private JPanel bottomStatusDisplayPanel;
	private JPanel playGroundPanel;
	private DesignGridLayout sideMenuLayout;
	
	//Components
	protected JLabel title = null;
	protected JLabel noOfRows = null;
	protected JLabel noOfColumns = null;
	protected JLabel rulesLabel = null;
	protected JLabel sleepTime = null;
	protected JLabel generations = null;
	protected static JComboBox<Integer> rowsDropDown = null;
	protected static JComboBox<Integer> columnDropDown = null;
	protected static JComboBox<RuleNames> rulesDropDown = null;
	protected static JComboBox<Integer> sleepTimeDropDown = null;
	protected static JComboBox<Integer> generationsDropDown = null;
	protected JButton startBtn = null;
	protected JButton pauseBtn = null;
	protected JButton resetBtn = null;
	protected JButton rewindBtn = null;
	protected JButton createButton = null;
	protected static JLabel genCount = null;
	protected static JLabel lblStatus = null;
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 600;
	private static final int BUTTONS_HEIGHT = 80;

	public CALauncher() {
		CustomLogger cl = new CustomLogger(CALauncher.class.getName());
		log = cl.getLoggerAndFileHandler();
		frame.setSize(500, 400); // initial Frame size
		frame.setTitle("Crystal Automata");
		frame.setVisible(true);

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

//	@Override
//	public JPanel getMainPanel() {
//		mainPanel = new JPanel();
//		mainPanel.setLayout(new BorderLayout());
//		CACanvas caC = new CACanvas();
//		 
//	//	mainPanel.add(BorderLayout.CENTER, caC);
//		mainPanel.add(BorderLayout.WEST, getSideMenuPanel());
//	//	mainPanel.add(BorderLayout.SOUTH, bottomStatusDisplayPanel);
//		return mainPanel;
//	}
	
	@Override
	public JPanel getMainPanel() {

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		/*
		 * call to initialize and get the north Panel which contains all the user
		 * interaction items
		 */
		mainPanel.add(BorderLayout.WEST, getSideMenuPanel());
		mainPanel.setVisible(true);
		return mainPanel;
	}
	
	public JPanel getSideMenuPanel() {
		sideMenuPanel = new JPanel();
		sideMenuLayout = new DesignGridLayout(sideMenuPanel);
		sideMenuPanel.setBackground(Color.DARK_GRAY);
		
		
		title = new JLabel("Crystal Automata App"); //title
		title.setForeground(Color.WHITE);
		sideMenuLayout.row().center().add(title);
		loadInputRowsAndColumns();
		loadRules();
		loadSleepTimeAndGenerations();
		loadButtons();
	//	Grid
		return sideMenuPanel;
	}
	
	private void loadInputRowsAndColumns(){

		noOfRows = new JLabel("Rows");
		noOfRows.setForeground(Color.WHITE);
		noOfColumns = new JLabel("Columns");
		noOfColumns.setForeground(Color.WHITE);
		sideMenuLayout.row().center().add(noOfRows,noOfColumns);
		final Integer rows[] = { 40, 60, 80, 100, 120 };
		rowsDropDown = new JComboBox<Integer>(rows);
		rowsDropDown.setMaximumRowCount(5);
		rowsDropDown.setEditable(false);
		rowsDropDown.addActionListener(this);

		final Integer cols[] = { 40, 60, 80, 100, 120 };
		columnDropDown = new JComboBox<Integer>(cols);
		columnDropDown.setMaximumRowCount(5);
		columnDropDown.setEditable(false);
		columnDropDown.addActionListener(this);
		sideMenuLayout.row().center().add(rowsDropDown,columnDropDown);
	}

	private void loadRules(){
		rulesLabel = new JLabel("Rules");
		rulesLabel.setForeground(Color.WHITE);
		sideMenuLayout.row().center().add(rulesLabel);
		final RuleNames rulesNames[] = { RuleNames.rule1, RuleNames.rule2,RuleNames.rule3};
		rulesDropDown = new JComboBox<RuleNames>(rulesNames);
		rulesDropDown.setMaximumRowCount(5);
		rulesDropDown.setEditable(false);
		rulesDropDown.addActionListener(this);
		sideMenuLayout.emptyRow();
		sideMenuLayout.row().center().add(rulesDropDown);
	}
	
	private void loadSleepTimeAndGenerations() {
		sleepTime = new JLabel("Sleep Time(Secs)");
		sleepTime.setForeground(Color.WHITE);
		generations = new JLabel("Generations");
		generations.setForeground(Color.WHITE);
		sideMenuLayout.row().center().add(sleepTime,generations);
		final Integer rows[] = { 40, 60, 80, 100, 120 };
		sleepTimeDropDown = new JComboBox<Integer>(rows);
		sleepTimeDropDown.setMaximumRowCount(5);
		sleepTimeDropDown.setEditable(false);
		sleepTimeDropDown.addActionListener(this);

		final Integer cols[] = { 40, 60, 80, 100, 120 };
		generationsDropDown = new JComboBox<Integer>(cols);
		generationsDropDown.setMaximumRowCount(5);
		generationsDropDown.setEditable(false);
		generationsDropDown.addActionListener(this);
		sideMenuLayout.emptyRow();
		sideMenuLayout.row().center().add(sleepTimeDropDown,generationsDropDown);
	}
	
	private void loadButtons() {
		createButton = new JButton("Create");
		createButton.setEnabled(true);
		createButton.addActionListener(this);
		
		final JButton startBtn = new JButton("Start"); // start button declared final to control from action events
		startBtn.setEnabled(true);
		startBtn.addActionListener(this);
		final JButton pauseBtn = new JButton("Pause"); // start button declared final to control from action events
		pauseBtn.setEnabled(true);
		pauseBtn.addActionListener(this);
		final JButton resetBtn = new JButton("Reset"); // start button declared final to control from action events
		resetBtn.setEnabled(true);
		resetBtn.addActionListener(this);
		final JButton rewindBtn = new JButton("Rewind"); // start button declared final to control from action events
		rewindBtn.setEnabled(true);
		rewindBtn.addActionListener(this);
		sideMenuLayout.emptyRow();
		sideMenuLayout.row().center().add(createButton);
		sideMenuLayout.emptyRow();
		sideMenuLayout.row().center().add(startBtn,pauseBtn);
		sideMenuLayout.emptyRow();
		sideMenuLayout.row().center().add(resetBtn,rewindBtn);
	}
		
	public void actionPerformed(ActionEvent ae) {

		if ("Create".equals(ae.getActionCommand())) { // Create event handler

			// Initialization

			if (Arrays.asList(mainPanel.getComponents()).contains(caCrystalSet))
				mainPanel.remove(caCrystalSet);

			CACell.cellCount = 0;

			int rows = (int) rowsDropDown.getSelectedItem();
			int cols = (int) columnDropDown.getSelectedItem();
			RuleNames rule = (RuleNames) rulesDropDown.getSelectedItem();
			int maximumGen = (int) generationsDropDown.getSelectedItem();
			int sleepTime = (int) sleepTimeDropDown.getSelectedItem();

			int middleCell = Math.round((rows * cols) / 2);
			int initAliveCell = middleCell - Math.round(cols / 2);

			if (rule.compareTo(RuleNames.rule1) == 0) {
				initAliveCell = Math.round(cols / 2);
			}

			CACrystal caCrystal = new CACrystal(rule, rows, cols, initAliveCell);
			caCrystalSet = new CACrystalSet(caCrystal, maximumGen, sleepTime);

			// maRegionSet.setLayout(new BoxLayout(maRegionSet, BoxLayout.Y_AXIS));
			caCrystalSet.setLayout(new BorderLayout());
			// Setting preferred Sizes
			caCrystalSet.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));
			caCrystalSet.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));

	
			caCrystalSet.setBackground(Color.BLACK);
			mainPanel.add(BorderLayout.CENTER, caCrystalSet);
			frame.setVisible(true);
			//lblStatus.setText("Simulation Region Created successfully...");

		} else if ("Start".equals(ae.getActionCommand())) { // Start button event

			caCrystalSet.nextCrystal();

		} else if ("Pause".equals(ae.getActionCommand())) { // Pause button event


		} else if ("Rewind".equals(ae.getActionCommand())) {


		} else if ("Stop".equals(ae.getActionCommand())) { // Stop button event
			lblStatus.setText("Simulation stopped . Thank you for using Mobile Automata ... !!");
			log.info("Simulation stopped . Thank you for using Mobile Automata ... !!");

			genCount.setText("0");
			caCrystalSet.stopThread();
			caCrystalSet.setVisible(false);
		}

	}
}
