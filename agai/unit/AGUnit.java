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
import com.springrts.ai.command.AttackAreaUnitAICommand;
import com.springrts.ai.command.AttackUnitAICommand;
import com.springrts.ai.command.BuildUnitAICommand;
import com.springrts.ai.command.MoveUnitAICommand;
import com.springrts.ai.command.SetOnOffUnitAICommand;
import com.springrts.ai.command.StopUnitAICommand;
import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.UnitDef;



// TODO: Auto-generated Javadoc
/**
 * The Class AGUnit.
 */
public class AGUnit{
	
	/** The unit. */
	protected Unit unit=null;
	
	/** The task. */
	private AGTask task=null;

	/** The last unit created. */
	private AGUnit builder=null;
	
	public AGUnit getBuilder() {
		return builder;
	}

	public void setBuilder(AGUnit builder) {
		this.builder = builder;
	}

	/** The ai. */
	protected AGAI ai=null;
	
	/**
	 * Instantiates a new aG unit.
	 * 
	 * @param ai the ai
	 * @param unit the unit
	 */
	public AGUnit(AGAI ai, Unit unit){
		this.unit=unit;
		this.ai=ai;
	}
	
	/**
	 * Gets the unit.
	 * 
	 * @return the unit
	 */
	public Unit getUnit(){
		return this.unit;
	}
	
	
	/**
	 * Gets the task.
	 * 
	 * @return the task
	 */
	public AGTask getTask() {
		return task;
	}
	
	/**
	 * Sets the task.
	 * 
	 * @param task the new task
	 */
	public void setTask(AGTask task) {
		if (this.task!=null){
			this.task.unassign(this);
		}
		this.task=task;
		if (task!=null)
			task.assign(this);
	}

	/**
	 * called if a unit is destroyed.
	 */
	public void destroyed(){
		if (task!=null){ //if unit had task, redo the task
			task.setRepeat(AGTask.defaultRepeatTime);
		}
	}
	
	/**
	 * Attack at position.
	 * 
	 * @param pos the position to attack
	 * 
	 * @return the int
	 */
	public int attackAt(AIFloat3 pos){
		AICommand command = new AttackAreaUnitAICommand(unit, -1,new ArrayList<AICommand.Option>(), 10000, pos, 0);
		return ai.handleEngineCommand(command);
	}
	
	/**
	 * Attack unit.
	 * 
	 * @param unit the unit
	 * 
	 * @return the int
	 */
	public int attackUnit(Unit unit){
		AICommand command = new AttackUnitAICommand(unit, -1,new ArrayList<AICommand.Option>(), 10000, unit);
		return ai.handleEngineCommand(command);
	}
	
	/**
	 * Gets the unit definiton.
	 * 
	 * @param str Short string of the definition (for ex. armcom)
	 * 
	 * @return the unit definiton
	 */
	public UnitDef getUnitDef(String str){
		return unit.getDef();
	}
	
	/**
	 * Gets the position of the Unit.
	 * 
	 * @return the pos
	 */
	public AIFloat3 getPos(){
		return unit.getPos();
	}
	
	/**
	 * Checks if is idle.
	 * 
	 * @return true, if is idle
	 */
	public boolean isIdle(){
		return task==null;
	}
	
	/**
	 * Move to position.
	 * 
	 * @param pos the pos
	 * 
	 * @return the int
	 */
	public int moveTo(AIFloat3 pos){
		AICommand command = new MoveUnitAICommand(unit, -1,new ArrayList<AICommand.Option>(), 10000, pos);
		return ai.handleEngineCommand(command);
	}

	/**
	 * Builds the unit.
	 * 
	 * @param type the type
	 * @param pos the pos
	 * @param facing the facing
	 * 
	 * @return the int
	 */
	public int buildUnit(UnitDef type,AIFloat3 pos, int facing){
		AICommand command = new BuildUnitAICommand(unit, -1,new ArrayList<AICommand.Option>(), 10000, type, pos, facing);
		return ai.handleEngineCommand(command);
	}

	/**
	 * Sets the power.
	 * 
	 * @param power the new power
	 */
	public void setPower(boolean power){
		AICommand command = new SetOnOffUnitAICommand(unit, -1, new ArrayList<AICommand.Option>(), 10000, power);
		ai.handleEngineCommand(command);
	}
	
	/**
	 * Gets the def.
	 * 
	 * @return the def
	 */
	public UnitDef getDef(){
		return unit.getDef();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String str=this.getClass().getName() +" "+unit.getDef().getName();
		if (task!=null)
			str=str+" "+task.toString();
		return str;
	}

	/**
	 * Can build at.
	 *
	 * @param pos the pos
	 * @param unit the unit to build
	 * @param radius the radius
	 * @param minDistance the min distance
	 *
	 * @return true, if successful
	 */
	public AIFloat3 canBuildAt(AIFloat3 pos, UnitDef unit, int radius, int minDistance){
		if ((pos==null)||((pos.x==-1) && (pos.y==0) && (pos.z==0)))
			pos=this.unit.getPos();
		AIFloat3 tmp=ai.getClb().getMap().findClosestBuildSite(unit, pos, radius, minDistance, 0);
		if ((tmp.x==-1) && (tmp.y==0) && (tmp.z==0)){
			ai.msg("Can't build here: "+unit.getName()+" radius "+radius+" x " +pos.x+" y "+pos.y+" z "+pos.z);
			return null;
		}
		ai.msg("Can build at " + tmp.x +" "+ tmp.y +" " +tmp.z);
		return tmp;
	}

	/**
	 * Sets Unit to Idle (Stop current command)
	 */
	public void setIdle() {
		AICommand command = new StopUnitAICommand(unit, -1, new ArrayList<AICommand.Option>(), 1000);
		ai.handleEngineCommand(command);
	}

}
