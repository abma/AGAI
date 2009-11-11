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
	private boolean retreat;
	private boolean retreating;
	private float minDistance;
	private AIFloat3 retreatpos;

	public TSecureMove(AGAI ai, Manager manager, Task taskWhenReached, ISector destination, boolean retreat, AIFloat3 retreatpos){
		super(ai, manager);
		this.taskWhenReached = taskWhenReached;
		this.destination = destination;
		this.retreat=retreat;
		this.retreating=false;
		this.retreatpos=retreatpos;
	}

	@Override
	public void assign(AGUnit unit) {
		ai.msg("");
		ISector cursec = ai.getInfos().getSectors().getSector(unit.getPos());
		path = ai.getInfos().getSectors().getSecurePath(cursec, destination, unit);
		execute(unit);
	}

	@Override
	public void unitCommandFinished(AGUnit unit) {
		execute(unit);
	}
	private void retreat(AGUnit unit){
		if (!retreat)
			return;
		if (retreating==true){
			ai.msg("unit already retreats!");
			return;
		}
		destination=ai.getInfos().getSectors().getSector(retreatpos);
		retreating=true;
		ai.msg("retreating!");
		ai.drawPoint(retreatpos, "safe area");
		unit.moveTo(destination.getPos());
	}
	@Override
	public void unitDamaged(AGUnit unit, float damage, AIFloat3 direction,WeaponDef weaponDef, boolean paralyzer) {
		ai.msg("");
		retreat(unit);
	}

	@Override
	public void enemyEnterLOS(AGUnit unit, Unit enemy){
		ai.msg("");
		float dist=enemy.getDef().getLosRadius();
		if (dist>minDistance)
			minDistance=dist;
		retreat(unit);
	}

	@Override
	public void unitEnemyDamaged(AGUnit u, Unit enemy) {
		ai.msg("");
		u.setTask(taskWhenReached);
		this.taskWhenReached.unitEnemyDamaged(u, enemy);
	}

	@Override
	public void unitIdle(AGUnit unit){
		execute(unit);
	}

	@Override
	public void execute(AGUnit unit) {
		if (retreating){
			unit.setTask(taskWhenReached);
			taskWhenReached.unitMoveFailed(unit);
		}

		if (ai.getInfos().getSectors().isPosInSec(unit.getPos(), destination)) {
			ai.msg("Destination reached, back to the old task!");
			unit.setTask(taskWhenReached);
		} else { // when moving, try to avoid danger sectors
			if ((path!=null) &&  (path.size() > 0)){
				ai.msg("Time left: "+ai.getInfos().getTime().getMoveTime(unit, path));
				unit.moveTo(path.remove(0).getPos());
			}else{
				ai.msg("move to pos, but unit isn't in sec? "+unit);
			}
		}
	}
	@Override
	public String toString(){
		String res = "SecureMove ";
		if (taskWhenReached!=null)
			res=res+ taskWhenReached.toString();
		return res;
	}
}