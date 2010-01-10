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
package agai.info;

import java.util.Comparator;
import java.util.LinkedList;

import agai.AGAI;

import com.springrts.ai.oo.UnitDef;

//TODO: Auto-generated Javadoc
//Use this Class to search for a Unit
abstract class IUnitProperty implements Comparator<IBuildTreeUnit> {
	protected AGAI ai;
	protected LinkedList<IUnitPropertyEvaluator> properties;

	protected IUnitProperty(AGAI ai) {
		this.ai = ai;
		properties = new LinkedList<IUnitPropertyEvaluator>();
	}

	/**
	 * Checks if Unit is in List
	 * 
	 * @param unit
	 *            the unit
	 * 
	 * @return true, if Unit is in list
	 */
	abstract public boolean isInlist(UnitDef unit);

	/**
	 * Checks if List can be sortet with this Property
	 * 
	 * @return true, if successful
	 */
	protected boolean sort() {
		return true;
	}

	/**
	 * To be called after the list is changed.
	 */
	protected void update() {
		for (int i = 0; i < properties.size(); i++) {
			properties.get(i).update();
		}
	}
}
