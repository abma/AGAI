package agai.info;

import java.util.LinkedList;

import com.springrts.ai.oo.UnitDef;

public class BuildTreeUnit{
	private LinkedList <BuildTreeUnit> nodes; //links to other nodes
	private LinkedList <BuildTreeUnit> backlink; //backlinks to Nodes linking to this node
	private UnitDef unit;
	private int mark; //for search path
	private BuildTreeUnit parent; //for search path
	private int unitcount;
	private int plannedunits;
	
	public int getPlannedunits() {
		return plannedunits;
	}

	public void setPlannedunits(int plannedunits) {
		this.plannedunits = plannedunits;
	}

	public BuildTreeUnit getParent() {
		return parent;
	}

	BuildTreeUnit(UnitDef unit){
		nodes = new LinkedList<BuildTreeUnit>();
		backlink = new LinkedList<BuildTreeUnit>();
		this.unit=unit;
		this.mark=0;
		this.parent=null;
		this.unitcount=0;
	}
	
	public UnitDef getUnit(){
		return this.unit;
	}
	
	public BuildTreeUnit addNode(BuildTreeUnit parent){
		nodes.add(parent);
		parent.backlink.add(this);
		return parent; 
	}

	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public void setParent(BuildTreeUnit parent){
		this.parent=parent;
	}

	public LinkedList<BuildTreeUnit> getBacklink() {
		return backlink;
	}

	public LinkedList<BuildTreeUnit> getNodes() {
		return nodes;
	}
	public void incUnitcount(){
		plannedunits--;
		unitcount++;
	}
	public void decUnitCount(){
		unitcount--;
	}
	public int getUnitcount(){
		return unitcount;
	}
}