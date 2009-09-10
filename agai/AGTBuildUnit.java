/*
 * Copyright (C) 2009 Matthias Ableitner (http://abma.de/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
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

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.UnitDef;


// TODO: Auto-generated Javadoc
// Task with unit to build
class AGTaskBuildUnit extends AGTask{
	
	/** The pos. */
	private AIFloat3 pos;
	
	/** The unit. */
	private UnitDef unit;
	
	/** The radius. */
	private int radius;
	private boolean solved;

	/**
	 * Instantiates a new aG task build unit.
	 * 
	 * @param ai the ai
	 * @param unit to build
	 * @param pos where to build the unit
	 * @param radius at which radius
	 */
	AGTaskBuildUnit(AGAI ai, UnitDef unit, AIFloat3 pos, int radius) {
		super(ai);
		this.pos=pos;
		this.unit=unit;
		this.radius=radius;
		solved=false;
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
	 * Gets the unit.
	 * 
	 * @return the unit
	 */
	public UnitDef getUnit() {
		return unit;
	}


	/**
	 * Gets the radius.
	 * 
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	/* (non-Javadoc)
	 * @see agai.AGTask#solve()
	 */
	@Override
	public void solve() {
		ai.getAGT().getBuildunit().solve(this);
	}
	
	/* (non-Javadoc)
	 * @see agai.AGTask#solveFailed()
	 */
	@Override
	public void solveFailed(){
		ai.getAGT().getBuildunit().solveFailed(this);
	}
	
	//To avoid duplicate buildings
	//FIXME?
	@Override
	public boolean equals(Object obj) {
		if (obj==null)
			return false;
		if (obj.getClass()==this.getClass()){
			AGTaskBuildUnit tmp=(AGTaskBuildUnit)obj;
			return tmp.unit.equals(unit); //FIXME: more checks : pos, radius (?)
		}
		return false;
	}

	@Override
	public String toString() {
		return "AGTaskBuildUnit " + unit.getName() + " "+ unit.getHumanName();
	}

	/**
	 * Checks if task-build-tree was created 
	 * 
	 * @return true, if is solved
	 */
	public boolean isSolved() {
		return solved;
	}

	/**
	 * Task buildtree was created, avoid it to run again
	 */
	public void setSolved() {
		this.solved = true;
	}
}

/**
 * The Class AGTaskBuildUnit, handles build commands.
 */
public class AGTBuildUnit extends AGTaskManager{
	
	/**
	 * Instantiates a new aG task build unit.
	 * 
	 * @param ai the ai
	 */
	AGTBuildUnit(AGAI ai) {
		super(ai);
	}

	
	/**
	 * Send build command to unit and do some checks to parameters.
	 * 
	 * @param unit the unit
	 * @param task the task
	 * 
	 * @return the int
	 */
	private int realBuild(AGUnit unit, AGTaskBuildUnit task){
		int mindist=3;
		int facing=0;
		AIFloat3 pos=task.getPos();
		if (unit.getDef().getSpeed()>0){ //Unit who builds can move
			ai.msg("Mobile builder ");
			if ((task.getPos()==null) ||
					((task.getPos().equals(new AIFloat3())))){//position was not initialized, use builder pos
				ai.msg("Using Unit pos to build, because pos wasn't initialized.");
				pos=unit.getPos();
			}
			pos=ai.getClb().getMap().findClosestBuildSite(task.getUnit(), pos , task.getRadius(), mindist, facing);
			if (!ai.getClb().getMap().isPossibleToBuildAt(task.getUnit(), pos, facing) || pos.equals(new AIFloat3(-1.0f, 0.0f, 0.0f))){
				ai.msg("AGTBuildUnit.realBuild(): Can't build at pos " + pos.x + " " + pos.y + " " + pos.z);
				task.setFailed();
				return -1;
			}
		}else{ //builder can't move, build at builder pos
			ai.msg("Static builder");
			if (pos==null)
				pos=unit.getPos();
		}
		ai.msg("Sending build command to "+unit.getDef().getName()+ " build " + task.getUnit().getName()+pos);
		int res=unit.buildUnit(task.getUnit(), pos, facing);
		task.setStatusWorking(unit);
		return res;
	}
	
	
	/**
	 * Builds the unit and adds all needed tasks (if for ex. a factory is needed)
	 * 
	 * @param unit the unit to be built
	 * 
	 */
	private void BuildUnit(UnitDef unit){
		ai.msg(unit.getName());
		AGUnit builder=ai.getAGU().getUnit(ai.getAGU().getUnitDef("armcom")); //FIXME search for commander (should search the "next" unit in tree to build)
		if (builder==null) //no commander found, search builder
			ai.msg("Commander is dead, here is missing some code...");
		AGBuildTreeUnit tmp=ai.getAGB().searchNode(builder.getUnit().getDef(),unit);
		if (tmp!=null){
			while(tmp.getUnit()!=builder.getUnit().getDef()){
				AGTaskBuildUnit cur=new AGTaskBuildUnit(ai, tmp.getUnit(),null, 200);
				cur.setSolved();
				if (!unit.equals(cur.getUnit())) //don't add the unit to build, because it's already in the task list
					ai.getAGT().addTask(cur);
				tmp=tmp.getParent();
			}
		}else
			ai.msg("couldn't solve");
		
	}
	
	/* (non-Javadoc)
	 * @see agai.AGTaskManager#solve(agai.AGTask)
	 */
	@Override
	public void solve(AGTask task) {
		ai.msg(""+task);
		AGTaskBuildUnit t=(AGTaskBuildUnit)task;
		List <AGUnit> builder=ai.getAGB().getBuilder(t.getUnit());
		if (builder.size()>0){ //found unit who can build.. try it
			for (int j=0; j<builder.size(); j++){
				if (builder.get(j).isIdle()){
					int res=realBuild(builder.get(j),t);
					if (res!=0){
						ai.msg("Error in building... "+res);
						task.solveFailed();
					}
					break;
				}
			}
		}else{ //found no builder -> resolve
			if (!t.isSolved()){
				BuildUnit(t.getUnit());
				t.setSolved();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see agai.AGTaskManager#solveFailed(agai.AGTask)
	 */
	@Override
	public boolean solveFailed(AGTask task){
		ai.msg(task.toString());
		task.setStatusIdle(); //retry
		return false;
	}
}
