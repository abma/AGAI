package agai.info;

import agai.AGAI;

import com.springrts.ai.oo.UnitDef;

//Class to get all Scouts (Scouts are fast, cheap, invisble, can fly,  
public class SearchScout extends UnitProperty{

	public SearchScout(AGAI ai) {
		super(ai);
		properties.add(new UnitPropertyEvaluatorLosRadius(ai, 0.01f, this));
		properties.add(new UnitPropertyEvaluatorSpeed(ai, 0.03f, this));
		properties.add(new UnitPropertyEvaluatorPrice(ai, 0.99f, this));
		this.update();
	}
	public int compare(BuildTreeUnit o1, BuildTreeUnit o2) {
		UnitDef u1=o1.getUnit();
		UnitDef u2=o2.getUnit();
		return (int)(ai.getAGU().getTotalPrice(u2)-ai.getAGU().getTotalPrice(u1));
	}

	public boolean isInlist(UnitDef unit){
		BuildTreeUnit tree = ai.getAGB().searchNode(unit);
		if ((tree!=null) && ((tree.getBacklink()==null) || (tree.getBacklink().size()==0))) //filter commander out
			return false;
		if  ((unit.getSpeed()>0) && (unit.getLosRadius()>0) &&
				(properties.get(1).getAverageComp(unit)>0) /*&&  //faster than average
				(properties.get(2).getAverageComp(unit)>0)*/){ //cheaper than average
			return true;
		}
		return false;
	}
}
