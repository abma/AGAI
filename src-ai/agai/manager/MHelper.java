package agai.manager;

import java.util.Iterator;

import com.springrts.ai.oo.CurrentCommand;
import com.springrts.ai.oo.Unit;

import agai.AGAI;
import agai.task.THelp;
import agai.unit.AGUnit;

public class MHelper extends Manager{

	public MHelper(AGAI ai) {
		super(ai);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void check() {
		
		
	}
	
	@Override
	public boolean assignTask(AGUnit u){
		if (u.getUnit().getDef().isAbleToAssist()){
			Iterator<AGUnit> it = ai.getUnits().getUnits().iterator();
			
			while(it.hasNext()){
				AGUnit unit =it.next();
				if (unit.getUnit().getCurrentCommands().size()>0){
					CurrentCommand command = unit.getUnit().getCurrentCommands().get(0);
					System.out.println(unit.toString() + command.getCommandId() +" " +command.getId());
					if (command.getId()==-173){ //FIXME, this is the internal command, not the id from c-interface
						u.setTask(new THelp(ai, this, unit));
						return true;
					}
				}
			}
		}
		return false;
	}
}
