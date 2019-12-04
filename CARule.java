
package edu.neu.csye6200.ca;

import java.util.logging.Logger;

/*
 * @author: Dileep Reddy 
 * NUID: 001063317
 * Class Description: CARule class extends from CACell abstract class which signifies each cell must be confined with a rule.
 * 					  It has RuleNames enum data type to select different rule names.
 */


enum RuleNames {
	SingleCrystal, RandomCrystals, Butterfly, Highway, Garden
}

public class CARule extends CACell {

	private RuleNames ruleName; 
	private static Logger log = Logger.getLogger(CACell.class.getName());

	public CARule(RuleNames ruleName, CACrystal crystal, CACellState initCellState) {
		super(crystal, initCellState);
		this.ruleName = ruleName;
	}

	// gets next cell state value based on selected rule and current state
	@Override
	public CACellState getNextCellState() {

		if (ruleName.equals(RuleNames.SingleCrystal)) {
			return getRule1State();
		}

		if (ruleName.equals(RuleNames.RandomCrystals)) {
			return getRule2State();
		}
		if (ruleName.equals(RuleNames.Butterfly)) {
			return getRule3State();
		}
		if (ruleName.equals(RuleNames.Highway)) {
			return getRule4State();
		}
		if (ruleName.equals(RuleNames.Garden)) {
			return getRule5State();
		}
			return getCellState();
	}

	/*
	 * Rule Description: 
	 * 
	 * Single Crystal: In this rule, initially only once cell in the center will be Frozen 
	 * and in the next iteration if only one cell around the cell in the previous crystal is Frozen 
	 * the state of the cell be transitioned to Frozen in the next iteration
	 * 
	 * 
	 * */
	private CACellState getRule1State() {
		if (getDesiredNeighborsCount(CACellState.FROZEN) ==1 || this.getCellState() == CACellState.FROZEN) {
			return CACellState.FROZEN;
		}
		if (getDesiredNeighborsCount(CACellState.LIQUID) == 1 || this.getCellState() == CACellState.LIQUID) {
			return CACellState.LIQUID;
		}
		return CACellState.VAPOUR;

	}

	/*
	 * Rule Descriptions: 
	 * Random Crystal Growth
	 * Cells in the nature are not always uniform. Through this rule I would like to simulate random crystal like in nature 
	 * and do the simulation. In this rule, initially 15 random liquid state cells and 10 random Frozen cell states are created in the grid.
	 * 
	 * 
	 * */
	private CACellState getRule2State() {
		if (getDesiredNeighborsCount(CACellState.FROZEN) == 1 || this.getCellState() == CACellState.FROZEN) {
			return CACellState.FROZEN;
		}
		if (getDesiredNeighborsCount(CACellState.FROZEN) > 3 && this.getCellState() == CACellState.LIQUID) {
			return CACellState.FROZEN;
		}
		if (getDesiredNeighborsCount(CACellState.VAPOUR) > 4 && this.getCellState() == CACellState.LIQUID) {
			return CACellState.LIQUID;
		}
		if (getDesiredNeighborsCount(CACellState.VAPOUR) < 3 && getDesiredNeighborsCount(CACellState.VAPOUR) > 1 && this.getCellState() == CACellState.VAPOUR) {
			return CACellState.LIQUID;
		}
		return CACellState.VAPOUR;

	}

	/*
	 * Rule Descriptions: 
	 * Butterfly pattern
	 Crystal growth is very fascinating. So I have decided to check how the crystal grows 
	 if there are small cells of frozen and liquid cells in the shape of a star. 
	 Turns out it takes the shape of the butterfly with the help of following logic which is self-explanatory
	 */
	private CACellState getRule3State() {
		if (getDesiredNeighborsCount(CACellState.FROZEN) >1 && this.getCellState() == CACellState.VAPOUR) {
			return CACellState.FROZEN;
		}
		if (getDesiredNeighborsCount(CACellState.FROZEN)  == 1 && this.getCellState() == CACellState.VAPOUR) {
			return CACellState.LIQUID;
		}
		if (getDesiredNeighborsCount(CACellState.LIQUID) > 1 ) {
			return CACellState.FROZEN;
		}
		if (getDesiredNeighborsCount(CACellState.VAPOUR) > 3 && this.getCellState() == CACellState.LIQUID) {
			return CACellState.FROZEN;
		}
		return CACellState.VAPOUR;

	}

	/*
	 * Rule Descriptions: 
	 * Highway pattern
	 * Crystal growth is very fascinating. So I have decided to check how the crystal grows 
	 * if there are small cells of frozen and liquid cells in the shape of a big cross. 
	 * Turns out it takes the shape of the highway with the help of following logic which is self-explanatory
	 */
	private CACellState getRule4State() {
		if (getDesiredNeighborsCount(CACellState.FROZEN) >1 && this.getCellState() == CACellState.VAPOUR) {
			return CACellState.FROZEN;
		}
		if (getDesiredNeighborsCount(CACellState.FROZEN)  == 1 && this.getCellState() == CACellState.VAPOUR) {
			return CACellState.LIQUID;
		}
		if (getDesiredNeighborsCount(CACellState.LIQUID) > 1 ) {
			return CACellState.FROZEN;
		}
		if (getDesiredNeighborsCount(CACellState.VAPOUR) > 3 && this.getCellState() == CACellState.LIQUID) {
			return CACellState.FROZEN;
		}
		return CACellState.VAPOUR;
	}
	
	/*
	 * Rule Descriptions: 
	 * Garden
	 * Crystal growth is very fascinating. So I have decided to check how the crystal grows 
	 * if there are small cells of frozen and liquid cells in the equal gaps. 
	 * Turns out it takes the shape of the garden with the help of following logic which is self-explanatory
	 */
	private CACellState getRule5State() {
		if (getDesiredNeighborsCount(CACellState.FROZEN) == 1 && this.getCellState() == CACellState.VAPOUR) {
			return CACellState.FROZEN;
		}
		if (getDesiredNeighborsCount(CACellState.LIQUID)  == 1 && this.getCellState() == CACellState.VAPOUR) {
			return CACellState.LIQUID;
		}
		return CACellState.VAPOUR;
	}

}
