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
		} else {
			return getCellState();
		}

	}
	public CARule() {
		
	}
	
	private CACellState getRule1State() {
		return getCellState();
	}


}
