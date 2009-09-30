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
import java.util.LinkedList;

import agai.AGAI;
import agai.unit.AGUnit;

import com.springrts.ai.oo.Resource;
import com.springrts.ai.oo.UnitDef;

/**
 * The Class AGBuildTree.
 */
public class BuildTree {
	
	/** The tree. */
	private BuildTreeUnit graph;
	
	/** The unit list. */
	private LinkedList <BuildTreeUnit> unitList;
	
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
	public BuildTree(AGAI ai){
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
		LinkedList <BuildTreeUnit> queue=new LinkedList<BuildTreeUnit>();
		BuildTreeUnit tmp;
		queue.add(graph);
		unitList=new LinkedList<BuildTreeUnit>();
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
	 * Gets an avaiable builder to build the unit.
	 * 
	 * @param unit the unit
	 * 
	 * @return the builders that can build the requested unit
	 */
	public List <AGUnit> getBuilder(UnitDef unit){
		BuildTreeUnit node=searchNode(unit);
		if (node==null)
			return null;
		LinkedList <BuildTreeUnit> list=node.getBacklink();
		LinkedList <AGUnit> res=new LinkedList<AGUnit>();
		for(int i=0; i<list.size(); i++){
			if (list.get(i).getUnitcount()>0){ //search only for units avaiable
				List <AGUnit> units=ai.getAGU().getUnits(list.get(i).getUnit());
				for(int j=0; j<units.size(); j++){
					res.add(units.get(j));
				}
			}
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
			graph=new BuildTreeUnit(unit);
			return;
		}
		BuildTreeUnit pnode=searchNode(parent); //search parent entry
		BuildTreeUnit node=searchNode(unit);
		if (pnode!=null){
			if (node!=null){
				pnode.addNode(node);
			}else
				pnode.addNode(new BuildTreeUnit(unit)); //insert new unit-node into graph
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
	public BuildTreeUnit searchNode(BuildTreeUnit root, UnitDef search){
		LinkedList <BuildTreeUnit> queue=new LinkedList<BuildTreeUnit>();
		BuildTreeUnit tmp;
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
	public BuildTreeUnit searchNode(UnitDef node){
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
	public BuildTreeUnit searchNode(UnitDef root, UnitDef unit){
		return searchNode(searchNode(root),unit);
	}

	/**
	 * Gets the unit list.
	 * 
	 * @return the unit list
	 */
	public LinkedList<BuildTreeUnit> getUnitList() {
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
	public float getPrice(BuildTreeUnit unit, int resource){
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
	public float getBuildTime(BuildTreeUnit unit){
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
	
	/**
	 * Dump graph to stdout
	 * copy the text into a file and then convert it with graphiz:
	 * dot -Tpdf techtree.dot -o techtree.pdf
	 */
	public void dumpGraph(){
		System.out.println("digraph G {");
		for(int i=0; i<unitList.size();i++){
			BuildTreeUnit u=unitList.get(i);
			for (int j=0; j<u.getNodes().size(); j++){
				BuildTreeUnit child=u.getNodes().get(j);
				System.out.println( u.getUnit().getName() + " -> "+child.getUnit().getName()+";");
			}
		}
		System.out.println("}");
	}
}

