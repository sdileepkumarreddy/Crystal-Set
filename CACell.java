package edu.neu.csye6200.ca;

import java.util.Random;
import java.util.logging.Logger;


enum CACellState {
	FROZEN, LIQUID, VAPOUR;
}

public abstract class CACell {



	public CACell() {
		// TODO Auto-generated constructor stub
	}

	private CACellState cellState; 
	protected CACrystal crystal; 
	private int cellXPos; 
	private int cellYPos; 
	protected static int cellCount = 0;

	private static Logger log = Logger.getLogger(CACell.class.getName());


	public CACell(CACrystal crystal, CACellState cellState) {

		this.crystal = crystal;

		if (crystal.getRuleName().compareTo(RuleNames.rule1) == 0) {
			if (cellCount == crystal.getInitialAliveCell())
				this.cellState = CACellState.VAPOUR;
			else
				this.cellState = cellState;
		} else {
			this.cellState = cellState;
		}
		cellCount++;

	}

	/*
	 * Implementation is provided by extending class (MARule)
	 */
	public abstract CACellState getNextCellState();

	/*
	 * Implementation is provided by extending class (MARule)
	 */
	//public abstract int[] getNextCellPos();

	/*
	 * Sets the cell's current state if the new state is different
	 * from the current state.
	 */
	protected void setState(CACellState state) {
		if (!cellState.equals(state))
			cellState = state;
	}

	/*
	 * Function to calculate the number of neighbors with a particular state for the
	 * current cell. State to look after is provided as Input.
	 */

	protected int getNeighborsCount(CACellState state) {

		int desiredNeighbors = 0;
		try {
			int xPos = this.getCellXPos();
			int z,a;

			for (int i = -1; i < 2; i++) {
				if(i == 0 )
				{
					z = -1;
					a = 3;
				}
				else
				{
					z = xPos %2 == 0? -1 : 0;
					a = 2;
				}
				for (int j = z; j < z+a; j++) {
					if (this.getCellXPos() + i >= 0 && this.getCellXPos() + i < getCrystal().getCrystalRows()
							&& this.getCellYPos() + j >= 0 && this.getCellYPos() + j < getCrystal().getCrystalColumns()) {
						if (getCrystal().getCellAt(this.getCellXPos() + i, this.getCellYPos() + j).getCellState()
								.compareTo(state) == 0) {
							if (!(i == 0 && j == 0)) // should not consider the current cell as neighbor
								desiredNeighbors++;
						}
					}
				}
			}

		} catch (Exception e) {
			log.severe("Exception occured while getting Neighbor Count : " + e.toString());
			desiredNeighbors = 0;
		}
		return desiredNeighbors;
	}

	protected int getTDNeighborsCount(CACellState state) {

		int desiredNeighbors = 0;
		try {
			for (int j = -1; j < 2; j++) {
				if (this.getCellXPos() - 1 >= 0 && this.getCellXPos() - 1 < getCrystal().getCrystalRows()
						&& this.getCellYPos() + j >= 0 && this.getCellYPos() + j < getCrystal().getCrystalColumns()) {
					if (getCrystal().getCellAt(this.getCellXPos() - 1, this.getCellYPos() + j).getCellState()
							.compareTo(state) == 0) {
						if (j != 0)
							desiredNeighbors++;
					}
				}
			}

		} catch (Exception e) {
			log.severe("Exception occured while getting Neighbor Count : " + e.toString());
			desiredNeighbors = 0;
		}
		return desiredNeighbors;
	}



	// Getters and Setters

	/**
	 * @return the cellState
	 */
	public CACellState getCellState() {
		return cellState;
	}

	/**
	 * @param cellState the cellState to set
	 */
	public void setCellState(CACellState cellState) {
		this.cellState = cellState;
	}

	/**
	 * @return the region
	 */
	public CACrystal getCrystal() {
		return crystal;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(CACrystal crystal) {
		this.crystal = crystal;
	}

	/**
	 * @return the cellXPos
	 */
	public int getCellXPos() {
		return cellXPos;
	}

	/**
	 * @param cellXPos the cellXPos to set
	 */
	public void setCellXPos(int cellXPos) {
		this.cellXPos = cellXPos;
	}

	/**
	 * @return the cellYPos
	 */
	public int getCellYPos() {
		return cellYPos;
	}

	/**
	 * @param cellYPos the cellYPos to set
	 */
	public void setCellYPos(int cellYPos) {
		this.cellYPos = cellYPos;
	}

}
