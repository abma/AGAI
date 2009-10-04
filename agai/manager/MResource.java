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
import agai.info.IBuildTreeUnit;
import agai.info.ISearchUnitResource;
import agai.info.IPoI;
import agai.task.Task;
import agai.task.TBuild;
import agai.unit.AGUnit;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.Feature;
import com.springrts.ai.oo.Map;
import com.springrts.ai.oo.Resource;
import com.springrts.ai.oo.UnitDef;




/**
 * The Class AGTBuildResource.
 */
public class MResource extends Manager {
	
	/** The list with units to build resource, the first id is the resid, the secound for the units sorted by priority to built (best units first) */
	protected List <List <IBuildTreeUnit>> list;

	/**
	 * Instantiates a new aGT build AGTBuildResource.
	 * 
	 * @param ai the ai
	 */
	public MResource(AGAI ai) {
		super(ai);
		this.map=ai.getClb().getMap();
		List <Resource> res=ai.getClb().getResources();
		list=new ArrayList  <List <IBuildTreeUnit>>();
		for (int i=0; i<res.size(); i++){
			initializeSpots(res.get(i));
			list.add(i,ai.getInfos().getAGF().Filter(new ISearchUnitResource(ai, res.get(i))));
		}
		List<Feature> f=ai.getClb().getFeatures();
		for (int i=0; i<f.size(); i++){ //FIXME: Engine issue, adds geothermal spots to PoI
			if (f.get(i).getDef().isGeoThermal()){
				ai.getInfos().getAGP().add(f.get(i).getPosition(), ai.getEnergy().getResourceId());
//				ai.drawPoint(f.get(i).getPosition(),"Pos " + i + ai.getEnergy().getName());
			}
		}
		for (int i=0; i<list.size();i++){
			for (int j=0; j<list.get(i).size(); j++){
				UnitDef u=list.get(i).get(j).getUnit();
				ai.msg(i +" "+u.getName() +"\tPrice: "+ai.getUnits().getTotalPrice(u)+"\t" +u.getHumanName() + "\t" + ai.getUnits().getProduction(u, res.get(i)) );
			}
		}
	}

	/** The map. */
	private Map map = null;
	
	/**
	 * Initialize spots.
	 * 
	 * @param res the res
	 */
	private void initializeSpots(Resource res){
		ai.msg("initializeSpots Function "+ai);
		List<AIFloat3> spots=map.getResourceMapSpotsPositions(res);
		for(int i=0; i<spots.size(); i++ ){
			spots.get(i).y=map.getElevationAt(spots.get(i).x, spots.get(i).z); //FIXME this is a engine-bug workaround
//			ai.drawPoint(spots.get(i),"Pos " + i + res.getName());
			ai.getInfos().getAGP().add(spots.get(i), res.getResourceId());
		}
	}

	public void tryTobuild(Resource resource) {
		UnitDef unit=null;
		AGUnit builder=null;
		IPoI poi=null;
		AIFloat3 pos=null; 
		int min=AGAI.minDistance;
		List <IBuildTreeUnit> tmp=list.get(resource.getResourceId());
		int radius=100; //default radius to search for buildings
		for(int i=0; i<tmp.size(); i++){ //search from units that fits best to the worst.. skip when unit can be built and enough resources found
			builder=ai.getUnits().getBuilder(tmp.get(i).getUnit());
			if (builder!=null){ //unit can be built! :-)
				poi=ai.getInfos().getAGP().getNearestFreePoi(builder.getPos(), resource.getResourceId());
				unit=tmp.get(i).getUnit();
				if ((unit.getExtractsResource(resource)>0) || unit.isNeedGeo()){ //unit needs spot to be built
					if (poi==null) //no point found to build, next building
						continue;
					pos=poi.getPos();
					radius=Math.round(ai.getClb().getMap().getExtractorRadius(resource));
					if (unit.isNeedGeo()){
						if (radius==0)
							radius=AGAI.searchDistance;
						min=0; //the only way to build on geo spots (?)
					}
					pos=builder.canBuildAt(pos, unit, radius, min);
					if (pos==null){
						ai.msg("Can't build here!");
						continue;
					}else{
						ai.msg("Can build here!");
						break;
					}
				}else { //doesn't need spot, build at next point to builder
					ai.msg("No spot needed");
					pos=builder.canBuildAt(pos, unit, radius, 2);
					if (pos==null) //can't build, next unit
						continue;
					pos=null; //delete pos, because builder could have already a task and is moving
				}
				Resource res=ai.enoughResourcesToBuild(unit);
				if (res==null){ //no resource is missing, building is ok!
					break;
				}
			}
		}
		if ((unit!=null)&&(builder!=null)){ //unit with builder found, build it!
			if (!builder.isIdle())
				return;
			if (poi!=null){ //build only one time at a spot
				poi.setBuilt(true);
			}
			ai.msg("Sending command to build unit");
			Task buildtask=new TBuild(ai, ai.getManagers().get(MBuild.class), unit, pos, radius, 2, null);
			buildtask.setPoi(poi);
			ai.getTasks().add(buildtask);
		}else{
			if (builder==null)
				ai.msg("No builder found ");
			if (unit==null)
				ai.msg("No resource producing unit found");
		}
	}

	@Override
	public void solve(Task task) {
		// TODO Auto-generated method stub
		
	}
}
