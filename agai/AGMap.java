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
	int attacker; //enemy attackers seen
	int died; //units died
	AGSector(){
		
	}
	public void incAttack(){
		attacker++;
	}
	public String toString(){
		return attacker +" "+died;
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
		map=new AGSector[width][height];
		for(int i=0;i<width; i++){
			for(int j=0;j<height; j++){
				map[i][j]=new AGSector();
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
		tmp.incAttack();
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
}
