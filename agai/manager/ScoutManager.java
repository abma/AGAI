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
import agai.info.BuildTreeUnit;
import agai.info.SearchScout;
import agai.unit.AGTask;

/**
 * The Class AGTScout.
 */
public class ScoutManager extends TaskManager{
	
	/** The count of all scouts */
	private int scouts = 0;
	
	/**
	 * Dec scouts.
	 */
	public void decScouts() {
		scouts--;
		ai.msg(""+scouts);
	}

	/**
	 * Inc scouts.
	 */
	public void incScouts() {
		scouts++;
		ai.msg(""+scouts);
	}

	/** The list. */
	protected List <BuildTreeUnit> list;
	
	/**
	 * Instantiates a new aG task scout.
	 * 
	 * @param ai the ai
	 */
	public ScoutManager(AGAI ai) {
		super(ai);
		list=ai.getAGF().Filter(new SearchScout(ai));
		for (int i=0; i<list.size(); i++){
			ai.msg(list.get(i).getUnit().getName() +"\t"+ ai.getAGU().getTotalPrice(list.get(i).getUnit()) );
		}
	}
	
	/* (non-Javadoc)
	 * @see agai.AGTaskManager#solve(agai.AGTask)
	 */
	@Override
	public void solve(AGTask task) {
		if (scouts<10){
			ai.buildUnit(task, list, task, AGAI.ElementType.unitAny);
		}
	}
}