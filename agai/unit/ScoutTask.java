package agai.unit;

import agai.AGAI;
import agai.info.PoIMap;
import agai.info.PoIsMap;
import agai.manager.ScoutManager;

public class ScoutTask extends AGTask{
	public ScoutTask(AGAI ai) {
		super(ai);
	}

	@Override
	public void solve() {
		ai.msg("");
		ai.getAGT().get(ScoutManager.class).solve(this);
	}

	@Override
	public void unitCommandFinished(AGUnit unit){
		PoIMap p=ai.getAGI().getAGP().getNearestPoi(unit.getPos(), PoIsMap.PoIAny, true, true);
		if (p!=null){
			ai.msg("moving to "+p.getPos().x +" " +p.getPos().z);
			unit.moveTo(p.getPos());
			p.setVisited(true);
		}else{
			ai.msg("No point to scout found!");
		}
	}

	@Override
	public void unitDestroyed(AGUnit unit){
		ai.msg("");
		ai.getAGT().addTask(new ScoutTask(ai));
		setRepeat(0);
	}
	public String toString(){
		return "";
	}
	@Override
	public void assign(AGUnit unit){
		unit.setIdle();
		((ScoutManager) ai.getAGT().get(ScoutManager.class)).incScouts();
	}

	@Override
	public void unassign(AGUnit unit){
		((ScoutManager) ai.getAGT().get(ScoutManager.class)).decScouts();
	}
}