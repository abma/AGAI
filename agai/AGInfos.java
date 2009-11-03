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

import agai.info.IBuildTree;
import agai.info.IBuildTreeUnit;
import agai.info.IPoIs;
import agai.info.IResources;
import agai.info.ISearchUnit;
import agai.info.ISectors;
import agai.info.ITime;
import agai.unit.AGUnit;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.UnitDef;

// TODO: Auto-generated Javadoc
/**
 * The Class Info.
 */
public class AGInfos {

	/** The a gb. */
	private IBuildTree aGB;

	/** The a gf. */
	private ISearchUnit search;

	/** The a gp. */
	private IPoIs aGP;

	private IResources resources;

	private ISectors sectors;

	private ITime time;
	private AGAI ai;

	public ISectors getSectors() {
		return sectors;
	}

	public IResources getResources() {
		return resources;
	}

	/**
	 * Instantiates a new info.
	 * 
	 * @param ai
	 *            the ai
	 */
	AGInfos(AGAI ai) {
		this.aGB = new IBuildTree(ai, this);
		this.search = new ISearchUnit(ai, this);
		this.aGP = new IPoIs(ai, this);
		this.sectors = new ISectors(ai, this);
		this.resources = new IResources(ai);
		this.time = new ITime(ai);
		this.ai=ai;
	}

	public ITime getTime() {
		return time;
	}

	/**
	 * Gets the aGB.
	 * 
	 * @return the aGB
	 */
	public IBuildTree getAGB() {
		return aGB;
	}

	public ISearchUnit getSearch() {
		return search;
	}

	/**
	 * Gets the aGP.
	 * 
	 * @return the aGP
	 */
	public IPoIs getAGP() {
		return aGP;
	}

	/**
	 * Gets the difference.
	 * 
	 * @param pos1
	 *            the pos1
	 * @param pos2
	 *            the pos2
	 * 
	 * @return the difference
	 */
	public double getDistance(AIFloat3 pos1, AIFloat3 pos2) {
		return Math.sqrt((Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.z
				- pos2.z, 2)));
	}

	public void UnitCreated(AGUnit u) {
		IBuildTreeUnit info =  aGB.searchNode(u.getDef());
		if (info==null){ //rebuild graph because we have a unit that isn't in the buildgraph!
			ai.msg("Got unit not in buildgraph! Rebulding it...");
			aGB.updateGraph();
			info = aGB.searchNode(u.getDef());
		}
		info.incUnitcount();
		info.setPlannedunits(info.getPlannedunits()-1);
		resources.UnitCreated(u);
	}
	
	public void UnitDestroyed(AGUnit u){
		aGB.searchNode(u.getDef()).decUnitCount();
		resources.UnitDestroyed(u);
	}

	public void IncPlannedUnit(UnitDef unitDef) {
		IBuildTreeUnit info = aGB.searchNode(unitDef);
		info.setPlannedunits(info.getPlannedunits()+1);
	}

}
