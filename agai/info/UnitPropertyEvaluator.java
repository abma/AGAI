package agai.info;

import java.util.List;

import agai.AGAI;

import com.springrts.ai.oo.UnitDef;

/*

Möglichkeit um gute Einheiten zu finden:

min + max einer bestimmten eigenschaft von allen einheiten finden (LOS, Speed, ...)

die werte normalisieren:

 */

abstract class UnitPropertyEvaluator{
	
	/**
	 * Returns the value, bigger means better, lower is bad (if it is different, add a *-1)
	 * 
	 * @param unit the unit
	 * 
	 * @return the value
	 */
	protected abstract float getValue(UnitDef unit); 
	
	final static int NORMMIN = 0;
	private float NORMMAX = 1;
	private float max;
	private float min;
	private float average;

	/**
	 * Gets the average value of a property.
	 *
	 * @param unit the unit
	 *
	 * @return the average
	 */
	float getAverageComp(UnitDef unit) {
		return this.getValue(unit)-average;
	}

	protected AGAI ai;
	
	/**
	 * Instantiates a new aG unit property evaluator.
	 * 
	 * @param ai the ai
	 * @param max the max value to normalize to
	 * @param prop the prop
	 */
	UnitPropertyEvaluator(AGAI ai, float max, UnitProperty prop){
		this.ai=ai;
		NORMMAX=max;
	}

	private void updateAverageValue(){
		List <BuildTreeUnit> list=ai.getAGI().getAGB().getUnitList();
		float tmp=0;
		for (int i=0; i<list.size(); i++){
			tmp=tmp + getValue(list.get(i).getUnit());
		}
		this.average=tmp/list.size();
	}

	/**
	 * Sets the min and max for from getValue.
	 * 
	 * @param unit the unit
	 */
	private void updateMinMax(){
		List <BuildTreeUnit> list=ai.getAGI().getAGB().getUnitList();
		for(int i=0; i<list.size(); i++){
			float tmp=getValue(list.get(i).getUnit());
			if (tmp>max)
				max=tmp;
			else if (tmp<min)
				min=tmp;
		}
	}

	/**
	 * Update values.
	 */
	void update(){
		updateMinMax();
		updateAverageValue();
	}

	/**
	 * Normalize a value and multiplicates with weighting.
	 * 
	 * @param unit the unit
	 * 
	 * @return the float
	 */
	float getNormValue(UnitDef unit){
		return (((getValue(unit) - min) / (max - min)) * (float)(NORMMAX - NORMMIN)) + NORMMIN;   
	}
	
}
