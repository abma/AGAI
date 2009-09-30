package agai.task;

import java.util.List;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.WeaponDef;

import agai.AGAI;
import agai.info.SectorMap;
import agai.unit.AGUnit;

public class TaskSecureMove extends Task{
	private Task taskWhenReached;
	private SectorMap destination;
	private List <SectorMap> path;
	public TaskSecureMove(AGAI ai, Task taskWhenReached, SectorMap destination) {
		super(ai);
		this.taskWhenReached=taskWhenReached;
		this.destination=destination;
	}
	@Override
	public void solve() {
	}
	@Override
	public void unitCommandFinished(AGUnit unit){
		if (ai.getAGI().getAGM().isPosInSec(unit.getPos(),destination)){
			ai.msg("Destination reached, back to the old task!");
			setRepeat(0);
			unit.setTask(taskWhenReached);
		}else{ //when moving, try to avoid danger sectors
			ai.msg("Sneak moving");
			if (path==null){
				SectorMap cursec=ai.getAGI().getAGM().getSector(unit.getPos());
				path=ai.getAGI().getAGM().getSecurePath(cursec, destination);
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
	public void unitEnemyDamaged(AGUnit u, Unit enemy){
		ai.msg("");
		setRepeat(0);
		u.setTask(taskWhenReached);
		u.setIdle();
	}
	@Override
	public void unitDamaged(AGUnit unit, float damage, AIFloat3 direction, WeaponDef weaponDef, boolean paralyzer){
		ai.msg("");
		setRepeat(0);
		unit.setTask(taskWhenReached);
		unit.setIdle();
	}
}