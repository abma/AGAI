package agai.manager;

import agai.AGAI;
import agai.info.IResource;
import agai.task.TBuild;
import agai.unit.AGUnit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
		for(int i=0; i<buildtasks.size(); i++){
			TBuild t=buildtasks.get(i);
			if (unit.canBuildAt(t.getPos(), t.getUnitDef(), t.getRadius(), t.getMinDistance())!=null){
				if (resToUse.lessOrEqual(buildtasks.get(i).getPrice(), unit.getBuildSpeed())){
					ai.msg("");
					resToUse.sub(buildtasks.get(i).getPrice());
					unit.setTask(buildtasks.get(i));
					buildtasks.get(i).setPriority(-1); //set lowest priority, because it will be built!
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
		for (int i=0; i<buildtasks.size(); i++)
		if (ai.getInfos().getAGB().getBuilder(buildtasks.get(i).getUnitDef())==null){//solve dependencies!
			ai.msg("Found no builder to build, adding dependencies to list "+ buildtasks.get(i).getUnitDef().getName());
			List<UnitDef> units = ai.getInfos().getAGB().getBuildPath(buildtasks.get(i).getUnitDef());
			if (units!=null){
				for(int j=0; j<units.size(); j++){
					add(units.get(j));
				}
			}
		}
	}

}
