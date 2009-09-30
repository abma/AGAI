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

package agai.task;

import agai.AGAI;
import agai.info.PoIMap;
import agai.unit.AGUnit;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.WeaponDef;


// TODO: Auto-generated Javadoc
/**
 * The Class AGTask.
 */
public abstract class Task{
	public static int defaultRepeatTime = 100;
	/** The time in frames the task is repeated, <=0 means no repeat */
	private int repeat;
	
	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	/** The frame the task was last run */
	public int lastrun;
	
	/** The priority. */
	private int priority=0;
	
	/** The ai. */
	protected AGAI ai;
	
	/** The poi where the task refers to. */
	private PoIMap poi=null;

	/**
	 * Instantiates a new task.
	 * 
	 * @param ai the ai
	 */
	protected Task(AGAI ai){
		this.ai=ai;
		this.repeat=defaultRepeatTime;
	}
	
	/**
	 * Gets the lastrun.
	 * 
	 * @return the lastrun
	 */
	public int getLastrun() {
		return lastrun;
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
	 * Try to solve the Task, is called, when a Task is in the Tasklist
	 */
	public abstract void solve();

	/**
	 * Dumps the Task.
	 * 
	 * @return the string
	 */
	public String toString(){
		return this.getClass().getName()+" "+this.priority+" "+this.lastrun+" toString not implemented!";
	}
	
	/**
	 * Gets the poi.
	 * 
	 * @return the poi
	 */
	public PoIMap getPoi() {
		return poi;
	}

	/**
	 * Sets the poi.
	 * 
	 * @param poi the new poi
	 */
	public void setPoi(PoIMap poi) {
		this.poi = poi;
	}

	/**
	 * Unit command finished.
	 *
	 * @param unit the unit
	 */
	public void unitCommandFinished(AGUnit unit){
		ai.msg(unit.getDef().getName());
	}

	/**
	 * Unit destroyed.
	 *
	 * @param unit the unit
	 */
	public void unitDestroyed(AGUnit unit){
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit idle.
	 *
	 * @param unit the unit
	 */
	public void unitIdle(AGUnit unit){
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit move failed.
	 *
	 * @param unit the unit
	 */
	public void unitMoveFailed(AGUnit unit){
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit damaged.
	 *
	 * @param unit the unit
	 * @param damage the damage
	 * @param direction the direction
	 * @param weaponDef the weapon def
	 * @param paralyzer the paralyzer
	 */
	public void unitDamaged(AGUnit unit, float damage, AIFloat3 direction, WeaponDef weaponDef, boolean paralyzer){
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit captured.
	 */
	public void unitCaptured(){
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit created.
	 *
	 * @param builder the builder
	 * @param unit the unit
	 */
	public void unitCreated(AGUnit builder, AGUnit unit){
		ai.msg(this.getClass().getName()+" "+builder.getDef().getName()+" "+unit.getDef().getName());
	}

	/**
	 * Unit weapon fired.
	 */
	public void unitWeaponFired(){
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit enemy damaged.
	 *
	 * @param u the u
	 * @param enemy the enemy
	 */
	public void unitEnemyDamaged(AGUnit u, Unit enemy){
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit enemy destroyed.
	 */
	public void unitEnemyDestroyed(){
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit finished.
	 *
	 * @param builder the builder
	 * @param unit the unit
	 */
	public void unitFinished(AGUnit builder, AGUnit unit){
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit given.
	 */
	public void unitGiven() {
		ai.msg(this.getClass().getName());
	}

	/**
	 * Task is assigned
	 *
	 * @param unit the unit
	 */
	public void assign(AGUnit unit){
		ai.msg(this.getClass().getName());
	}

	/**
	 * Task is Unassigned.
	 *
	 * @param unit the unit
	 */
	public void unassign(AGUnit unit){
		ai.msg(this.getClass().getName());
	}
}