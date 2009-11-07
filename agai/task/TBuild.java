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
import agai.info.IResource;
import agai.info.ISector;
import agai.manager.Manager;
import agai.unit.AGUnit;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.UnitDef;
import com.springrts.ai.oo.WeaponDef;

// Task with unit to build
public class TBuild extends Task {
	private int mindistance;

	/** The pos. */
	private AIFloat3 pos;

	/** The radius. */
	private int radius;
	private boolean solved;
	private Task tasktoassign;
	/** The unit. */
	private UnitDef unitdef;

	/**
	 * Instantiates a new aG task build unit.
	 * 
	 * @param ai
	 *            the ai
	 * @param unitdef
	 *            the unitdef
	 * @param pos
	 *            where to build the unit
	 * @param radius
	 *            at which radius
	 * @param mindistance
	 *            the mindistance
	 * @param tasktoassign
	 *            the tasktoassign
	 * @param manager
	 *            the manager
	 */
	public TBuild(AGAI ai, Manager manager, UnitDef unitdef, AIFloat3 pos,
			int radius, int mindistance, Task tasktoassign) {
		super(ai, manager);
		this.pos = pos;
		this.unitdef = unitdef;
		this.radius = radius;
		this.mindistance = mindistance;
		this.tasktoassign = tasktoassign;
		solved = false;
	}

	public int getMinDistance() {
		return this.mindistance;
	}

	/**
	 * Gets the pos.
	 * 
	 * @return the pos
	 */
	public AIFloat3 getPos() {
		return pos;
	}

	/**
	 * Gets the radius.
	 * 
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	public Task getTasktoassign() {
		return tasktoassign;
	}

	/**
	 * Gets the unit.
	 * 
	 * @return the unit
	 */
	public UnitDef getUnitDef() {
		return unitdef;
	}

	/**
	 * Checks if task-build-tree was created
	 * 
	 * @return true, if is solved
	 */
	public boolean isSolved() {
		return solved;
	}

	public void setPos(AIFloat3 pos) {
		this.pos = pos;
	}

	/**
	 * Task buildtree was created, avoid it to run again
	 */
	public void setSolved() {
		this.solved = true;
	}

	public void setTasktoassign(Task tasktoassign) {
		this.tasktoassign = tasktoassign;
	}

	@Override
	public String toString() {
		return "AGTaskBuildUnit " + unitdef.getName() + " "
				+ unitdef.getHumanName() + manager.getClass().getSimpleName();
	}

	@Override
	public void unitDamaged(AGUnit unit, float damage, AIFloat3 direction,
			WeaponDef weaponDef, boolean paralyzer) {
		ai.msg("");
	}

	@Override
	public void unitFinished(AGUnit builder, AGUnit unit) {
		ai.msg(unit.getDef().getName());
		Task tmp = getTasktoassign();
		unit.setTask(tmp);
		if (tmp != null)
			tmp.unitFinished(builder, unit); // call unitfinished event
		builder.setTask(null); // mark unit as idle, because unit was built
		ai.getInfos().UnitCreated(unit);
	}

	@Override
	public void unitMoveFailed(AGUnit unit) {
		ai.msg("");
	}

	@Override
	public void unitWeaponFired() {
		ai.msg("");
	}

	public IResource getPrice(){
		return ai.getUnits().getPrice(unitdef);
	}
	@Override
	public void execute(AGUnit unit) {
		if (pos==null){ //task has no buildpos, assign one!
			pos=unit.getPos();
			if (unitdef.getSpeed()<=0) //unit to build can move itself, ignore position
				pos=unit.getBuildPos(pos , unitdef, this.radius, this.mindistance);
			if (pos==null)//empty buildpos
				pos=new AIFloat3();
		}
		ISector target=ai.getInfos().getSectors().getSector(pos);
		if ((ai.getInfos().getSectors().isPosInSec(unit.getPos(), target)) || (unit.getDef().getSpeed()<=0)){
			unit.buildUnit(unitdef, pos, AGAI.defaultFacing);
		}else
			unit.setTask(new TSecureMove(ai, null, this, target, true));
			
	}
	@Override
	public void assign(AGUnit unit){
		ai.getInfos().IncPlannedUnit(unitdef);
		execute(unit);
	}
}