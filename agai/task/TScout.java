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
import agai.info.ISector;
import agai.manager.MScout;
import agai.manager.Manager;
import agai.unit.AGUnit;

public class TScout extends Task {
	public TScout(AGAI ai, Manager manager) {
		super(ai, manager);
	}

	@Override
	public void assign(AGUnit unit) {
		((MScout) ai.getManagers().get(MScout.class)).incScouts();
		execute(unit);
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
		execute(unit);
	}

	@Override
	public void unitDestroyed(AGUnit unit) {
		ai.msg("");
		setRepeat(0);
	}
	@Override
	public void execute(AGUnit unit) {
		IPoI p = ai.getInfos().getAGP().getNearestPoi(unit.getPos(),
				IPoIs.PoIAny, true, true);
		if (p == null) {
			ai.msg("No point to scout found!");
			return;
		}
		if (unit.canMoveTo(p.getPos())){
			ISector destination=ai.getInfos().getSectors().getSector(p.getPos());
			unit.setTask(new TSecureMove(ai, null, this, destination));
			p.setVisited(true);
		}else{
			ai.msg("Unit can't move to PoI");
		}
	}

}