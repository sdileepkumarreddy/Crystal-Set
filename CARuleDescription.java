package edu.neu.csye6200.ca;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


//Singleton Pattern
public class CARuleDescription implements WindowListener {
	
	protected static JFrame ruleFrame = null;
	protected JPanel mainPanel = null;
	protected JTextArea textArea = null;
	private static CARuleDescription instance = null;
	private CARuleDescription() {
		ruleFrame = new JFrame();
		initGUI();
	}
	
	public static CARuleDescription getInstance() {
		if(instance == null)
			instance = new CARuleDescription();
		CARuleDescription.ruleFrame.setVisible(true);
		return instance;
	}
	
	/**
	 * Initialize the Graphical User Interface
	 */
	public void initGUI() {
		System.out.println("View Rules");
		
		ruleFrame.setSize(500, 400);
		ruleFrame.setTitle("Rules");

		ruleFrame.setResizable(true);
		
		// Permit the app to hear about the window opening
		ruleFrame.addWindowListener(this); 
		
		ruleFrame.setLayout(new BorderLayout());
		ruleFrame.add(getMainPanel(),BorderLayout.CENTER); 
		ruleFrame.setVisible(true);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * @return the main panel that holds the details of rules
	 */
	public JPanel getMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.BLACK);
		
		textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(100,100));
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.white);
		
		mainPanel.add(BorderLayout.CENTER, textArea);
		return mainPanel;
	}
	
	public void writeText(String text) {
		textArea.append(text);
	}

}
