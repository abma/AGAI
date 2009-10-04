package agai.info;

import java.util.List;

import agai.AGAI;

import com.springrts.ai.oo.UnitDef;

/*

 Möglichkeit um gute Einheiten zu finden:

 min + max einer bestimmten eigenschaft von allen einheiten finden (LOS, Speed, ...)

 die werte normalisieren:

 */

abstract class IUnitPropertyEvaluator {

	final static int NORMMIN = 0;

	protected AGAI ai;
	private float average;
	private float max;
	private float min;
	private float NORMMAX = 1;

	/**
	 * Instantiates a new aG unit property evaluator.
	 * 
	 * @param ai
	 *            the ai
	 * @param max
	 *            the max value to normalize to
	 * @param prop
	 *            the prop
	 */
	IUnitPropertyEvaluator(AGAI ai, float max, IUnitProperty prop) {
		this.ai = ai;
		NORMMAX = max;
	}

	/**
	 * Gets the average value of a property.
	 * 
	 * @param unit
	 *            the unit
	 * 
	 * @return the average
	 */
	float getAverageComp(UnitDef unit) {
		return this.getValue(unit) - average;
	}

	/**
	 * Normalize a value and multiplicates with weighting.
	 * 
	 * @param unit
	 *            the unit
	 * 
	 * @return the float
	 */
	float getNormValue(UnitDef unit) {
		return (((getValue(unit) - min) / (max - min)) * (NORMMAX - NORMMIN))
				+ NORMMIN;
	}

	/**
	 * Returns the value, bigger means better, lower is bad (if it is different,
	 * add a *-1)
	 * 
	 * @param unit
	 *            the unit
	 * 
	 * @return the value
	 */
	protected abstract float getValue(UnitDef unit);

	/**
	 * Update values.
	 */
	void update() {
		updateMinMax();
		updateAverageValue();
	}

	private void updateAverageValue() {
		List<IBuildTreeUnit> list = ai.getInfos().getAGB().getUnitList();
		float tmp = 0;
		for (int i = 0; i < list.size(); i++) {
			tmp = tmp + getValue(list.get(i).getUnit());
		}
		this.average = tmp / list.size();
	}

	/**
	 * Sets the min and max for from getValue.
	 * 
	 * @param unit
	 *            the unit
	 */
	private void updateMinMax() {
		List<IBuildTreeUnit> list = ai.getInfos().getAGB().getUnitList();
		for (int i = 0; i < list.size(); i++) {
			float tmp = getValue(list.get(i).getUnit());
			if (tmp > max)
				max = tmp;
			else if (tmp < min)
				min = tmp;
		}
	}

}
