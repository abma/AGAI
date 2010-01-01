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

package agai.manager;

import java.util.LinkedList;
import java.util.List;

import agai.AGAI;
import agai.info.IBuildTreeUnit;
import agai.info.IResource;
import agai.unit.AGUnit;

/**
 * The Class AGTaskManager.
 */
public abstract class Manager {

	/** The ai. */
	protected AGAI ai;
	protected IResource resToUse; 
	private int idleTasks;
	protected List <IBuildTreeUnit> list;
	public int getIdleTasks() {
		return idleTasks;
	}

	public void setIdleTasks(int idleTasks) {
		this.idleTasks = idleTasks;
	}

	public IResource getResToUse() {
		return resToUse;
	}

	public void setResToUse(IResource res, int timetonextchange) {
		resToUse.setFrom(res);
		ai.logDebug(""+this.getClass()+res);
		check();
	}
	
	public void incResToUse(IResource res){
		ai.logDebug(""+this.getClass()+res);
		resToUse.add(res);
		check();
	}

	/**
	 * Instantiates a new aG task manager.
	 * 
	 * @param ai
	 *            the ai
	 */
	protected Manager(AGAI ai) {
		ai.logDebug("Initialized AGTaskManager " + this.getClass() + " " + ai);
		this.ai = ai;
		this.resToUse = new IResource(ai);
		this.idleTasks=0;
		this.list = new LinkedList<IBuildTreeUnit>();
	}
	
	/**
	 * Assign task to unit
	 * 
	 * @param unit the unit
	 * 
	 * @return true, if successful
	 */
	public boolean assignTask(AGUnit unit){
		ai.logDebug(this.getClass().getName()+ " Warning: assignTask() needs to be implemented!");
		return false;
	}
    
	/**
	 * Check if a Unit can help a Manager to solve a Task
	 * 
	 * @param unit the unit
	 * 
	 * @return true, if successful
	 */
	public boolean canSolve(AGUnit unit){
    	for (int i=0; i<list.size(); i++)
    		if (unit.getDef().getUnitDefId()==list.get(i).getUnit().getUnitDefId())
    			return true;
    	return false;
    }
	
	/**
	 * This function is called regulary to do some checks
	 */
	public abstract void check();

	public boolean needsResources() {
		return false;
	}

}
