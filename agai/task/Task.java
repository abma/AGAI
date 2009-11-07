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
import agai.info.IPoI;
import agai.manager.Manager;
import agai.unit.AGUnit;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.WeaponDef;

// TODO: Auto-generated Javadoc
/**
 * The Class AGTask.
 */
public abstract class Task implements Comparable<Object> {
	
	/** The ai. */
	protected AGAI ai;

	/** The manager. */
	protected Manager manager;

	/** The poi where the task refers to. */
	private IPoI poi = null;

	/** The priority. */
	private int priority = 0;

	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Instantiates a new task.
	 * 
	 * @param ai the ai
	 * @param manager the manager
	 */
	protected Task(AGAI ai, Manager manager) {
		this.ai = ai;
		this.manager = manager;
	}
	
	
	/**
	 * Execute the task! (just do it!)
	 * 
	 * @param unit the unit
	 */
	public abstract void execute(AGUnit unit);

	/**
	 * Task is assigned.
	 * 
	 * @param unit the unit
	 */
	public void assign(AGUnit unit) {
		ai.msg(this.getClass().getName());
		execute(unit);
	}

	/**
	 * Dec priority of this task.
	 */
	public void decPriority() {
		priority--;
	}

	/**
	 * Gets the poi.
	 * 
	 * @return the poi
	 */
	public IPoI getPoi() {
		return poi;
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
	 * Inc priority of this task.
	 */
	public void incPriority() {
		priority++;
	}

	/**
	 * Sets the poi.
	 * 
	 * @param poi the new poi
	 */
	public void setPoi(IPoI poi) {
		this.poi = poi;
	}

	/**
	 * Dumps the Task.
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + " " + this.priority + " "
				+ " toString not implemented!";
	}

	/**
	 * Task is Unassigned.
	 * 
	 * @param unit the unit
	 */
	public void unassign(AGUnit unit) {
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit captured.
	 */
	public void unitCaptured() {
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit command finished.
	 * 
	 * @param unit the unit
	 */
	public void unitCommandFinished(AGUnit unit) {
		ai.msg(unit.getDef().getName() + " "+this.getClass().getSimpleName());
	}

	/**
	 * Unit created.
	 * 
	 * @param builder the builder
	 * @param unit the unit
	 */
	public void unitCreated(AGUnit builder, AGUnit unit) {
		ai.msg(this.getClass().getName() + " " + builder.getDef().getName()
				+ " " + unit.getDef().getName());
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
	public void unitDamaged(AGUnit unit, float damage, AIFloat3 direction,
			WeaponDef weaponDef, boolean paralyzer) {
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit destroyed.
	 * 
	 * @param unit the unit
	 */
	public void unitDestroyed(AGUnit unit) {
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit enemy damaged.
	 * 
	 * @param u the u
	 * @param enemy the enemy
	 */
	public void unitEnemyDamaged(AGUnit u, Unit enemy) {
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit enemy destroyed.
	 */
	public void unitEnemyDestroyed() {
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit finished.
	 * 
	 * @param builder the builder
	 * @param unit the unit
	 */
	public void unitFinished(AGUnit builder, AGUnit unit) {
		ai.msg(this.getClass().getName());
		execute(unit);
	}

	/**
	 * Unit given.
	 */
	public void unitGiven() {
		ai.msg(this.getClass().getName());
	}

	/**
	 * Unit idle.
	 * 
	 * @param unit the unit
	 */
	public void unitIdle(AGUnit unit) {
		ai.msg(this.getClass().getName());
		execute(unit);
	}

	/**
	 * Unit move failed.
	 * 
	 * @param unit the unit
	 */
	public void unitMoveFailed(AGUnit unit) {
		ai.msg(this.getClass().getName());
		execute(unit);
	}

	/**
	 * Unit weapon fired.
	 */
	public void unitWeaponFired() {
		ai.msg(this.getClass().getName());
	}
	
	/**
	 * Compare.
	 * 
	 * @param o1 the o1
	 * @param o2 the o2
	 * 
	 * @return the int
	 */
	public int compare(Object o1, Object o2) {
		Task t1=(Task) o1;
		Task t2=(Task) o2;
		if (t1.getPriority()>t2.getPriority())
			return 1;
		if (t1.getPriority()<t2.getPriority())
			return -1;
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o){
		return compare(this,o);
	}
}
