package agai;

import java.util.LinkedList;

import agai.task.Task;

public class AGTasks {
	LinkedList <Task> list;
	AGAI ai;
	AGTasks(AGAI ai){
		this.ai=ai;
		list=new LinkedList<Task>();
	}
	public void add(Task task){
		list.add(task);
	}
	public void dump() {
		// TODO Auto-generated method stub
		
	}
	public void clear() {
		// TODO Auto-generated method stub
		
	}
}
