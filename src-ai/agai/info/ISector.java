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

import agai.AGAI;

import com.springrts.ai.AIFloat3;

//TODO: Auto-generated Javadoc
public class ISector {
	private AGAI ai;
	private int damageReceived;
	private int enemyBuildings; // enemy attackers seen
	private int enemyUnits;
	private int lastvisit; // frame
	private int mark;
	private int markgrey;
	private float maxslope; //max slope in sector (is it passable?)
	private float waterdepth;

	public float getWaterdepth() {
		return waterdepth;
	}

	public void setWaterdepth(float waterdepth) {
		this.waterdepth = waterdepth;
	}

	private ISector parent;
	private int unitsDied;

	private int x;

	private int z;
	
	private float realx;
	private float realz;

	ISector(AGAI ai, int x, int z, AIFloat3 pos) {
		this.ai = ai;
		this.x = x;
		this.z = z;
		this.realx=pos.x;
		this.realz=pos.z;
	}

	public float getMaxslope() {
		return maxslope;
	}

	public void setMaxslope(float maxslope) {
		this.maxslope = maxslope;
	}

	public int getDamageReceived() {
		return damageReceived;
	}

	public int getDanger() {
		return enemyBuildings + damageReceived + enemyUnits + unitsDied;
	}

	/**
	 * Gets the distance from this sector
	 * 
	 * @param pos the pos
	 * 
	 * @return the distance
	 */
	public double getDistance(AIFloat3 pos) {
		return ai.getInfos().getDistance(this.getPos(), pos);
	}

	public int getEnemy() {
		return enemyBuildings + enemyUnits;
	}

	public int getEnemyBuildings() {
		return enemyBuildings;
	}

	public int getEnemyUnits() {
		return enemyUnits;
	}

	public int getLastvisit() {
		return lastvisit;
	}

	public int getMark() {
		return mark;
	}

	public int getMarkgrey() {
		return markgrey;
	}

	public ISector getParent() {
		return parent;
	}

	public AIFloat3 getPos() {
		return new AIFloat3(realx, ai.getClb().getMap().getElevationAt(realx, realz), realz);
	}

	public int getUnitsDied() {
		return unitsDied;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return z;
	}

	public void setClean() {
		enemyBuildings = 0;
		enemyUnits = 0;
		damageReceived = 0;
		unitsDied = 0;
	}

	public void setDamageReceived(int damageReceived) {
		this.damageReceived = damageReceived;
	}

	public void setEnemyBuildings(int enemyBuildings) {
		this.enemyBuildings = enemyBuildings;
	}

	public void setEnemyUnits(int enemyUnits) {
		this.enemyUnits = enemyUnits;
	}

	public void setLastvisit(int lastvisit) {
		this.lastvisit = lastvisit;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public void setMarkgrey(int markgrey) {
		this.markgrey = markgrey;
	}

	public void setParent(ISector parent) {
		this.parent = parent;
	}

	public void setUnitsDied(int unitsDied) {
		this.unitsDied = unitsDied;
	}

	@Override
	public String toString() {
		return "" + enemyBuildings + " " + enemyUnits + " " + damageReceived;
	}

}