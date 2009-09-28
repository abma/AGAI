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

import com.springrts.ai.oo.UnitDef;

// TODO: Auto-generated Javadoc
/**
 * @author matze
 *
 */

//Class to get all Scouts (Scouts are fast, cheap, invisble, can fly,  
class UnitPropertyScout extends AGUnitProperty{

	UnitPropertyScout(AGAI ai) {
		super(ai);
		properties.add(new AGUnitPropertyEvaluatorLosRadius(ai, 0.01f, this));
		properties.add(new AGUnitPropertyEvaluatorSpeed(ai, 0.03f, this));
		properties.add(new AGUnitPropertyEvaluatorPrice(ai, 0.99f, this));
		this.update();
	}
	public int compare(AGBuildTreeUnit o1, AGBuildTreeUnit o2) {
		UnitDef u1=o1.getUnit();
		UnitDef u2=o2.getUnit();
		return (int)(ai.getAGU().getTotalPrice(u2)-ai.getAGU().getTotalPrice(u1));
	}

	public boolean isInlist(UnitDef unit){
		AGBuildTreeUnit tree = ai.getAGB().searchNode(unit);
		if ((tree!=null) && ((tree.getBacklink()==null) || (tree.getBacklink().size()==0))) //filter commander out
			return false;
		if  ((unit.getSpeed()>0) && (unit.getLosRadius()>0) &&
				(properties.get(1).getAverageComp(unit)>0) /*&&  //faster than average
				(properties.get(2).getAverageComp(unit)>0)*/){ //cheaper than average
			return true;
		}
		return false;
	}
}

class AGTaskScout extends AGTask{
	AGTaskScout(AGAI ai) {
		super(ai);
	}

	@Override
	public void solve() {
		ai.msg("");
		ai.getAGT().get(AGTScout.class).solve(this);
	}

	@Override
	public void unitCommandFinished(AGUnit unit){
		AGPoI p=ai.getAGP().getNearestPoi(unit.getPos(), AGPoIs.PoIAny, true, true);
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
		ai.getAGT().addTask(new AGTaskScout(ai));
		setRepeat(0);
	}
	public String toString(){
		return "";
	}
	@Override
	public void assign(AGUnit unit){
		unit.setIdle();
		((AGTScout) ai.getAGT().get(AGTScout.class)).incScouts();
	}

	@Override
	public void unassign(AGUnit unit){
		((AGTScout) ai.getAGT().get(AGTScout.class)).decScouts();
	}
}


/**
 * The Class AGTScout.
 */
public class AGTScout extends AGTaskManager{
	
	/** The count of all scouts */
	private int scouts = 0;
	
	/**
	 * Dec scouts.
	 */
	public void decScouts() {
		scouts--;
		ai.msg(""+scouts);
	}

	/**
	 * Inc scouts.
	 */
	public void incScouts() {
		scouts++;
		ai.msg(""+scouts);
	}

	/** The list. */
	protected List <AGBuildTreeUnit> list;
	
	/**
	 * Instantiates a new aG task scout.
	 * 
	 * @param ai the ai
	 */
	AGTScout(AGAI ai) {
		super(ai);
		list=ai.getAGF().Filter(new UnitPropertyScout(ai));
		for (int i=0; i<list.size(); i++){
			ai.msg(list.get(i).getUnit().getName() +"\t"+ ai.getAGU().getTotalPrice(list.get(i).getUnit()) );
		}
	}
	
	/* (non-Javadoc)
	 * @see agai.AGTaskManager#solve(agai.AGTask)
	 */
	@Override
	public void solve(AGTask task) {
		if (scouts<10){
			ai.buildUnit(task, list, task, AGAI.ElementType.unitAny);
		}
	}
}