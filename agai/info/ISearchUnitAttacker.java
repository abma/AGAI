package agai.info;

import agai.AGAI;

import com.springrts.ai.oo.UnitDef;

public class ISearchUnitAttacker extends IUnitProperty{

	public ISearchUnitAttacker(AGAI ai) {
		super(ai);
		properties.add(new IUnitPropertyEvaluatorLosRadius(ai, 0.01f, this));
		properties.add(new IUnitPropertyEvaluatorSpeed(ai, 0.03f, this));
		properties.add(new IUnitPropertyEvaluatorPrice(ai, 0.99f, this));
	}
	public int compare(IBuildTreeUnit o1, IBuildTreeUnit o2) {
		UnitDef u1=o1.getUnit();
		UnitDef u2=o2.getUnit();
		float a=0;
		float b=0;
		for(int i=0; i<properties.size(); i++){
			a=a+properties.get(i).getNormValue(u1);
			b=b+properties.get(i).getNormValue(u2);
		}
		if (a>b)
			return 1;
		if (a<b)
			return -1;
		return 0;
	}
	public boolean isInlist(UnitDef unit){
		IBuildTreeUnit tree = ai.getInfos().getAGB().searchNode(unit);
		if ((tree!=null) && ((tree.getBacklink()==null) || (tree.getBacklink().size()==0))) //filter commander out
			return false;
		if  (((unit.getSpeed()>0) && (unit.getLosRadius()>0) && (ai.getWeaponDamage(unit)>0))){
			return true;
		}
		return false;
	}
}