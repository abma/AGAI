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

package agai;

import java.util.List;

import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.UnitDef;

class UnitPropertyAttacker extends AGUnitProperty{

	UnitPropertyAttacker(AGAI ai) {
		super(ai);
		properties.add(new AGUnitPropertyEvaluatorLosRadius(ai, 0.01f, this));
		properties.add(new AGUnitPropertyEvaluatorSpeed(ai, 0.03f, this));
		properties.add(new AGUnitPropertyEvaluatorPrice(ai, 0.99f, this));
	}
	public int compare(AGBuildTreeUnit o1, AGBuildTreeUnit o2) {
		UnitDef u1=o1.getUnit();
		UnitDef u2=o2.getUnit();
		float a=0;
		float b=0;
		for(int i=0; i<properties.size(); i++){
			a=a+properties.get(i).getNormValue(u1);
			b=b+properties.get(i).getNormValue(u2);
		}
		if (a>b)
			return 1;
		if (a<b)
			return -1;
		return 0;
	}
	public boolean isInlist(UnitDef unit){
		AGBuildTreeUnit tree = ai.getAGB().searchNode(unit);
		if ((tree!=null) && ((tree.getBacklink()==null) || (tree.getBacklink().size()==0))) //filter commander out
			return false;
		if  (((unit.getSpeed()>0) && (unit.getLosRadius()>0) && (ai.getWeaponDamage(unit)>0))){
			return true;
		}
		return false;
	}
}

class AGTaskAttackMove extends AGTask{
	private AGTask taskWhenReached;
	private AGSector destination;
	private List <AGSector> path;
	AGTaskAttackMove(AGAI ai, AGTask taskWhenReached, AGSector destination) {
		super(ai);
		this.taskWhenReached=taskWhenReached;
		this.destination=destination;
	}

	@Override
	public void solve() {
	}
	@Override
	public void unitCommandFinished(AGUnit unit){
		if (ai.getAGM().isPosInSec(unit.getPos(),destination)){
			ai.msg("Destination reached, back to the old task!");
			setStatusFinished();
			unit.setTask(taskWhenReached);
		}else{ //when moving, try to avoid danger sectors
			ai.msg("Sneak moving");
			if (path==null){
				AGSector cursec=ai.getAGM().getSector(unit.getPos());
				path=ai.getAGM().getSecurePath(cursec, destination);
			}
			if (path.size()>0)
				unit.moveTo(path.remove(0).getPos());
			else
				unit.moveTo(destination.getPos());
		}
	}
	@Override
	public void assign(AGUnit unit){
		unit.setIdle();
	}
}

// TODO: Auto-generated Javadoc
class AGTaskAttack extends AGTask{
	AGSector currentsec;
	AGTaskAttack(AGAI ai) {
		super(ai);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void solve() {
		ai.msg("attacking");
		
	}
	@Override
	public void assign(AGUnit unit){
		ai.msg("unit assigned");
		unit.setIdle();
	}
	@Override
	public void unitCommandFinished(AGUnit unit){
		ai.msg(""+unit);
		if(currentsec!=null){//unit reached sec, cleaned?
			List<Unit> list=ai.getClb().getEnemyUnitsIn(currentsec.getPos(), ai.getAGM().getSectorSize());
			if (list.size()>0){
				unit.attackUnit(list.get(0));
				return;
			}else{
				currentsec.setClean(); //sector is clean
			}
		}
		AGSector sec=ai.getAGM().getNextEnemyTarget(unit.getPos(), 0);
		if (sec!=null){
			unit.setTask(new AGTaskAttackMove(ai,this, sec));
			ai.msg("attacking at "+sec.getPos().x +" "+ sec.getPos().y+" "+ sec.getPos().z);
			this.currentsec=sec;
			return;
		}
		ai.getAGT().addTask(new AGTaskBuildScout(ai));
		setStatusFinished();
		unit.setTask(null);
		ai.msg("nothing to attack found!");
	}
}

class AGTaskBuildAttacker extends AGTask{
	private AGAI.ElementType type;

	public AGAI.ElementType getType() {
		return type;
	}

	AGTaskBuildAttacker(AGAI ai, AGAI.ElementType type) {
		super(ai);
		this.type=type;
	}

	@Override
	public void solve() {
		ai.msg("");
		ai.getAGT().get(AGTAttack.class).solve(this);
	}
	@Override
	public void unitFinished(AGUnit builder, AGUnit unit){
		ai.msg("");
		this.setStatusFinished();
	}
}

/**
 * The Class AGTAttack.
 * 
 * @author matze
 */
public class AGTAttack extends AGTaskManager{
	
	protected List <AGBuildTreeUnit> list;
	/**
	 * Instantiates a new aG task attack.
	 * 
	 * @param ai the ai
	 */
	AGTAttack(AGAI ai) {
		super(ai);
		list=ai.getAGF().Filter(new UnitPropertyAttacker(ai));
		for (int i=0; i<list.size(); i++){
			ai.msg(list.get(i).getUnit().getName() +"\t"+ ai.getAGU().getTotalPrice(list.get(i).getUnit()) );
		}
	}
	
	/**
	 * Dump.
	 */
	public void dump(){
		ai.msg("Task Attack");
	}

	/* (non-Javadoc)
	 * @see agai.AGTaskManager#solve(agai.AGTask)
	 */
	@Override
	public void solve(AGTask task) {
		ai.buildUnit(task, list, new AGTaskAttack(ai), ((AGTaskBuildAttacker)task).getType());
	}
}
