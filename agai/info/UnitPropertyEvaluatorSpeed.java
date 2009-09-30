package agai.info;

import agai.AGAI;

import com.springrts.ai.oo.UnitDef;

public class UnitPropertyEvaluatorSpeed extends UnitPropertyEvaluator{
	public UnitPropertyEvaluatorSpeed(AGAI ai, float weighting, UnitProperty prop) {
		super(ai, weighting, prop);
	}
	@Override
	public float getValue(UnitDef unit) {
		return unit.getSpeed();
	}
}