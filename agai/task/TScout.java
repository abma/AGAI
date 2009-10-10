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

import agai.AGAI;
import agai.info.IPoI;
import agai.info.IPoIs;
import agai.manager.MScout;
import agai.manager.Manager;
import agai.unit.AGUnit;

public class TScout extends Task {
	public TScout(AGAI ai, Manager manager) {
		super(ai, manager);
	}

	@Override
	public void assign(AGUnit unit) {
		unit.setIdle();
		((MScout) ai.getManagers().get(MScout.class)).incScouts();
	}

	@Override
	public String toString() {
		return "";
	}

	@Override
	public void unassign(AGUnit unit) {
		((MScout) ai.getManagers().get(MScout.class)).decScouts();
	}

	@Override
	public void unitCommandFinished(AGUnit unit) {
		IPoI p = ai.getInfos().getAGP().getNearestPoi(unit.getPos(),
				IPoIs.PoIAny, true, true);
		if (p != null) {
			ai.msg("moving to " + p.getPos().x + " " + p.getPos().z);
			unit.moveTo(p.getPos());
			p.setVisited(true);
		} else {
			ai.msg("No point to scout found!");
		}
	}

	@Override
	public void unitDestroyed(AGUnit unit) {
		ai.msg("");
		ai.getTasks().add(new TScout(ai, ai.getManagers().get(MScout.class)));
		setRepeat(0);
	}

	@Override
	public boolean canBeDone(AGUnit unit) {
		Manager m=ai.getManagers().get(MScout.class);
		return m.canSolve(this, unit);
	}
}