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
import java.util.LinkedList;
import java.util.List;

import agai.AGAI;
import agai.info.IResource;
import agai.info.ISector;
import agai.task.Task;

import com.springrts.ai.AICommand;
import com.springrts.ai.AIFloat3;
import com.springrts.ai.command.AttackAreaUnitAICommand;
import com.springrts.ai.command.AttackUnitAICommand;
import com.springrts.ai.command.BuildUnitAICommand;
import com.springrts.ai.command.MoveUnitAICommand;
import com.springrts.ai.command.PatrolUnitAICommand;
import com.springrts.ai.command.SetOnOffUnitAICommand;
import com.springrts.ai.command.StopUnitAICommand;
import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.UnitDef;

// TODO: Auto-generated Javadoc
/**
 * The Class AGUnit.
 */
public class AGUnit {

	/** The ai. */
	protected AGAI ai = null;

	/** The last unit created. */
	private AGUnit builder = null;

	/** The task. */
	private Task task = null;

	public boolean isBuilt() {
		return unit.isBeingBuilt();
	}

	/** The unit. */
	protected Unit unit = null;

	/**
	 * Instantiates a new aG unit.
	 * 
	 * @param ai
	 *            the ai
	 * @param unit
	 *            the unit
	 */
	public AGUnit(AGAI ai, Unit unit) {
		this.unit = unit;
		this.ai = ai;
	}

	/**
	 * Attack at position.
	 * 
	 * @param pos
	 *            the position to attack
	 * 
	 * @return the int
	 */
	public int attackAt(AIFloat3 pos) {
		AICommand command = new AttackAreaUnitAICommand(unit, -1,
				new ArrayList<AICommand.Option>(), 10000, pos, 0);
		return ai.handleEngineCommand(command);
	}

	/**
	 * Attack unit.
	 * 
	 * @param unit
	 *            the unit
	 * 
	 * @return the int
	 */
	public int attackUnit(Unit unit) {
		AICommand command = new AttackUnitAICommand(unit, -1,
				new ArrayList<AICommand.Option>(), 10000, unit);
		return ai.handleEngineCommand(command);
	}

	/**
	 * Builds the unit.
	 * 
	 * @param type
	 *            the type
	 * @param pos
	 *            the pos
	 * @param facing
	 *            the facing
	 * 
	 * @return the int
	 */
	public int buildUnit(UnitDef type, AIFloat3 pos, int facing) {
		ai.logDebug(""+unit.getDef().getName() +" builds " + type.getName() + pos + facing);
		AICommand command = new BuildUnitAICommand(unit, -1,
				new ArrayList<AICommand.Option>(), 10000, type, pos, facing);
		return ai.handleEngineCommand(command);
	}

	/**
	 * Can build at.
	 * 
	 * @param pos the pos
	 * @param radius the radius
	 * @param minDistance the min distance
	 * @param tobuilt the tobuilt
	 * 
	 * @return position, if successful, null if can't built there
	 */
	public AIFloat3 getBuildPos	(AIFloat3 pos, UnitDef tobuilt, int radius, int minDistance) {
		if (!isAbleToBuilt(tobuilt)){
			return null;
		}
		if ((tobuilt.getSpeed()>0) && (tobuilt.getSpeed()>0)){ //unit to be built can move, build here!
			pos=unit.getPos();
			return pos;
		}
		if ((unit.getSpeed()>0) && (canMoveTo(pos))){ //builder can move to buildpos, build at buildpos
			if (pos==null)
				pos=unit.getPos();
			AIFloat3 tmp = ai.getClb().getMap().findClosestBuildSite(tobuilt, pos, radius, minDistance, 0);
			if ((tmp.x == -1) && (tmp.y == 0) && (tmp.z == 0)) {
				ai.logDebug(tobuilt.getName());
				if (pos==null)
					pos=new AIFloat3();
				ai.logInfo("Can't build here: " + tobuilt.getName() + " radius " + radius
						+ " x " + pos.x + " y " + pos.y + " z " + pos.z);
				return null;
			}
			return tmp;
		}
		if (canMoveTo(pos))
			return pos;
		ai.logInfo("Can't build at " + pos.x + " " + pos.y + " " + pos.z);
		return null;
	}

	/**
	 * called if a unit is destroyed.
	 */
	public void destroyed() {
	}

	public AGUnit getBuilder() {
		return builder;
	}

	/**
	 * Gets the def.
	 * 
	 * @return the def
	 */
	public UnitDef getDef() {
		return unit.getDef();
	}

	/**
	 * Gets the position of the Unit.
	 * 
	 * @return the pos
	 */
	public AIFloat3 getPos() {
		return unit.getPos();
	}

	/**
	 * Gets the task.
	 * 
	 * @return the task
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * Gets the unit.
	 * 
	 * @return the unit
	 */
	public Unit getUnit() {
		return this.unit;
	}

	/**
	 * Gets the unit definiton.
	 * 
	 * @param str
	 *            Short string of the definition (for ex. armcom)
	 * 
	 * @return the unit definiton
	 */
	public UnitDef getUnitDef(String str) {
		return unit.getDef();
	}

	/**
	 * Checks if is idle.
	 * 
	 * @return true, if is idle
	 */
	public boolean isIdle() {
		if (unit.isBeingBuilt())
			return false;
		return (task == null);
	}

	/**
	 * Move to position.
	 * 
	 * @param pos
	 *            the pos
	 * 
	 * @return the int
	 */
	public int moveTo(AIFloat3 pos) {
		AICommand command = new MoveUnitAICommand(unit, -1,
				new ArrayList<AICommand.Option>(), 10000, pos);
		return ai.handleEngineCommand(command);
	}

	public void setBuilder(AGUnit builder) {
		this.builder = builder;
	}

	/**
	 * Sets Unit to Idle (Stop current command)
	 */
	public void setIdle() {
		AICommand command = new StopUnitAICommand(unit, -1,
				new ArrayList<AICommand.Option>(), 1000);
		ai.handleEngineCommand(command);
	}

	/**
	 * Sets the power.
	 * 
	 * @param power
	 *            the new power
	 */
	public void setPower(boolean power) {
		AICommand command = new SetOnOffUnitAICommand(unit, -1,
				new ArrayList<AICommand.Option>(), 10000, power);
		ai.handleEngineCommand(command);
	}

	/**
	 * Sets the task.
	 * 
	 * @param task
	 *            the new task
	 */
	public void setTask(Task task) {
		if (this.task != null) {
			this.task.unassign(this);
		}
		this.task = task;
		if (task != null)
			task.assign(this);
		else
			fetchTask(); //always try to keep a task
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = this.getClass().getName() + " " + unit.getDef().getName();
		if (task != null)
			str = str + " " + task.toString();
		return str;
	}
	
	public boolean isAbleToBuilt(UnitDef type){
		List<UnitDef> buildOptions = unit.getDef().getBuildOptions();
		for (int i = 0; i < buildOptions.size(); i++) {
			if (buildOptions.get(i).getUnitDefId() == type.getUnitDefId()) {
				return true;
			}
		}
		return false;
	}
	private IResource production=null;
	public IResource getProduction(){
		if (production==null){
			production = new IResource(ai);
			for (int i=0; i<ai.getResourcecount(); i++){
				production.setIncome(i, 
				ai.getUnits().getProduction(getDef(), ai.getClb().getResources().get(i)));
				}
			}
		return production;
	}

	public float getMaxSlope() {
		return unit.getDef().getMaxSlope();
	}

	public float getMinWaterDepth() {
		return unit.getDef().getMinWaterDepth();
	}

	public float getMaxWaterDepth() {
		return unit.getDef().getMaxWaterDepth();
	}
	public boolean canMoveTo(AIFloat3 pos){
		if (pos==null) //null means unit pos, so it is already there!
			return true;
		ISector from=ai.getInfos().getSectors().getSector(unit.getPos());
		if (ai.getInfos().getSectors().isPosInSec(pos,from)) //unit is already in sector!
			return true;
		ISector to=ai.getInfos().getSectors().getSector(pos);
		LinkedList<ISector> path = ai.getInfos().getSectors().getSecurePath(from,to, this);
		return (path!=null);
	}

	public int getBuildSpeed() {
		return Math.round(unit.getDef().getBuildSpeed());
	}

	public void fetchTask() {
		ai.getManagers().assignTask(this);
	}

	public int patrolTo(AIFloat3 pos) {
		AICommand command = new PatrolUnitAICommand(unit, -1,
				new ArrayList<AICommand.Option>(), 10000, pos);
		return ai.handleEngineCommand(command);
	}

	/**
	 * Gets the lOS.
	 *
	 * @return the lOS
	 */
	public float getLOS() {
		float ret, tmp;
		ret=unit.getDef().getLosRadius();
/*		unit.getDef().getAirLosRadius();
		unit.getDef().getLosHeight()*/
		tmp=unit.getDef().getRadarRadius();
		if (tmp>ret)
			ret=tmp;
		tmp=unit.getDef().getSonarRadius();
		if (tmp>ret)
			ret=tmp;
		return ret;
	}
	@Override
	public int hashCode(){
		return unit.hashCode();
	}
	
}
