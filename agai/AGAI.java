/*
 * Copyright (C) 2009 Matthias Ableitner (http://abma.de/)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
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


import com.springrts.ai.AICommand;
import com.springrts.ai.AIFloat3;
import com.springrts.ai.command.AddPointDrawAICommand;
import com.springrts.ai.command.SendTextMessageAICommand;
import com.springrts.ai.oo.*;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.List;
import java.util.logging.*;




// TODO: Auto-generated Javadoc
/**
 * Serves as Interface for a Java Skirmish AIs for the Spring engine.
 * 
 * @author	Matze
 * @version	0.1
 */
public class AGAI extends AbstractOOAI implements OOAI {

	/**
	 * The Class MyCustomLogFormatter.
	 */
	private static class MyCustomLogFormatter extends Formatter {

		/** The date format. */
		private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS dd.MM.yyyy");

		/* (non-Javadoc)
		 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
		 */
		public String format(LogRecord record) {

			// Create a StringBuffer to contain the formatted record
			// start with the date.
			StringBuffer sb = new StringBuffer();

			// Get the date from the LogRecord and add it to the buffer
			Date date = new Date(record.getMillis());
			sb.append(dateFormat.format(date));
			sb.append(" ");

			// Get the level name and add it to the buffer
			sb.append(record.getLevel().getName());
			sb.append(": ");

			// Get the formatted message (includes localization
			// and substitution of paramters) and add it to the buffer
			sb.append(formatMessage(record));
			sb.append("\n");

			return sb.toString();
		}
	}

	/**
	 * Log properties.
	 * 
	 * @param log the log
	 * @param level the level
	 * @param props the props
	 */
	private static void logProperties(Logger log, Level level, Properties props) {

		log.log(level, "properties (items: " + props.size() + "):");
		for (String key : props.stringPropertyNames()) {
			log.log(level, key + " = " + props.getProperty(key));
		}
	}

	/** The team id. */
	private int teamId = -1;
	
	/** The info. */
	private Properties info = null;
	
	/** The option values. */
	private Properties optionValues = null;
	
	/** The clb. */
	private OOAICallback clb = null;
	
	/** The my log file. */
	private String myLogFile = null;
	
	/** The log. */
	private Logger log = null;


	/** The a gu. */
	private AGUnits aGU = null;
	
	/** The a gt. */
	private AGTaskManagers aGT = null;
	
	/** The a gp. */
	private AGPoIs aGP = null;
	
	/** The a gb. */
	private AGBuildTree aGB = null;
	
	/** The a gf. */
	private AGFilter aGF = null;
	
	/** The metal. */
	private Resource metal = null;
	
	/** The energy. */
	private Resource energy = null;
	
	/**
	 * Instantiates a new anti george ai.
	 * 
	 * @param teamId the team id
	 * @param callback the callback
	 */
	AGAI(int teamId, OOAICallback callback) {

		this.teamId = teamId;
		this.clb = callback;

		info = new Properties();
		Info inf = clb.getSkirmishAI().getInfo();
		int numInfo = inf.getSize();
		for (int i=0; i < numInfo; i++) {
			String key = inf.getKey(i);
			String value = inf.getValue(i);
			info.setProperty(key, value);
		}
		optionValues = new Properties();
		OptionValues opVals = clb.getSkirmishAI().getOptionValues();
		int numOpVals = opVals.getSize();
		for (int i=0; i < numOpVals; i++) {
			String key = opVals.getKey(i);
			String value = opVals.getValue(i);
			optionValues.setProperty(key, value);
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
		int ret = -1;
		this.clb = callback;
		// initialize the log
		try {
			myLogFile = callback.getDataDirs().allocatePath("log-team-" + teamId + ".txt", true, true, false, false);
			FileHandler fileLogger = new FileHandler(myLogFile, false);
			fileLogger.setFormatter(new MyCustomLogFormatter());
			fileLogger.setLevel(Level.ALL);
			log = Logger.getLogger("nulloojavaai");
			log.addHandler(fileLogger);
			if (AGAIFactory.isDebugging()) {
				log.setLevel(Level.ALL);
			} else {
				log.setLevel(Level.INFO);
			}
		} catch (Exception ex) {
			msg("NullOOJavaAI: Failed initializing the logger!");
			ex.printStackTrace();
			ret = -2;
		}
		this.clb = callback;

		try {
			log.info("initializing team " + teamId);

			log.log(Level.FINE, "info:");
			logProperties(log, Level.FINE, info);

			log.log(Level.FINE, "options:");
			logProperties(log, Level.FINE, optionValues);

			ret = 0;
		} catch (Exception ex) {
			log.log(Level.SEVERE, "Failed initializing", ex);
			ret = -3;
		}
		List <Resource> res=this.getClb().getResources();
		metal=res.get(0);
		energy=res.get(1);

		this.aGU = new AGUnits(this);
		this.aGB = new AGBuildTree(this);

		this.aGP = new AGPoIs(this);
		this.aGF = new AGFilter(this);
		this.aGT = new AGTaskManagers(this);
		return ret;
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
			aGT.DoSomething();
		}
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
						aGT.addTask(new AGTaskBuildUnit(this, u, null, 100));
					}
				}
			}else if (argv[0].equalsIgnoreCase("dump")){
				aGT.dump();
			}else if (argv[0].equalsIgnoreCase("buildenergy")){
				aGT.addTask(new AGTaskBuildResource(this,getEnergy()));
			}else if (argv[0].equalsIgnoreCase("buildmetal")){
				aGT.addTask(new AGTaskBuildResource(this,getMetal()));
			}else if (argv[0].equalsIgnoreCase("scout")){
				aGT.addTask(new AGTaskScout(this));
			}else if (argv[0].equalsIgnoreCase("dumpunits")){
				aGU.dump();
			}else if (argv[0].equalsIgnoreCase("dumpbuildtree")){
				aGB.dumpUnits();
			}
		}
		return 0; 
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
		AGUnit b=aGU.getUnit(builder);
		if (b!=null){
			AGTask task=b.getTask();
			if (task!=null){
				task.setUnitChild(unit);
				AGUnit u=aGU.getUnit(unit);
				if (u==null)
					u=aGU.add(unit);
				u.setTask(task);
			}
		}
		return 0;
	}

	/**
	 * Unit finished.
	 * 
	 * @param unit the unit
	 * 
	 * @return the int
	 */
	@Override
	public int unitFinished(Unit unit) {
		msg(unit.getDef().getName());
		AGUnit u=aGU.getUnit(unit);
		if (u!=null){
			u.setTaskFinished();
		}else aGU.add(unit);
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
		msg(""+unit.getDef());
		AGUnit u=aGU.getUnit(unit);
		if (u!=null){
			u.setTaskFinished();
		}else
			msg("Error: Unit finished who is not in unit list!");
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
		msg(unit.getDef().getName());
		AGUnit u=aGU.getUnit(unit);
		if ((u!=null) && (u.getTask()!=null)){
			u.getTask().setFailed();
			u.setTask(null);
		}
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
		msg(unit.getDef().getName());
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
		msg(unit.getDef().getName());
		aGU.destroyed(unit,attacker);
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
		msg("");
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
		msg("");
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
		msg("");
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
		msg("");
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
		msg("");
		AGUnit u=aGU.getUnit(unit);
		if (u!=null){
			if (u.getTask()!=null)
				u.getTask().setStatusFinished();
		}else{
			msg("Unit wasnt found!");
			aGU.add(unit);
		}
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
	public AGTaskManagers getAGT() {
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
	public AGPoIs getAGP() {
		return aGP;
	}

	/**
	 * Gets the aGB.
	 * 
	 * @return the aGB
	 */
	public AGBuildTree getAGB() {
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
	public double getDifference(AIFloat3 pos1, AIFloat3 pos2){
		return Math.sqrt((Math.pow(pos1.x-pos2.x,2) + Math.pow(pos1.y-pos2.y,2) + Math.pow(pos1.z-pos2.z,2 )));
	}

	/**
	 * Gets the aGF.
	 * 
	 * @return the aGF
	 */
	public AGFilter getAGF() {
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
	 * Print a debug message on console
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
	public float getProduction(UnitDef unit, Resource res){
	 return 
	 	(unit.getUpkeep(res) *-1) + unit.getResourceMake(res) + 
				unit.getWindResourceGenerator(res) +unit.getTidalResourceGenerator(res)
				+ unit.getMakesResource(res);
	}
	
	public float getTotalPrice(UnitDef unit){
		float cost=0;
		List <Resource> res=clb.getResources();
		for (int i=0; i<res.size(); i++ ){
			cost=cost+unit.getCost(res.get(i));
		}
		return cost;
	}

}
