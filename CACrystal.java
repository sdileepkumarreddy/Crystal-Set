package edu.neu.csye6200.ca;

import java.util.logging.Logger;

public class CACrystal {

	private RuleNames ruleName; // Holds RuleNames Enum passed by the user he wishes to see.
	protected CACell[][] arrCells; // Holds MACells which contain MACellState.
	private int crystalRows; // Dimensions of the Region passed by the user.
	private int crystalColumns; // Dimensions of the Region passed by the user.
	private int initialAliveCell; // To start the region generation, initially we are making some cells active.


	// For Logging application process to the console.
	private static Logger log = Logger.getLogger(CALauncher.class.getName());
	
	public CACrystal(RuleNames rule, int crystalRows, int crystalColumns, int initialAliveCell) {

		this.ruleName = rule;
		this.crystalRows = crystalRows;
		this.crystalColumns = crystalColumns;
		this.arrCells = new CACell[crystalRows][crystalColumns];
		this.setInitialAliveCell(initialAliveCell);
		regionInitialize();

	}
	public CACrystal(CACrystal previousCrystal) {
		ruleName = previousCrystal.ruleName; // Copying the same rule used by the instance
		crystalRows = previousCrystal.crystalRows;
		crystalColumns = previousCrystal.crystalColumns;
		arrCells = new CACell[crystalRows][crystalColumns];
		regionInitialize();

	}
	
	private void regionInitialize() {
		for (int x = 0; x < crystalRows; x++) {
			for (int y = 0; y < crystalColumns; y++) {
				CACell ca = new CARule(this.ruleName, this, CACellState.VAPOUR); // Based on the rule name the cells are
																				// initialized
				ca.setCellXPos(x);
				ca.setCellYPos(y);
				ca.setRegion(this);
				this.arrCells[x][y] = ca;
			}
		}
	}
	public CACrystal createNextCrystal(int counter) {
		
		CACrystal newCrystal = new CACrystal(this);
		CACellState[][] newCellStates;

		try {

			if (newCrystal.ruleName.compareTo(RuleNames.rule1) == 0) {

				newCellStates = nextCellStates(counter);

				/*
				 * Looping through each cell to determine the neighbors state and deciding the
				 * cell's state based on the comboRules (except LOCKME)
				 */
				for (int i = 0; i < getCrystalRows(); i++) {
					for (int j = 0; j < getCrystalColumns(); j++) {
						newCrystal.getCellAt(i, j).setState(newCellStates[i][j]);
					}
				}
			}
		} catch (Exception e) {
			log.severe("Exception occured while creating next Region : " + e.toString());

		}

		return newCrystal;
	}
	
	public CACell getCellAt(int row, int col) {
		if ((row < 0) || (row >= getCrystalRows())) {
			throw new RuntimeException("The referenced cell at " + row + "is not valid row in the current region.");
		}
		if ((col < 0) || (col >= getCrystalColumns())) {
			throw new RuntimeException("The referenced cell at " + col + "is not valid column in the current region.");
		}
		return arrCells[row][col];
	}
	
	public CACellState[][] nextCellStates(int counter) {

		CACellState[][] nextStates = new CACellState[getCrystalRows()][getCrystalColumns()];

		int x = getCrystalRows()/2;
		int y = getCrystalColumns()/2;
		int innerRowCount = 0;
		for(int i = (x-counter); i <= (x+ counter);i++) {
			for(int j = (y-counter);j <= (y+ counter);j++) {
				if(i % 2 == 0 && (j >= x-innerRowCount && j <= x+innerRowCount))
				{
					nextStates[i][j] = CACellState.FROZEN;
					
					
				}
				else if(i % 2 == 1 && (j >= x-innerRowCount && j <= x+innerRowCount-1)) {
					nextStates[i][j] = CACellState.FROZEN;
				}
				
				else
				{
					nextStates[i][j] = CACellState.VAPOUR;
				}
			}
			if(i%2 == 0 && i < x)
			{
				innerRowCount++;
			}
			if(i%2 == 1 && i > x)
			{
				innerRowCount--;
			}
						
		}
//		for (int i = 0; i < getCrystalRows(); i++) {
//			for (int j = 0; j < getCrystalColumns(); j++) {
//				nextStates[i][j] = getCellAt(i, j).getNextCellState();
//
//			}
//		}

		return nextStates;
	}
	

	// Getters and Setters

	/**
	 * @return the comboRows
	 */
	public int getCrystalRows() {
		return crystalRows;
	}

	/**
	 * @param comboRows the comboRows to set
	 */
	public void setCrystalRows(int regionWidth) {
		this.crystalRows = regionWidth;
	}

	/**
	 * @return the regionColumns
	 */
	public int getCrystalColumns() {
		return crystalColumns;
	}

	/**
	 * @param regionColumns the regionColumns to set
	 */
	public void setCrystalColumns(int regionHeight) {
		this.crystalColumns = regionHeight;
	}

	/**
	 * @return the initialAliveCell
	 */
	public int getInitialAliveCell() {
		return initialAliveCell;
	}

	/**
	 * @param initialAliveCell the initialAliveCell to set
	 */
	public void setInitialAliveCell(int initialAliveCell) {
		this.initialAliveCell = initialAliveCell;
	}

	/**
	 * @return the ruleName
	 */
	public RuleNames getRuleName() {
		return ruleName;
	}

	/**
	 * @param ruleName the ruleName to set
	 */
	public void setRuleName(RuleNames ruleName) {
		this.ruleName = ruleName;
	}
}
