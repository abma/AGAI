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
	private int y;

	AGSector(int x, int y){
		this.x=x;
		this.y=y;
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
		return y;
	}

	public int getLastvisit() {
		return lastvisit;
	}
	public void setLastvisit(int lastvisit) {
		this.lastvisit = lastvisit;
	}
}


/**
 * The Class AGMap, used to store Information about the map (insecure area, enemy locations...)
 */
public class AGMap {
	
	/** The avg los. */
	private float avgLos; //average line of sight of all units
	
	/** The map. */
	private AGSector map [][];
	
	/** The width of map sectors. */
	private int width;
	
	/** The height of map sectors */
	private int height;
	
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
		avgLos=tmp/list.size();
		width=Math.round(ai.getClb().getMap().getWidth()/avgLos);
		height=Math.round(ai.getClb().getMap().getHeight()/avgLos);
		if ((width==0) || (height==0))
			ai.msg("Fatal: Couldn't create sector map!");
		map=new AGSector[width][height];
		for(int i=0;i<width; i++){
			for(int j=0;j<height; j++){
				map[i][j]=new AGSector(i,j);
			}
		}
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
		x=Math.round(pos.x / width);
		y=Math.round(pos.z / height);
		if ((x>0) && (x<width)
			&& (y>0) && (y<height))
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
		for(int i=0;i<width; i++){
			String line="";
			for(int j=0;j<height; j++){
				line = line + map[i][j].toString();
			}
			ai.msg(line);
		}
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
				if ((posx>0) && (posx<width)
						&& (posy>0) && (posy<height))
				danger=danger + map[posx][posy].getDanger() /
						(Math.abs(size-i) + Math.abs(size-j))+1; //greater distance, lower weight
			}
		}
		return danger;
	}
}
