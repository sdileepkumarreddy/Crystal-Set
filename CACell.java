package edu.neu.csye6200.ca;

import java.util.logging.Logger;


enum CACellState {
	FROZEN, LIQUID, VAPOUR;
}

public abstract class CACell {

	private int cellRowPos; 
	private int cellColPos;
	private CACellState cellState; 
	protected CACrystal crystal;
	private static Logger log = null;

	//default constructor
	public CACell() {

	}

	public CACell(CACrystal crystal, CACellState cellState) {
		log = Logger.getLogger(CACell.class.getName());
		this.crystal = crystal;
		this.cellState = cellState;
	}

	// CA Rule will implement this abstract method
	public abstract CACellState getNextCellState();

	//setter method for setting the cell state
	protected void setState(CACellState state) {
		cellState = state;
	}

	
	protected int getDesiredNeighborsCount(CACellState desiredState) {
		int desiredNeighborCount = 0;
		
		try {
			int colStartPos,ColEndPos;

			for (int rowCounter = -1; rowCounter < 2; rowCounter++) {
				colStartPos = rowCounter == 0 ? -1 : (cellRowPos %2 == 0? -1 : 0); // Neighbor Rows and columns position changes for hexagon for even and odd rows
				ColEndPos = rowCounter == 0? 3 : 2;
				for (int colCounter = colStartPos; colCounter < colStartPos+ColEndPos; colCounter++) {
					if(isDesiredNeighbour(rowCounter,colCounter,desiredState))
						desiredNeighborCount++;
				}
			}

		} catch (Exception e) {
			log.severe("Exception occured while getting Neighbor Count : " + e.toString());
			desiredNeighborCount = 0;
		}
		return desiredNeighborCount;
	}

	private boolean isDesiredNeighbour(int rowCounter,int colCounter,CACellState desiredState)
	{
		if (cellRowPos + rowCounter >= 0 && cellRowPos + rowCounter < getCrystal().getCrystalRows()
				&& cellColPos + colCounter >= 0 && cellColPos + colCounter < getCrystal().getCrystalColumns()) {
			if (getCrystal().getCellAt(cellRowPos + rowCounter, cellColPos + colCounter).getCellState()
					.compareTo(desiredState) == 0) {
				if (!(rowCounter == 0 && colCounter == 0)) // should not consider the current cell as neighbor
					return true;
			}
		}
		return false;
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
	public void setCrystal(CACrystal crystal) {
		this.crystal = crystal;
	}

	/**
	 * @return the cellXPos
	 */
	public int getCellRowPos() {
		return cellRowPos;
	}

	/**
	 * @param cellXPos the cellXPos to set
	 */
	public void setCellRowPos(int cellRowPos) {
		this.cellRowPos = cellRowPos;
	}

	/**
	 * @return the cellYPos
	 */
	public int getCellColPos() {
		return cellColPos;
	}

	/**
	 * @param cellYPos the cellYPos to set
	 */
	public void setCellColPos(int cellColPos) {
		this.cellColPos = cellColPos;
	}

}
