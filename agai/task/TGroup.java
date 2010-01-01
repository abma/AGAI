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
import agai.manager.MGroup;
import agai.manager.Manager;
import agai.unit.AGUnit;
import agai.unit.UGroup;

// TODO: Auto-generated Javadoc
//A group of Units to give them all the same task
/**
 * The Class TaskGroup.
 */
public class TGroup extends Task {
	private boolean go;
	private UGroup group;
	private int lastFrame;

	private int size;
	private Task task;

	public TGroup(AGAI ai, Manager manager, Task task, int size) {
		super(ai, manager);
		this.size = size;
		this.task = task;
		go = false;
		group = new UGroup(ai, this);
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@Override
	public void unitCommandFinished(AGUnit unit) {
		execute(unit);
	}

	@Override
	public void unitDestroyed(AGUnit unit) {
		ai.logDebug("");
		if (!go)
			return;
		group.remove(unit);
		if (group.size() == 0)
			((MGroup) manager).remove(this);
	}

	/**
	 * Adds the unit.
	 * 
	 * @param unit
	 *            the unit
	 */
	@Override
	public void unitFinished(AGUnit builder, AGUnit unit) {
		ai.logDebug("");
		group.add(unit);
		if (group.size() == size) {
			ai.logDebug("clear to start!");
			go = true;
		}
	}

	@Override
	public void execute(AGUnit unit) {
		ai.logDebug(""); // wait until enough units have finished...
		group.getPos();
		if (!go)
			return;
		if (lastFrame + 3 > ai.getFrame()) // FIXME: there should be a better
											// solution?: avoid mass-unit
											// finished events
			return;
		lastFrame = ai.getFrame();
		if (task != null)
			task.unitCommandFinished(group);
	}

}