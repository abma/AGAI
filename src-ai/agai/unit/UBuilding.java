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

import java.util.ArrayList;

import agai.AGAI;

import com.springrts.ai.AICommand;
import com.springrts.ai.AIFloat3;
import com.springrts.ai.command.BuildUnitAICommand;
import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.UnitDef;

public class UBuilding extends AGUnit {

	protected UBuilding(AGAI ai, Unit unit) {
		super(ai, unit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agai.unit.AGUnit#buildUnit(com.springrts.ai.oo.UnitDef,
	 * com.springrts.ai.AIFloat3, int)
	 */
	@Override
	public int buildUnit(UnitDef type, AIFloat3 pos, int facing) {
		pos = unit.getPos();
		AICommand command = new BuildUnitAICommand(unit, -1,
				new ArrayList<AICommand.Option>(), 10000, type, pos, facing);
		return ai.handleEngineCommand(command);
	}

	@Override
	public int moveTo(AIFloat3 pos) {
		ai.logDebug("a building can't move!");
		return 0;
	}

}
