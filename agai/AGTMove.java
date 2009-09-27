package agai;

import java.util.List;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.WeaponDef;

class AGTaskSecureMove extends AGTask{
	private AGTask taskWhenReached;
	private AGSector destination;
	private List <AGSector> path;
	AGTaskSecureMove(AGAI ai, AGTask taskWhenReached, AGSector destination) {
		super(ai);
		this.taskWhenReached=taskWhenReached;
		this.destination=destination;
	}
	@Override
	public void solve() {
	}
	@Override
	public void unitCommandFinished(AGUnit unit){
		if (ai.getAGM().isPosInSec(unit.getPos(),destination)){
			ai.msg("Destination reached, back to the old task!");
			setStatusFinished();
			unit.setTask(taskWhenReached);
		}else{ //when moving, try to avoid danger sectors
			ai.msg("Sneak moving");
			if (path==null){
				AGSector cursec=ai.getAGM().getSector(unit.getPos());
				path=ai.getAGM().getSecurePath(cursec, destination);
			}
			if (path.size()>0)
				unit.moveTo(path.remove(0).getPos());
			else
				unit.moveTo(destination.getPos());
		}
	}
	@Override
	public void assign(AGUnit unit){
		ai.msg("");
		unit.setIdle();
	}
	@Override
	public void unitEnemyDamaged(AGUnit unit, Unit enemy){
		ai.msg("");
		setStatusFinished();
		unit.setTask(taskWhenReached);
		unit.setIdle();
	}
	@Override
	public void unitDamaged(AGUnit unit, float damage, AIFloat3 direction, WeaponDef weaponDef, boolean paralyzer){
		ai.msg("");
		setStatusFinished();
		unit.setTask(taskWhenReached);
		unit.setIdle();
	}
}

public class AGTMove extends AGTaskManager{

	AGTMove(AGAI ai) {
		super(ai);
	}

	@Override
	public void solve(AGTask task) {
		
	}
	
	
}
