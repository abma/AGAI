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
		if (a>b)
			return 1;
		else return 0;
	}
	public boolean isInlist(UnitDef unit){
		if  (((unit.getSpeed()>0) && (unit.getLosRadius()>0))){
			updateMinMax(unit); //This call is needed to "add" the list to the properties list
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
		ai.getAGT().getScout().solve(this);
	}
	
}


/**
 * The Class AGTScout.
 */
public class AGTScout extends AGTaskManager{

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
		/*		AGUnit scout=ai.getAGU().getIdle(type)
		if (scout==null){ //no idle scout found, build scout
			ai.msg("No scout found, Building scout!");
			ai.getAGT().getBuildunit().add(ai.getAGU().getUnitDef(AGUnitScout.class), null);
			return;
		}
		PoI point=ai.getAGP().getNearestPoi(scout.getPos(),AGPoIs.PoIAny);
		point.setVisited(true);
//		scout.setTask(this);
		if ((scout.moveTo(point.getPos())==0))
//			this.setStatus(AGTask.statusWorking);
 * 
 */
	}
}