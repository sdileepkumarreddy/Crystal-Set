package edu.neu.csye6200.ca;

import java.util.logging.Logger;



enum RuleNames {
	rule1, rule2, rule3
}

public class CARule extends CACell {

	private RuleNames ruleName; 


	private static Logger log = Logger.getLogger(CACell.class.getName());

	public CARule() {

	}
	public CARule(RuleNames ruleName, CACrystal crystal, CACellState initCellState) {
		super(crystal, initCellState);
		this.ruleName = ruleName;
	}

	@Override
	public CACellState getNextCellState() {

		if (ruleName.equals(RuleNames.rule1)) {
			return getRule1State();
		}
		
		if (ruleName.equals(RuleNames.rule2)) {
			return getRule2State();
		}
		if (ruleName.equals(RuleNames.rule3)) {
			return getRule3State();
		}
		else {
			return getCellState();
		}

	}


	private CACellState getRule1State() {
		if (getDesiredNeighborsCount(CACellState.FROZEN) ==1 || this.getCellState() == CACellState.FROZEN) {
			return CACellState.FROZEN;
		}
		if (getDesiredNeighborsCount(CACellState.LIQUID) == 1 || this.getCellState() == CACellState.LIQUID) {
			return CACellState.LIQUID;
		}
		return CACellState.VAPOUR;

	}
	
	private CACellState getRule2State() {
		if (getDesiredNeighborsCount(CACellState.FROZEN) == 1 || this.getCellState() == CACellState.FROZEN) {
			return CACellState.FROZEN;
		}
		return CACellState.VAPOUR;

	}
	private CACellState getRule3State() {
		if (getDesiredNeighborsCount(CACellState.FROZEN) ==1 || this.getCellState() == CACellState.FROZEN) {
			return CACellState.FROZEN;
		}
		return CACellState.VAPOUR;

	}




}
