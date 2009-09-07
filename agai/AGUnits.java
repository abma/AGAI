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

import java.util.List;
import java.util.ArrayList;


import com.springrts.ai.oo.*;

// TODO: Auto-generated Javadoc
/**
 * Class with list of all Units (and its tasks/status).
 */
public class AGUnits {
	
	/** The units. */
	private List <AGUnit> units=null;
	
	/** The ai. */
	private AGAI ai=null;
	
	/**
	 * Instantiates a new aG units.
	 * 
	 * @param ai the ai
	 */
	AGUnits(AGAI ai){
		units=new ArrayList <AGUnit>();
		unitDefs=ai.getClb().getUnitDefs();
		this.ai=ai;
	}
	
	/**
	 * Adds the.
	 * 
	 * @param unit the unit
	 */
	public AGUnit add(Unit unit){
        units.add(new AGUnit(ai, unit));
        ai.msg("size "+units.size());
        return units.get(units.size()-1);
		
	}
	
	/**
	 * Gets the unit.
	 * 
	 * @param unit the unit
	 * 
	 * @return the unit
	 */
	public AGUnit getUnit(Unit unit){
		ai.msg("size "+units.size());
		for(int i=0; i<units.size();i++)
			if (units.get(i).getUnit().equals(unit))
				return units.get(i);
		return null;
	}
	
	
	/**
	 * Search unit of unit type.
	 * 
	 * @param unit the unit
	 * 
	 * @return the aG unit
	 */
	public AGUnit getUnit(UnitDef unit){
		for (int i=0; i<units.size(); i++){
			if (units.get(i).getUnit().getDef().equals(unit))
				return units.get(i);
		}
		return null;
	}
	
	
	/**
	 * returns Unit of type, any idle type if type==null.
	 * 
	 * @param type the type
	 * 
	 * @return the idle
	 */
	public AGUnit getIdle(UnitDef type){
		for (int i=0; i<units.size(); i++){
			if (units.get(i).isIdle())
				if (type==null)
					return units.get(i);
				else if (units.get(i).getUnit().getDef().equals(type)) //FIXME?
					return units.get(i);
		}
		return null;
	}
	
	/**
	 * Returns a unit who can build the unitdef type.
	 * 
	 * @param type the type
	 * 
	 * @return the builder
	 */
	public AGUnit getBuilder(UnitDef type){ //FIXME: this function is really slow!
		for(int i=0; i<units.size();i++){
			List<UnitDef> buildOptions = units.get(i).getUnit().getDef().getBuildOptions();
	        for (UnitDef unitDef : buildOptions) {
	        	if (unitDef.equals(type)){
	        		return units.get(i);
	        	}
			}
		}
		return null;
	}
	

	/**
	 * Unit is destroyed.
	 * 
	 * @param unit the unit
	 * @param attacker the attacker
	 */
	public void destroyed(Unit unit, Unit attacker){
		for(int i=0; i<units.size();i++)
			if (units.get(i).getUnit().equals(unit)){
				units.get(i).destroyed();
				units.remove(i);
				return;
			}
		ai.msg("Couldn't find unit which was destroyed");
	}
	
	/** Gets the unit def. */
	private List<UnitDef> unitDefs = null;
	
	/**
	 * Gets the unit def.
	 * 
	 * @param type the type
	 * 
	 * @return the unit def
	 */
	public UnitDef getUnitDef(String type){
		UnitDef bldunit = null;
		for (UnitDef def : unitDefs)
			if (def.getName().equals(type))
			{
				bldunit = def;
				break;
			}
		if (bldunit==null)
			ai.msg("Warning: Couldn't find unit "+type);
		return bldunit;
	}

	/**
	 * Returns all Units of type unit.
	 * 
	 * @param type the type
	 * 
	 * @return the units
	 */
	public List <AGUnit> getUnits(UnitDef type) {
		List<AGUnit> res=new ArrayList<AGUnit>();
		for (int i=0; i<units.size(); i++){
			if (units.get(i).getUnit().getDef().equals(type))
				res.add(units.get(i));
		}
		return res;
	}

	/**
	 * Gets the energy production.
	 * 
	 * @param unit the unit
	 * 
	 * @return the energy production
	 */
	public float getEnergyProduction(UnitDef unit){
		return unit.getUpkeep(ai.getEnergy()) *-1 + 
			unit.getResourceMake(ai.getEnergy()) + 
			unit.getWindResourceGenerator(ai.getEnergy()) +
			unit.getTidalResourceGenerator(ai.getEnergy());		
	}

	public void dump() {
		for (int i=0; i<units.size(); i++){
			ai.msg(units.get(i).getUnit().hashCode() +" "+ units.get(i).toString());
		}
		
	}
}
