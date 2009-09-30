package agai.unit;

import java.util.LinkedList;

import agai.AGAI;
import agai.task.Task;
import agai.task.TaskGroup;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.UnitDef;

public class UnitGroup extends AGUnit{
	private LinkedList <AGUnit> units;
	private TaskGroup taskGroup;
	public UnitGroup(AGAI ai, TaskGroup taskGroup) {
		super(ai, null);
		units=new LinkedList<AGUnit>();
		this.taskGroup=taskGroup;
	}
	@Override public int moveTo(AIFloat3 pos){
		for (int i=0; i<units.size(); i++)
			units.get(i).moveTo(pos);
		return 0;
	}
	public void build(UnitDef type, AIFloat3 pos){
		for (int i=0; i<units.size(); i++)
			units.get(i).buildUnit(type, pos, AGAI.defaultFacing);
	}
	
	public void add(AGUnit unit){
		units.add(unit);
	}
	public void remove(AGUnit unit){
		for (int i=units.size()-1; i>=0; i--){
			if (units.get(i)==unit){
				units.remove(i);
				return;
			}
		}
	}
	
	public int size(){
		return units.size();
	}
	@Override
	public String toString(){
		return "AGUnitGroup "+units.size();
	}
	@Override
	public AIFloat3 getPos(){
		float x=0,z=0;
		AIFloat3 pos;
		for (int i=0; i<units.size(); i++){
			pos=units.get(i).getPos();
			x=x+pos.x;
			z=z+pos.z;
		}
		pos=new AIFloat3();
		pos.x=x/units.size();
		pos.z=z/units.size();
		return pos;
	}
	@Override
	public void setIdle(){
		for (int i=0; i<units.size(); i++){
			units.get(i).setIdle();
		}
	}
	@Override
	public void setTask(Task task){
		ai.msg(""+task);
		if (task==null){ //task deleted, delete group
			for(int i=0; i<units.size(); i++){
				units.get(i).setTask(null);
			}
		}
		taskGroup.setTask(task);
	}
}

