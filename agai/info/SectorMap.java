package agai.info;

import agai.AGAI;

import com.springrts.ai.AIFloat3;

//TODO: Auto-generated Javadoc
public class SectorMap{
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
	private SectorMap parent;
	public SectorMap getParent() {
		return parent;
	}

	public void setParent(SectorMap parent) {
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
	SectorMap(AGAI ai, int x, int z, AIFloat3 pos){
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