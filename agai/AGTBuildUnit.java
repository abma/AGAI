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

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.UnitDef;


class AGTaskRealBuild extends AGTask{
	AGTaskBuildUnit buildtask;
	AGTaskRealBuild(AGAI ai, AGTaskBuildUnit buildtask) {
		super(ai);
		this.buildtask=buildtask;
	}
	private void build(AGUnit unit){
		if (buildtask!=null){
			ai.msg(buildtask.toString());
			AIFloat3 pos=buildtask.getPos();
			if (pos==null)
				pos=new AIFloat3(unit.getPos());
			unit.buildUnit(buildtask.getUnitDef(), pos, AGAI.defaultFacing);
		}
	}
	@Override
	public void solve() {
		ai.msg("");
	}

	@Override
	public void unitMoveFailed(AGUnit unit){
		ai.msg("");
	}

	@Override
	public void unitFinished(AGUnit builder, AGUnit unit){
		ai.msg(unit.getDef().getName());
		AGTask tmp=buildtask.getTasktoassign();
		unit.setTask(tmp);
		if (tmp!=null)
			tmp.unitFinished(builder, unit); //call unitfinished event
		setStatusFinished();
		buildtask.setStatusFinished();
		builder.setTask(null); //mark unit as idle, because unit was built
	}
	public void unitIdle(AGUnit unit){
		if (getStatus()!=AGTask.statusFinished){
			build(unit);
		}
	}
	@Override
	public void unitCreated(AGUnit builder, AGUnit unit){
		ai.msg("Setting LastUnitCreated");
		unit.setBuilder(builder);
	}
	@Override
	public void assign(AGUnit unit){
		ai.msg("");
		unit.setIdle();
	}
}

// Task with unit to build
class AGTaskBuildUnit extends AGTask{
	
	/** The pos. */
	private AIFloat3 pos;
	
	/** The unit. */
	private UnitDef unitdef;
	
	/** The radius. */
	private int radius;
	private boolean solved;
	private int mindistance;
	private AGTask tasktoassign;

	public AGTask getTasktoassign() {
		return tasktoassign;
	}

	public void setTasktoassign(AGTask tasktoassign) {
		this.tasktoassign = tasktoassign;
	}

	/**
	 * Instantiates a new aG task build unit.
	 * 
	 * @param ai the ai
	 * @param unit to build
	 * @param pos where to build the unit
	 * @param radius at which radius
	 */
	AGTaskBuildUnit(AGAI ai, UnitDef unitdef, AIFloat3 pos, int radius, int mindistance, AGTask tasktoassign) {
		super(ai);
		this.pos=pos;
		this.unitdef=unitdef;
		this.radius=radius;
		this.mindistance=mindistance;
		this.tasktoassign=tasktoassign;
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
	public UnitDef getUnitDef() {
		return unitdef;
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
		ai.getAGT().get(AGTBuildUnit.class).solve(this);
	}
	
	/* (non-Javadoc)
	 * @see agai.AGTask#solveFailed()
	 */
	@Override
	public void solveFailed(){
		ai.getAGT().get(AGTBuildUnit.class).solveFailed(this);
	}
	
	@Override
	public String toString() {
		return "AGTaskBuildUnit " + unitdef.getName() + " "+ unitdef.getHumanName();
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

	public int getMinDistance() {
		return this.mindistance;
	}

	public void setPos(AIFloat3 pos) {
		this.pos=pos;
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
		AIFloat3 pos=task.getPos();
		if (unit.getDef().getSpeed()>0){ //Unit who builds can move
			ai.msg("Mobile builder ");
			pos=unit.canBuildAt(pos, task.getUnitDef(), task.getRadius(), task.getMinDistance());
			if (pos==null){
				ai.msg("Can't build at pos");
				task.setFailed();
				return -1;
			}
		}else{ //builder can't move, build at builder pos
			ai.msg("Static builder");
			if (pos==null)
				pos=unit.getPos();
		}
		ai.msg("Sending build command to "+unit.getDef().getName()+ " build " + task.getUnitDef().getName()+pos);
		task.setStatusWorking();
		unit.setTask(new AGTaskRealBuild(ai, task));
		unit.buildUnit(task.getUnitDef(), pos, AGAI.defaultFacing);
		return 0;
	}
	
	
	/**
	 * Builds the unit and adds all needed tasks (if for ex. a factory is needed)
	 * 
	 * @param unit the unit to be built
	 */
	private void BuildUnit(UnitDef unit){
		ai.msg(unit.getName());
		AGUnit builder=ai.getAGU().getUnit(ai.getAGU().getUnitDef("armcom")); //FIXME search for commander (should search the "next" unit in tree to build)
		if (builder==null) //no commander found, search builder
			ai.msg("Commander is dead, here is missing some code...");
		AGBuildTreeUnit tmp=ai.getAGB().searchNode(builder.getUnit().getDef(),unit);
		if (tmp!=null){
			while(tmp.getUnit()!=builder.getUnit().getDef()){
				AGTaskBuildUnit cur=new AGTaskBuildUnit(ai, tmp.getUnit(),null, AGAI.searchDistance, AGAI.minDistance, null);
				cur.setSolved();
				if (!unit.equals(cur.getUnitDef())) //don't add the unit to build, because it's already in the task list
					if (tmp.getUnitcount()+tmp.getPlannedunits()<=0){ //don't build builders that are already present (see FIXME also)
						ai.getAGT().addTask(cur);
						tmp.setPlannedunits(tmp.getPlannedunits()+1);
					}
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
		List <AGUnit> builder=ai.getAGB().getBuilder(t.getUnitDef());
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
				BuildUnit(t.getUnitDef());
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
