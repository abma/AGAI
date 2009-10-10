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
import agai.info.IBuildTreeUnit;
import agai.info.IResource;
import agai.info.ISearchUnitAttacker;
import agai.task.Task;
import agai.unit.AGUnit;

/**
 * The Class AGTAttack.
 * 
 * @author matze
 */
public class MAttack extends Manager {
	private int groups = 0;

	protected List<IBuildTreeUnit> list;

	/**
	 * Instantiates a new aG task attack.
	 * 
	 * @param ai
	 *            the ai
	 */
	public MAttack(AGAI ai) {
		super(ai);
		list = ai.getInfos().getSearch().Filter(new ISearchUnitAttacker(ai));
		for (int i = 0; i < list.size(); i++) {
			ai.msg(list.get(i).getUnit().getName() + "\t"
					+ ai.getUnits().getTotalPrice(list.get(i).getUnit()));
		}
	}

	/**
	 * Dump.
	 */
	public void dump() {
		ai.msg("Task Attack");
	}

	// private int groupsize=0;
	public int getGroups() {
		return groups;
	}

	public void setGroups(int groups) {
		this.groups = groups;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agai.AGTaskManager#solve(agai.AGTask)
	 */
	public void solve(Task task) {
		/*
		 * if (ai.getAGG().getGroups(TAttack.class)<groups){ TAttack
		 * a=((TAttack)task); TGroup group=new TGroup(ai, new TAttack(ai,
		 * ElementType.unitLand), groupsize); for (int i=0; i<groupsize; i++){
		 * ai.buildUnit(group, list, a, a.getType()); }
		 * ai.getAGG().addGroup(group); }
		 */
	}
	public boolean canSolve(Task task, AGUnit unit){
		for (int i=0; i<list.size(); i++)
			if (unit.getDef().equals(list.get(i).getUnit()))
				return true;
		return false;
	}
	@Override
	public void setResToUse(IResource res) {
		ai.msg("");
	}
}
