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

import java.util.List;

import agai.AGAI;
import agai.AGAI.ElementType;
import agai.info.BuildTreeUnit;
import agai.info.SearchAttacker;
import agai.task.Task;
import agai.task.TaskAttack;
import agai.task.TaskGroup;


/**
 * The Class AGTAttack.
 * 
 * @author matze
 */
public class ManagerAttack extends Manager{
	private int groupsize=0;
	public int getGroups() {
		return groups;
	}

	public void setGroups(int groups) {
		this.groups = groups;
	}

	private int groups=0;
	protected List <BuildTreeUnit> list;
	/**
	 * Instantiates a new aG task attack.
	 * 
	 * @param ai the ai
	 */
	public ManagerAttack(AGAI ai) {
		super(ai);
		list=ai.getAGI().getAGF().Filter(new SearchAttacker(ai));
		for (int i=0; i<list.size(); i++){
			ai.msg(list.get(i).getUnit().getName() +"\t"+ ai.getAGU().getTotalPrice(list.get(i).getUnit()) );
		}
	}
	
	/**
	 * Dump.
	 */
	public void dump(){
		ai.msg("Task Attack");
	}

	/* (non-Javadoc)
	 * @see agai.AGTaskManager#solve(agai.AGTask)
	 */
	@Override
	public void solve(Task task) {
		if (ai.getAGG().getGroups(TaskAttack.class)<groups){
			TaskAttack a=((TaskAttack)task);
			TaskGroup group=new TaskGroup(ai, new TaskAttack(ai, ElementType.unitLand), groupsize);
			for (int i=0; i<groupsize; i++){
				ai.buildUnit(group, list, a, a.getType());
			}
			ai.getAGG().addGroup(group);
		}
	}
}
