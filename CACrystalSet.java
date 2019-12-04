package edu.neu.csye6200.ca;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.swing.JPanel;
import edu.neu.csye6200.ca.HexagonCell.Hexagon;

/*
 * @author: Dileep Reddy 
 * NUID: 001063317
 * Class Description: CACrystal set itself implements Runnable which handle thread control 
					  Also it extends from JPanel so that whenever an instance of CACrystalSet is created it can be directly rendered to the UI
					  It also has the record of all the crystals generated which can be used for rewind functionality
 */


public class CACrystalSet extends JPanel implements Runnable {

	private Map<Integer, CACrystal> caCrystalRecord;
	private static final long serialVersionUID = 1L;
	public static final int S = 50;
	public static final int A = (int) (Math.sqrt(3)*(S/2));
	private int generationCount, genLimit, sleepTime;
	private CACrystal previousCrystal;
	private Thread cellTh;
	
	// to signify current status of simulation
	private volatile boolean completeFlag,paused,stopped,rewind;
	
	private static Logger log;
	final int[] xs = new int[6];
	final int[] ys = new int[6];

	public CACrystalSet(CACrystal caCrystal, int genLimit, int sleepTime) {
		CustomLogger cl = new CustomLogger(CALauncher.class.getName());  //Custom logger that returns logger along with file handler which is common for all classes
		log = cl.getLoggerAndFileHandler();
		initializeCrystalSet();
		this.previousCrystal = caCrystal;
		this.genLimit = genLimit;
		this.sleepTime = sleepTime;
		addCrystalToMap(generationCount, previousCrystal);
	}

	/* Thread starting point executes every time thread sleeps and restarts*/
	@Override
	public void run() {
		try {
			while (!paused && generationCount != genLimit && !completeFlag && !stopped) {
				handleForwardAndRewindSimulation();
				try {
					Thread.sleep(this.sleepTime);
				} catch (InterruptedException e) {
					log.severe("The thread execution was interrupted. Details : " + e.toString());
					break;
				}
			}
			if (stopped) {
				stopped = false;
			} 
			else if (generationCount < genLimit && paused) {
				handleForwardAndRewindDuringPauseEvent();
			}
			else if (completeFlag) {
				CALauncher.pauseBtn.setEnabled(false);
				CALauncher.startBtn.setEnabled(false);
				CALauncher.rewindBtn.setEnabled(false);
			} else if (generationCount == genLimit) {
				CALauncher.pauseBtn.setEnabled(false);
				CALauncher.startBtn.setEnabled(false);
			}
		} catch (Exception e) {
			log.severe("Severe Exception occured during the execution of the thread " + e.toString());
		}
	}
	
	
/* main method responsible for painting the graphics*/
	public void paintComponent(Graphics g) {
		try {
			for(int row = 0; row < previousCrystal.getCrystalRows(); row++) {
				for(int col = 0; col < previousCrystal.getCrystalColumns(); col++) {
					createHexagonalGrid(row,col);
					fillOrDrawGridBasedOnCellState(g,row,col);
				}
			}
		}
		catch (Exception e) {
			log.severe("Severe exception occured while loading the graphics" + e.toString());
		}
	}

	public void pauseThread() {
		paused = true;
	}

	public void rewindThread() {
		rewind = true;
	}

	public void stopThread() {
		stopped = true;
	}

	public void addCrystalToMap(int currentGen, CACrystal currentCrystal) {
		caCrystalRecord.put(currentGen, currentCrystal);
	}

	public void removeCrystalFromMap(int currentGen) {
		caCrystalRecord.remove(currentGen);
	}

	// Getters and Setters Section
	public Map<Integer, CACrystal> getCACrystalRecord() {
		return caCrystalRecord;
	}

	public void setCaCrystalRecord(Map<Integer, CACrystal> caCrystalRecord) {
		this.caCrystalRecord = caCrystalRecord;
	}

	public int getGenerationCount() {
		return generationCount;
	}

	public void setGenerationCount(int generationCount) {
		this.generationCount = generationCount;
	}

	public int getGenLimit() {
		return genLimit;
	}

	public void setGenLimit(int genLimit) {
		this.genLimit = genLimit;
	}

	public boolean isCompleteFlag() {
		return completeFlag;
	}

	public void setCompleteFlag(boolean completeFlag) {
		this.completeFlag = completeFlag;
	}

	public CACrystal getPreviousCrystal() {
		return previousCrystal;
	}

	public void setPreviousRegion(CACrystal currentCrystal) {
		this.previousCrystal = currentCrystal;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	public boolean isRewind() {
		return rewind;
	}

	public void setRewind(boolean rewind) {
		this.rewind = rewind;
	}

	// creation of new thread for forward simulation
	public void nextCrystal() {
		cellTh = new Thread(this, "CrystalGrowthThread");
		this.paused = false;
		this.rewind = false;
		cellTh.start();
	}

	public void rewindCrystal() {
		cellTh = new Thread(this, "CrystalGrowthThread");
		this.paused = false;
		this.rewind = true;
		cellTh.start();
	}

	private void initializeCrystalSet() {
		this.caCrystalRecord = new TreeMap<Integer, CACrystal>();
		this.generationCount = 0;
		this.completeFlag = false;
		setOpaque(false);
	}
	
	/*handle forwards and backwards simulation in case of pause event*/
	private void handleForwardAndRewindDuringPauseEvent() {
		if (rewind && generationCount == 0) {
			rewind = false;
			log.info("Simulation paused as user went back to the initial state...");
			CALauncher.pauseBtn.setEnabled(false);
			CALauncher.startBtn.setEnabled(true);
		} else if (rewind) {
			log.info("Simulation paused while user was rewinding...");
			CALauncher.startBtn.setEnabled(true);
			CALauncher.rewindBtn.setEnabled(true);
		} else {
			CALauncher.statusDisplayText.setText("Simulation Paused...");
			log.info("Simulation Paused...");
		}
	}

	/*handles forward and backward simulation*/ 	
	private void handleForwardAndRewindSimulation() {
		CACrystal nextCrystal;
		if (rewind && generationCount > 0) {
			previousCrystal = getCACrystalRecord().get(generationCount - 1);
			removeCrystalFromMap(generationCount);
			generationCount--;
			repaint();
		} else if (rewind && generationCount == 0) {
			paused = true;
		} else {
			nextCrystal = previousCrystal.createNextCrystal(generationCount);
			generationCount++;
			addCrystalToMap(generationCount, nextCrystal);
			previousCrystal = nextCrystal;
			repaint();
		}
		CALauncher.generationText.setText(generationCount + "");
		simulationCheck();
	}
	
	/*creates the hexagonal grid*/
	private void createHexagonalGrid(int row, int col) {
		final Hexagon[][] grid = new Hexagon[previousCrystal.getCrystalRows()][ previousCrystal.getCrystalColumns()];
		grid[row][col] = new Hexagon(row, col, 5);
		final int[] i = {0};
		grid[row][col].foreachVertex((x, y) -> {
			xs[i[0]] = (int)((double)x);
			ys[i[0]] = (int)((double)y);
			i[0]++;
		});
	}
	
	/*fills or draws hexagons based on cell states*/
	private void fillOrDrawGridBasedOnCellState(Graphics g,int row, int col) {
		if (previousCrystal.getCellAt(row, col).getCellState() == CACellState.FROZEN) {
			g.setColor(Color.blue.brighter());
			g.fillPolygon(xs, ys, 6);
		} else if (previousCrystal.getCellAt(row, col).getCellState() == CACellState.VAPOUR) {
			if(CALauncher.isGridMode) {
				g.setColor(CALauncher.isLightMode? Color.black : Color.white);
				g.drawPolygon(xs, ys, 6);
			}
			else {
				g.setColor(CALauncher.isLightMode? Color.white : Color.black);
				g.fillPolygon(xs, ys, 6);
			}
		} else {
			g.setColor(Color.green);
			g.fillPolygon(xs, ys, 6);
		}
	}
	
	private void simulationCheck() {
		if (generationCount == genLimit) {
			completeFlag = true;
		}
	}
}
