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

import java.util.List;

import agai.AGAI;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.Resource;
import com.springrts.ai.oo.Unit;

/**
 * The Class PoIMap.
 */
public class IPoI { // Point of Interest
	/** The pos. */
	private AIFloat3 pos;

	/** The type. */
	private int type = -1;

	/** The visited. */
	private boolean visited;

	private AGAI ai;

	/**
	 * Instantiates a new po i map.
	 * 
	 * @param pos
	 *            the pos
	 * @param type
	 *            the type
	 */
	IPoI(AGAI ai, AIFloat3 pos, int type) {
		this.pos = pos;
		this.type = type;
		this.ai=ai;
		visited = false;
	}

	/**
	 * Gets the pos.
	 * 
	 * @return the pos
	 */
	public AIFloat3 getPos() {
		return this.pos;
	}

	/**
	 * Type.
	 * 
	 * @return the int
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * Checks if is built.
	 * 
	 * @return true, if is built
	 */
	public boolean isBuilt() {
		Resource res = ai.getClb().getResources().get(type);
		float radius;
		if (res!=null)
			radius=ai.getClb().getMap().getExtractorRadius(res);
		else
			radius=ai.getInfos().getSectors().getSectorSize();
		List<Unit> units = ai.getClb().getFriendlyUnitsIn(pos, radius);
		for(int j=0; j<units.size(); j++){
			if (units.get(j).getDef().getExtractsResource(res)>0){
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets if the PoI was already visited (or if a building is built there,
	 * visiting isn't necessary).
	 * 
	 * @return the visited
	 */
	public boolean isVisited() {
		return visited;
	}

	/**
	 * Sets the visited.
	 * 
	 * @param visited
	 *            the new visited
	 */
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
}