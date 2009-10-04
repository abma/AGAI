package agai.info;

import agai.AGAI;

import com.springrts.ai.oo.UnitDef;

class IUnitPropertyEvaluatorLosRadius extends IUnitPropertyEvaluator{
	public IUnitPropertyEvaluatorLosRadius(AGAI ai, float weighting, IUnitProperty prop) {
		super(ai, weighting, prop);
	}
	@Override
	public float getValue(UnitDef unit) {
		return unit.getLosRadius();
	}
	
}