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

import com.springrts.ai.oo.Resource;

import agai.AGAI;
import agai.AGUnits;
import agai.info.IBuildTreeUnit;
import agai.info.IResource;
import agai.info.ISearchUnitScout;
import agai.task.TBuild;
import agai.task.TScout;
import agai.task.Task;
import agai.unit.AGUnit;

/**
 * The Class AGTScout.
 */
public class MScout extends Manager {

	/** The list. */
	protected List<IBuildTreeUnit> list;

	/** The count of all scouts */
	private int scouts = 0;

	/**
	 * Instantiates a new aG task scout.
	 * 
	 * @param ai
	 *            the ai
	 */
	public MScout(AGAI ai) {
		super(ai);
		list = ai.getInfos().getSearch().Filter(new ISearchUnitScout(ai));
		for (int i = 0; i < list.size(); i++) {
			ai.msg(list.get(i).getUnit().getName() + "\t"
					+ ai.getUnits().getTotalPrice(list.get(i).getUnit()));
		}
	}

	/**
	 * Dec scouts.
	 */
	public void decScouts() {
		scouts--;
		ai.msg("" + scouts);
	}

	/**
	 * Inc scouts.
	 */
	public void incScouts() {
		scouts++;
		ai.msg("" + scouts);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agai.AGTaskManager#solve(agai.AGTask)
	 */
	public void solve(Task task) {
		if (scouts < 10) {
			Resource res=((MBuild) ai.getManagers().get(MBuild.class)).buildUnit(task, list,
					task, AGUnits.ElementType.unitAny, getResToUse());
			if (res!=null){
				ai.getManagers().get(MResource.class).setResToUse(getResToUse(), 1000);
				//FIXME: clear used resources
			}
		}
	}
	@Override
	public boolean canSolve(Task task, AGUnit unit){
		for (int i=0; i<list.size(); i++)
			if (unit.getDef().equals(list.get(i).getUnit()))
				return true;
		return false;
	}

	@Override
	public void setResToUse(IResource res, int timetonextchange) {
		ai.msg("");
		ai.getInfos().getResources().get();
		for(int i=0; i<list.size();i++){
			if (list.get(i).getCost().lessOrEqual(res)){
				MBuild m= (MBuild) ai.getManagers().get(MBuild.class);
				ai.getTasks().add(new TBuild(ai, m, list.get(i).getUnit(), null, 0, 0, new TScout(ai, this)));
				return;
			}
		}
		ai.msg("To few resources to build a scout!");
		ai.getManagers().get(MResource.class).incResToUse(res);
	}

}