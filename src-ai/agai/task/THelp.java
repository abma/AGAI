package agai.task;

import agai.AGAI;
import agai.manager.Manager;
import agai.unit.AGUnit;

public class THelp extends Task {
	AGUnit unitToHelp;
	public THelp(AGAI ai, Manager manager, AGUnit unitToHelp) {
		super(ai, manager);
		this.unitToHelp=unitToHelp;
	}

	@Override
	public void execute(AGUnit unit) {
		unit.help(unitToHelp);
	}

}
