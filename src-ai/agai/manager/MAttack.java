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
import agai.info.IBuildTreeUnit;
import agai.info.IElement;
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
			ai.logDebug(list.get(i).getUnit().getName() + "\t"
					+ list.get(i).getPrice());
		}
		
	}

	/**
	 * Dump.
	 */
	public void dump() {
		ai.logNormal("Task Attack");
	}

	public int getGroups() {
		return groups;
	}

	public void setGroups(int groups) {
		this.groups = groups;
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
	@Override
	public void check(){
		ISector target = ai.getInfos().getSectors().getNextEnemyTarget(ai.getStartpos(), 0);
		if (target==null){ //no targets, build scouts
			ai.logInfo("Giving all resources to scouts, because no target exists");
			MScout scout=(MScout) ai.getManagers().get(MScout.class);
			scout.incResToUse(resToUse);
			resToUse.zero();
			return;
		}
		for(int i = 0; i<list.size();i++){
			IBuildTreeUnit u = list.get(i); 
			if (ai.getInfos().getAGB().getBuilder(u.getUnit())!=null){ //factory is available
				if (u.getCost().lessOrEqual(resToUse, 100)){
					ai.logDebug("building "+u.getUnit().getName());
					MBuild m= (MBuild) ai.getManagers().get(MBuild.class);
					m.add(new TBuild(ai, m, u.getUnit(), null, 0, 0, new TAttack(ai, this, new IElement(IElement.any))));
					resToUse.sub(u.getCost());
					return;
				}else{
					ai.logWarning("to few resources to build "+u.getUnit().getName());
				}
			}
		}
		MResource resource=(MResource) ai.getManagers().get(MResource.class); //add all unneeded resources to resource-manager
		resource.incResToUse(resToUse);
	}

}
