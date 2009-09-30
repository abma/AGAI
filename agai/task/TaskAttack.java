package agai.task;

import java.util.List;

import agai.AGAI;
import agai.info.SectorMap;
import agai.unit.AGUnit;

import com.springrts.ai.oo.Unit;

//TODO: Auto-generated Javadoc
public class TaskAttack extends Task{
	private AGAI.ElementType type;

	public AGAI.ElementType getType() {
		return type;
	}

	SectorMap currentsec;
	public TaskAttack(AGAI ai, AGAI.ElementType type) {
		super(ai);
		this.type=type;
	}

	@Override
	public void solve() {
		ai.msg("attacking");
		ai.getAGT().get(agai.manager.ManagerAttack.class).solve(this);
	}
	@Override
	public void assign(AGUnit unit){
		ai.msg("unit assigned");
		unit.setIdle();
	}
	@Override
	public void unitCommandFinished(AGUnit unit){
		ai.msg(""+unit);
		if(currentsec!=null){//unit reached sec, cleaned?
			List<Unit> list=ai.getClb().getEnemyUnitsIn(currentsec.getPos(), ai.getAGI().getAGM().getSectorSize());
			if (list.size()>0){
				unit.attackUnit(list.get(0));
				return;
			}else{
				currentsec.setClean(); //sector is clean
			}
		}
		SectorMap sec=ai.getAGI().getAGM().getNextEnemyTarget(unit.getPos(), 0);
		if (sec!=null){
			unit.setTask(new TaskSecureMove(ai,this, sec));
			ai.msg("attacking at "+sec.getPos().x +" "+ sec.getPos().y+" "+ sec.getPos().z);
			this.currentsec=sec;
			return;
		}
		ai.getAGT().addTask(new TaskScout(ai));
		setRepeat(0);
		unit.setTask(null);
		ai.msg("nothing to attack found!");
	}
}
