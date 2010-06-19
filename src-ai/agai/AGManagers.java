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

import java.util.Collections;
import java.util.LinkedList;

import agai.info.IResource;
import agai.manager.MAttack;
import agai.manager.MBuild;
import agai.manager.MExpensiveBuild;
import agai.manager.MHelper;
import agai.manager.MResource;
import agai.manager.MScout;
import agai.manager.Manager;
import agai.task.Task;
import agai.unit.AGUnit;

// TODO: Auto-generated Javadoc
/**
 * The Class AGTasks.
 */

public class AGManagers {

	/** The ai. */
	AGAI ai;

	/** The list with all Task managers. */
	LinkedList<Manager> list;

	/** The tasks. */
	LinkedList<Task> tasks;

	/**
	 * Instantiates a new aG tasks.
	 * 
	 * @param ai
	 *            the ai
	 */
	public AGManagers(AGAI ai) {
		this.ai = ai;
		tasks = new LinkedList<Task>();
		list = new LinkedList<Manager>();

		list.add(new MHelper(ai));//ordered by priority!
		list.add(new MAttack(ai));
		list.add(new MScout(ai));
		list.add(new MExpensiveBuild(ai));
		list.add(new MBuild(ai));
		list.add(new MResource(ai));
	}

	/**
	 * Clear.
	 */
	public void clear() {
		tasks.clear();
	}

	/**
	 * Dump all Tasks.
	 */
	public void dump() {
		ai.logNormal("Tasks: ");
		for (int i = 0; i < tasks.size(); i++) {
			ai.logNormal(i + " " + tasks.get(i).getClass().getName() + " Priority "
					+ tasks.get(i).getPriority() + " "
					+ tasks.get(i).toString());
		}
	}

	/**
	 * Gets the list.
	 * 
	 * @param classname
	 *            the classname
	 * 
	 * @return the list
	 */
	public Manager get(Class<?> classname) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getClass() == classname)
				return list.get(i);
		}
		ai.logError("Couldn't find required class!" + classname.getCanonicalName());
		return null;
	}

	/**
	 * Sort list bye priorities.
	 */
	public void sort() {
		Collections.sort(tasks);
	}
	
	public void update(IResource res, int timetonextchange){
		LinkedList<Manager> ms=new LinkedList<Manager>();
		for (int i=0; i<list.size(); i++){
			if (list.get(i).needsResources()){
				ms.add(list.get(i));
			}
		}
		ai.logDebug("Good: "+ms.size() + " Managers need resources:" + res);
		for (int i=0; i<ms.size(); i++){
			ms.get(i).setResToUse(res, timetonextchange);
		}
	}
	/*
	 * assigns a task to an unit, it asks all managers to do so
	 */
	public boolean assignTask(AGUnit unit) {
		ai.getInfos().getResources().update();
		for (int i=0; i<list.size(); i++){
			for (int resid=0; resid<ai.getInfos().getResources().get().size();resid++){
				float store=ai.getInfos().getResources().getStore(resid);
				if (store>0.75){
					System.out.println("we need more storage / buildpower!"+store);
					if (list.get(i).setBuildTask(unit))
						return true;
				}
			}
			if (list.get(i).assignTask(unit))
				return true;
			if (list.get(i).setBuildTask(unit))
				return true;
		}
		System.out.println("assignTask false");
		return false;
	}

	public void check() {
		for (int i=0; i<list.size(); i++){
			list.get(i).check();
		}
	}
}
