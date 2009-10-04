package agai.info;

import agai.AGAI;

import com.springrts.ai.oo.UnitDef;

class IUnitPropertyEvaluatorSpeed extends IUnitPropertyEvaluator {
	public IUnitPropertyEvaluatorSpeed(AGAI ai, float weighting,
			IUnitProperty prop) {
		super(ai, weighting, prop);
	}

	@Override
	public float getValue(UnitDef unit) {
		return unit.getSpeed();
	}
}