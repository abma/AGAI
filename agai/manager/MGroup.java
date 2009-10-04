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

import agai.AGAI;
import agai.task.Task;
import agai.task.TGroup;

/**
 * The Class AGTGroupManager.
 */
public class MGroup extends Manager{
	
	/** The list. */
	private LinkedList<TGroup> list;
	
	/**
	 * Instantiates a new aGT group manager.
	 * 
	 * @param ai the ai
	 */
	public MGroup(AGAI ai) {
		super(ai);
		list=new LinkedList<TGroup>();
	}


	public void remove(TGroup taskGroup) {
		ai.msg("group died!");
		list.remove(taskGroup);
	}


	/* (non-Javadoc)
	 * @see agai.AGTaskManager#solve(agai.AGTask)
	 */
	@Override
	public void solve(Task task) {
	}
	
	/**
	 * Adds the group.
	 *
	 * @param group the group
	 */
	public void addGroup(TGroup group){
		list.add(group);
	}

	/**
	 * Gets the count of type group
	 *
	 * @param cn the cn
	 *
	 * @return the groups
	 */
	public int getGroups(Class <?>cn){
		int count=0;
		for (int i=0; i<list.size(); i++){
			if (cn==list.get(i).getTask().getClass())
				count++;
		}
		return count;
	}
	
}