package edu.neu.csye6200.ca;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/*
 * @author: Dileep Reddy 
 * NUID: 001063317
 * Class Description: CARuleDescription creates a new frame to disploay the rule descriptions on the frame.
 * Since it is needed to be created only once I have used singleton pattern
 */

//Singleton Pattern
public class CARuleDescription implements WindowListener {
	
	protected static JFrame ruleFrame = null;
	protected JPanel mainPanel = null;
	protected JTextArea textArea = null;
	private static CARuleDescription instance = null;
	private CARuleDescription() {
		ruleFrame = new JFrame();
		initGUI();
		writeDescription();
		
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
	
	private void writeDescription() {
		textArea.setForeground(Color.white);
		textArea.append("Rule 1 : Single Crystal");
		textArea.append("\nDescription: ");
		textArea.append("In this rule, initially only once cell in the center will be Frozen \r\n" + 
				"	 and in the next iteration if only one cell around the cell in the previous crystal is Frozen \r \n" + 
				"	 the state of the cell be transitioned to Frozen in the next iteration");
		textArea.append("\n\nRule 2 : Random Crystal Growth");
		textArea.append("\nDescription: ");
		textArea.append("Cells in the nature are not always uniform. \r \n	Through this rule I would like to simulate random crystal like in nature\r\n"+ 
				 " 	and do the simulation. In this rule, initially 15 random liquid state cells \r \n	and 10 random Frozen cell states are created in the grid.");
		textArea.append("\n\nRule 3 : Butterfly");
		textArea.append("\nDescription: Crystal growth is very fascinating. So I have decided to check how the crystal grows \r\n" + 
				"	 if there are small cells of frozen and liquid cells in the shape of a star. \r\n" + 
				"	 Turns out it takes the shape of the butterfly");
		textArea.append("\n\nRule 4 : Highway");
		textArea.append("\nDescription: Crystal growth is very fascinating. So I have decided to check how the crystal grows \r\n" + 
				"	 if there are small cells of frozen and liquid cells in the shape of a big cross. \r\n" + 
				"	 Turns out it takes the shape of the highway");
		textArea.append("\n\nRule 5 : Garden");
		textArea.append("\nDescription: Crystal growth is very fascinating. So I have decided to check how the crystal grows \r\n" + 
				"	 if there are small cells of frozen and liquid cells in the equal gaps. \r\n" + 
				"	 Turns out it takes the shape of the garden with the help of following logic which is self-explanatory");
	}

}
