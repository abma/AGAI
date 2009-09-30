package agai.info;

import agai.AGAI;

import com.springrts.ai.oo.UnitDef;

public class UnitPropertyEvaluatorPrice extends UnitPropertyEvaluator{
	public UnitPropertyEvaluatorPrice(AGAI ai, float weighting, UnitProperty prop) {
		super(ai, weighting, prop);
	}
	@Override
	public float getValue(UnitDef unit) {
		return ai.getAGU().getTotalPrice(unit)*-1;
	}
}