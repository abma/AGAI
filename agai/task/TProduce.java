package agai.task;

import agai.AGAI;
import agai.manager.Manager;
import agai.unit.AGUnit;

public class TProduce extends Task {
	public TProduce(AGAI ai, Manager manager) {
		super(ai, manager);
	}
	@Override
	public void unitIdle(AGUnit unit){
		ai.msg("");
	}
	
	@Override
	public void unitCommandFinished(AGUnit unit){
		ai.msg("");
	}
	
	@Override
	public void execute(AGUnit unit) {
		ai.msg("");
	}

}
