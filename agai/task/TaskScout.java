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
import agai.info.PoIMap;
import agai.info.PoIsMap;
import agai.manager.ManagerScout;
import agai.unit.AGUnit;

public class TaskScout extends Task{
	public TaskScout(AGAI ai) {
		super(ai);
	}

	@Override
	public void solve() {
		ai.msg("");
		ai.getAGT().get(ManagerScout.class).solve(this);
	}

	@Override
	public void unitCommandFinished(AGUnit unit){
		PoIMap p=ai.getAGI().getAGP().getNearestPoi(unit.getPos(), PoIsMap.PoIAny, true, true);
		if (p!=null){
			ai.msg("moving to "+p.getPos().x +" " +p.getPos().z);
			unit.moveTo(p.getPos());
			p.setVisited(true);
		}else{
			ai.msg("No point to scout found!");
		}
	}

	@Override
	public void unitDestroyed(AGUnit unit){
		ai.msg("");
		ai.getAGT().addTask(new TaskScout(ai));
		setRepeat(0);
	}
	public String toString(){
		return "";
	}
	@Override
	public void assign(AGUnit unit){
		unit.setIdle();
		((ManagerScout) ai.getAGT().get(ManagerScout.class)).incScouts();
	}

	@Override
	public void unassign(AGUnit unit){
		((ManagerScout) ai.getAGT().get(ManagerScout.class)).decScouts();
	}
}