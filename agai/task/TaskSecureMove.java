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

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.WeaponDef;

import agai.AGAI;
import agai.info.SectorMap;
import agai.unit.AGUnit;

// TODO: Auto-generated Javadoc
/**
 * The Class TaskSecureMove.
 */
public class TaskSecureMove extends Task{
	private Task taskWhenReached;
	private SectorMap destination;
	private List <SectorMap> path;
	public TaskSecureMove(AGAI ai, Task taskWhenReached, SectorMap destination) {
		super(ai);
		this.taskWhenReached=taskWhenReached;
		this.destination=destination;
	}
	@Override
	public void solve() {
	}
	@Override
	public void unitCommandFinished(AGUnit unit){
		if (ai.getAGI().getAGM().isPosInSec(unit.getPos(),destination)){
			ai.msg("Destination reached, back to the old task!");
			setRepeat(0);
			unit.setTask(taskWhenReached);
		}else{ //when moving, try to avoid danger sectors
			ai.msg("Sneak moving");
			if (path==null){
				SectorMap cursec=ai.getAGI().getAGM().getSector(unit.getPos());
				path=ai.getAGI().getAGM().getSecurePath(cursec, destination);
			}
			if (path.size()>0)
				unit.moveTo(path.remove(0).getPos());
			else
				unit.moveTo(destination.getPos());
		}
	}
	@Override
	public void assign(AGUnit unit){
		ai.msg("");
		unit.setIdle();
	}
	@Override
	public void unitEnemyDamaged(AGUnit u, Unit enemy){
		ai.msg("");
		setRepeat(0);
		u.setTask(taskWhenReached);
		u.setIdle();
	}
	@Override
	public void unitDamaged(AGUnit unit, float damage, AIFloat3 direction, WeaponDef weaponDef, boolean paralyzer){
		ai.msg("");
		setRepeat(0);
		unit.setTask(taskWhenReached);
		unit.setIdle();
	}
}