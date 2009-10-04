package agai.info;

import java.util.Comparator;
import java.util.LinkedList;

import agai.AGAI;

import com.springrts.ai.oo.UnitDef;

//TODO: Auto-generated Javadoc
//Use this Class to search for a Unit
abstract class IUnitProperty implements Comparator<IBuildTreeUnit>{
	protected AGAI ai;
	protected LinkedList <IUnitPropertyEvaluator> properties;

	protected IUnitProperty(AGAI ai){
		this.ai=ai;
		properties=new LinkedList<IUnitPropertyEvaluator>();
	}
	/**
	 * Checks if Unit is in List
	 * 
	 * @param unit the unit
	 * 
	 * @return true, if Unit is  in list
	 */
	abstract public boolean isInlist(UnitDef unit);
	
	/**
	 * To be called after the list is changed
	 *
	 * @param unit the unit
	 */
	protected void update(){
		for(int i=0; i<properties.size(); i++){
			properties.get(i).update();
		}
	}
	
	/**
	 * Checks if List can be sortet with this Property
	 * 
	 * @return true, if successful
	 */
	protected boolean sort(){
		return true;
	}
}
