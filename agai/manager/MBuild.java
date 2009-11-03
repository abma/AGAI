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

import agai.AGAI;
import agai.task.TBuild;
import agai.unit.AGUnit;

/**
 * The Class AGTaskBuildUnit, handles build commands.
 */
public class MBuild extends Manager {
	private List <TBuild> buildtasks;
	
	/**
	 * Instantiates a new aG task build unit.
	 * 
	 * @param ai
	 *            the ai
	 */
	public MBuild(AGAI ai) {
		super(ai);
		buildtasks=new LinkedList<TBuild>();
	}

	public void add(TBuild task){
		buildtasks.add(task);
	}
	@Override
	public boolean assignTask(AGUnit unit){
		if (unit.getDef().getBuildOptions().size()<=0)
			return false;
		if (buildtasks.size()>0){
			for (int i=0; i<buildtasks.size(); i++){
				if (unit.canBuildAt(buildtasks.get(i).getPos(), buildtasks.get(i).getUnitDef(), buildtasks.get(i).getRadius(), buildtasks.get(i).getMinDistance())!=null){
					unit.setTask(buildtasks.remove(i));
					return true;
				}
			}
		}
		return false;
	}
}
