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
import agai.AGUnits;
import agai.info.IBuildTreeUnit;
import agai.task.TBuild;
import agai.task.Task;
import agai.unit.AGUnit;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.UnitDef;

/**
 * The Class AGTaskBuildUnit, handles build commands.
 */
public class MBuild extends Manager {

	/**
	 * Instantiates a new aG task build unit.
	 * 
	 * @param ai
	 *            the ai
	 */
	public MBuild(AGAI ai) {
		super(ai);
	}

	/**
	 * Builds a best fitting unit and assigns task.
	 * 
	 * @param task
	 *            the task
	 * @param list
	 *            the list
	 * @param tasktoassign
	 *            the tasktoassign
	 * @param type
	 *            the type (Air, Water, ...)
	 */
	public void buildUnit(Task task, List<IBuildTreeUnit> list,
			Task tasktoassign, AGUnits.ElementType type) {
		UnitDef unit = null;
		task.setRepeat(Task.defaultRepeatTime);
		for (int i = 0; i < list.size(); i++) { // searching for existing unit
			if (ai.getUnits().UnitInType(list.get(i).getUnit(), type)) {
				unit = list.get(i).getUnit();
				AGUnit u = ai.getUnits().getIdle(unit);
				if (u != null) { // unit to scout exists, assign task!
					ai.msg("assigned task to existing idle unit");
					u.setTask(tasktoassign);
					task.setRepeat(0);
					return;
				}
			}
		}
		for (int i = 0; i < list.size(); i++) { // no unit found, try to build
												// with exisiting factories
			if (ai.getUnits().UnitInType(list.get(i).getUnit(), type)) {
				AGUnit builder = ai.getUnits().getBuilder(unit);
				if (builder != null) {
					Task buildtask = new TBuild(ai, ai.getManagers().get(
							MBuild.class), unit, null, AGAI.searchDistance,
							AGAI.minDistance, tasktoassign);
					ai.getTasks().add(buildtask);
					task.setRepeat(0);
					return;
				}
			}
		}
		// no unit found, build cheapest one
		if (unit != null) {
			Task buildtask = new TBuild(ai, ai.getManagers().get(MBuild.class),
					unit, null, AGAI.searchDistance, AGAI.minDistance,
					tasktoassign);
			ai.getTasks().add(buildtask);
			task.setRepeat(0);
		} else {
			ai.msg("No unit found to build " + type);
		}
	}

	/**
	 * Builds the unit and adds all needed tasks (if for ex. a factory is
	 * needed)
	 * 
	 * @param unit
	 *            the unit to be built
	 */
	private void BuildUnit(UnitDef unit) {
		ai.msg(unit.getName());
		AGUnit builder = ai.getUnits().getUnit(
				ai.getUnits().getUnitDef("armcom")); // FIXME search for
														// commander (should
														// search the "next"
														// unit in tree to
														// build)
		if (builder == null) // no commander found, search builder
			ai.msg("Commander is dead, here is missing some code...");
		IBuildTreeUnit tmp = ai.getInfos().getAGB().searchNode(
				builder.getUnit().getDef(), unit);
		if (tmp != null) {
			while (tmp.getUnit() != builder.getUnit().getDef()) {
				TBuild cur = new TBuild(ai, ai.getManagers().get(MBuild.class),
						tmp.getUnit(), null, AGAI.searchDistance,
						AGAI.minDistance, null);
				cur.setSolved();
				if (!unit.equals(cur.getUnitDef())) // don't add the unit to
													// build, because it's
													// already in the task list
					if (tmp.getUnitcount() + tmp.getPlannedunits() <= 0) { // don
																			// 't
																			// build
																			// builders
																			// that
																			// are
																			// already
																			// present
																			// (
																			// see
																			// FIXME
																			// also
																			// )
						ai.getTasks().add(cur);
						tmp.setPlannedunits(tmp.getPlannedunits() + 1);
					}
				tmp = tmp.getParent();
			}
		} else
			ai.msg("couldn't solve");
	}

	/**
	 * Send build command to unit and do some checks to parameters.
	 * 
	 * @param unit
	 *            the unit
	 * @param task
	 *            the task
	 * 
	 * @return the int
	 */
	private int realBuild(AGUnit unit, TBuild task) {
		AIFloat3 pos = task.getPos();
		if (unit.getDef().getSpeed() > 0) { // Unit who builds can move
			ai.msg("Mobile builder ");
			pos = unit.canBuildAt(pos, task.getUnitDef(), task.getRadius(),
					task.getMinDistance());
			if (pos == null) {
				ai.msg("Can't build at pos");
				task.setRepeat(0);
				return -1;
			}
		} else { // builder can't move, build at builder pos
			ai.msg("Static builder");
			if (pos == null)
				pos = unit.getPos();
		}
		ai.msg("Sending build command to " + unit.getDef().getName()
				+ " build " + task.getUnitDef().getName() + pos);
		task.setRepeat(0);
		unit.setTask(new TBuild(ai, ai.getManagers().get(MBuild.class), null,
				pos, 0, 0, task));
		unit.buildUnit(task.getUnitDef(), pos, AGAI.defaultFacing);
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agai.AGTaskManager#solve(agai.AGTask)
	 */
	@Override
	public void solve(Task task) {
		ai.msg("" + task);
		TBuild t = (TBuild) task;
		List<AGUnit> builder = ai.getInfos().getAGB()
				.getBuilder(t.getUnitDef());
		if (builder.size() > 0) { // found unit who can build.. try it
			for (int j = 0; j < builder.size(); j++) {
				if (builder.get(j).isIdle()) {
					int res = realBuild(builder.get(j), t);
					if (res != 0) {
						ai.msg("Error in building... " + res);
						task.setRepeat(Task.defaultRepeatTime);
					}
					break;
				}
			}
		} else { // found no builder -> resolve
			if (!t.isSolved()) {
				BuildUnit(t.getUnitDef());
				t.setSolved();
			}
			task.setRepeat(Task.defaultRepeatTime);
		}
	}

}
