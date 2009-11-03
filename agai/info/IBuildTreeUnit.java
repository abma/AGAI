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

import java.util.LinkedList;
import java.util.List;

import agai.AGAI;

import com.springrts.ai.oo.Resource;
import com.springrts.ai.oo.UnitDef;

// TODO: Auto-generated Javadoc
/**
 * The Class BuildTreeUnit is an node in the Buildtree
 */
public class IBuildTreeUnit {

	/** The backlink. */
	private LinkedList<IBuildTreeUnit> backlink; // backlinks to Nodes linking
													// to this node

	/** The mark. */
	private int mark; // for search path

	/** The nodes. */
	private LinkedList<IBuildTreeUnit> nodes; // links to other nodes

	/** The parent. */
	private IBuildTreeUnit parent; // for search path

	/** The plannedunits. */
	private int plannedunits;

	/** The unit. */
	private UnitDef unit;

	/** The unitcount. */
	private int unitcount;
	
	private IResource cost;
	private AGAI ai;

	public IResource getCost() {
		if (cost==null){
			List <Resource> res=ai.getClb().getResources();
			cost = new IResource(ai);
			for(int i=0; i<res.size(); i++){
				cost.setCurrent(i, unit.getCost(res.get(i)));
			}
		}
		return cost;
	}

	/**
	 * Instantiates a new builds the tree unit.
	 * 
	 * @param unit
	 *            the unit
	 */
	IBuildTreeUnit(AGAI ai, UnitDef unit) {
		nodes = new LinkedList<IBuildTreeUnit>();
		backlink = new LinkedList<IBuildTreeUnit>();
		this.ai=ai;
		this.unit = unit;
		this.mark = 0;
		this.parent = null;
		this.unitcount = 0;
	}

	/**
	 * Adds the node.
	 * 
	 * @param parent
	 *            the parent
	 * 
	 * @return the builds the tree unit
	 */
	public IBuildTreeUnit addNode(IBuildTreeUnit parent) {
		nodes.add(parent);
		parent.backlink.add(this);
		return parent;
	}

	/**
	 * Dec unit count.
	 */
	public void decUnitCount() {
		if (unitcount>0)
			unitcount--;
	}

	/**
	 * Gets the backlink.
	 * 
	 * @return the backlink
	 */
	public LinkedList<IBuildTreeUnit> getBacklink() {
		return backlink;
	}

	/**
	 * Gets the mark.
	 * 
	 * @return the mark
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public LinkedList<IBuildTreeUnit> getNodes() {
		return nodes;
	}

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public IBuildTreeUnit getParent() {
		return parent;
	}

	/**
	 * Gets the plannedunits.
	 * 
	 * @return the plannedunits
	 */
	public int getPlannedunits() {
		return plannedunits;
	}

	/**
	 * Gets the unit.
	 * 
	 * @return the unit
	 */
	public UnitDef getUnit() {
		return this.unit;
	}

	/**
	 * Gets the unitcount.
	 * 
	 * @return the unitcount
	 */
	public int getUnitcount() {
		return unitcount;
	}

	/**
	 * Inc unitcount.
	 */
	public void incUnitcount() {
		plannedunits--;
		unitcount++;
	}

	/**
	 * Sets the mark.
	 * 
	 * @param mark
	 *            the new mark
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * Sets the parent.
	 * 
	 * @param parent
	 *            the new parent
	 */
	public void setParent(IBuildTreeUnit parent) {
		this.parent = parent;
	}

	/**
	 * Sets the plannedunits.
	 * 
	 * @param plannedunits
	 *            the new plannedunits
	 */
	public void setPlannedunits(int plannedunits) {
		if (plannedunits<0)
			this.plannedunits = 0;
		else
			this.plannedunits = plannedunits;
	}

	/**
	 * Can build.
	 * 
	 * @return true, if a builder is avaiable to build this unit
	 */
	public boolean canBuild() {
		return (ai.getInfos().getAGB().getBuilder(unit)!=null);
	}
	
	public String toString(){
		String res="";
		res = unit.getName() +"\t" +unitcount +"\t" + plannedunits;  
		return res;
	}
	public int getId(){
		return unit.getUnitDefId();
	}
}