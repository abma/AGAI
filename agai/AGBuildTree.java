/*
 * Copyright (C) 2009 Matthias Ableitner (http://abma.de/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
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

import java.util.List;
import java.util.LinkedList;

import com.springrts.ai.oo.Resource;
import com.springrts.ai.oo.UnitDef;

// TODO: Auto-generated Javadoc
/**
 * @author matze
 *
 */

class AGBuildTreeUnit{
	private LinkedList <AGBuildTreeUnit> nodes; //links to other nodes
	private LinkedList <AGBuildTreeUnit> backlink; //backlinks to Nodes linking to this node
	private UnitDef unit;
	private int mark; //for search path
	private AGBuildTreeUnit parent; //for search path
	
	public AGBuildTreeUnit getParent() {
		return parent;
	}

	AGBuildTreeUnit(UnitDef unit){
		nodes = new LinkedList<AGBuildTreeUnit>();
		backlink = new LinkedList<AGBuildTreeUnit>();
		this.unit=unit;
		this.mark=0;
		this.parent=null;
	}
	
	public UnitDef getUnit(){
		return this.unit;
	}
	
	public AGBuildTreeUnit addNode(AGBuildTreeUnit parent){
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
	public void setParent(AGBuildTreeUnit parent){
		this.parent=parent;
	}

	public LinkedList<AGBuildTreeUnit> getBacklink() {
		return backlink;
	}

	public LinkedList<AGBuildTreeUnit> getNodes() {
		return nodes;
	}
}

/**
 * The Class AGBuildTree.
 */
public class AGBuildTree {
	
	/** The tree. */
	private AGBuildTreeUnit graph;
	
	/** The unit list. */
	private LinkedList <AGBuildTreeUnit> unitList;
	
	/** The ai. */
	private AGAI ai;
	
	/** The mark. */
	private int mark; //value to mark nodes already visited in search
	
	/** The ress. */
	private List<Resource> ress;
	
	/**
	 * Instantiates a new aG build tree.
	 * 
	 * @param ai the ai
	 */
	AGBuildTree(AGAI ai){
		this.mark=0;
		this.ai=ai;
		this.ress=ai.getClb().getResources();
		generateGraph(ai.getAGU().getUnitDef("armcom"));
		generateList();
	}
	
	/**
	 * Fill unitList with all avaiable Units from Graph.
	 */
	private void generateList(){
		LinkedList <AGBuildTreeUnit> queue=new LinkedList<AGBuildTreeUnit>();
		AGBuildTreeUnit tmp;
		queue.add(graph);
		unitList=new LinkedList<AGBuildTreeUnit>();
		mark++;
		while(queue.size()>0){
			tmp=queue.removeFirst();
			if (tmp.getMark()!=mark)
				unitList.add(tmp);
			tmp.setMark(mark);
			for(int i=0; i<tmp.getNodes().size(); i++){
				if (tmp.getNodes().get(i).getMark()!=mark){
					queue.add(tmp.getNodes().get(i));
				}
			}
		}
	}
	
	/**
	 * Inits the build tree.
	 * 
	 * @param cur the cur
	 */
	private void generateGraph(UnitDef cur){
		ai.msg("Initializing Build Graph....");
		LinkedList <UnitDef>queue=new LinkedList<UnitDef>();
		addNode(cur, null);
		do{
			List <UnitDef>units = cur.getBuildOptions();
			for(int i=0; i<units.size();i++){
				if (searchNode(graph, units.get(i))==null){ //unit isnt in tree, insert it
					queue.add(units.get(i));
				}
				addNode(units.get(i), cur);
			}
			cur=queue.getFirst();
			queue.removeFirst();
		}while(queue.size()>0);
	}
	
	
	/**
	 * Gets the builder to build the unit.
	 * 
	 * @param unit the unit
	 * 
	 * @return the builders that can build the requested unit
	 */
	public List <AGUnit> getBuilder(UnitDef unit){
		AGBuildTreeUnit node=searchNode(unit);
		if (node==null)
			return null;
		LinkedList <AGBuildTreeUnit> list=node.getBacklink();
		LinkedList <AGUnit> res=new LinkedList<AGUnit>();
		for(int i=0; i<list.size(); i++){
			List <AGUnit> units=ai.getAGU().getUnits(list.get(i).getUnit());
			for(int j=0; j<units.size(); j++)
				res.add(units.get(j));
		}
		if (res.size()==0)
			ai.msg("found no builder to build unit: "+unit.getName());
		return res;
	}
	
	
	/**
	 * Adds the node to the graph (links to other units / create sublinks if not exist).
	 * 
	 * @param parent the parent node
	 * @param unit the unit
	 */
	public void addNode(UnitDef unit, UnitDef parent){
		if (graph==null){
			graph=new AGBuildTreeUnit(unit);
			return;
		}
		AGBuildTreeUnit pnode=searchNode(parent); //search parent entry
		AGBuildTreeUnit node=searchNode(unit);
		if (pnode!=null){
			if (node!=null){
				pnode.addNode(node);
			}else
				pnode.addNode(new AGBuildTreeUnit(unit)); //insert new unit-node into graph
			return;
		}
		ai.msg("Parent not found: "+parent.getName());
	}
	
	/**
	 * Search node.
	 * 
	 * @param root the root
	 * @param search the search
	 * 
	 * @return the aG build tree unit, tmp.parent.parent.parent ... until root contains the build path
	 */
	public AGBuildTreeUnit searchNode(AGBuildTreeUnit root, UnitDef search){
		LinkedList <AGBuildTreeUnit> queue=new LinkedList<AGBuildTreeUnit>();
		AGBuildTreeUnit tmp;
		queue.add(root);
		mark++;
		while(queue.size()>0){
			tmp=queue.removeFirst();
			if (tmp.getUnit()==search){ //node found, return path
				return tmp;
			}
			tmp.setMark(mark);
			for(int i=0; i<tmp.getNodes().size(); i++){
				if (tmp.getNodes().get(i).getMark()!=mark){
					queue.add(tmp.getNodes().get(i));
					tmp.getNodes().get(i).setParent(tmp); //set backlink to recognize later the path we wentso
				}
			}
		}
		return null;
	}
	
	/**
	 * Search node.
	 * 
	 * @param node the node
	 * 
	 * @return the aG build tree unit
	 */
	public AGBuildTreeUnit searchNode(UnitDef node){
		return searchNode(graph, node);
	}
	
	/**
	 * Search node.
	 * 
	 * @param root the root
	 * @param unit the unit
	 * 
	 * @return the aG build tree unit
	 */
	public AGBuildTreeUnit searchNode(UnitDef root, UnitDef unit){
		return searchNode(searchNode(root),unit);
	}

	/**
	 * Gets the unit list.
	 * 
	 * @return the unit list
	 */
	public LinkedList<AGBuildTreeUnit> getUnitList() {
		return unitList;
	}
	
	/**
	 * Gets the price.
	 * 
	 * @param unit the unit
	 * @param resource the resource
	 * 
	 * @return the price
	 */
	public float getPrice(AGBuildTreeUnit unit, int resource){
		if ((resource<ress.size()) && (resource>=0)) 
			return unit.getUnit().getCost(ress.get(resource));
		return 0;
	}
	
	/**
	 * Gets the builds the time.
	 * 
	 * @param unit the unit
	 * 
	 * @return the builds the time
	 */
	public float getBuildTime(AGBuildTreeUnit unit){
		return unit.getUnit().getBuildTime();
	}
	
	/**
	 * Dump units.
	 */
	public void dumpUnits(){
		for(int i=0; i<unitList.size();i++){
			UnitDef u=unitList.get(i).getUnit();
			ai.msg(u.getName() +" " + u.hashCode() );
		}
	}
	
}

