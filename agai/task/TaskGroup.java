package agai.task;

import agai.AGAI;
import agai.unit.AGUnit;
import agai.unit.UnitGroup;

//A group of Units to give them all the same task
public class TaskGroup extends Task{
	private UnitGroup group;
	private Task task;
	public Task getTask() {
		return task;
	}

	private int size;
	private boolean go;
	private int lastFrame;
	public TaskGroup(AGAI ai, Task task, int size) {
		super(ai);
		this.size=size;
		this.task=task;
		go=false;
		group=new UnitGroup(ai, this);
	}
	
	public void setTask(Task task) {
		this.task=task;
	}

	/**
	 * Adds the unit.
	 * 
	 * @param unit the unit
	 */
	@Override
	public void unitFinished(AGUnit builder, AGUnit unit){
		ai.msg("");
		group.add(unit);
		if (group.size()==size){
			ai.msg("clear to start!");
			go=true;
		}
	}
	
	@Override
	public void solve() {
		ai.msg("");
	}
	
	@Override
	public void unitCommandFinished(AGUnit unit){
		ai.msg(""); //wait until enough units have finished...
		group.getPos();
		if (!go)
			return;
		if (lastFrame+3>ai.getFrame()) //FIXME: there should be a better solution?: avoid mass-unit finished events
			return;
		lastFrame=ai.getFrame();
		if (task!=null)
			task.unitCommandFinished(group);
	}
	
	@Override
	public void unitDestroyed(AGUnit unit){
		ai.msg("");
		if (!go)
			return;
		group.remove(unit);
		if (group.size()==0)
			ai.getAGG().remove(this);
	}
}