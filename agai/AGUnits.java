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

package agai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import agai.info.IResource;
import agai.unit.AGUnit;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.Resource;
import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.UnitDef;

// TODO: Auto-generated Javadoc
/**
 * Class with list of all Units (and its tasks/status).
 */
public class AGUnits {

	/**
	 * The Enum ElementType.
	 */
	public static enum ElementType {

		/** The unit is amphibian. */
		unitAmphibian(16),

		/** The any unit. */
		unitAny(0),

		/** The unit can fly. */
		unitFly(1),

		/** The unit is at land. */
		unitLand(8),

		/** The unit is sub. */
		unitSub(4),

		/** The unit can swim. */
		unitSwim(2);

		/** The type. */
		private int type;

		/**
		 * Instantiates a new element type.
		 * 
		 * @param type
		 *            the type
		 */
		ElementType(int type) {
			this.type = type;
		}

		/**
		 * Gets the type.
		 * 
		 * @return the type
		 */
		public int getType() {
			return type;
		}
	}

	/** The ai. */
	private AGAI ai = null;

	/** The averageres. */
	private float averageres = -1;

	/** The units. */
	private HashMap<Integer, AGUnit> units = null;

	public Collection<AGUnit> getUnits() {
		return units.values();
	}

	/**
	 * Instantiates a new aG units.
	 * 
	 * @param ai
	 *            the ai
	 */
	public AGUnits(AGAI ai) {
		units = new HashMap<Integer, AGUnit> ();
		this.ai = ai;
		List<Unit> list = ai.getClb().getTeamUnits();
		for (int i = 0; i < list.size(); i++) {
			add(list.get(i));
		}
	}

	/**
	 * Adds the Unit to list
	 * 
	 * @param unit
	 *            the unit
	 * 
	 * @return the AG unit
	 */
	public AGUnit add(Unit unit) {
		if (unit == null) {
			ai.msg("Error: tried to add null");
			return null;
		}
		AGUnit u=new AGUnit(ai, unit);
		units.put(unit.hashCode(), u);
		AGInfos info=ai.getInfos();
		if (info!=null) //avoid null reference on init
			info.UnitCreated(u);
		return u;
	}

	/**
	 * Unit is destroyed.
	 * 
	 * @param unit
	 *            the unit
	 * @param attacker
	 *            the attacker
	 */
	public void destroyed(Unit unit, Unit attacker) {
		AGUnit u=units.get(unit.hashCode());
		ai.getInfos().UnitDestroyed(u);
		units.remove(unit.hashCode());
		if (u==null)
			ai.msg("Couldn't find unit which was destroyed");
	}

	/**
	 * Dump.
	 */
	public void dump() {
		Iterator<AGUnit> iterator = units.values().iterator();
		while (iterator.hasNext()){
			AGUnit u = iterator.next();
			ai.msg(u.hashCode() + " "+ u.toString());
		}
	}

	/**
	 * Employ idle.
	 */
	public void employIdle() {
		Iterator<AGUnit> iterator = units.values().iterator();
		while (iterator.hasNext()){
			AGUnit u = iterator.next();
			if (u.isIdle())
				u.fetchTask();
		}
	}

	/**
	 * Gets the element type.
	 * 
	 * @param unit
	 *            the unit
	 * 
	 * @return the element type
	 */
	private ElementType getElementType(UnitDef unit) {
		if (unit.isAbleToFly())
			return ElementType.unitFly;
		if (unit.isAbleToHover())
			return ElementType.unitAmphibian;
		if (unit.isLevelGround())
			return ElementType.unitLand;
		if (unit.getMinWaterDepth() > 0)
			if (unit.getWaterline() > 0)
				return ElementType.unitSub;
			else
				return ElementType.unitSwim;
		ai.msg("Unknown Unittype: " + unit.getName());
		return ElementType.unitAny;
	}

	/**
	 * Gets the production of a Unit.
	 * 
	 * @param unit
	 *            the unit
	 * @param res
	 *            the res
	 * 
	 * @return the production
	 */
	public float getProduction(UnitDef unit, Resource res) {
		if (averageres == -1) {
			List<AIFloat3> list = ai.getClb().getMap().getResourceMapSpotsPositions(res);
			float sum = 0;
			for (int i = 0; i < list.size(); i++) {
				sum = sum + list.get(i).y;
			}
			if (list.size() > 0)
				averageres = sum / list.size();
		}
		// TODO: calculate energy a unit produces on map
		float wind = Math.min(unit.getWindResourceGenerator(res), ai.getClb().getMap().getMinWind()); // worst case
		float tidal = unit.getTidalResourceGenerator(res)* ai.getClb().getMap().getTidalStrength();
		return (unit.getUpkeep(res) * -1) + unit.getResourceMake(res) + wind
				+ tidal + unit.getMakesResource(res)
				+ (unit.getExtractsResource(res) * averageres);
	}

	/**
	 * Gets the total price.
	 * 
	 * @param unit
	 *            the unit
	 * 
	 * @return the total price
	 */
	public float getTotalPrice(UnitDef unit) {
		float cost = 0;
		List<Resource> res = ai.getClb().getResources();
		for (int i = 0; i < res.size(); i++) {
			cost = cost + unit.getCost(res.get(i));
		}
		return cost;
	}

	/**
	 * Gets the unit.
	 * 
	 * @param unit
	 *            the unit
	 * 
	 * @return the unit
	 */
	public AGUnit getUnit(Unit unit) {
		if (unit == null) {
			ai.msg("Null requested!");
			return null;
		}
		AGUnit ret=units.get(unit.hashCode());
		if (ret==null)
			ret=add(unit);
		return ret;
	}

	/**
	 * Search unit of unit type.
	 * 
	 * @param unit
	 *            the unit
	 * 
	 * @return the aG unit
	 */
	public AGUnit getUnit(UnitDef unit) {
		for (int i = 0; i < units.size(); i++) {
			if (units.get(i).getUnit().getDef().equals(unit))
				return units.get(i);
		}
		return null;
	}

	/**
	 * Returns all Units of type unit.
	 * 
	 * @param type
	 *            the type
	 * 
	 * @return the units
	 */
	public List<AGUnit> getUnits(UnitDef type) {
		List<AGUnit> res = new ArrayList<AGUnit>();
		Iterator<AGUnit> iterator = units.values().iterator();
		while (iterator.hasNext()){
			AGUnit u=iterator.next();
			if (u.getDef() == null) { // FIXME: unit was killed (?)
				units.remove(u.hashCode());
			} else if (u.getUnit().getDef().equals(type)){
				res.add(u);
			}
		}
		return res;
	}

	/**
	 * Returns true, if a Unit has an Upkeep.
	 * 
	 * @param unit
	 *            the unit
	 * @param res
	 *            the res
	 * 
	 * @return true, if successful
	 */
	public boolean hasUpKeep(UnitDef unit, Resource res) {
		List<Resource> ress = ai.getClb().getResources();
		for (int i = 0; i < ress.size(); i++) {
			if (!ress.get(i).equals(res)) {
				if (unit.getUpkeep(res) > 0)
					return true;
			}
		}
		return false;
	}

	/**
	 * Unit in type.
	 * 
	 * @param unit
	 *            the unit
	 * @param type
	 *            the type
	 * 
	 * @return true, if successful
	 */
	public boolean UnitInType(UnitDef unit, ElementType type) {
		if (type == ElementType.unitAny)
			return true;
		ElementType type1 = getElementType(unit);
		return type == type1;
	}

	public IResource getPrice(UnitDef unit){
		IResource price=new IResource(ai);
		for (int i=0; i<price.size(); i++){
			Resource r=ai.getClb().getResources().get(i);
			price.setCurrent(i, unit.getCost(r));
		}
		return price;
	}
}
