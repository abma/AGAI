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
package agai.task;

import java.util.List;

import agai.AGAI;
import agai.info.ISector;
import agai.manager.Manager;
import agai.unit.AGUnit;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.WeaponDef;

// TODO: Auto-generated Javadoc
/**
 * The Class TaskSecureMove.
 */
public class TSecureMove extends Task {
	private ISector destination;
	private List<ISector> path;
	private Task taskWhenReached;

	public TSecureMove(AGAI ai, Manager manager, Task taskWhenReached,
			ISector destination) {
		super(ai, manager);
		this.taskWhenReached = taskWhenReached;
		this.destination = destination;
	}

	@Override
	public void assign(AGUnit unit) {
		ai.msg("");
		unit.setIdle();
	}

	@Override
	public void unitCommandFinished(AGUnit unit) {
		if (ai.getInfos().getSectors().isPosInSec(unit.getPos(), destination)) {
			ai.msg("Destination reached, back to the old task!");
			setRepeat(0);
			unit.setTask(taskWhenReached);
		} else { // when moving, try to avoid danger sectors
			ai.msg("Sneak moving");
			if (path == null) {
				ISector cursec = ai.getInfos().getSectors()
						.getSector(unit.getPos());
				path = ai.getInfos().getSectors().getSecurePath(cursec, destination, unit.getMaxSlope(), unit.getMinWaterDepth(), unit.getMaxWaterDepth());
			}
			if (path.size() > 0)
				unit.moveTo(path.remove(0).getPos());
			else
				unit.moveTo(destination.getPos());
		}
	}

	@Override
	public void unitDamaged(AGUnit unit, float damage, AIFloat3 direction,
			WeaponDef weaponDef, boolean paralyzer) {
		ai.msg("");
		setRepeat(0);
		unit.setTask(taskWhenReached);
		unit.setIdle();
	}

	@Override
	public void unitEnemyDamaged(AGUnit u, Unit enemy) {
		ai.msg("");
		setRepeat(0);
		u.setTask(taskWhenReached);
		u.setIdle();
	}

	@Override
	public boolean canBeDone(AGUnit unit) {
		ai.msg("Warning: this task shouldn't be in the task list!");
		return false;
	}
}