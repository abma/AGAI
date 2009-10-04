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
import agai.manager.MAttack;
import agai.manager.MScout;
import agai.manager.Manager;
import agai.unit.AGUnit;

import com.springrts.ai.oo.Unit;

// TODO: Auto-generated Javadoc
//TODO: Auto-generated Javadoc
/**
 * The Class TaskAttack.
 */
public class TAttack extends Task{

	/** The type. */
	private AGAI.ElementType type;

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public AGAI.ElementType getType() {
		return type;
	}

	/** The currentsec. */
	ISector currentsec;

	/**
	 * Instantiates a new task attack.
	 * 
	 * @param ai the ai
	 * @param type the type
	 * @param manager the manager
	 */
	public TAttack(AGAI ai, Manager manager, AGAI.ElementType type) {
		super(ai, manager);
		this.type=type;
	}

	/* (non-Javadoc)
	 * @see agai.task.Task#assign(agai.unit.AGUnit)
	 */
	@Override
	public void assign(AGUnit unit){
		ai.msg("unit assigned");
		unit.setIdle();
	}

	/* (non-Javadoc)
	 * @see agai.task.Task#unitCommandFinished(agai.unit.AGUnit)
	 */
	@Override
	public void unitCommandFinished(AGUnit unit){
		ai.msg(""+unit);
		if(currentsec!=null){//unit reached sec, cleaned?
			List<Unit> list=ai.getClb().getEnemyUnitsIn(currentsec.getPos(), ai.getInfos().getAGM().getSectorSize());
			if (list.size()>0){
				unit.attackUnit(list.get(0));
				return;
			}else{
				currentsec.setClean(); //sector is clean
			}
		}
		ISector sec=ai.getInfos().getAGM().getNextEnemyTarget(unit.getPos(), 0);
		if (sec!=null){
			unit.setTask(new TSecureMove(ai,ai.getManagers().get(MAttack.class),this, sec));
			ai.msg("attacking at "+sec.getPos().x +" "+ sec.getPos().y+" "+ sec.getPos().z);
			this.currentsec=sec;
			return;
		}
		ai.getTasks().add(new TScout(ai, ai.getManagers().get(MScout.class)));
		setRepeat(0);
		unit.setTask(null);
		ai.msg("nothing to attack found!");
	}
}
