package agai.unit;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.Unit;

import agai.AGAI;

public class UnitBuilding extends AGUnit{

	protected UnitBuilding(AGAI ai, Unit unit) {
		super(ai, unit);
	}
	@Override
	public int moveTo(AIFloat3 pos){
		ai.msg("a building can't move!");
		return 0;
	}

}
