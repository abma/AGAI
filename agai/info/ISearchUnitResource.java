package agai.info;

import agai.AGAI;

import com.springrts.ai.oo.Resource;
import com.springrts.ai.oo.UnitDef;

//Class to search for Resource Extractors, sort by max. production
public class ISearchUnitResource extends IUnitProperty{
	private Resource res;
	public ISearchUnitResource(AGAI ai, Resource res) {
		super(ai);
		this.res=res;
	}
	
	/* (non-Javadoc)
	 * @see agai.UnitProperty#isInlist(com.springrts.ai.oo.UnitDef)
	 */
	@Override
	public boolean isInlist(UnitDef unit) {
		IBuildTreeUnit tree = ai.getInfos().getAGB().searchNode(unit);
		if ((tree!=null) && ((tree.getBacklink()==null) || (tree.getBacklink().size()==0))) //unit can't be build! (filter commander out)
			return false;
		if (ai.getUnits().getProduction(unit, res)>0){
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(IBuildTreeUnit o1, IBuildTreeUnit o2) {
		double a=0, b=0;
		UnitDef u1=o1.getUnit();
		UnitDef u2=o2.getUnit();
		a=a+ai.getUnits().getProduction(u1, res);
		b=b+ai.getUnits().getProduction(u2, res);
		if (a<b)
			return 1;
		if (a>b)
			return -1;
		return 0;
	}

}