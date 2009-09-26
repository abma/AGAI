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

// TODO: Auto-generated Javadoc
class AGSector{
	private int danger; //enemy attackers seen
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

	public void incDanger(int danger){
		this.danger=this.danger+danger;
	}
	public String toString(){
		return ""+danger;
	}
	public int getDanger() {
		return danger;
	}

	public void setAttacker(int danger) {
		this.danger = danger;
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
	
	public static final int unitAttacked = 10;
	public static final int enemyBuilding = 1;

	/**
	 * Instantiates a new aG map.
	 * 
	 * @param ai the ai
	 */
	AGMap(AGAI ai){
		this.ai=ai;
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
	 * @param pos the pos
	 */
	public void addAttacker(AIFloat3 pos){
		AGSector tmp=getSector(pos);
		tmp.incDanger(unitAttacked);
	}
	
	/**
	 * Dump.
	 */
	public void dump(){
		for(int i=0;i<map.length; i++){
			String line="";
			for(int j=0;j<map[i].length; j++){
				line = line + map[i][j].toString();
			}
			ai.msg(line);
		}
		for (int i=1; i<10; i++)
			ai.drawPoint(map[map.length-i][map[0].length-i].getPos(),""+ i);
	}

	/**
	 * Gets the danger.
	 *
	 * @param pos the pos
	 * @param size the of the block to check danger
	 *
	 * @return the danger
	 */
	public int getDanger(AIFloat3 pos, int size){
		AGSector tmp=getSector(pos);
		int x=tmp.getX();
		int y=tmp.getY();
		int posx;
		int posy;
		int danger=tmp.getDanger();
		for (int i=0; i<=size*2; i++){
			for (int j=0; j<=size*2; j++){
				posx=x+i-size;
				posy=y+j-size;
				if ((posx>0) && (posx<secWidth)
						&& (posy>0) && (posy<secHeight))
				danger=danger + map[posx][posy].getDanger() /
						(Math.abs(size-i) + Math.abs(size-j))+1; //greater distance, lower weight
			}
		}
		return danger;
	}

	public void setDanger(AIFloat3 pos, int danger){
		AGSector tmp=getSector(pos);
		tmp.setAttacker(danger);
	}

	public AGSector getNextEnemyTarget(AIFloat3 pos,int minDanger){
		LinkedList <AGSector> secs=new LinkedList <AGSector>();
		for(int i=0; i<map.length; i++){
			for(int j=0; j<map[i].length; j++){
				if (map[i][j].getDanger()>minDanger)
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

}
