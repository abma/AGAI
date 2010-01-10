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
package agai.manager;

import agai.AGAI;
import agai.info.IResource;
import agai.task.TBuild;
import agai.unit.AGUnit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.UnitDef;

// TODO: Auto-generated Javadoc
/**
 * The Class MExpensiveBuild.
 */
public class MExpensiveBuild extends Manager{
	
	/** The buildtasks. */
	private LinkedList <TBuild> buildtasks;
	
	/**
	 * Instantiates a new m expensive build.
	 * 
	 * @param ai the ai
	 */
	public MExpensiveBuild(AGAI ai) {
		super(ai);
		buildtasks = new LinkedList <TBuild>();
	}
	
	/**
	 * Adds the.
	 * 
	 * @param unitDef the unit def
	 */
	public void add(UnitDef unitDef){
		ai.logDebug(""+unitDef.getName()+" "+unitDef.getHumanName());
		if (ai.getInfos().getAGB().isUnitAvailableOrPlaned(unitDef))//unit already exists or is planed to be built
			return;
		for(int i=0; i<buildtasks.size(); i++){
			if (buildtasks.get(i).getUnitDef().getUnitDefId()==unitDef.getUnitDefId()){
				buildtasks.get(i).incPriority();
				return;
			}
		}
		ai.getInfos().IncPlannedUnit(unitDef);
		TBuild b=new TBuild(ai, this, unitDef, null, AGAI.searchDistance, AGAI.minDistance, null);
		buildtasks.add(b);
	}

	/* (non-Javadoc)
	 * @see agai.manager.Manager#setResToUse(agai.info.IResource, int)
	 */
	@Override
	public void setResToUse(IResource res, int timetonextchange){
		resToUse.setFrom(res);
	}

	/* (non-Javadoc)
	 * @see agai.manager.Manager#assignTask(agai.unit.AGUnit)
	 */
	@Override
	public boolean assignTask(AGUnit unit){
		Collections.sort(buildtasks);
		for(int i=buildtasks.size()-1; i>=0; i--){
			TBuild t=buildtasks.get(i);
			AIFloat3 pos=unit.getBuildPos(t.getPos(), t.getUnitDef(), t.getRadius(), t.getMinDistance());
			if (pos!=null){
				if (resToUse.lessOrEqual(buildtasks.get(i).getPrice(), unit.getBuildSpeed())){
					ai.logDebug("");
					t.setPos(pos);
					resToUse.sub(t.getPrice());
					unit.setTask(t);
					buildtasks.remove(i);
					return true;
				}else
					ai.logWarning("To few resources to build "+buildtasks.get(i).getUnitDef().getName());
			}
		}
		//assign all unused resources to new resources
		ai.getManagers().get(MResource.class).incResToUse(resToUse);
		return false; //this manager doesn't assign directly
	}

	/* (non-Javadoc)
	 * @see agai.manager.Manager#canSolve(agai.unit.AGUnit)
	 */
	@Override
	public boolean canSolve(AGUnit unit){
		if (unit.getDef().getBuildOptions().size()<=0)
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see agai.manager.Manager#check()
	 */
	@Override
	public void check(){
		ai.logDebug("");
		for (int i=0; i<buildtasks.size(); i++){
			if (ai.getInfos().getAGB().getBuilder(buildtasks.get(i).getUnitDef())==null){//solve dependencies!
				List<UnitDef> units = ai.getInfos().getAGB().getBuildPath(buildtasks.get(i).getUnitDef());
				if (units!=null){
					for(int j=0; j<units.size(); j++){
						ai.logDebug("Depend add: "+ units.get(j).getName() + " for " + buildtasks.get(i).getUnitDef().getName());
						add(units.get(j));
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see agai.manager.Manager#needsResources()
	 */
	@Override
	public boolean needsResources() {
		return true;
	}
}
