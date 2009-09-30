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


import java.util.List;

import agai.info.BuildTree;
import agai.info.BuildTreeUnit;
import agai.info.PoIsMap;
import agai.info.Search;
import agai.info.TreatMap;
import agai.loader.IAGAI;
import agai.manager.TaskManagers;
import agai.manager.GroupManager;
import agai.manager.ResourceManagerTask;
import agai.unit.AGTask;
import agai.unit.AGUnit;
import agai.unit.AGUnits;
import agai.unit.AttackTask;
import agai.unit.BuildTask;
import agai.unit.GroupTask;
import agai.unit.ScoutTask;

import com.springrts.ai.AICommand;
import com.springrts.ai.AIFloat3;
import com.springrts.ai.command.AddPointDrawAICommand;
import com.springrts.ai.command.RemovePointDrawAICommand;
import com.springrts.ai.command.SendTextMessageAICommand;
import com.springrts.ai.oo.AbstractOOAI;
import com.springrts.ai.oo.OOAICallback;
import com.springrts.ai.oo.Point;
import com.springrts.ai.oo.Resource;
import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.UnitDef;
import com.springrts.ai.oo.WeaponDef;
import com.springrts.ai.oo.WeaponMount;



// TODO: Auto-generated Javadoc
/**
 * Serves as Interface for a Java Skirmish AIs for the Spring engine.
 * 
 * @author	Matze
 * @version	0.1
 */
public class AGAI extends AbstractOOAI implements IAGAI {

	/**
	 * Instantiates a new aGAI.
	 */
	public AGAI() {
		System.out.println("Default Constructor");
	}

	/** Log properties. */
	/** The team id. */
	private int teamId = -1;
	
	/** The clb. */
	private OOAICallback clb = null;
	
	/** The a gu. */
	private AGUnits aGU = null;
	
	/** The a gt. */
	private TaskManagers aGT = null;
	
	/** The a gp. */
	private PoIsMap aGP = null;
	
	/** The a gb. */
	private BuildTree aGB = null;
	
	/** The a gf. */
	private Search aGF = null;
	
	/** The a gm. */
	private TreatMap aGM = null;

	/** The metal. */
	private Resource metal = null;
	
	/** The energy. */
	private Resource energy = null;

	/** The a gg. */
	private GroupManager aGG;

	public GroupManager getAGG() {
		return aGG;
	}

	/** The frame. */
	private int frame;
	
	/**
	 * Gets the current frame (game time) of the game
	 *
	 * @return the frame
	 */
	public int getFrame() {
		return frame;
	}

	/** The default minimal distance between buildings. */
	public static final int minDistance=4;

	/** The default distance to search for building poisitions. */
	public static final int searchDistance=100;

	/** The Constant defaultFacing. */
	public static final int defaultFacing=0;

	/**
	 * The Enum ElementType.
	 */
	public static enum ElementType{

		/** The any unit. */
		unitAny(0),

		/** The unit can fly. */
		unitFly(1),

		/** The unit can swim. */
		unitSwim(2),

		/** The unit is sub. */
		unitSub(4),

		/** The unit is at land. */
		unitLand(8),

		/** The unit is amphibian. */
		unitAmphibian(16);

		/** The type. */
		private int type;

		/**
		 * Gets the type.
		 *
		 * @return the type
		 */
		public int getType() {
			return type;
		}

		/**
		 * Instantiates a new element type.
		 *
		 * @param type the type
		 */
		ElementType(int type){
			this.type=type;
		}
	}

	/**
	 * Inits the.
	 * 
	 * @param teamId the team id
	 * @param callback the callback
	 * 
	 * @return the int
	 */
	@Override
	public int init(int teamId, OOAICallback callback) {
		this.clb = callback;
		this.teamId=teamId;
		List <Resource> res=this.getClb().getResources();
		metal=res.get(0);
		energy=res.get(1);

		this.aGU = new AGUnits(this);
		this.aGB = new BuildTree(this);

		this.aGP = new PoIsMap(this);
		this.aGF = new Search(this);
		this.aGT = new TaskManagers(this);
		this.aGM = new TreatMap(this);
		this.aGG = new GroupManager(this);
//		this.aGC = new AGController(this);

		List <Unit> list=clb.getTeamUnits();
		for(int i=0; i<list.size(); i++){
			aGU.add(list.get(i));
		}
		msg("");
		return 0;
	}

	/**
	 * Gets the aGM.
	 *
	 * @return the aGM
	 */
	public TreatMap getAGM() {
		return aGM;
	}

	/**
	 * Update.
	 * 
	 * @param frame the frame
	 * 
	 * @return the int
	 */
	@Override
	public int update(int frame) {
		if (frame%30==0){
			aGT.DoSomething(frame);
		}
		this.frame=frame;
		return 0; 
	}

	/**
	 * Debuging console.
	 * 
	 * @param player the player
	 * @param message the message
	 * 
	 * @return the int
	 */
	@Override
	public int message(int player, String message) {
		String [] argv=message.split(" ");
		if (argv.length>0){
			if (argv[0].equalsIgnoreCase("buildunit")){
				if (argv.length==2){
					UnitDef u=aGU.getUnitDef(argv[1]);
					if (u!=null){
						aGT.addTask(new BuildTask(this, u, null, 100, 2, null));
					}
				}
			}else if (argv[0].equalsIgnoreCase("dump")){
				aGT.dump();
			}else if (argv[0].equalsIgnoreCase("scout")){
				aGT.addTask(new ScoutTask(this));
			}else if (argv[0].equalsIgnoreCase("dumpunits")){
				aGU.dump();
			}else if (argv[0].equalsIgnoreCase("dumpunitdefs")){
				aGU.dumpUnitDefs();
			}else if (argv[0].equalsIgnoreCase("dumpbuildtree")){
				aGB.dumpUnits();
			}else if (argv[0].equalsIgnoreCase("dumpmap")){
				aGM.dump();
			}else if (argv[0].equalsIgnoreCase("attack")){
				aGT.addTask(new AttackTask(this, AGAI.ElementType.unitLand));
			}else if (argv[0].equalsIgnoreCase("clear")){
				clear();
			}else if (argv[0].equalsIgnoreCase("group")){
				group();
			}else if (argv[0].equalsIgnoreCase("start")){
				start();
			}else if (argv[0].equalsIgnoreCase("stop")){
				stop();
			}else if (argv[0].equalsIgnoreCase("dumpgraph")){
				aGB.dumpGraph();
			}
		}
		return 0; 
	}

	private void stop() {
		aGT.clear();
	}

	private void start() {
		aGT.addTask(new ResourceManagerTask(this));
		aGT.addTask(new ScoutTask(this));
		aGT.addTask(new AttackTask(this, ElementType.unitLand));
	}

	/**
	 * Group.
	 */
	private void group() {
		GroupTask group=new GroupTask(this, new AttackTask(this, ElementType.unitLand), 10);
		for (int i=0; i<10; i++){
			AGTask tmp= new BuildTask(this, aGU.getUnitDef("armflea"), null, 0, 0, group);
			aGT.addTask(tmp);
		}
		aGG.addGroup(group);
	}

	/**
	 * Clear.
	 */
	private void clear() {
		List <Point>l=clb.getMap().getPoints(true);
		for(int i=0; i<l.size(); i++){
			RemovePointDrawAICommand cmd=new RemovePointDrawAICommand();
			cmd.pos=l.get(i).getPosition();
			this.handleEngineCommand(cmd);
		}
	}

	/**
	 * Unit created.
	 * 
	 * @param unit the unit
	 * @param builder the builder
	 * 
	 * @return the int
	 */
	@Override
	public int unitCreated(Unit unit, Unit builder) {
		msg (unit.getDef().getName());
		//search builder and check if he has a task, when so, add unit to units and set task from builder (if unit gets destroyed, only re-add to tasklist...)
		AGUnit u=aGU.getUnit(unit);
		if (builder!=null){
			AGUnit b=aGU.getUnit(builder);
			if (b!=null){
				AGTask t=b.getTask();
				if (t!=null){
					t.unitCreated(b, u);
				}
			}
		}
		return 0;
	}

	/**
	 * Unit built is complete.
	 * 
	 * @param unit the unit
	 * 
	 * @return the int
	 */
	@Override
	public int unitFinished(Unit unit) {
		msg(unit.getDef().getName());
		AGUnit u=aGU.getUnit(unit);
		AGUnit b=u.getBuilder();
		if (b!=null){
			AGTask t=b.getTask();
			if (t!=null) //call unitFinished for the builder
				t.unitFinished(b, u);
		}
		return 0; 
	}

	/**
	 * Unit idle.
	 * 
	 * @param unit the unit
	 * 
	 * @return the int
	 */
	@Override
	public int unitIdle(Unit unit) {
		AGUnit u=aGU.getUnit(unit);
		AGTask t=u.getTask();
		if ((u!=null) && (t!=null))
			t.unitIdle(u);
		else
			msg("");
		return 0; 
	}

	/**
	 * Unit move failed.
	 * 
	 * @param unit the unit
	 * 
	 * @return the int
	 */
	@Override
	public int unitMoveFailed(Unit unit) {
		AGUnit u=aGU.getUnit(unit);
		AGTask t=u.getTask();
		if ((u!=null) && (t!=null))
			t.unitMoveFailed(u);
		else
			msg(unit.getDef().getName());
		return 0; 
	}

	/**
	 * Unit damaged.
	 * 
	 * @param unit the unit
	 * @param attacker the attacker
	 * @param damage the damage
	 * @param dir the dir
	 * @param weaponDef the weapon def
	 * @param paralyzer the paralyzer
	 * 
	 * @return the int
	 */
	@Override
	public int unitDamaged(Unit unit, Unit attacker, float damage, AIFloat3 dir, WeaponDef weaponDef, boolean paralyzer) {
		AGUnit u=aGU.getUnit(unit);
		AGTask t=u.getTask();
		if ((u!=null) && (t!=null))
			t.unitDamaged(u, damage, dir, weaponDef, paralyzer);
		else
			msg(unit.getDef().getName());
		aGM.unitDamaged(unit,attacker,damage);
		return 0; 
	}

	/**
	 * Unit destroyed.
	 * 
	 * @param unit the unit
	 * @param attacker the attacker
	 * 
	 * @return the int
	 */
	@Override
	public int unitDestroyed(Unit unit, Unit attacker) {
		AGUnit u=aGU.getUnit(unit);
		AGTask t=u.getTask();
		if ((u!=null) && (t!=null))
			t.unitDestroyed(u);
		else
			msg(unit.getDef().getName());
		aGU.destroyed(unit,attacker);
		aGM.unitDestroyed(unit);
		return 0; 
	}

	/**
	 * Unit given.
	 * 
	 * @param unit the unit
	 * @param oldTeamId the old team id
	 * @param newTeamId the new team id
	 * 
	 * @return the int
	 */
	@Override
	public int unitGiven(Unit unit, int oldTeamId, int newTeamId) {
		msg(unit.getDef().getName());
		AGUnit u=aGU.getUnit(unit);
		AGTask t=u.getTask();
		if ((u!=null) && (t!=null))
			t.unitGiven();
		else
			msg(unit.getDef().getName());
		return 0; 
	}

	/**
	 * Unit captured.
	 * 
	 * @param unit the unit
	 * @param oldTeamId the old team id
	 * @param newTeamId the new team id
	 * 
	 * @return the int
	 */
	@Override
	public int unitCaptured(Unit unit, int oldTeamId, int newTeamId) {
		msg(unit.getDef().getName());
		if (teamId!=newTeamId){
			aGU.destroyed(unit, null);
			return 0;
		}
		AGUnit u=aGU.getUnit(unit);
		AGTask t=u.getTask();
		if ((u!=null) && (t!=null))
			t.unitCaptured();
		return 0; 
	}

	/**
	 * Enemy enter los.
	 * 
	 * @param enemy the enemy
	 * 
	 * @return the int
	 */
	@Override
	public int enemyEnterLOS(Unit enemy) {
		aGM.addDanger(enemy);
		return 0; 
	}

	/**
	 * Enemy leave los.
	 * 
	 * @param enemy the enemy
	 * 
	 * @return the int
	 */
	@Override
	public int enemyLeaveLOS(Unit enemy) {
		return 0; 
	}

	/**
	 * Enemy enter radar.
	 * 
	 * @param enemy the enemy
	 * 
	 * @return the int
	 */
	@Override
	public int enemyEnterRadar(Unit enemy) { //never call enemy.getDef().getName()!!
		aGM.addDanger(enemy);
		return 0; 
	}

	/**
	 * Enemy leave radar.
	 * 
	 * @param enemy the enemy
	 * 
	 * @return the int
	 */
	@Override
	public int enemyLeaveRadar(Unit enemy) { //never call enemy.getDef().getName()!!
		return 0; 
	}

	/**
	 * Enemy damaged.
	 * 
	 * @param enemy the enemy
	 * @param attacker the attacker
	 * @param damage the damage
	 * @param dir the dir
	 * @param weaponDef the weapon def
	 * @param paralyzer the paralyzer
	 * 
	 * @return the int
	 */
	@Override
	public int enemyDamaged(Unit enemy, Unit attacker, float damage, AIFloat3 dir, WeaponDef weaponDef, boolean paralyzer) {
		if (enemy.getDef()!=null)
			msg(enemy.getDef().getName());
		else
			msg("Unknown");
		AGUnit u=aGU.getUnit(attacker);
		AGTask t=u.getTask();
		if ((u!=null) && (t!=null))
			t.unitEnemyDamaged(u, enemy);
		return 0; 
	}

	/**
	 * Enemy destroyed.
	 * 
	 * @param enemy the enemy
	 * @param attacker the attacker
	 * 
	 * @return the int
	 */
	@Override
	public int enemyDestroyed(Unit enemy, Unit attacker) {
		if (enemy.getDef()!=null)
			msg(enemy.getDef().getName());
		else
			msg("Unknown");
		if (attacker!=null){
			AGUnit u=aGU.getUnit(attacker);
			AGTask t=u.getTask();
			if ((u!=null) && (t!=null))
				t.unitEnemyDestroyed();
		}
		return 0; 
	}

	/**
	 * Weapon fired.
	 * 
	 * @param unit the unit
	 * @param weaponDef the weapon def
	 * 
	 * @return the int
	 */
	@Override
	public int weaponFired(Unit unit, WeaponDef weaponDef) {
		msg(unit.getDef().getName());
		AGUnit u=aGU.getUnit(unit);
		AGTask t=u.getTask();
		if ((u!=null) && (t!=null))
			t.unitWeaponFired();
		return 0; 
	}

	/**
	 * Player command.
	 * 
	 * @param units the units
	 * @param command the command
	 * @param playerId the player id
	 * 
	 * @return the int
	 */
	@Override
	public int playerCommand(List<Unit> units, AICommand command, int playerId) {
		msg("");
		return 0; 
	}

	/**
	 * Command finished.
	 * 
	 * @param unit the unit
	 * @param commandId the command id
	 * @param commandTopicId the command topic id
	 * 
	 * @return the int
	 */
	@Override
	public int commandFinished(Unit unit, int commandId, int commandTopicId) {
		AGUnit u=aGU.getUnit(unit);
		AGTask t=u.getTask();
		if ((u!=null) && (t!=null))
			t.unitCommandFinished(u);
		return 0; 
	}

	/**
	 * Seismic ping.
	 * 
	 * @param pos the pos
	 * @param strength the strength
	 * 
	 * @return the int
	 */
	@Override
	public int seismicPing(AIFloat3 pos, float strength) {
		msg("");
		return 0; 
	}

	/**
	 * Gets the callback.
	 * 
	 * @return the clb
	 */
	public OOAICallback getClb() {
		return clb;
	}

	/**
	 * Gets the aGD.
	 * 
	 * @return the aGD
	 */
	public TaskManagers getAGT() {
		return aGT;
	}

	/**
	 * Gets the aGU.
	 * 
	 * @return the aGU
	 */
	public AGUnits getAGU() {
		return aGU;
	}

	/**
	 * Gets the team id.
	 * 
	 * @return the team id
	 */
	public int getTeamId() {
		return teamId;
	}
	
	/**
	 * Handle engine command.
	 * 
	 * @param command the command
	 * 
	 * @return the int
	 */
	public int handleEngineCommand(AICommand command){
		return clb.getEngine().handleCommand(com.springrts.ai.AICommandWrapper.COMMAND_TO_ID_ENGINE,-1, command);
	}

	/**
	 * Gets the aGP.
	 * 
	 * @return the aGP
	 */
	public PoIsMap getAGP() {
		return aGP;
	}

	/**
	 * Gets the aGB.
	 * 
	 * @return the aGB
	 */
	public BuildTree getAGB() {
		return aGB;
	}

	/** The Constant DEFAULT_ZONE. */
	private static final int DEFAULT_ZONE = 0;
	
	/**
	 * Send text msg.
	 * 
	 * @param msg the msg
	 * 
	 * @return the int
	 */
	public int sendTextMsg(String msg){
		SendTextMessageAICommand msgCmd = new SendTextMessageAICommand(msg, DEFAULT_ZONE);
		return handleEngineCommand(msgCmd);
	}
	
	/**
	 * Draw point.
	 * 
	 * @param pos the pos
	 * @param label the label
	 * 
	 * @return the int
	 */
	public int drawPoint(AIFloat3 pos, String label){
		msg("drawPoint "+label);
		if (pos==null)
			return -1; 
		AddPointDrawAICommand cmd = new AddPointDrawAICommand();
		cmd.pos=pos;
		cmd.label=label;
		return handleEngineCommand(cmd);
	}
	
	/**
	 * Gets the difference.
	 * 
	 * @param pos1 the pos1
	 * @param pos2 the pos2
	 * 
	 * @return the difference
	 */
	public double getDistance(AIFloat3 pos1, AIFloat3 pos2){
		return Math.sqrt((Math.pow(pos1.x-pos2.x,2) + Math.pow(pos1.z-pos2.z,2 )));
	}

	/**
	 * Gets the aGF.
	 * 
	 * @return the aGF
	 */
	public Search getAGF() {
		return aGF;
	}

	/**
	 * Gets the energy.
	 * 
	 * @return the energy
	 */
	public Resource getEnergy() {
		return energy;
	}

	/**
	 * Gets the metal.
	 * 
	 * @return the metal
	 */
	public Resource getMetal() {
		return metal;
	}
	
	/**
	 * Check for enough resources to build the unit.
	 * 
	 * @param unit the unit
	 * 
	 * @return the resource that is mostly missing
	 */
	public Resource enoughResourcesToBuild(UnitDef unit){
		List <Resource> res=clb.getResources();
		Resource ret=null;
		float min=Float.MAX_VALUE;
		for (int i=0; i<res.size(); i++ ){
			float usage, unitcost, current, income;
			usage=clb.getEconomy().getUsage(res.get(i)); //usage of energy
			income=clb.getEconomy().getIncome(res.get(i)); //incoming
			unitcost=unit.getCost(res.get(i)); //cost of unit
			current=clb.getEconomy().getCurrent(res.get(i)); //current avaiable resources
			float cur=unitcost-(current + (income - usage));
			if ( (cur>0) &&  (cur<min)){
				ret=res.get(i);
				min=cur;
			}
		}
		return ret;
	}
	
	/**
	 * Print a debug message on console.
	 * 
	 * @param str the str
	 */
	public void msg(String str){
		try{
			throw new Exception();
		}
		catch (Exception e){
			str=e.getStackTrace()[1].getClassName()+"."+e.getStackTrace()[1].getMethodName()+":"+e.getStackTrace()[1].getLineNumber()+" "+str;
		}
		System.out.println(str);
	}

	/**
	 * Gets the element type.
	 *
	 * @param unit the unit
	 *
	 * @return the element type
	 */
	private ElementType getElementType(UnitDef unit){
		if (unit.isAbleToFly())
			return ElementType.unitFly;
		if (unit.isAbleToHover())
			return ElementType.unitAmphibian;
		if (unit.isLevelGround())
			return ElementType.unitLand;
		if (unit.getMinWaterDepth()>0)
			if (unit.getWaterline()>0)
				return ElementType.unitSub;
			else
				return ElementType.unitSwim;
		msg("Unknown Unittype: "+unit.getName());
		return ElementType.unitAny;
	}

	/**
	 * Unit in type.
	 *
	 * @param unit the unit
	 * @param type the type
	 *
	 * @return true, if successful
	 */
	public boolean UnitInType(UnitDef unit, ElementType type){
		if (type==ElementType.unitAny)
			return true;
		ElementType type1=getElementType(unit);
		return type==type1;
	}

	/**
	 * Builds a best fitting unit and assigns task.
	 *
	 * @param task the task
	 * @param list the list
	 * @param tasktoassign the tasktoassign
	 * @param type the type (Air, Water, ...)
	 */
	public void buildUnit(AGTask task, List <BuildTreeUnit> list, AGTask tasktoassign, ElementType type){
		UnitDef unit=null;
		task.setRepeat(AGTask.defaultRepeatTime);
		for(int i=0; i<list.size(); i++){ //searching for existing unit
			if (UnitInType(list.get(i).getUnit(), type)){
				unit=list.get(i).getUnit();
				AGUnit u=getAGU().getIdle(unit);
				if (u!=null){ //unit to scout exists, assign task!
					msg("assigned task to existing idle unit");
					u.setTask(tasktoassign);
					task.setRepeat(0);
					return;
				}
			}
		}
		for(int i=0; i<list.size(); i++){ //no unit found, try to build with exisiting factories
			if (UnitInType(list.get(i).getUnit(), type)){
				AGUnit builder=getAGU().getBuilder(unit);
				if (builder!=null){
					AGTask buildtask=new BuildTask(this, unit, null, AGAI.searchDistance, AGAI.minDistance, tasktoassign);
					getAGT().addTask(buildtask);
					task.setRepeat(0);
					return;
				}
			}
		}
		//no unit found, build cheapest one
		if (unit!=null){
			AGTask buildtask=new BuildTask(this, unit, null, AGAI.searchDistance, AGAI.minDistance, tasktoassign);
			getAGT().addTask(buildtask);
			task.setRepeat(0);
		}else{
			msg("No unit found to build "+type);
		}
	}

	/**
	 * Gets the weapon damage.
	 *
	 * @param unit the unit
	 *
	 * @return the weapon damage
	 */
	public float getWeaponDamage(UnitDef unit){
		List <WeaponMount> w=unit.getWeaponMounts();
		float ret=0;
		for (int i=0; i<w.size(); i++){
			ret=ret+w.get(i).getWeaponDef().getDamage().getImpulseBoost();
		}
		return ret;
	}
}
