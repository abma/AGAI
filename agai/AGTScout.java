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
		if (a<b)
			return 1;
		if (a>b)
			return -1;
		return 0;
	}
	public boolean isInlist(UnitDef unit){
		AGBuildTreeUnit tree = ai.getAGB().searchNode(unit);
		if ((tree!=null) && ((tree.getBacklink()==null) || (tree.getBacklink().size()==0))) //filter commander out
			return false;
		if  (((unit.getSpeed()>0) && (unit.getLosRadius()>0))){
			updateMinMax(unit); //This call is needed to "add" the list to the properties list
			return true;
		}
		return false;
	}
}

class AGTaskBuildScout extends AGTask{

	AGTaskBuildScout(AGAI ai) {
		super(ai);
	}

	@Override
	public void solve() {
		ai.msg("");
		ai.getAGT().getScout().solve(this);
	}
	public void unitFinished(AGUnit unit){
		ai.msg("");
		this.setStatusFinished();
	}
}

class AGTaskScout extends AGTask{
	AGTaskScout(AGAI ai) {
		super(ai);
	}

	@Override
	public void solve() {
		ai.msg("");
	}

	@Override
	public void unitCommandFinished(AGUnit unit){
		ai.msg("");
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
	public void unitDestroyed(){
		ai.msg("");
		ai.getAGT().addTask(new AGTaskBuildScout(ai));
		this.setStatusFinished();
	}
	public String toString(){
		return "";
	}
	@Override
	public void assign(AGUnit unit){
		unit.setIdle();
		ai.getAGT().getScout().incScouts();
	}

	@Override
	public void unassign(AGUnit unit){
		ai.getAGT().getScout().decScouts();
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
		ai.msg("");
		UnitDef unit=null;
		if (task.getUnit()==null){
			for(int i=0; i<list.size(); i++){
				unit=list.get(i).getUnit();
				AGUnit u=ai.getAGU().getIdle(unit);
				if (u!=null){ //unit to scout exists, assign task!
					ai.msg("assigned task to existing scout");
					u.setTask(new AGTaskScout(ai));
					task.setStatusFinished();
					return;
				}
				AGUnit builder=ai.getAGU().getBuilder(unit);
				if (builder!=null){
					AGTask buildtask=new AGTaskBuildUnit(ai, unit, null, AGAI.searchDistance, AGAI.minDistance, new AGTaskScout(ai));
					ai.getAGT().addTask(buildtask);
					task.setStatusFinished();
					return;
				}
			}
		}
		//no scout found / couldn't build scout, build cheapest one
		if (unit!=null){
			AGTask buildtask=new AGTaskBuildUnit(ai, unit, null, AGAI.searchDistance, AGAI.minDistance, new AGTaskScout(ai));
			ai.getAGT().addTask(buildtask);
			task.setStatusFinished();
		}
	}
}