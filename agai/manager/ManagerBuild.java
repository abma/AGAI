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
import agai.task.Task;
import agai.task.TaskBuild;
import agai.unit.AGUnit;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.UnitDef;

/**
 * The Class AGTaskBuildUnit, handles build commands.
 */
public class ManagerBuild extends Manager{
	
	/**
	 * Instantiates a new aG task build unit.
	 * 
	 * @param ai the ai
	 */
	public ManagerBuild(AGAI ai) {
		super(ai);
	}

	
	/**
	 * Send build command to unit and do some checks to parameters.
	 * 
	 * @param unit the unit
	 * @param task the task
	 * 
	 * @return the int
	 */
	private int realBuild(AGUnit unit, TaskBuild task){
		AIFloat3 pos=task.getPos();
		if (unit.getDef().getSpeed()>0){ //Unit who builds can move
			ai.msg("Mobile builder ");
			pos=unit.canBuildAt(pos, task.getUnitDef(), task.getRadius(), task.getMinDistance());
			if (pos==null){
				ai.msg("Can't build at pos");
				task.setRepeat(0);
				return -1;
			}
		}else{ //builder can't move, build at builder pos
			ai.msg("Static builder");
			if (pos==null)
				pos=unit.getPos();
		}
		ai.msg("Sending build command to "+unit.getDef().getName()+ " build " + task.getUnitDef().getName()+pos);
		task.setRepeat(0);
		unit.setTask(new TaskBuild(ai, null, pos, 0, 0, task));
		unit.buildUnit(task.getUnitDef(), pos, AGAI.defaultFacing);
		return 0;
	}
	
	
	/**
	 * Builds the unit and adds all needed tasks (if for ex. a factory is needed)
	 * 
	 * @param unit the unit to be built
	 */
	private void BuildUnit(UnitDef unit){
		ai.msg(unit.getName());
		AGUnit builder=ai.getAGU().getUnit(ai.getAGU().getUnitDef("armcom")); //FIXME search for commander (should search the "next" unit in tree to build)
		if (builder==null) //no commander found, search builder
			ai.msg("Commander is dead, here is missing some code...");
		BuildTreeUnit tmp=ai.getAGI().getAGB().searchNode(builder.getUnit().getDef(),unit);
		if (tmp!=null){
			while(tmp.getUnit()!=builder.getUnit().getDef()){
				TaskBuild cur=new TaskBuild(ai, tmp.getUnit(),null, AGAI.searchDistance, AGAI.minDistance, null);
				cur.setSolved();
				if (!unit.equals(cur.getUnitDef())) //don't add the unit to build, because it's already in the task list
					if (tmp.getUnitcount()+tmp.getPlannedunits()<=0){ //don't build builders that are already present (see FIXME also)
						ai.getAGT().addTask(cur);
						tmp.setPlannedunits(tmp.getPlannedunits()+1);
					}
				tmp=tmp.getParent();
			}
		}else
			ai.msg("couldn't solve");
	}
	
	/* (non-Javadoc)
	 * @see agai.AGTaskManager#solve(agai.AGTask)
	 */
	@Override
	public void solve(Task task) {
		ai.msg(""+task);
		TaskBuild t=(TaskBuild)task;
		List <AGUnit> builder=ai.getAGI().getAGB().getBuilder(t.getUnitDef());
		if (builder.size()>0){ //found unit who can build.. try it
			for (int j=0; j<builder.size(); j++){
				if (builder.get(j).isIdle()){
					int res=realBuild(builder.get(j),t);
					if (res!=0){
						ai.msg("Error in building... "+res);
						task.setRepeat(Task.defaultRepeatTime);
					}
					break;
				}
			}
		}else{ //found no builder -> resolve
			if (!t.isSolved()){
				BuildUnit(t.getUnitDef());
				t.setSolved();
			}
			task.setRepeat(Task.defaultRepeatTime);
		}
	}
}
