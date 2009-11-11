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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import agai.AGAI;
import agai.AGInfos;

/**
 * The Class ISearch to find Units.
 */
public class ISearchUnit {

	private AGAI ai;

	/**
	 * Instantiates a new aG filter.
	 * 
	 * @param ai
	 *            the ai
	 * @param infos 
	 */
	public ISearchUnit(AGAI ai, AGInfos infos) {
		this.ai=ai;
	}

	/**
	 * Search for a unit with UnitPropertys, the first props is used to sort the
	 * list.
	 * 
	 * @param props
	 *            the props
	 * 
	 * @return the list< ag build tree unit>
	 */
	public List<IBuildTreeUnit> Filter(IUnitProperty props) {
		Collection<IBuildTreeUnit> list= ai.getInfos().getAGB().getUnitList();
		LinkedList<IBuildTreeUnit> res = new LinkedList<IBuildTreeUnit>();
		Iterator<IBuildTreeUnit> i = list.iterator();
		while(i.hasNext()){
			IBuildTreeUnit unit=i.next();
			if((props==null) || (props.isInlist(unit.getUnit())))
				res.add(unit);
		}
		if (props == null)
			return res;
		props.update();
		if (res.size() > 0) {
			if (props.sort()) {
				Collections.sort(res, props);
				return res;
			}
		}
		return res;
	}

}
