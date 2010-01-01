package agai.info;

import agai.AGAI;

import com.springrts.ai.oo.UnitDef;

//Class to get all Scouts (Scouts are fast, cheap, invisble, can fly,  
public class ISearchUnitScout extends IUnitProperty {

	public ISearchUnitScout(AGAI ai) {
		super(ai);
		properties.add(new IUnitPropertyEvaluatorSpeed(ai, 1, this));
		properties.add(new IUnitPropertyEvaluatorPrice(ai, 1, this));
		this.update();
	}

	public int compare(IBuildTreeUnit o1, IBuildTreeUnit o2) {
		return (int) (o1.getTotalPrice() - o1.getTotalPrice());
	}

	@Override
	public boolean isInlist(UnitDef unit) {
		IBuildTreeUnit tree = ai.getInfos().getAGB().searchNode(unit);
		if ((unit.getLosRadius()<=0) || (unit.getSpeed() <= 0))
			return false;
		if ((tree != null)
				&& ((tree.getBacklink() == null) || (tree.getBacklink().size() == 0))) // filter
																						// commander
																						// out
			return false;
		if ((properties.get(0).getAverageComp(unit) > 0)
				&& (properties.get(1).getAverageComp(unit) > 0) /*
																 * && //faster
																 * than average
																 * (
																 * properties.get
																 * (
																 * 2).getAverageComp
																 * (unit)>0)
																 */) { // cheaper
																		// than
																		// average
			return true;
		}
		return false;
	}
}
