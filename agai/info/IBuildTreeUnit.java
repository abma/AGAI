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

import com.springrts.ai.oo.UnitDef;

// TODO: Auto-generated Javadoc
/**
 * The Class BuildTreeUnit is an node in the Buildtree 
 */
public class IBuildTreeUnit{
	
	/** The nodes. */
	private LinkedList <IBuildTreeUnit> nodes; //links to other nodes
	
	/** The backlink. */
	private LinkedList <IBuildTreeUnit> backlink; //backlinks to Nodes linking to this node
	
	/** The unit. */
	private UnitDef unit;
	
	/** The mark. */
	private int mark; //for search path
	
	/** The parent. */
	private IBuildTreeUnit parent; //for search path
	
	/** The unitcount. */
	private int unitcount;
	
	/** The plannedunits. */
	private int plannedunits;
	
	/**
	 * Gets the plannedunits.
	 * 
	 * @return the plannedunits
	 */
	public int getPlannedunits() {
		return plannedunits;
	}

	/**
	 * Sets the plannedunits.
	 * 
	 * @param plannedunits the new plannedunits
	 */
	public void setPlannedunits(int plannedunits) {
		this.plannedunits = plannedunits;
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
	 * Instantiates a new builds the tree unit.
	 * 
	 * @param unit the unit
	 */
	IBuildTreeUnit(UnitDef unit){
		nodes = new LinkedList<IBuildTreeUnit>();
		backlink = new LinkedList<IBuildTreeUnit>();
		this.unit=unit;
		this.mark=0;
		this.parent=null;
		this.unitcount=0;
	}
	
	/**
	 * Gets the unit.
	 * 
	 * @return the unit
	 */
	public UnitDef getUnit(){
		return this.unit;
	}
	
	/**
	 * Adds the node.
	 * 
	 * @param parent the parent
	 * 
	 * @return the builds the tree unit
	 */
	public IBuildTreeUnit addNode(IBuildTreeUnit parent){
		nodes.add(parent);
		parent.backlink.add(this);
		return parent; 
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
	 * Sets the mark.
	 * 
	 * @param mark the new mark
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}
	
	/**
	 * Sets the parent.
	 * 
	 * @param parent the new parent
	 */
	public void setParent(IBuildTreeUnit parent){
		this.parent=parent;
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
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public LinkedList<IBuildTreeUnit> getNodes() {
		return nodes;
	}
	
	/**
	 * Inc unitcount.
	 */
	public void incUnitcount(){
		plannedunits--;
		unitcount++;
	}
	
	/**
	 * Dec unit count.
	 */
	public void decUnitCount(){
		unitcount--;
	}
	
	/**
	 * Gets the unitcount.
	 * 
	 * @return the unitcount
	 */
	public int getUnitcount(){
		return unitcount;
	}
}