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

	private ISector parent;
	private AIFloat3 pos;
	private int unitsDied;

	private int x;

	private int z;

	ISector(AGAI ai, int x, int z, AIFloat3 pos) {
		this.ai = ai;
		this.x = x;
		this.z = z;
		this.pos = pos;
	}

	public int getDamageReceived() {
		return damageReceived;
	}

	public int getDanger() {
		return enemyBuildings + damageReceived + enemyUnits + unitsDied;
	}

	public double getDistance(AIFloat3 pos) {
		return ai.getInfos().getDistance(this.pos, pos);
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
		return pos;
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