package edu.neu.csye6200.ca;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;



public class CACrystalSet extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private Map<Integer, CACrystal> caCrystalRecord; // To store all the Regions. Will be helpful in retrieving data back
	private int generationCount; // To keep track of region generations.

	/*
	 * User defined limit until which the Regions are generated and stored in the
	 * MAP and simulation stops when the limit is reached
	 */
	private int genLimit;

	private int sleepTime; // User defined sleep Time between generations.
	private CACrystal previousCrystal; // Previous region in the RegionSet
	private Thread cellTh; // Thread which executes once the user Starts the Simulation

	/*
	 * Indicator to alert if the automata is complete. Using which we can show some
	 * custom message to the user.
	 */
	private volatile boolean completeFlag;
	private volatile boolean paused; // Helps in determining if the generation is paused
	private volatile boolean stopped;// Helps in stopping the simulation.
	private volatile boolean rewind; // Helps in determining if rewind is called

	// For Logging application process to the console.
	private static Logger log = Logger.getLogger(CALauncher.class.getName());

	// Constructor to initialize the MARegionSet
	public CACrystalSet(CACrystal caCrystal, int genLimit, int sleepTime) {

		// Initializing the properties of a RegionSet
		initializeCrystalSet();
		this.previousCrystal = caCrystal;
		this.genLimit = genLimit;
		this.sleepTime = sleepTime;

		addCrystalToMap(generationCount, previousCrystal);

	}


	private void initializeCrystalSet() {

		/*
		 * Using Tree Map as we can allow user to retrieve any region based on
		 * generation and not just the previous generation. Also the search using Tree
		 * Map is faster.
		 */

		this.caCrystalRecord = new TreeMap<Integer, CACrystal>();
		this.generationCount = 0;
		this.completeFlag = false;
		setOpaque(false); // This is necessary to make painting work
	}

	/*
	 * Function which gets triggered when the thread is executed to process the
	 * Simulation.
	 */

	@Override
	public void run() {

		try {

			// Multiple checks are made before new region is created.
			while (!paused && generationCount != genLimit && !completeFlag && !stopped) {

				CACrystal nextCrystal;

				// To check if user is rewinding the simulation
				if (rewind && generationCount > 0) {
					previousCrystal = getCACrystalRecord().get(generationCount - 1);
					removeCrystalFromMap(generationCount);
					generationCount--;
					repaint(); // Paints the new state of the region using paintComponent.

				} else if (rewind && generationCount == 0) { // To pause the simulation when user goes to initial state.
					paused = true;

				} else { // Forward simulation
					nextCrystal = previousCrystal.createNextCrystal(generationCount);
					generationCount++;
					addCrystalToMap(generationCount, nextCrystal); // Once done, the region is added to the MAP
					previousCrystal = nextCrystal;
					repaint(); // Paints the new state of the region using paintComponent.
				}

			//	CALauncher.genCount.setText(generationCount + "");
				simulationCheck(); // helper method to check if the simulation is completed

				try {
					Thread.sleep(this.sleepTime * 10); // customized sleep time
				} catch (InterruptedException e) {
					log.severe("The thread execution was interrupted. Details : " + e.toString());
					break;
				}
			} // Custom messages for the user both in console and UI to help user for
			// identifying simulation state.
			if (stopped) {
				stopped = false;
			} else if (generationCount < genLimit && paused) {
				if (rewind && generationCount == 0) {
//					rewind = false;
//					CALauncher.lblStatus.setText("Simulation paused as user went back to the initial state...");
//					log.info("Simulation paused as user went back to the initial state...");
//					CALauncher.pauseButton.setEnabled(false);
//					CALauncher.startButton.setEnabled(true);

				} else if (rewind) {
//					CALauncher.lblStatus.setText("Simulation paused while user was rewinding...");
//					log.info("Simulation paused while user was rewinding...");
//					CALauncher.startButton.setEnabled(true);
//					CALauncher.rewindButton.setEnabled(true);
				} else {
//					MAutomataDriver.lblStatus.setText("Simulation Paused...");
//					log.info("Simulation Paused...");
				}

			} else if (completeFlag) {

//				if (previousCrystal.isLocked()) {
//					CALauncher.lblStatus.setText("OOPS!! You are locked... Simulation completed Successfully...");
//					log.info("OOPS!! You are locked... Simulation completed Successfully...");
//				} else {
//					CALauncher.lblStatus.setText("Simulation completed Successfully...");
//					log.info("Simulation completed Successfully...");
//				}
//				CALauncher.pauseButton.setEnabled(false);
//				CALauncher.startButton.setEnabled(false);

			} else if (generationCount == genLimit) {
//				CALauncher.lblStatus.setText("Simulation reached maximum generation Limit...");
//				log.info("Simulation reached maximum generation Limit...");
//
//				CALauncher.pauseButton.setEnabled(false);
//				CALauncher.startButton.setEnabled(false);
			}
		} catch (Exception e) {
			log.severe("OOPS!! Some issue occured while simulation was in progress. Details : " + e.toString());
		}

	}

	// Helper method to check if the simulation is completed even before generation
	// Limit.
	private void simulationCheck() {

		if (previousCrystal.getRuleName().compareTo(RuleNames.rule1) == 0) {
			
				if (generationCount == genLimit) {
					completeFlag = true;
			}
			
		} 

	}

	// Start point for generating next Regions. Called by MAutomataDriver.
	public void nextCrystal() {
		cellTh = new Thread(this, "automataThread"); // Starts a new Thread
		this.paused = false;
		this.rewind = false;
		cellTh.start(); // Calls run method of the thread
	}

	// Start point for retrieving previous Regions. Called by MAutomataDriver.
	public void rewindCrystal() {
		cellTh = new Thread(this, "automataThread"); // Starts a new Thread
		this.paused = false;
		this.rewind = true;
		cellTh.start(); // Calls run method of the thread
	}

	// Routine to update the colors or paint the state of the cell.
	public void paintComponent(Graphics g) {

		try {
			// helper methods to calculate the rectangle size in the panel.
			int squarewidth = getSquareWidth();
			int squareheight = getSquareHeight();

			// helper methods to get the co-ordinates of the cell to fill them.
			int hoffset = getHorizontalOffset();
			int voffset = getVerticalOffset();

		 if (completeFlag && previousCrystal.getRuleName().compareTo(RuleNames.rule1) == 0) {
				for (int row = 0; row < previousCrystal.getCrystalRows(); row++) {
					for (int col = 0; col < previousCrystal.getCrystalColumns(); col++) {
						if (previousCrystal.getCellAt(row, col).getCellState() == CACellState.FROZEN) {
							g.setColor(Color.BLACK);
						} else if (previousCrystal.getCellAt(row, col).getCellState() == CACellState.VAPOUR) {
							Color newColor = new Color(255, 215, 0); // For Gold Color
							g.setColor(newColor);
						} else {
							g.setColor(Color.BLUE);
						}
						g.fillRect(hoffset + col * squarewidth, voffset + row * squareheight, squarewidth - 1,
								squareheight - 1);
					}

				}
			}
		 else {
			// BufferedImage myPicture = ImageIO.read(new File("1.png"));
				for (int row = 0; row < previousCrystal.getCrystalRows(); row++) {
					for (int col = 0; col < previousCrystal.getCrystalColumns(); col++) {
						if (previousCrystal.getCellAt(row, col).getCellState() == CACellState.FROZEN) {
//							ImageObserver io = new ImageObserver() {
//								
//								@Override
//								public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
//									// TODO Auto-generated method stub
//									return false;
//								}
//							};
//							g.drawImage(myPicture, row, col, io);
							g.setColor(Color.GREEN);
						} else if (previousCrystal.getCellAt(row, col).getCellState() == CACellState.VAPOUR) {
							g.setColor(Color.WHITE);
						} else {
							g.setColor(Color.WHITE);
						}
						boolean evenRow = ((row % 2) == 0);
						int shift = evenRow ? 0 : squarewidth/2;
						g.fillRect(hoffset + col * squarewidth, voffset + row * squareheight, squarewidth - 1,
								squareheight - 1);

					}

				}
			}
		} catch (Exception e) {
			log.severe("Whoa!! Some exception occurred while setting up graphics. Details : " + e.toString());
		}

	}

	// Pausing the thread when User insists. Called by MAutomataDriver
	public void pauseThread() {
		paused = true;
	}

	// Going to the previous generation. Called by MAutomataDriver
	public void rewindThread() {
		rewind = true;
	}

	// Terminating the simulation. Called by MAutomataDriver
	public void stopThread() {
		stopped = true;
	}

	// Helper methods for calculating the dimensions and helping in filling the
	// rectangle
	private int getSquareWidth() {
		return getSize().width / previousCrystal.getCrystalColumns();
	}

	private int getSquareHeight() {
		return getSize().height / previousCrystal.getCrystalRows();
	}

	private int getHorizontalOffset() {
		return (getSize().width - (getSquareWidth() * previousCrystal.getCrystalColumns())) / 2;
	}

	private int getVerticalOffset() {
		return (getSize().height - (getSquareHeight() * previousCrystal.getCrystalRows())) / 2;
	}

	// Helper Method to add Regions to the Map
	public void addCrystalToMap(int currentGen, CACrystal currentCrystal) {
		caCrystalRecord.put(currentGen, currentCrystal);
	}

	// Helper Method to add Regions to the Map
	public void removeCrystalFromMap(int currentGen) {
		caCrystalRecord.remove(currentGen);
	}

	// Getters and Setters Section

	/**
	 * @return the maRegionRecord
	 */
	public Map<Integer, CACrystal> getCACrystalRecord() {
		return caCrystalRecord;
	}

	/**
	 * @param maRegionRecord the maRegionRecord to set
	 */
	public void setCaCrystalRecord(Map<Integer, CACrystal> caCrystalRecord) {
		this.caCrystalRecord = caCrystalRecord;
	}

	/**
	 * @return the generationCount
	 */
	public int getGenerationCount() {
		return generationCount;
	}

	/**
	 * @param generationCount the generationCount to set
	 */
	public void setGenerationCount(int generationCount) {
		this.generationCount = generationCount;
	}

	/**
	 * @return the comboGenLimit
	 */
	public int getGenLimit() {
		return genLimit;
	}

	/**
	 * @param comboGenLimit the comboGenLimit to set
	 */
	public void setGenLimit(int genLimit) {
		this.genLimit = genLimit;
	}

	/**
	 * @return the completeFlag
	 */
	public boolean isCompleteFlag() {
		return completeFlag;
	}

	/**
	 * @param completeFlag the completeFlag to set
	 */
	public void setCompleteFlag(boolean completeFlag) {
		this.completeFlag = completeFlag;
	}

	/**
	 * @return the current Region
	 */
	public CACrystal getPreviousCrystal() {
		return previousCrystal;
	}

	/**
	 * @param previousRegion the previousRegion to set
	 */
	public void setPreviousRegion(CACrystal currentCrystal) {
		this.previousCrystal = currentCrystal;
	}

	/**
	 * @return the paused
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * @param paused the paused to set
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	/**
	 * @return the stopped
	 */
	public boolean isStopped() {
		return stopped;
	}

	/**
	 * @param stopped the stopped to set
	 */
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	/**
	 * @return the rewind
	 */
	public boolean isRewind() {
		return rewind;
	}

	/**
	 * @param rewind the rewind to set
	 */
	public void setRewind(boolean rewind) {
		this.rewind = rewind;
	}

}
