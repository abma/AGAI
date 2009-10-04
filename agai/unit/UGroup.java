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
package agai.unit;

import java.util.LinkedList;

import agai.AGAI;
import agai.task.TGroup;
import agai.task.Task;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.UnitDef;

// TODO: Auto-generated Javadoc
/**
 * The Class UnitGroup.
 */
public class UGroup extends AGUnit {
	private TGroup taskGroup;
	private LinkedList<AGUnit> units;

	public UGroup(AGAI ai, TGroup taskGroup) {
		super(ai, null);
		units = new LinkedList<AGUnit>();
		this.taskGroup = taskGroup;
	}

	public void add(AGUnit unit) {
		units.add(unit);
	}

	public void build(UnitDef type, AIFloat3 pos) {
		for (int i = 0; i < units.size(); i++)
			units.get(i).buildUnit(type, pos, AGAI.defaultFacing);
	}

	@Override
	public AIFloat3 getPos() {
		float x = 0, z = 0;
		AIFloat3 pos;
		for (int i = 0; i < units.size(); i++) {
			pos = units.get(i).getPos();
			x = x + pos.x;
			z = z + pos.z;
		}
		pos = new AIFloat3();
		pos.x = x / units.size();
		pos.z = z / units.size();
		return pos;
	}

	@Override
	public int moveTo(AIFloat3 pos) {
		for (int i = 0; i < units.size(); i++)
			units.get(i).moveTo(pos);
		return 0;
	}

	public void remove(AGUnit unit) {
		for (int i = units.size() - 1; i >= 0; i--) {
			if (units.get(i) == unit) {
				units.remove(i);
				return;
			}
		}
	}

	@Override
	public void setIdle() {
		for (int i = 0; i < units.size(); i++) {
			units.get(i).setIdle();
		}
	}

	@Override
	public void setTask(Task task) {
		ai.msg("" + task);
		if (task == null) { // task deleted, delete group
			for (int i = 0; i < units.size(); i++) {
				units.get(i).setTask(null);
			}
		}
		taskGroup.setTask(task);
	}

	public int size() {
		return units.size();
	}

	@Override
	public String toString() {
		return "AGUnitGroup " + units.size();
	}
}
