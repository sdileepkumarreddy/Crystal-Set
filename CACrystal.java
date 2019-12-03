package edu.neu.csye6200.ca;

import java.util.Random;
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
		//this.setInitialAliveCell(initialAliveCell);
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
				ca.setCellRowPos(x);
				ca.setCellColPos(y);
				ca.setCrystal(this);
				this.arrCells[x][y] = ca;

			}
		}
		createInitialCrystal();
	}

	private void createInitialCrystal() {
		CACell caFrozen = new CARule(this.ruleName, this, CACellState.FROZEN);
		CACell caLiquid = new CARule(this.ruleName, this, CACellState.LIQUID);
		CACell caVapour = new CARule(this.ruleName, this, CACellState.VAPOUR);
		int x = getCrystalRows()/2;
		int y = getCrystalColumns()/2;
		int innerRowCount = 0;
		if(this.ruleName == RuleNames.SingleSnowflake) {
			this.arrCells[x][y] = caFrozen;
		}
		else if(this.ruleName == RuleNames.RandomSnowFlakes){

			for(int i = 0; i < 15; i++) {
				Random rand = new Random();
				this.arrCells[rand.nextInt(94)][rand.nextInt(125)] = caFrozen;
				this.arrCells[rand.nextInt(94)][rand.nextInt(125)] = caLiquid;
				this.arrCells[rand.nextInt(94)][rand.nextInt(125)] = caLiquid;
				this.arrCells[rand.nextInt(94)][rand.nextInt(125)] = caLiquid;
			}
		}
		else if(this.ruleName == RuleNames.rule3){

			this.arrCells[getCrystalRows() / 2][getCrystalColumns() / 2] = caFrozen;
			this.arrCells[getCrystalRows()/2 - 2][getCrystalColumns()/2 - 2] = caFrozen;
			this.arrCells[getCrystalRows()/2 - 2][getCrystalColumns()/2 + 2] = caFrozen;
			this.arrCells[getCrystalRows()/2 + 2][getCrystalColumns()/2 - 2] = caFrozen;
			this.arrCells[getCrystalRows()/2 + 2][getCrystalColumns()/2 + 2] = caFrozen;
			this.arrCells[getCrystalRows()/2 -4 ][getCrystalColumns()/2 - 4] = caFrozen;
			this.arrCells[getCrystalRows()/2 - 4 ][getCrystalColumns()/2 + 4] = caFrozen;
			this.arrCells[getCrystalRows()/2 + 4][getCrystalColumns()/2 + 4] = caFrozen;
			this.arrCells[getCrystalRows()/2 + 4][getCrystalColumns()/2 - 4] = caFrozen;
			this.arrCells[getCrystalRows()/2 -4 ][getCrystalColumns()/2] = caLiquid;
			this.arrCells[getCrystalRows()/2 - 2 ][getCrystalColumns()/2] = caLiquid;
			this.arrCells[getCrystalRows()/2 + 2][getCrystalColumns()/2] = caLiquid;
			this.arrCells[getCrystalRows()/2 + 4][getCrystalColumns()/2] = caLiquid;
			this.arrCells[getCrystalRows()/2 ][getCrystalColumns()/2 + 2] = caLiquid;
			this.arrCells[getCrystalRows()/2 ][getCrystalColumns()/2 + 4] = caLiquid;
			this.arrCells[getCrystalRows()/2][getCrystalColumns()/2 -2 ] = caLiquid;
			this.arrCells[getCrystalRows()/2][getCrystalColumns()/2 - 4] = caLiquid;
		}

		else if(this.ruleName == RuleNames.rule4){
			for(int i = 0; i < getCrystalRows(); i++)
			{
				for(int j =0; j < getCrystalColumns(); j++) {
					if((i == getCrystalRows()/2 && j %2 == 0) || (j== getCrystalColumns()/2 && i%2 == 1))
					{
						this.arrCells[i][j] = caLiquid;
					}
				}
			}
			for(int i = 0; i < 47; i = i+2) {
				this.arrCells[47 - i][62 - i] = caFrozen;
				this.arrCells[47 + i][62 + i] = caFrozen;
				this.arrCells[47 - i][62 + i] = caFrozen;
				this.arrCells[47 + i][62 - i] = caFrozen;
			}
		}



	}
	public CACrystal createNextCrystal(int counter) {

		CACrystal newCrystal = new CACrystal(this);
		CACellState[][] newCellStates;

		try {

			if (newCrystal.ruleName.compareTo(RuleNames.SingleSnowflake) == 0) {

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
			if (newCrystal.ruleName.compareTo(RuleNames.RandomSnowFlakes) == 0) {

				newCellStates = nextCellStates(counter);
				for (int i = 0; i < getCrystalRows(); i++) {
					for (int j = 0; j < getCrystalColumns(); j++) {
						newCrystal.getCellAt(i, j).setState(newCellStates[i][j]);
					}
				}
			}
			else {

				newCellStates = nextCellStates(counter);
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

		if(this.ruleName == RuleNames.RandomSnowFlakes){

			for (int i = 0; i < getCrystalRows(); i++) {
				for (int j = 0; j < getCrystalColumns(); j++) {
					nextStates[i][j] = getCellAt(i, j).getNextCellState();

				}
			}
			return nextStates;
		}
		else {

			for (int i = 0; i < getCrystalRows(); i++) {
				for (int j = 0; j < getCrystalColumns(); j++) {
					nextStates[i][j] = getCellAt(i, j).getNextCellState();

				}
			}
			return nextStates;
		}
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
