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
	
	/** The Constant statusFailed. */
	final static int statusFailed = 3;
	
	private AGUnit unit;

	public AGUnit getUnit() {
		return unit;
	}

	public void setUnit(AGUnit unit) {
		this.unit = unit;
	}

	/** The ai. */
	protected AGAI ai;
	
	/** The poi where the task refers to. */
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
	public void setStatusWorking(){
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
		return this.getClass().getName()+" "+this.priority+" "+this.status+" toString not implemented!";
	}
	
	/**
	 * Sets the task to failed.
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

	/**
	 * Gets the poi.
	 * 
	 * @return the poi
	 */
	public AGPoI getPoi() {
		return poi;
	}

	/**
	 * Sets the poi.
	 * 
	 * @param poi the new poi
	 */
	public void setPoi(AGPoI poi) {
		this.poi = poi;
	}
	public void unitCommandFinished(AGUnit unit){
		ai.msg(unit.getUnit().getDef().getName());
	}
	
	public void unitDestroyed(){
		ai.msg("");
	}
	public void unitIdle(AGUnit unit){
		ai.msg("");
	}
	public void unitMoveFailed(){
		ai.msg("");
	}
	public void unitDamaged(){
		ai.msg("");
	}
	public void unitCaptured(){
		ai.msg("");
	}
	public void unitCreated(AGUnit builder, AGUnit unit){
		ai.msg("");
	}
	public void unitWeaponFired(){
		ai.msg("");
	}
	public void unitEnemyDamaged(){
		ai.msg("");
	}
	public void unitEnemyDestroyed(){
		ai.msg("");
	}
	public void unitFinished(AGUnit unit){
		ai.msg("");
	}
	public void unitGiven() {
		ai.msg("");
	}
}
