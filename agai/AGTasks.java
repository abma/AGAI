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

import agai.task.Task;
import agai.unit.AGUnit;

public class AGTasks {
	AGAI ai;
	LinkedList<Task> list;

	public LinkedList<Task> getTasks() {
		return list;
	}

	AGTasks(AGAI ai) {
		this.ai = ai;
		list = new LinkedList<Task>();
	}

	public void add(Task task) {
		list.add(task);
	}

	public void clear() {
		ai.msg("");
		list.clear();
	}

	public void dump() {
		for (int i=0; i<list.size(); i++){
			ai.msg(list.get(i).toString());
		}
	}

	/**
	 * Removes a Task that can be done from a unit from queue list
	 * 
	 * @param unit the unit
	 * 
	 * @return the task
	 */
	public Task getTask(AGUnit unit) {
		for (int i=0; i<list.size(); i++){
			if (list.get(i).canBeDone(unit)){
				return list.remove(i);
			}
		}
		return null;
	}
}
