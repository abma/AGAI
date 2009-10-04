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
import agai.manager.Manager;
import agai.unit.AGUnit;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.UnitDef;
import com.springrts.ai.oo.WeaponDef;

// Task with unit to build
public class TBuild extends Task {
	TBuild buildtask;
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

	@Override
	public void assign(AGUnit unit) {
		ai.msg("");
		unit.setIdle();
	}

	private void build(AGUnit unit) {
		if (buildtask != null) {
			ai.msg(buildtask.toString());
			AIFloat3 pos = buildtask.getPos();
			if (pos == null)
				pos = new AIFloat3(unit.getPos());
			unit.buildUnit(buildtask.getUnitDef(), pos, AGAI.defaultFacing);
		}
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
				+ unitdef.getHumanName();
	}

	@Override
	public void unitCreated(AGUnit builder, AGUnit unit) {
		ai.msg("Setting LastUnitCreated");
		unit.setBuilder(builder);
	}

	@Override
	public void unitDamaged(AGUnit unit, float damage, AIFloat3 direction,
			WeaponDef weaponDef, boolean paralyzer) {
		ai.msg("");
	}

	@Override
	public void unitFinished(AGUnit builder, AGUnit unit) {
		ai.msg(unit.getDef().getName());
		Task tmp = buildtask.getTasktoassign();
		unit.setTask(tmp);
		if (tmp != null)
			tmp.unitFinished(builder, unit); // call unitfinished event
		setRepeat(0);
		buildtask.setRepeat(0);
		builder.setTask(null); // mark unit as idle, because unit was built
	}

	@Override
	public void unitIdle(AGUnit unit) {
		build(unit);
	}

	@Override
	public void unitMoveFailed(AGUnit unit) {
		ai.msg("");
	}

	@Override
	public void unitWeaponFired() {
		ai.msg("");
	}

}