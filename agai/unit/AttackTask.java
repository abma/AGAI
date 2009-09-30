package agai.unit;

import java.util.List;

import agai.AGAI;
import agai.info.SectorMap;

import com.springrts.ai.oo.Unit;

//TODO: Auto-generated Javadoc
public class AttackTask extends AGTask{
	private AGAI.ElementType type;

	public AGAI.ElementType getType() {
		return type;
	}

	SectorMap currentsec;
	public AttackTask(AGAI ai, AGAI.ElementType type) {
		super(ai);
		this.type=type;
	}

	@Override
	public void solve() {
		ai.msg("attacking");
		ai.getAGT().get(agai.manager.AttackManager.class).solve(this);
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
			List<Unit> list=ai.getClb().getEnemyUnitsIn(currentsec.getPos(), ai.getAGM().getSectorSize());
			if (list.size()>0){
				unit.attackUnit(list.get(0));
				return;
			}else{
				currentsec.setClean(); //sector is clean
			}
		}
		SectorMap sec=ai.getAGM().getNextEnemyTarget(unit.getPos(), 0);
		if (sec!=null){
			unit.setTask(new SecureMoveTask(ai,this, sec));
			ai.msg("attacking at "+sec.getPos().x +" "+ sec.getPos().y+" "+ sec.getPos().z);
			this.currentsec=sec;
			return;
		}
		ai.getAGT().addTask(new ScoutTask(ai));
		setRepeat(0);
		unit.setTask(null);
		ai.msg("nothing to attack found!");
	}
}
