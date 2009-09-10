/*
 * Copyright (C) 2009 Matthias Ableitner (http://abma.de/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package agai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;

import com.springrts.ai.oo.UnitDef;


class AGUnitPropertyEvaluatorLosRadius extends AGUnitPropertyEvaluator{
	AGUnitPropertyEvaluatorLosRadius(AGAI ai, float weighting, AGUnitProperty prop) {
		super(ai, weighting, prop);
	}
	@Override
	public float getValue(UnitDef unit) {
		return unit.getLosRadius();
	}
	
}
class AGUnitPropertyEvaluatorSpeed extends AGUnitPropertyEvaluator{
	AGUnitPropertyEvaluatorSpeed(AGAI ai, float weighting, AGUnitProperty prop) {
		super(ai, weighting, prop);
	}
	@Override
	public float getValue(UnitDef unit) {
		return unit.getSpeed();
	}
}

class AGUnitPropertyEvaluatorPrice extends AGUnitPropertyEvaluator{
	AGUnitPropertyEvaluatorPrice(AGAI ai, float weighting, AGUnitProperty prop) {
		super(ai, weighting, prop);
	}
	@Override
	public float getValue(UnitDef unit) {
		return ai.getTotalPrice(unit)*-1;
	}
}


/*

 Möglichkeit um gute Einheiten zu finden:
 
 min + max einer bestimmten eigenschaft von allen einheiten finden (LOS, Speed, ...)
 
 die werte normalisieren:
 
  */

abstract class AGUnitPropertyEvaluator{
	
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
	protected AGAI ai;
	
	/**
	 * Instantiates a new aG unit property evaluator.
	 * 
	 * @param ai the ai
	 * @param max the max value to normalize to
	 * @param prop the prop
	 */
	AGUnitPropertyEvaluator(AGAI ai, float max, AGUnitProperty prop){
		this.ai=ai;
		NORMMAX=max;
		
	}

	/**
	 * Sets the min and max for from getValue.
	 * 
	 * @param unit the unit
	 */
	public void updateMinMax(UnitDef unit){
		List <AGBuildTreeUnit> list=ai.getAGB().getUnitList();
		for(int i=0; i<list.size(); i++){
			float tmp=getValue(list.get(i).getUnit());
			if (tmp>max)
				max=tmp;
			else if (tmp<min)
				min=tmp;
		}
	}
	
	/**
	 * Normalize a value and multiplicates with weighting.
	 * 
	 * @param unit the unit
	 * 
	 * @return the float
	 */
	public float getNormValue(UnitDef unit){
		return (((getValue(unit) - min) / (max - min)) * (float)(NORMMAX - NORMMIN)) + NORMMIN;   
	}
	
}


// TODO: Auto-generated Javadoc
// Use this Class to search for a Unit
abstract class AGUnitProperty implements Comparator<AGBuildTreeUnit>{
	protected AGAI ai;
	protected LinkedList <AGUnitPropertyEvaluator> properties;

	AGUnitProperty(AGAI ai){
		this.ai=ai;
		properties=new LinkedList<AGUnitPropertyEvaluator>();
	}
	/**
	 * Checks if Unit is in List
	 * 
	 * @param unit the unit
	 * 
	 * @return true, if Unit is  in list
	 */
	abstract public boolean isInlist(UnitDef unit);
	
	public void updateMinMax(UnitDef unit){
		for(int i=0; i<properties.size(); i++){
			properties.get(i).updateMinMax(unit);
		}
	}
	
	/**
	 * Checks if List can be sortet with this Property
	 * 
	 * @return true, if successful
	 */
	public boolean sort(){
		return true;
	}
}

/**
 * The Class AGFilter to find Units.
 */
public class AGFilter{
	
	/** The list. */
	private LinkedList <AGBuildTreeUnit> list;
	
	/**
	 * Instantiates a new aG filter.
	 * 
	 * @param ai the ai
	 */
	AGFilter(AGAI ai){
		list=ai.getAGB().getUnitList();
	}
	
	/**
	 * Search for a unit with UnitPropertys, the first props is used to sort the list.
	 * 
	 * @param props the props
	 * 
	 * @return the list< ag build tree unit>
	 */
	public List <AGBuildTreeUnit> Filter(AGUnitProperty props){
		if (props==null)
			return list;
		LinkedList<AGBuildTreeUnit> res=new LinkedList<AGBuildTreeUnit>();
		for(int i=0; i<list.size();i++){
			if (props.isInlist(list.get(i).getUnit())){
				res.add(list.get(i));
			}
		}
	
		if (res.size()>0){
			if (props.sort()){
				Collections.sort(res,props);
				return res;
			}
		}
		return res;
	}


	
}
