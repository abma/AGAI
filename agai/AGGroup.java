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

import java.util.LinkedList;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.UnitDef;

class AGUnitGroup extends AGUnit{
	private LinkedList <AGUnit> units;
	private AGTaskGroup taskGroup;
	AGUnitGroup(AGAI ai, AGTaskGroup taskGroup) {
		super(ai, null);
		units=new LinkedList<AGUnit>();
		this.taskGroup=taskGroup;
	}
	@Override public int moveTo(AIFloat3 pos){
		for (int i=0; i<units.size(); i++)
			units.get(i).moveTo(pos);
		return 0;
	}
	public void build(UnitDef type, AIFloat3 pos){
		for (int i=0; i<units.size(); i++)
			units.get(i).buildUnit(type, pos, AGAI.defaultFacing);
	}
	
	public void add(AGUnit unit){
		units.add(unit);
	}
	public void remove(AGUnit unit){
		for (int i=units.size()-1; i>=0; i--){
			if (units.get(i)==unit){
				units.remove(i);
				return;
			}
		}
	}
	
	public int size(){
		return units.size();
	}
	@Override
	public String toString(){
		return "AGUnitGroup "+units.size();
	}
	@Override
	public AIFloat3 getPos(){
		float x=0,z=0;
		AIFloat3 pos;
		for (int i=0; i<units.size(); i++){
			pos=units.get(i).getPos();
			x=x+pos.x;
			z=z+pos.z;
		}
		pos=new AIFloat3();
		pos.x=x/units.size();
		pos.z=z/units.size();
		return pos;
	}
	@Override
	public void setIdle(){
		for (int i=0; i<units.size(); i++){
			units.get(i).setIdle();
		}
	}
	@Override
	public void setTask(AGTask task){
		ai.msg(""+task);
		taskGroup.setTask(task);
	}
}

// A group of Units to give them all the same task
class AGTaskGroup extends AGTask{
	private AGUnitGroup group;
	private AGTask task;
	private int size;
	private boolean go;
	
	AGTaskGroup(AGAI ai, AGTask task, int size) {
		super(ai);
		this.size=size;
		this.task=task;
		go=false;
		group=new AGUnitGroup(ai, this);
	}
	
	public void setTask(AGTask task) {
		this.task=task;
	}

	/**
	 * Adds the unit.
	 * 
	 * @param unit the unit
	 */
	@Override
	public void unitFinished(AGUnit builder, AGUnit unit){
		ai.msg("");
		group.add(unit);
		if (group.size()==size){
			ai.msg("clear to start!");
			go=true;
		}
	}
	
	@Override
	public void solve() {
		ai.msg("");
	}
	
	@Override
	public void unitCommandFinished(AGUnit unit){
		ai.msg(""); //wait until enough units have finished...
		group.getPos();
		if (!go)
			return;
		task.unitCommandFinished(group);
	}
	
	@Override
	public void unitDestroyed(AGUnit unit){
		ai.msg("");
		if (!go)
			return;
		group.remove(unit);
	}
}

/**
 * The Class AGTGroupManager.
 */
public class AGGroup extends AGTaskManager{
	
	/** The list. */
	private LinkedList<AGTaskGroup> list;
	
	/**
	 * Instantiates a new aGT group manager.
	 * 
	 * @param ai the ai
	 */
	AGGroup(AGAI ai) {
		super(ai);
		list=new LinkedList<AGTaskGroup>();
	}


	/* (non-Javadoc)
	 * @see agai.AGTaskManager#solve(agai.AGTask)
	 */
	@Override
	public void solve(AGTask task) {
	}
	
	/**
	 * Adds the group.
	 * 
	 * @param group the group
	 */
	public void addGroup(AGTaskGroup group){
		list.add(group);
	}
	
}