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
package agai.info;

import java.util.List;
import com.springrts.ai.oo.Resource;

import agai.AGAI;
import agai.unit.AGUnit;

public class IResources {
	private AGAI ai;
	private List <Resource> resources;
	private IResource res;
	private int lastupdate=-1;
	
	private int lastchanged;
	public int getLastchanged() {
		return lastchanged;
	}

	public IResource get() {
		this.update();
		return res;
	}

	public IResources(AGAI ai){
		this.ai=ai;
		resources=ai.getClb().getResources();
		this.res=new IResource(ai);
	}
	
	/*
	 * update the resource infos to the current state
	 */
	public IResource update(){
		if (ai.getFrame()<lastupdate)
			for (int i=0; i<resources.size(); i++){
				res.setIncome(i, ai.getClb().getEconomy().getIncome(resources.get(i)));
				res.setCurrent(i, ai.getClb().getEconomy().getCurrent(resources.get(i)));
				res.setUseage(i, ai.getClb().getEconomy().getUsage(resources.get(i)));
				res.setStorage(i, ai.getClb().getEconomy().getStorage(resources.get(i)));
			}
		lastupdate=ai.getFrame();
		return res;
	}
	public void UnitCreated(AGUnit u) {
		if (!u.getProduction().isZero())
			lastchanged=ai.getFrame();
	}

	public void UnitDestroyed(AGUnit u) {
		if (!u.getProduction().isZero())
			lastchanged=ai.getFrame();
	}
	
	/*
	 * Check storage useage, 0.0 means empty 1.0 means full
	 */
	public float getStore(int resid) {
		this.update();
		if (res.getStorage(resid)==0)
			return 0;
		return res.getCurrent(resid)/res.getStorage(resid);
	}

}
