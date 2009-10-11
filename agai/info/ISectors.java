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

import java.util.LinkedList;
import java.util.List;

import agai.AGAI;
import agai.AGInfos;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.UnitDef;

/**
 * The Class AGMap, used to store Information about the map (insecure area,
 * enemy locations...)
 */
public class ISectors {

	public static final int enemyBuilding = 1;

	public static final int unitAttacked = 10;

	/** The ai. */
	private AGAI ai;

	/** The avg los. */
	private float avgLos; // average line of sight of all units

	/** The map. */
	private ISector map[][];

	private int mark;
	/** The height of map sectors */
	private int secHeight;
	/** The width of map sectors. */
	private int secWidth;

	/**
	 * Instantiates a new aG map.
	 * 
	 * @param ai
	 *            the ai
	 * @param infos 
	 */
	public ISectors(AGAI ai, AGInfos infos) {
		this.ai = ai;
		this.mark = 1;
		LinkedList<IBuildTreeUnit> list = infos.getAGB().getUnitList();
		float tmp = 0;
		for (int i = 0; i < list.size(); i++) {
			tmp = tmp + list.get(i).getUnit().getLosRadius();
		}
		avgLos = Math.round((tmp * 8) / list.size());
		secWidth = Math.round((ai.getClb().getMap().getWidth() * 8) / avgLos);
		secHeight = Math.round((ai.getClb().getMap().getHeight() * 8) / avgLos);
		ai.msg(secWidth + " x " + secHeight + "real map size"
				+ ai.getClb().getMap().getWidth() * 8 + "x"
				+ ai.getClb().getMap().getHeight() * 8);
		if ((secWidth == 0) || (secHeight == 0))
			ai.msg("Fatal: Couldn't create sector map!");
		map = new ISector[secWidth][secHeight];
		for (int i = 0; i < secWidth; i++) {
			for (int j = 0; j < secHeight; j++) {
				map[i][j] = new ISector(ai, i, j, new AIFloat3((avgLos * i), 0,
						(avgLos * j)));
			}
		}

		for (int i = 0; i < secWidth; i++) { // setting evelation
			for (int j = 0; j < secHeight; j++) {
				AIFloat3 pos = map[i][j].getPos();
				pos.y = ai.getClb().getMap().getElevationAt(pos.x, pos.z);
			}
		}
		ai.msg("real map size" + ai.getClb().getMap().getWidth() * 8 + "x"
				+ ai.getClb().getMap().getHeight() * 8);
		updateSlope();
		updateWaterDepth();
	}
	
	/**
	 * Update slope.
	 * each data position in SlopeMap is  2*2 in size
	 */
	public void updateSlope(){
		List<Float> heightmap = ai.getClb().getMap().getSlopeMap();
		int width = ai.getClb().getMap().getWidth()*8;
		AIFloat3 pos=new AIFloat3();
		for (int i=0; i<heightmap.size(); i++){
			pos.x=(i*16)%width;
			pos.z=(i*256)/width;
			ISector sec=getSector(pos);
			float tmp=sec.getMaxslope();
			float slope=heightmap.get(i);
			if (slope>tmp)
				sec.setMaxslope(slope);
		}
	}
	
	/**
	 * Update water depth.
	 */
	public void updateWaterDepth(){
		for (int i=0; i<map.length; i++)
			for (int j=0; j<map[i].length; j++){
				AIFloat3 pos=map[i][j].getPos();
				map[i][j].setWaterdepth(ai.getClb().getMap().getElevationAt(pos.x, pos.z));
		}
	}

	/**
	 * Adds the attacker.
	 * 
	 * @param unit
	 *            the unit
	 */
	public void addDanger(Unit unit) {
		ISector tmp = getSector(unit.getPos());
		UnitDef def = unit.getDef();
		if (tmp == null) {
			ai.msg("couldn't find sector!");
			return;
		}
		if ((def != null) && (def.isAbleToMove()))
			tmp.setEnemyUnits(tmp.getEnemyUnits() + 1);
		else
			tmp.setEnemyBuildings(tmp.getEnemyBuildings() + 1);
	}

	/**
	 * Dump.
	 */
	public void dump() {
		ai.msg(""+secWidth +" "+secHeight);
		for (int i = 0; i < map.length; i++) {
			String line = "";
			for (int j = 0; j < map[i].length; j++) {
				line = line + map[i][j].toString();
				if (map[i][j].getDanger() > 0)
					ai.drawPoint(map[i][j].getPos(), ""
									+ map[i][j].getDanger());
				if (map[i][j].getWaterdepth()<0)
					ai.drawPoint(map[i][j].getPos(), ".");
			}
			ai.msg(line);
		}
	}

	private int extractMin(LinkedList<ISector> queue) {
		int min = Integer.MAX_VALUE;
		int pos = 0;
		for (int i = 0; i < queue.size(); i++) {
			int tmp = getDanger(queue.get(i), 1);
			if (tmp == 0)
				return i;
			if (tmp < min) {
				pos = i;
			}
		}
		return pos;
	}

	public ISector get(int x, int y) {
		if ((x >= 0) && (y >= 0) && (x < secWidth) && (y < secHeight))
			return map[x][y];
		return null;
	}

	/**
	 * Gets the danger.
	 * 
	 * @param sec
	 *            the Sector
	 * @param size
	 *            the of the block to check danger
	 * 
	 * @return the danger
	 */
	public int getDanger(ISector sec, int size) {
		int x = sec.getX();
		int y = sec.getY();
		int posx;
		int posy;
		int danger = sec.getDanger();
		for (int i = 0; i <= size * 2; i++) {
			for (int j = 0; j <= size * 2; j++) {
				posx = x + i - size;
				posy = y + j - size;
				if ((posx > 0) && (posx < secWidth) && (posy > 0)
						&& (posy < secHeight))
					danger = danger + map[posx][posy].getDanger()
							/ (Math.abs(size - i) + Math.abs(size - j) + 1); // greater
																				// distance
																				// ,
																				// lower
																				// weight
			}
		}
		return danger;
	}

	public ISector getNextEnemyTarget(AIFloat3 pos, int minDanger) {
		LinkedList<ISector> secs = new LinkedList<ISector>();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j].getEnemy() > minDanger)
					secs.add(map[i][j]);
			}
		}
		float min = Float.MAX_VALUE;
		int p = 0;
		for (int i = 0; i < secs.size(); i++) {
			double diff = secs.get(i).getDistance(pos);
			if (diff < min) {
				p = i;
			}
		}
		if (secs.size() > 0)
			return secs.get(p);
		return null;
	}

	/**
	 * Gets the sector.
	 * 
	 * @param pos
	 *            the pos
	 * 
	 * @return the sector
	 */
	public ISector getSector(AIFloat3 pos) {
		int x, y;
		x = Math.round(pos.x / avgLos);
		y = Math.round(pos.z / avgLos);
		if (x<=0)
			x=0;
		if (y<=0)
			y=0;
		if (y>=secHeight)
			y=secHeight-1;
		if (x>=secWidth)
			x=secWidth-1;
		return map[x][y];
	}

	public float getSectorSize() {
		return avgLos;
	}

	/**
	 * Gets the secure path, cur.getParent().getParent()... contains the path
	 * 
	 * @param from the from
	 * @param to the to
	 * @param MaxSlope the max slope
	 * @param MaxWaterDepth the max water depth
	 * @param MinWaterDepth the min water depth
	 * 
	 * @return the secure path
	 */
	public LinkedList<ISector> getSecurePath(ISector from, ISector to, float MaxSlope, float MinWaterDepth, float MaxWaterDepth) {
		LinkedList<ISector> queue = new LinkedList<ISector>();
		mark++;
		queue.clear();
		queue.add(from);
		from.setParent(null);
		while (queue.size() > 0) {
			ISector cur = queue.remove(extractMin(queue));
			if (cur == to) { // target reached
				ai.msg("found path!");
				LinkedList<ISector> path = new LinkedList<ISector>();
				while (cur != null) {
					path.addFirst(cur);
					cur = cur.getParent();
				}
				return path;
			}
			cur.setMark(mark);
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (!((i == 1) && (j == 1))) { // do not add curpos to queue
						ISector sec = get(cur.getX() + (1 - i), cur.getY()
								+ (1 - j));
						if ((sec != null) && (sec.getMark() != mark) &&
								(sec.getMaxslope()<=MaxSlope) &&
								(sec.getWaterdepth()>=MinWaterDepth) &&
								(sec.getWaterdepth()<=MaxWaterDepth))
						{
							sec.setMark(mark);
							queue.add(sec);
							sec.setParent(cur);
						}
					}
				}
			}
		}
		return null;
	}

	public boolean isPosInSec(AIFloat3 pos, ISector destination) {
		return (destination.getDistance(pos)<=this.avgLos);
	}

	public void unitDamaged(Unit unit, Unit attacker, float damage) {
		if (attacker != null)
			addDanger(attacker);
		ISector sec = getSector(unit.getPos());
		sec.setDamageReceived(sec.getDamageReceived() + Math.round(damage));
	}

	public void unitDestroyed(Unit unit) {
		ISector tmp = getSector(unit.getPos());
		tmp.setUnitsDied(tmp.getUnitsDied() + 1);
	}

}
