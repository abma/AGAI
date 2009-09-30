package agai.info;

import agai.AGAI;

import com.springrts.ai.oo.UnitDef;

public class UnitPropertyEvaluatorLosRadius extends UnitPropertyEvaluator{
	public UnitPropertyEvaluatorLosRadius(AGAI ai, float weighting, UnitProperty prop) {
		super(ai, weighting, prop);
	}
	@Override
	public float getValue(UnitDef unit) {
		return unit.getLosRadius();
	}
	
}