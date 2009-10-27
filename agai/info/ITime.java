package agai.info;

import java.util.List;

import com.springrts.ai.oo.UnitDef;

import agai.AGAI;
import agai.unit.AGUnit;

public class ITime {
	private AGAI ai;

	public ITime(AGAI ai){
		this.ai=ai;
	}
	public int getMoveTime(AGUnit unit, List<ISector> path){
		float speed=unit.getDef().getSpeed();
		double dist=0;
		for (int i=0; i<path.size()-1; i++){
			dist=dist+(ai.getInfos().getDistance(path.get(i).getPos(), path.get(i+1).getPos()));
		}
		return (int) Math.round(dist/speed);
	}
	
	public int getBuildTime(AGUnit unit, UnitDef tobuild){
		return Math.round(unit.getDef().getBuildSpeed()/tobuild.getBuildTime());
	}
}
