package agai.manager;

import agai.AGAI;
import agai.info.IResource;
import agai.task.TBuild;
import agai.unit.AGUnit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.UnitDef;

public class MExpensiveBuild extends Manager{
	private LinkedList <TBuild> buildtasks;
	public MExpensiveBuild(AGAI ai) {
		super(ai);
		buildtasks = new LinkedList <TBuild>();
	}
	
	public void add(UnitDef unitDef){
		ai.msg(""+unitDef.getName()+" "+unitDef.getHumanName());
		if (ai.getInfos().getAGB().isUnitAvailableOrPlaned(unitDef))//unit already exists or is planed to be built
			return;
		for(int i=0; i<buildtasks.size(); i++){
			if (buildtasks.get(i).getUnitDef().getUnitDefId()==unitDef.getUnitDefId()){
				buildtasks.get(i).incPriority();
				return;
			}
		}
		ai.getInfos().IncPlannedUnit(unitDef);
		TBuild b=new TBuild(ai, this, unitDef, null, AGAI.searchDistance, AGAI.minDistance, null);
		buildtasks.add(b);
	}

	@Override
	public void setResToUse(IResource res, int timetonextchange){
		resToUse.setFrom(res);
	}

	@Override
	public boolean assignTask(AGUnit unit){
		Collections.sort(buildtasks);
		for(int i=buildtasks.size()-1; i>=0; i--){
			TBuild t=buildtasks.get(i);
			AIFloat3 pos=unit.getBuildPos(t.getPos(), t.getUnitDef(), t.getRadius(), t.getMinDistance());
			if (pos!=null){
				if (resToUse.lessOrEqual(buildtasks.get(i).getPrice(), unit.getBuildSpeed())){
					ai.msg("");
					t.setPos(pos);
					resToUse.sub(t.getPrice());
					unit.setTask(t);
					buildtasks.remove(i);
					return true;
				}else
					ai.msg("To few resources to build "+buildtasks.get(i).getUnitDef().getName());
			}
		}
		//assign all unused resources to new resources
		ai.getManagers().get(MResource.class).incResToUse(resToUse);
		return false; //this manager doesn't assign directly
	}

	@Override
	public boolean canSolve(AGUnit unit){
		if (unit.getDef().getBuildOptions().size()<=0)
			return false;
		return true;
	}
	
	@Override
	public void check(){
		ai.msg("");
		for (int i=0; i<buildtasks.size(); i++){
			if (ai.getInfos().getAGB().getBuilder(buildtasks.get(i).getUnitDef())==null){//solve dependencies!
				List<UnitDef> units = ai.getInfos().getAGB().getBuildPath(buildtasks.get(i).getUnitDef());
				if (units!=null){
					for(int j=0; j<units.size(); j++){
						ai.msg("Depend add: "+ units.get(j).getName() + " for " + buildtasks.get(i).getUnitDef().getName());
						add(units.get(j));
					}
				}
			}
		}
	}
	@Override
	public boolean needsResources() {
		return true;
	}
}
