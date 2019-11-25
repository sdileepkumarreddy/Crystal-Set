package edu.neu.csye6200.ca;

import java.util.logging.Logger;



enum RuleNames {
	rule1, rule2, rule3
}

public class CARule extends CACell {

	private RuleNames ruleName; 


	private static Logger log = Logger.getLogger(CACell.class.getName());

	public CARule(RuleNames ruleName, CACrystal crystal, CACellState initCellState) {
		super(crystal, initCellState); // call to MACell
		this.ruleName = ruleName;
	}

	@Override
	public CACellState getNextCellState() {

		if (ruleName.equals(RuleNames.rule1)) {
			return getRule1State();
		} 
		if (ruleName.equals(RuleNames.rule2)) {
			return getRule2State();
		}else {
			return getCellState();
		}

	}
	public CARule() {
		
	}
	
	private CACellState getRule1State() {
		if (getTDNeighborsCount(CACellState.FROZEN) == 2) {
			return CACellState.LIQUID;
		} else if (getTDNeighborsCount(CACellState.FROZEN) == 1) {
			return CACellState.FROZEN;
		}
		return getCellState();
	}
	
	private CACellState getRule2State() {
		if (getNeighborsCount(CACellState.FROZEN) == 1 || this.getCellState() == CACellState.FROZEN) {
			return CACellState.FROZEN;
		}
		return CACellState.VAPOUR;

	}


	
	
}
