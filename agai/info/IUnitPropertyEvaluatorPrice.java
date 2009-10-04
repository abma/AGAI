package agai.info;

import agai.AGAI;

import com.springrts.ai.oo.UnitDef;

class IUnitPropertyEvaluatorPrice extends IUnitPropertyEvaluator{
	public IUnitPropertyEvaluatorPrice(AGAI ai, float weighting, IUnitProperty prop) {
		super(ai, weighting, prop);
	}
	@Override
	public float getValue(UnitDef unit) {
		return ai.getUnits().getTotalPrice(unit)*-1;
	}
}