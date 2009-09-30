/*
 * Copyright (C) 2009 Matthias Ableitner (http://abma.de/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
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

package agai.unit;

import java.util.List;
import java.util.ArrayList;


import agai.AGAI;
import agai.info.BuildTreeUnit;

import com.springrts.ai.AIFloat3;
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
	public AGUnits(AGAI ai){
		units=new ArrayList <AGUnit>();
		unitDefs=ai.getClb().getUnitDefs();
		this.ai=ai;
	}
	
	/**
	 * Adds the.
	 * 
	 * @param unit the unit
	 * 
	 * @return the AG unit
	 */
	public AGUnit add(Unit unit){
		if (unit==null){
			ai.msg("Error: tried to add null");
			return null;
		}
        units.add(new AGUnit(ai, unit));
        ai.getAGI().getAGB().searchNode(unit.getDef()).incUnitcount();
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
		if (unit==null){
			ai.msg("Null requested!");
			return null;
		}
		for(int i=0; i<units.size();i++)
			if (units.get(i).getUnit().equals(unit))
				return units.get(i);
		return add(unit);
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
		if (type==null){
			ai.msg("Null!");
			return null;
		}
		for (int i=0; i<units.size(); i++){
			if (units.get(i).isIdle()){
				if (type==null)
					return units.get(i);
				else if ((units.get(i).getDef()!=null) && (units.get(i).getDef().equals(type)))
					return units.get(i);
			}
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
		for(int i=0; i<units.size();i++){ //walk through all units, and check if they can build type
			List<UnitDef> buildOptions = units.get(i).getUnit().getDef().getBuildOptions();
			int j;
			for (j=0; j<buildOptions.size(); j++){
				if (buildOptions.get(j).getUnitDefId()==type.getUnitDefId()){
					ai.msg("Builder found to build "+type.getName());
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
		ai.getAGI().getAGB().searchNode(unit.getDef()).decUnitCount();
		for(int i=units.size()-1; i>=0;i--)
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
		for (int i=units.size()-1; i>=0; i--){
			if (units.get(i).getDef()==null){ //FIXME: unit was killed (?)
				units.remove(i);
			}else if (units.get(i).getUnit().getDef().equals(type))
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

	/**
	 * Dump.
	 */
	public void dump() {
		for (int i=0; i<units.size(); i++){
			ai.msg(units.get(i).getUnit().hashCode() +" "+ units.get(i).toString());
		}
		
	}
	private float averageres=-1;

	/**
	 * Gets the production of a Unit
	 *
	 * @param unit the unit
	 * @param res the res
	 *
	 * @return the production
	 */
	public float getProduction(UnitDef unit, Resource res){
		if (averageres==-1){
			List <AIFloat3> list = ai.getClb().getMap().getResourceMapSpotsPositions(res);
			float sum=0;
			for (int i=0; i<list.size(); i++){
				sum=sum+list.get(i).y;
			}
			if (list.size()>0)
				averageres=sum/list.size();
		}
		//TODO: calculate energy a unit produces on map

		float wind=Math.min(unit.getWindResourceGenerator(res),ai.getClb().getMap().getMinWind()); //worst case
		float tidal=unit.getTidalResourceGenerator(res)*ai.getClb().getMap().getTidalStrength();
		return
			(unit.getUpkeep(res) *-1) + unit.getResourceMake(res) +
			wind + tidal + unit.getMakesResource(res) +
			(unit.getExtractsResource(res)*averageres);
	}

	/**
	 * Returns true, if a Unit has an Upkeep
	 *
	 * @param unit the unit
	 * @param res the res
	 *
	 * @return true, if successful
	 */
	public boolean hasUpKeep(UnitDef unit, Resource res){
		List <Resource> ress=ai.getClb().getResources();
		for (int i=0; i<ress.size(); i++){
			if (!ress.get(i).equals(res)){
				if (unit.getUpkeep(res)>0)
					return true;
			}
		}
		return false;
	}
	/**
	 * Gets the total price.
	 *
	 * @param unit the unit
	 *
	 * @return the total price
	 */
	public float getTotalPrice(UnitDef unit){
		float cost=0;
		List <Resource> res=ai.getClb().getResources();
		for (int i=0; i<res.size(); i++ ){
			cost=cost+unit.getCost(res.get(i));
		}
		return cost;
	}

	public void dumpUnitDefs(){
		List <BuildTreeUnit> list= ai.getAGI().getAGB().getUnitList();
		for (int i=0; i<list.size(); i++){
			UnitDef u=list.get(i).getUnit();
			System.out.print(u.getName());
			System.out.print("\t" + u.getHumanName());

			System.out.print("\t" + u.getUpkeep(ai.getEnergy()));
			System.out.print("\t" + u.getUpkeep(ai.getMetal()));

			System.out.print("\t" + u.getCost(ai.getEnergy()));
			System.out.print("\t" + u.getCost(ai.getMetal()));

			System.out.print("\t" + u.getTidalResourceGenerator(ai.getEnergy()));
			System.out.print("\t" + u.getTidalResourceGenerator(ai.getMetal()));

			System.out.print("\t" + u.getResourceMake(ai.getEnergy()));
			System.out.print("\t" + u.getResourceMake(ai.getMetal()));

			System.out.print("\t" + u.getMakesResource(ai.getEnergy()));
			System.out.print("\t" + u.getMakesResource(ai.getMetal()));

			System.out.print("\t" + u.getWindResourceGenerator(ai.getEnergy()));
			System.out.print("\t" + u.getWindResourceGenerator(ai.getMetal()));

			System.out.println();
		}
	}
}
