package agai.manager;

import agai.AGAI;
import agai.info.IResource;
import agai.task.TBuild;
import agai.unit.AGUnit;

import java.util.Collections;
import java.util.LinkedList;

import com.springrts.ai.oo.UnitDef;

public class MExpensiveBuild extends Manager{
	LinkedList <TBuild> list;
	public MExpensiveBuild(AGAI ai) {
		super(ai);
		list=new LinkedList<TBuild>();
	}
	
	public void add(UnitDef unitDef){
		ai.msg(""+unitDef.getName()+" "+unitDef.getHumanName());
		if (ai.getInfos().getAGB().searchNode(unitDef).getPlannedunits()>0) //unit will already be built
			return;
		for(int i=0; i<list.size(); i++){
			if (list.get(i).getUnitDef()==unitDef){
				list.get(i).incPriority();
				return;
			}
		}
		TBuild b=new TBuild(ai, this, unitDef, null, AGAI.searchDistance, AGAI.minDistance, null);
		list.add(b);
	}

	@Override
	public void setResToUse(IResource res, int timetonextchange){
		Collections.sort(list);
		for(int i=0; i<list.size(); i++){
			AGUnit builder = ai.getUnits().getBuilder(list.get(i).getUnitDef());
			if (builder!=null){
				ai.msg("");
				if (res.lessOrEqual(list.get(i).getPrice(), builder.getBuildSpeed())){
					ai.msg("");
					ai.getTasks().add(list.get(i));
					ai.getInfos().IncPlannedUnit(list.get(i).getUnitDef());
					res.sub(list.get(i).getPrice());
				}else
					ai.msg("To few resources to build "+list.get(i).getUnitDef().getName());
			}else
				ai.msg("Found no builder to build "+ list.get(i).getUnitDef().getName());
		}
		//assign all unused resources to new resources
		ai.getManagers().get(MResource.class).incResToUse(res);
	}
}
