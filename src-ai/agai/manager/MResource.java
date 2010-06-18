/*
 * Copyright (C) 2009 Matthias Ableitner (http://abma.de/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package agai.manager;

import java.util.ArrayList;
import java.util.List;

import agai.AGAI;
import agai.Str;
import agai.info.IBuildTreeUnit;
import agai.info.IPoI;
import agai.info.IResource;
import agai.info.ISearchUnitResource;
import agai.task.TBuild;
import agai.unit.AGUnit;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.Feature;
import com.springrts.ai.oo.Map;
import com.springrts.ai.oo.Resource;
import com.springrts.ai.oo.UnitDef;

// TODO: Auto-generated Javadoc
/**
 * The Class AGTBuildResource.
 */
public class MResource extends Manager {

	/** The list with units to build resource, the first id is the resid, the secound for the units sorted by priority to built (best units first). */
	protected ArrayList<List<IBuildTreeUnit>> list;

	/** The map. */
	private Map map = null;

	/**
	 * Instantiates a new aGT build AGTBuildResource.
	 * 
	 * @param ai the ai
	 */
	public MResource(AGAI ai) {
		super(ai);
		this.map = ai.getClb().getMap();
		List<Resource> res = ai.getClb().getResources();
		list = new ArrayList<List<IBuildTreeUnit>>();
		for (int i = 0; i < res.size(); i++) {
			initializeSpots(res.get(i));
			list.add(i, ai.getInfos().getSearch().Filter(new ISearchUnitResource(ai, res.get(i))));
		}
		List<Feature> f = ai.getClb().getFeatures();
		for (int i = 0; i < f.size(); i++) { // FIXME: Engine issue, adds
												// geothermal spots to PoI
			if (f.get(i).getDef().isGeoThermal()) {
				ai.getInfos().getAGP().add(f.get(i).getPosition(),ai.getEnergy().getResourceId());
			}
		}
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).size(); j++) {
				UnitDef u = list.get(i).get(j).getUnit();
				ai.logDebug(i + " " + new Str(u.getName()) + new Str("Price:")
						+new Str(""+list.get(i).get(j).getPrice())
						+new Str(u.getHumanName())
						+new Str(ai.getUnits().getProduction(u, res.get(i))));
			}
		}
	}

	/**
	 * Initialize spots.
	 * 
	 * @param res the res
	 */
	private void initializeSpots(Resource res) {
		ai.logDebug("initializeSpots Function " + ai);
		List<AIFloat3> spots = map.getResourceMapSpotsPositions(res);
		for (int i = 0; i < spots.size(); i++) {
			spots.get(i).y = map.getElevationAt(spots.get(i).x, spots.get(i).z);
			ai.getInfos().getAGP().add(spots.get(i), res.getResourceId());
		}
	}

	/**
	 * Try tobuild.
	 * 
	 * @param builder the builder
	 * @param resource the resource
	 * @param time the time
	 * 
	 * @return the t build
	 */
	private TBuild tryTobuild(AGUnit builder, Resource resource, int time) {
		UnitDef unit = null;
		IPoI poi = null;
		AIFloat3 pos = null;
		int min = AGAI.minDistance;
		List<IBuildTreeUnit> tmp = list.get(resource.getResourceId());
		int radius = AGAI.searchDistance; // default radius to search for buildings
		for (int i = 0; i < tmp.size(); i++) { // search from units that fits
												// best to the worst.. skip when
												// unit can be built and enough
												// resources found
			if (builder.isAbleToBuilt(tmp.get(i).getUnit())) { // unit can be built! :-)
				unit = tmp.get(i).getUnit();
				if ((unit.getExtractsResource(resource) > 0)|| unit.isNeedGeo()) { // unit needs spot to be built
					poi = ai.getInfos().getAGP().getNearestFreeBuildPoi(builder.getPos(), resource.getResourceId());
					if (poi == null){ // no point found to build, next building
						ai.logWarning("No spot found to build " + unit.getName());
						continue;
					}
					pos = poi.getPos();
					radius = Math.round(ai.getClb().getMap().getExtractorRadius(resource));
					if (unit.isNeedGeo()) {
						if (radius == 0)
							radius = AGAI.searchDistance;
						min = 0; // the only way to build on geo spots (?)
					}
					pos = builder.getBuildPos(pos, unit, radius, min);
					if (pos == null) {
						ai.logDebug("Can't build here!");
						continue;
					} else {
						ai.logDebug("Can build here!");
						break;
					}
				} else { // doesn't need spot, build at next point to builder
					ai.logDebug("No spot needed");
					pos = builder.getBuildPos(pos, unit, radius, min);
					if (pos == null) // can't build, next unit
						continue;
					pos = null; // delete pos, because builder could have
								// already a task and is moving
				}
				if (ai.getInfos().getAGB().getPrice(unit).lessOrEqual(getResToUse(), time)){
					ai.logDebug("Enough Resources!");
					break;
				}
			}
			unit=null;
		}
		if (unit != null) { // unit with builder found, build it!
			ai.logDebug("Creating Task for Unit!");
			MBuild b=(MBuild) ai.getManagers().get(MBuild.class);
			TBuild buildtask = new TBuild(ai, b, unit, pos, radius, min, null);
			buildtask.setPoi(poi);
			return buildtask;
		} else {
			ai.logInfo("No resource producing unit found for "+builder.getUnit().getDef().getName());
		}
		return null;
	}
	
	
	/**
	 * Decide which resource to built.
	 * 
	 * @return the resource
	 */
	private Resource decide(){
		float maxpercent=0;
		int pos=0;;
		ai.logDebug("");
		IResource cur=ai.getInfos().getResources().get();
		ai.logDebug(""+cur);
		for (int i=0; i<ai.getResourcecount(); i++){ //build resource whos usage has most percent
			float income=cur.getIncome(i);
			float usage=cur.getUseage(i);
			float percent=0;
			if (income!=0){
				percent=usage/income;
			}
			if (percent>maxpercent){
				pos=i;
				maxpercent=percent;
			}
		}
		for (int i=0; i<ai.getResourcecount(); i++){ //or build resource whos storage is maxpercent
			float current=cur.getCurrent(i);
			float storage=cur.getStorage(i);
			float percent=0;
			if (current!=0){
				percent=1-storage/current;
			}
			ai.logDebug(""+percent);
			if (percent>maxpercent){
				pos=i;
				maxpercent=percent;
			}
		}
		ai.logDebug(""+pos);
		return ai.getClb().getResources().get(pos);
	}
	
	
	/* (non-Javadoc)
	 * @see agai.manager.Manager#setResToUse(agai.info.IResource, int)
	 */
	@Override
	public void setResToUse(IResource res, int timetonextchange){
		resToUse.setFrom(res);
	}

	/* (non-Javadoc)
	 * @see agai.manager.Manager#assignTask(agai.unit.AGUnit)
	 */
	@Override
	public boolean assignTask(AGUnit unit){
		Resource r=decide();
		ai.logInfo("Building "+r.getName());
		TBuild task=tryTobuild(unit, r, 1000);
		if (task!=null){
			unit.setTask(task);
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see agai.manager.Manager#canSolve(agai.unit.AGUnit)
	 */
	@Override
	public boolean canSolve(AGUnit unit){ 
		if (unit.getDef().getBuildOptions().size()<=0)
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see agai.manager.Manager#needsResources()
	 */
	@Override
	public boolean needsResources() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see agai.manager.Manager#check()
	 */
	@Override
	public void check(){
	}
	
}
