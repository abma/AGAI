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

import agai.AGAI;
import agai.info.IResource;
import agai.task.Task;
import agai.unit.AGUnit;

/**
 * The Class AGTaskManager.
 */
public abstract class Manager {

	/** The ai. */
	protected AGAI ai;
	protected IResource resToUse; 
	private int idleTasks;

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
		this.resToUse = res;
	}
	
	public void incResToUse(IResource res){
		resToUse.add(res);
	}

	/**
	 * Instantiates a new aG task manager.
	 * 
	 * @param ai
	 *            the ai
	 */
	protected Manager(AGAI ai) {
		ai.msg("Initialized AGTaskManager " + this.getClass() + " " + ai);
		this.ai = ai;
		this.idleTasks=0;
	}

	public boolean canSolve(Task task, AGUnit unit){
		ai.msg(this.getClass().getName()+ " Warning: canSolve() needs to be implemented!");
		return false;
	}
	
}
