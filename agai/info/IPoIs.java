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

import java.util.ArrayList;
import java.util.List;

import agai.AGAI;
import agai.AGInfos;

import com.springrts.ai.AIFloat3;

/**
 * The Class AGPoIs.
 */
public class IPoIs {

	/** The Constant PoIAny. */
	public static final int PoIAny = Integer.MAX_VALUE; // FIXME: descending,
														// because we use the id
														// of the resources (0,
														// 1, 2, ... to avoid
														// collisions)

	/** The Constant PoIEnemy. */
	static final int PoIEnemy = Integer.MAX_VALUE - 1;

	/** The ai. */
	AGAI ai;

	/** The poi. */
	List<IPoI> poi;

	/**
	 * Instantiates a new aG po is.
	 * 
	 * @param ai
	 *            the ai
	 * @param infos 
	 */
	public IPoIs(AGAI ai, AGInfos infos) {
		this.ai = ai;
		this.poi = new ArrayList<IPoI>();
	}

	/**
	 * Adds the Point to the list of PoIs.
	 * 
	 * @param pos
	 *            the pos
	 * @param type
	 *            the type
	 */
	public void add(AIFloat3 pos, int type) {
		poi.add(new IPoI(pos, type));
	}

	/**
	 * Gets the PoI at pos.
	 * 
	 * @param index
	 *            the index
	 * @param type
	 *            the type
	 * 
	 * @return the po i
	 */
	public IPoI get(int index, int type) {
		int count = 0;
		for (int i = 0; i < poi.size(); i++) {
			if ((poi.get(i).getType() & type) != -1) {
				if (count == index)
					return poi.get(i);
				count++;
			}
		}
		return null;
	}

	/**
	 * Gets the nearest poi where no building is built.
	 * 
	 * @param curpos
	 *            the curpos
	 * @param type
	 *            the type
	 * 
	 * @return the nearest free poi
	 */
	public IPoI getNearestFreePoi(AIFloat3 curpos, int type) {
		return getNearestPoi(curpos, type, true, false);
	}

	/**
	 * Gets the nearest point of interest to scout.
	 * 
	 * @param curpos
	 *            position to search the nearest point from
	 * @param type
	 *            the type
	 * @param free
	 *            list all free PoIs?
	 * @param visited
	 *            list already visted PoIs?
	 * 
	 * @return the nearest poi
	 */
	private IPoI getNearestPoi(AIFloat3 curpos, int type, boolean free, boolean visited, boolean recursion) {
		double mindistance = Double.MAX_VALUE;
		double tmp;
		IPoI ret = null;
		if (poi.size() == 0) {
			ai.msg("getNearestPoi: No PoI found!");
			return ret;
		}
		for (int i = 0; i < poi.size(); i++) {
			if (((type == PoIAny) || (poi.get(i).getType() == type))
					&& !(visited && poi.get(i).isVisited())
					&& !(free && poi.get(i).isBuilt())) {
				tmp = ai.getInfos().getDistance(curpos, poi.get(i).getPos());
				if (tmp < mindistance) {
					mindistance = tmp;
					ret = poi.get(i);
				}
			}
		}
		if ((free) && (ret == null)) { // all points visited.. start from beginning
			if (recursion)
				return null;
			ai.msg("no valid point found, clearing visited flag");
			for (int i = 0; i < poi.size(); i++) {
				poi.get(i).setVisited(false);
			}
			return getNearestPoi(curpos, type, free, visited, true);
		}
		return ret;
	}

	/**
	 * Returns count of PoIs of type.
	 * 
	 * @param type
	 *            the type
	 * 
	 * @return the int
	 */
	public int size(int type) {
		int count = 0;
		for (int i = 0; i < poi.size(); i++) {
			if ((poi.get(i).getType() & type) != 0)
				count++;
		}
		return count;

	}
	public IPoI getNearestPoi(AIFloat3 curpos, int type, boolean free, boolean visited) {
		return getNearestPoi( curpos,  type,  free,  visited, false);
	}
}
