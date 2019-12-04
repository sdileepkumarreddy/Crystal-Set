package edu.neu.csye6200.ca;

import java.util.Random;
/*
 * @author: Dileep Reddy 
 * NUID: 001063317
 * Class Description: CACrystal holds the 2D array of CACells and also initializes the crystal based on the rule selected
 * 					  It also fetches next crystals based on next cell states in CA Rule class
 */

public class CACrystal {

	private int crystalRows,crystalColumns;
	private RuleNames ruleName;
	protected CACell[][] arrCells;	
	CACell caFrozen = new CARule(this.ruleName, this, CACellState.FROZEN);
	CACell caLiquid = new CARule(this.ruleName, this, CACellState.LIQUID);


	public CACrystal(RuleNames rule, int crystalRows, int crystalColumns) {
		this.ruleName = rule;
		this.crystalRows = crystalRows;
		this.crystalColumns = crystalColumns;
		this.arrCells = new CACell[crystalRows][crystalColumns];
		regionInitialize();
	}

	public CACrystal(CACrystal previousCrystal) {
		ruleName = previousCrystal.ruleName;
		crystalRows = previousCrystal.crystalRows;
		crystalColumns = previousCrystal.crystalColumns;
		arrCells = new CACell[crystalRows][crystalColumns];
		regionInitialize();
	}

	/*initializes the entire region with basic rule Vapour assuming all the cells around us are in vapour state
	*/ 
	private void regionInitialize() {
		for (int row = 0; row < crystalRows; row++) {
			for (int col = 0; col < crystalColumns; col++) {
				CACell ca = new CARule(this.ruleName, this, CACellState.VAPOUR);
				ca.setCellRowPos(row);
				ca.setCellColPos(col);
				ca.setCrystal(this);
				this.arrCells[row][col] = ca;
			}
		}
		createInitialCrystal();
	}

	/*creates different initial crystals for different rules
	 *47 - center 62 - center column */ 
	private void createInitialCrystal() {
		CACell caFrozen = new CARule(this.ruleName, this, CACellState.FROZEN);
		CACell caLiquid = new CARule(this.ruleName, this, CACellState.LIQUID);
		int row = getCrystalRows()/2;
		int col = getCrystalColumns()/2;
		if(this.ruleName == RuleNames.SingleCrystal) {
			this.arrCells[row][col] = caFrozen;
		}
		else if(this.ruleName == RuleNames.RandomCrystals){

			for(int i = 0; i < 15; i++) {
				Random rand = new Random();
				this.arrCells[rand.nextInt(94)][rand.nextInt(125)] = caFrozen;
				this.arrCells[rand.nextInt(94)][rand.nextInt(125)] = caLiquid;
				this.arrCells[rand.nextInt(94)][rand.nextInt(125)] = caLiquid;
				this.arrCells[rand.nextInt(94)][rand.nextInt(125)] = caLiquid;
			}
		}
		else if(this.ruleName == RuleNames.Butterfly){
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

		else if(this.ruleName == RuleNames.Highway){
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
		else if(this.ruleName == RuleNames.Garden){
			for(int i = 0; i < getCrystalRows(); i++)
			{
				for(int j =0; j < getCrystalColumns(); j++) {
					if((i%5 == 0) && (j % 4 == 0))
					{
						this.arrCells[i][j] = caLiquid;
					}
					if((i%4 == 0) && (j % 5 == 0))
					{
						this.arrCells[i][j] = caFrozen;
					}
				}
			}
		}
	}

	/* creates next crystals based previous crystal cell states */
	public CACrystal createNextCrystal(int counter) {
		CACrystal newCrystal = new CACrystal(this);
		CACellState[][] newCellStates;
		newCellStates = nextCellStates(counter);
		for (int i = 0; i < getCrystalRows(); i++) {
			for (int j = 0; j < getCrystalColumns(); j++) {
				newCrystal.getCellAt(i, j).setState(newCellStates[i][j]);
			}
		}
		return newCrystal;
	}

	/*returns CACell based on row and column value*/
	public CACell getCellAt(int row, int col) {
		if ((row < 0) || (row >= getCrystalRows())) {
			throw new RuntimeException("The referenced cell at " + row + "is not valid row in the current region.");
		}
		if ((col < 0) || (col >= getCrystalColumns())) {
			throw new RuntimeException("The referenced cell at " + col + "is not valid column in the current region.");
		}
		return arrCells[row][col];
	}

	/*gets nextCellStates based on selected rules*/
	public CACellState[][] nextCellStates(int counter) {
		CACellState[][] nextStates = new CACellState[getCrystalRows()][getCrystalColumns()];
		for (int i = 0; i < getCrystalRows(); i++) {
			for (int j = 0; j < getCrystalColumns(); j++) {
				nextStates[i][j] = getCellAt(i, j).getNextCellState();
			}
		}
		return nextStates;
	}

	/*Getters and Setters*/
	public int getCrystalRows() {
		return crystalRows;
	}

	public void setCrystalRows(int regionWidth) {
		this.crystalRows = regionWidth;
	}

	public int getCrystalColumns() {
		return crystalColumns;
	}

	public void setCrystalColumns(int regionHeight) {
		this.crystalColumns = regionHeight;
	}

	public RuleNames getRuleName() {
		return ruleName;
	}

	public void setRuleName(RuleNames ruleName) {
		this.ruleName = ruleName;
	}

}
