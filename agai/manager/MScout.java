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
import java.util.List;

import com.springrts.ai.oo.UnitDef;

import agai.AGAI;
import agai.info.IBuildTreeUnit;
import agai.info.IResource;
import agai.info.ISearchUnitScout;
import agai.task.TBuild;
import agai.task.TScout;
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

	@Override
	public boolean assignTask(AGUnit unit){
		if (unit.getDef().getSpeed()<=0)
			return false;
		for (int i=0; i<list.size(); i++){
			if (unit.getDef().equals(list.get(i).getUnit())){
				unit.setTask(new TScout(ai, this));
				return true;
			}
		}
		return false;
	}

	@Override
	public void setResToUse(final IResource res, int timetonextchange) {
		resToUse.setFrom(res);
		ai.msg(""+res);
		int i=0;
		for(i=0; i<list.size();i++){
			IBuildTreeUnit u = list.get(i); 
			if (u.getCost().lessOrEqual(res, timetonextchange)){
				if (ai.getInfos().getAGB().getBuilder(u.getUnit())!=null){ //factory is avaiable
					ai.msg("building scout");
					MBuild m= (MBuild) ai.getManagers().get(MBuild.class);
					m.add(new TBuild(ai, m, u.getUnit(), null, 0, 0, new TScout(ai, this)));
					res.sub(u.getCost());
					return;
				}
			}
		}
		//no factory, build factory!
		ai.msg("Giving all resources to build factory");
		MExpensiveBuild m= (MExpensiveBuild) ai.getManagers().get(MExpensiveBuild.class);
		LinkedList<UnitDef> tmp = ai.getInfos().getAGB().getAllBuilders(list.get(list.size()-1).getUnit()); //FIXME: could be null if no builder is avaiable
		m.add(tmp.get(0));
		m.incResToUse(resToUse);
		resToUse.zero();
	}

}