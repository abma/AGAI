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
import agai.AGUnits;
import agai.info.IBuildTreeUnit;
import agai.info.IResource;
import agai.info.ISearchUnitAttacker;
import agai.info.ISector;
import agai.task.TAttack;
import agai.task.TBuild;
import agai.unit.AGUnit;

/**
 * The Class AGTAttack.
 * 
 * @author matze
 */
public class MAttack extends Manager {
	private int groups = 0;

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

	public int getGroups() {
		return groups;
	}

	public void setGroups(int groups) {
		this.groups = groups;
	}

	@Override
	public void setResToUse(IResource res, int timetonextchange){
		resToUse.setFrom(res);
		ISector target = ai.getInfos().getSectors().getNextEnemyTarget(ai.getStartpos(), 0);
		if (target==null){ //no targets, build scouts
			ai.msg("Giving all resources to scouts, because no target exists");
			MScout scout=(MScout) ai.getManagers().get(MScout.class);
			scout.incResToUse(res);
			return;
		}
		for(int i = 0; i<list.size();i++){
			IBuildTreeUnit u = list.get(i); 
			if (u.getCost().lessOrEqual(res, timetonextchange)){
				if (ai.getInfos().getAGB().getBuilder(u.getUnit())!=null){ //factory is avaiable
					ai.msg("building ");
					MBuild m= (MBuild) ai.getManagers().get(MBuild.class);
					m.add(new TBuild(ai, m, u.getUnit(), null, 0, 0, new TAttack(ai, this, AGUnits.ElementType.unitAny)));
					return;
				}
			}
		}
		
	}
	@Override
	public boolean assignTask(AGUnit unit){
		ISector target = ai.getInfos().getSectors().getNextEnemyTarget(ai.getStartpos(), 0);
			if (target!=null){
				unit.setTask(new TAttack(ai, this, null));
				return true;
			}
		return false;
	}
	@Override
	public boolean needsResources() {
		return true;
	}
}
