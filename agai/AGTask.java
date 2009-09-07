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

import com.springrts.ai.oo.Unit;


// TODO: Auto-generated Javadoc
/**
 * The Class AGTask.
 */
abstract class AGTask{
	
	/** The status. */
	protected int status;
	
	/** The priority. */
	private int priority=0;
	
	/** The Constant statusIdle. */
	final static int statusIdle = 0;
	
	/** The Constant statusWorking. */
	final static int statusWorking = 1;
	
	/** The Constant statusFinished. */
	final static int statusFinished = 2;
	final static int statusFailed = 3;
	
	/** The ai. */
	protected AGAI ai;
	
	/** child unit assigned to task (for ex. unit that is built */
	private Unit unitChild=null;
	
	/** The poi where the task refers to */
	private AGPoI poi=null;

	/**
	 * Instantiates a new task.
	 * 
	 * @param ai the ai
	 */
	AGTask(AGAI ai){
		this.ai=ai;
		this.status=statusIdle;
	}
	
	/**
	 * Gets the status of the Task.
	 * 
	 * @return the status
	 */
	public int getStatus(){
		return status;
	}
	
	/**
	 * Sets the status to idle (when a task is for ex. aborted)
	 */
	public void setStatusIdle(){
		status=AGTask.statusIdle;
	}
	
	/**
	 * Sets the status to finished.
	 */
	public void setStatusFinished(){
		status=AGTask.statusFinished;
	}
	
	/**
	 * Inc priority of this task.
	 */
	public void incPriority(){
		priority++;
	}
	
	/**
	 * Dec priority of this task.
	 */
	public void decPriority(){
		priority--;
	}
	
	/**
	 * Gets the priority.
	 * 
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * Sets the status of this task to be under process.
	 */
	public void setStatusWorking(AGUnit unit) {
		unit.setTask(this);
		status=statusWorking;
	}
	
	/**
	 * Try to solve the Task.
	 */
	public abstract void solve();

	/**
	 * Dumps the Task.
	 * 
	 * @return the string
	 */
	public String toString(){
		return this.getClass().getName()+this.priority+this.status+" +dump not implemented!";
	}
	
	/**
	 * Sets the task to failed
	 */
	public void setFailed(){
		status=statusFailed;
	}

	/**
	 * Try to solve a failed task. (buildpos blocked, ...)
	 */
	public void solveFailed() {
		ai.msg(this.getClass().getName()+ " solveFailed() not implemented, marking as finished");
		setStatusFinished();
	}

	public Unit getUnitChild() {
		return unitChild;
	}

	public void setUnitChild(Unit unitChild) {
		this.unitChild = unitChild;
	}

	public AGPoI getPoi() {
		return poi;
	}

	public void setPoi(AGPoI poi) {
		this.poi = poi;
	}
	
}
