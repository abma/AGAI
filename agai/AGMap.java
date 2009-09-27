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
package agai;


import java.util.LinkedList;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.UnitDef;

// TODO: Auto-generated Javadoc
class AGSector{
	private int enemyBuildings; //enemy attackers seen
	private int damageReceived;
	private int enemyUnits;
	private int unitsDied;
	private int mark;
	private int markgrey;
	public int getMarkgrey() {
		return markgrey;
	}

	public void setMarkgrey(int markgrey) {
		this.markgrey = markgrey;
	}
	private AGSector parent;
	public AGSector getParent() {
		return parent;
	}

	public void setParent(AGSector parent) {
		this.parent = parent;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public int getUnitsDied() {
		return unitsDied;
	}

	public void setUnitsDied(int unitsDied) {
		this.unitsDied = unitsDied;
	}

	public int getEnemyBuildings() {
		return enemyBuildings;
	}

	public void setEnemyBuildings(int enemyBuildings) {
		this.enemyBuildings = enemyBuildings;
	}

	public int getDamageReceived() {
		return damageReceived;
	}

	public void setDamageReceived(int damageReceived) {
		this.damageReceived = damageReceived;
	}

	public int getEnemyUnits() {
		return enemyUnits;
	}

	public void setEnemyUnits(int enemyUnits) {
		this.enemyUnits = enemyUnits;
	}
	private int lastvisit; //frame
	private int x;
	private int z;
	private AIFloat3 pos;
	public AIFloat3 getPos() {
		return pos;
	}
	private AGAI ai;
	AGSector(AGAI ai, int x, int z, AIFloat3 pos){
		this.ai=ai;
		this.x=x;
		this.z=z;
		this.pos=pos;
	}

	public String toString(){
		return ""+enemyBuildings+" "+enemyUnits +" " +damageReceived;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return z;
	}

	public int getLastvisit() {
		return lastvisit;
	}
	public void setLastvisit(int lastvisit) {
		this.lastvisit = lastvisit;
	}
	public double getDistance(AIFloat3 pos){
		return ai.getDistance(this.pos, pos);
	}
	public int getDanger(){
		return enemyBuildings + damageReceived + enemyUnits + unitsDied;
	}
	public int getEnemy(){
		return enemyBuildings + enemyUnits;
	}
	public void setClean(){
		enemyBuildings=0;
		enemyUnits=0;
		damageReceived=0;
		unitsDied=0;
	}

}


/**
 * The Class AGMap, used to store Information about the map (insecure area, enemy locations...)
 */
public class AGMap {
	
	/** The avg los. */
	private float avgLos; //average line of sight of all units
	
	public float getSectorSize() {
		return avgLos;
	}

	/** The map. */
	private AGSector map [][];
	
	/** The width of map sectors. */
	private int secWidth;
	
	/** The height of map sectors */
	private int secHeight;
	
	/** The ai. */
	private AGAI ai;
	private int mark;
	public static final int unitAttacked = 10;
	public static final int enemyBuilding = 1;

	/**
	 * Instantiates a new aG map.
	 * 
	 * @param ai the ai
	 */
	AGMap(AGAI ai){
		this.ai=ai;
		this.mark=1;
		LinkedList<AGBuildTreeUnit> list=ai.getAGB().getUnitList();
		float tmp=0;
		for (int i=0; i<list.size(); i++){
			tmp=tmp+list.get(i).getUnit().getLosRadius();
		}
		avgLos=Math.round((tmp*8)/list.size());
		secWidth=Math.round((ai.getClb().getMap().getWidth()*8)/avgLos);
		secHeight=Math.round((ai.getClb().getMap().getHeight()*8)/avgLos);
		ai.msg(secWidth + " x " + secHeight + "real map size"+ai.getClb().getMap().getWidth()*8 +"x"+ai.getClb().getMap().getHeight()*8);
		if ((secWidth==0) || (secHeight==0))
			ai.msg("Fatal: Couldn't create sector map!");
		map=new AGSector[secWidth][secHeight];
		for(int i=0;i<secWidth; i++){
			for(int j=0;j<secHeight; j++){
				map[i][j]=new AGSector(ai, i,j, new AIFloat3((avgLos*i), 0, (avgLos*j)));
			}
		}

		for(int i=0;i<secWidth; i++){ //setting evelation
			for(int j=0;j<secHeight; j++){
				AIFloat3 pos=map[i][j].getPos();
				pos.y=ai.getClb().getMap().getElevationAt(pos.x, pos.z);
			}
		}
		ai.msg("real map size"+ai.getClb().getMap().getWidth()*8 +"x"+ai.getClb().getMap().getHeight()*8);
	}
	
	/**
	 * Gets the sector.
	 * 
	 * @param pos the pos
	 * 
	 * @return the sector
	 */
	public AGSector getSector(AIFloat3 pos){
		int x, y;
		x=Math.round(pos.x / avgLos);
		y=Math.round(pos.z / avgLos);
		if ((x>0) && (x<secWidth)
			&& (y>0) && (y<secHeight))
				return map[x][y];
		return null;
	}
	
	/**
	 * Adds the attacker.
	 * 
	 * @param unit the unit
	 */
	public void addDanger(Unit unit){
		AGSector tmp=getSector(unit.getPos());
		UnitDef def=unit.getDef();
		if ((def!=null) && (def.isAbleToMove()))
			tmp.setEnemyUnits(tmp.getEnemyUnits()+1);
		else
			tmp.setEnemyBuildings(tmp.getEnemyBuildings()+1);
	}
	
	public void unitDestroyed(Unit unit){
		AGSector tmp=getSector(unit.getPos());
		tmp.setUnitsDied(tmp.getUnitsDied()+1);
	}
	
	/**
	 * Dump.
	 */
	public void dump(){
		for(int i=0;i<map.length; i++){
			String line="";
			for(int j=0;j<map[i].length; j++){
				line = line + map[i][j].toString();
				if (map[i][j].getDanger()>0)
					ai.drawPoint(map[i][j].getPos(),""+map[i][j].getDanger());
			}
			ai.msg(line);
		}
	}

	/**
	 * Gets the danger.
	 *
	 * @param sec the Sector
	 * @param size the of the block to check danger
	 *
	 * @return the danger
	 */
	public int getDanger(AGSector sec, int size){
		int x=sec.getX();
		int y=sec.getY();
		int posx;
		int posy;
		int danger=sec.getDanger();
		for (int i=0; i<=size*2; i++){
			for (int j=0; j<=size*2; j++){
				posx=x+i-size;
				posy=y+j-size;
				if ((posx>0) && (posx<secWidth)
						&& (posy>0) && (posy<secHeight))
				danger=danger + map[posx][posy].getDanger() /
						(Math.abs(size-i) + Math.abs(size-j)+1); //greater distance, lower weight
			}
		}
		return danger;
	}

	public AGSector getNextEnemyTarget(AIFloat3 pos,int minDanger){
		LinkedList <AGSector> secs=new LinkedList <AGSector>();
		for(int i=0; i<map.length; i++){
			for(int j=0; j<map[i].length; j++){
				if (map[i][j].getEnemy()>minDanger)
					secs.add(map[i][j]);
			}
		}
		float min=Float.MAX_VALUE;
		int p=0;
		for (int i=0; i<secs.size(); i++){
			double diff=secs.get(i).getDistance(pos);
			if (diff<min){
				p=i;
			}
		}
		if (secs.size()>0)
			return secs.get(p);
		return null;
	}

	public void unitDamaged(Unit unit, Unit attacker, float damage) {
		addDanger(attacker);
		AGSector sec=getSector(unit.getPos());
		sec.setDamageReceived(sec.getDamageReceived()+Math.round(damage));
	}

	public boolean isPosInSec(AIFloat3 pos, AGSector destination) {
		return (getSector(pos)==destination);
	}
	public AGSector get(int x, int y){
		if ((x>=0) && (y>=0) &&
			(x<secWidth) && (y<secHeight))
			return map[x][y];
		return null;
	}

	private int extractMin(LinkedList <AGSector> queue){
		int min=Integer.MAX_VALUE;
		int pos=0;
		for (int i=0; i<queue.size();i++){
			int tmp=getDanger(queue.get(i),1);
			if (tmp==0)
				return i;
			if (tmp<min){
				pos=i;
			}
		}
		return pos;
	}

	/**
	 * Gets the secure path, cur.getParent().getParent()... contains the path
	 *
	 * @param from the from
	 * @param to the to
	 *
	 * @return the secure path
	 */
	public LinkedList <AGSector> getSecurePath(AGSector from, AGSector to){
		LinkedList <AGSector> queue=new LinkedList  <AGSector>();
		mark++;
		queue.clear();
		queue.add(from);
		from.setParent(null);
		while(queue.size()>0){
			AGSector cur=queue.remove(extractMin(queue));
			if (cur==to){ //target reached
				ai.msg("found path!");
				LinkedList <AGSector> path=new LinkedList<AGSector>();
				while (cur!=null){
					path.addFirst(cur);
					ai.drawPoint(cur.getPos(), ""+path.size());
					cur=cur.getParent();
				}
				return path;
			}
			cur.setMark(mark);
			for (int i=0; i<3; i++){
				for (int j=0; j<3; j++){
					if (!((i==1) && (j==1))){ //do not add curpos to queue
						AGSector sec=get(cur.getX()+(1-i), cur.getY()+(1-j));
						if ((sec!=null) && (sec.getMark()!=mark)){
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

}
