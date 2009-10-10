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

import agai.info.IResource;
import agai.loader.IAGAI;
import agai.manager.MAttack;
import agai.manager.MBuild;
import agai.manager.MScout;
import agai.task.TAttack;
import agai.task.TBuild;
import agai.task.TScout;
import agai.task.Task;
import agai.unit.AGUnit;

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
 * @author Matthias Ableitner
 * @version 0.1
 */
public class AGAI extends AbstractOOAI implements IAGAI {

	/** The Constant DEFAULT_ZONE. */
	private static final int DEFAULT_ZONE = 0;

	/** The Constant defaultFacing. */
	public static final int defaultFacing = 0;

	/** The default minimal distance between buildings. */
	public static final int minDistance = 4;

	/** The default distance to search for building poisitions. */
	public static final int searchDistance = 1000;

	/** The clb. */
	private OOAICallback clb = null;

	/** The controller. */
	private AGController controller = null;

	/** The energy. */
	private Resource energy = null;

	/** The frame. */
	private int frame;

	/** The infos. */
	private AGInfos infos = null;

	/** The a gt. */
	private AGManagers managers = null;

	/** The metal. */
	private Resource metal = null;

	private int resourcecount=0;
	
	public int getResourcecount() {
		return resourcecount;
	}

	/** The tasks. */
	private AGTasks tasks = null;

	/** Log properties. */
	/** The team id. */
	private int teamId = -1;

	/** The a gu. */
	private AGUnits units = null;

	/**
	 * Instantiates a new aGAI.
	 */
	public AGAI() {
		System.out.println("Default Constructor");
	}

	/**
	 * Clear.
	 */
	private void clear() {
		List<Point> l = clb.getMap().getPoints(true);
		for (int i = 0; i < l.size(); i++) {
			RemovePointDrawAICommand cmd = new RemovePointDrawAICommand();
			cmd.pos = l.get(i).getPosition();
			this.handleEngineCommand(cmd);
		}
	}

	/**
	 * Command finished.
	 * 
	 * @param unit
	 *            the unit
	 * @param commandId
	 *            the command id
	 * @param commandTopicId
	 *            the command topic id
	 * 
	 * @return the int
	 */
	@Override
	public int commandFinished(Unit unit, int commandId, int commandTopicId) {
		AGUnit u = units.getUnit(unit);
		Task t = u.getTask();
		if ((u != null) && (t != null))
			t.unitCommandFinished(u);
		return 0;
	}

	/**
	 * Draw point.
	 * 
	 * @param pos
	 *            the pos
	 * @param label
	 *            the label
	 * 
	 * @return the int
	 */
	public int drawPoint(AIFloat3 pos, String label) {
		msg("drawPoint " + label);
		if (pos == null)
			return -1;
		AddPointDrawAICommand cmd = new AddPointDrawAICommand();
		cmd.pos = pos;
		cmd.label = label;
		return handleEngineCommand(cmd);
	}

	/**
	 * Enemy damaged.
	 * 
	 * @param enemy
	 *            the enemy
	 * @param attacker
	 *            the attacker
	 * @param damage
	 *            the damage
	 * @param dir
	 *            the dir
	 * @param weaponDef
	 *            the weapon def
	 * @param paralyzer
	 *            the paralyzer
	 * 
	 * @return the int
	 */
	@Override
	public int enemyDamaged(Unit enemy, Unit attacker, float damage,
			AIFloat3 dir, WeaponDef weaponDef, boolean paralyzer) {
		if (enemy.getDef() != null)
			msg(enemy.getDef().getName());
		else
			msg("Unknown");
		AGUnit u = units.getUnit(attacker);
		Task t = u.getTask();
		if ((u != null) && (t != null))
			t.unitEnemyDamaged(u, enemy);
		return 0;
	}

	/**
	 * Enemy destroyed.
	 * 
	 * @param enemy
	 *            the enemy
	 * @param attacker
	 *            the attacker
	 * 
	 * @return the int
	 */
	@Override
	public int enemyDestroyed(Unit enemy, Unit attacker) {
		if (enemy.getDef() != null)
			msg(enemy.getDef().getName());
		else
			msg("Unknown");
		if (attacker != null) {
			AGUnit u = units.getUnit(attacker);
			Task t = u.getTask();
			if ((u != null) && (t != null))
				t.unitEnemyDestroyed();
		}
		return 0;
	}

	/**
	 * Enemy enter los.
	 * 
	 * @param enemy
	 *            the enemy
	 * 
	 * @return the int
	 */
	@Override
	public int enemyEnterLOS(Unit enemy) {
		infos.getSectors().addDanger(enemy);
		return 0;
	}

	/**
	 * Enemy enter radar.
	 * 
	 * @param enemy
	 *            the enemy
	 * 
	 * @return the int
	 */
	@Override
	public int enemyEnterRadar(Unit enemy) { // never call
		// enemy.getDef().getName()!!
		infos.getSectors().addDanger(enemy);
		return 0;
	}

	/**
	 * Enemy leave los.
	 * 
	 * @param enemy
	 *            the enemy
	 * 
	 * @return the int
	 */
	@Override
	public int enemyLeaveLOS(Unit enemy) {
		return 0;
	}

	/**
	 * Enemy leave radar.
	 * 
	 * @param enemy
	 *            the enemy
	 * 
	 * @return the int
	 */
	@Override
	public int enemyLeaveRadar(Unit enemy) { // never call
		// enemy.getDef().getName()!!
		return 0;
	}

	/**
	 * Check for enough resources to build the unit.
	 * 
	 * @param unit
	 *            the unit
	 * @param fs 
	 * 
	 * @return the resource that is mostly missing
	 */
	public Resource enoughResourcesToBuild(UnitDef unit, IResource fs) {
		List<Resource> res = clb.getResources();
		Resource ret = null;
		float min = Float.MAX_VALUE;
		for (int i = 0; i < res.size(); i++) {
			float usage, unitcost, current, income;
			usage = fs.getUseage(i);
			income = fs.getIncome(i);
			unitcost = unit.getCost(res.get(i)); // cost of unit
			current = fs.getCurrent(i);
			// avaiable
			// resources
			float cur = unitcost - (current + (income - usage));
			if ((cur > 0) && (cur < min)) {
				ret = res.get(i);
				min = cur;
			}
		}
		return ret;
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
	 * Gets the controller.
	 * 
	 * @return the controller
	 */
	public AGController getController() {
		return controller;
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
	 * Gets the current frame (game time) of the game.
	 * 
	 * @return the frame
	 */
	public int getFrame() {
		return frame;
	}

	/**
	 * Gets the Infos Manager.
	 * 
	 * @return the infos
	 */
	public AGInfos getInfos() {
		return infos;
	}

	/**
	 * Gets the Manager Managers.
	 * 
	 * @return the managers
	 */
	public AGManagers getManagers() {
		return managers;
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
	 * Gets the Tasks Manager.
	 * 
	 * @return the tasks
	 */
	public AGTasks getTasks() {
		return tasks;
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
	 * Gets the Units Manager.
	 * 
	 * @return the units
	 */
	public AGUnits getUnits() {
		return units;
	}

	/**
	 * Gets the weapon damage.
	 * 
	 * @param unit
	 *            the unit
	 * 
	 * @return the weapon damage
	 */
	public float getWeaponDamage(UnitDef unit) {
		List<WeaponMount> w = unit.getWeaponMounts();
		float ret = 0;
		for (int i = 0; i < w.size(); i++) {
			ret = ret + w.get(i).getWeaponDef().getDamage().getImpulseBoost();
		}
		return ret;
	}

	/**
	 * Handle engine command.
	 * 
	 * @param command
	 *            the command
	 * 
	 * @return the int
	 */
	public int handleEngineCommand(AICommand command) {
		return clb.getEngine().handleCommand(
				com.springrts.ai.AICommandWrapper.COMMAND_TO_ID_ENGINE, -1,
				command);
	}

	/**
	 * Inits the.
	 * 
	 * @param teamId
	 *            the team id
	 * @param callback
	 *            the callback
	 * 
	 * @return the intgwer
	 */
	@Override
	public int init(int teamId, OOAICallback callback) {
		this.clb = callback;
		this.teamId = teamId;
		List<Resource> res = this.getClb().getResources();
		resourcecount = res.size(); 
		metal = res.get(0);
		energy = res.get(1);

		units = new AGUnits(this);
		infos = new AGInfos(this);
		controller = new AGController(this);
		managers = new AGManagers(this);
		tasks = new AGTasks(this);


		List<Unit> list = clb.getTeamUnits();
		for (int i = 0; i < list.size(); i++) {
			units.add(list.get(i));
		}
		msg("");
		return 0;
	}

	/**
	 * Debuging console.
	 * 
	 * @param player
	 *            the player
	 * @param message
	 *            the message
	 * 
	 * @return the int
	 */
	@Override
	public int message(int player, String message) {
		String[] argv = message.split(" ");
		if (argv.length > 0) {
			if (argv[0].equalsIgnoreCase("buildunit")) {
				if (argv.length == 2) {
					UnitDef u = units.getUnitDef(argv[1]);
					if (u != null) {
						tasks.add(new TBuild(this, managers.get(MBuild.class),
								u, null, 100, 2, null));
					}
				}
			} else if (argv[0].equalsIgnoreCase("dump")) {
				tasks.dump();
			} else if (argv[0].equalsIgnoreCase("scout")) {
				tasks.add(new TScout(this, managers.get(MScout.class)));
			} else if (argv[0].equalsIgnoreCase("dumpunits")) {
				units.dump();
			} else if (argv[0].equalsIgnoreCase("dumpunitdefs")) {
				units.dumpUnitDefs();
			} else if (argv[0].equalsIgnoreCase("dumpbuildtree")) {
				infos.getAGB().dumpUnits();
			} else if (argv[0].equalsIgnoreCase("dumpmap")) {
				infos.getSectors().dump();
			} else if (argv[0].equalsIgnoreCase("attack")) {
				tasks.add(new TAttack(this, managers.get(MAttack.class),
						AGUnits.ElementType.unitLand));
			} else if (argv[0].equalsIgnoreCase("clear")) {
				clear();
			} else if (argv[0].equalsIgnoreCase("start")) {
				controller.start();
			} else if (argv[0].equalsIgnoreCase("stop")) {
				controller.stop();
			} else if (argv[0].equalsIgnoreCase("dumpgraph")) {
				infos.getAGB().dumpGraph();
			}
		}
		return 0;
	}

	/**
	 * Print a debug message on console.
	 * 
	 * @param str
	 *            the str
	 */
	public void msg(String str) {
		try {
			throw new Exception();
		} catch (Exception e) {
			str = e.getStackTrace()[1].getClassName() + "."
					+ e.getStackTrace()[1].getMethodName() + ":"
					+ e.getStackTrace()[1].getLineNumber() + " " + str;
		}
		System.out.println(frame+" "+str);
	}

	/**
	 * Player command.
	 * 
	 * @param units
	 *            the units
	 * @param command
	 *            the command
	 * @param playerId
	 *            the player id
	 * 
	 * @return the int
	 */
	@Override
	public int playerCommand(List<Unit> units, AICommand command, int playerId) {
		msg("");
		return 0;
	}

	/**
	 * Seismic ping.
	 * 
	 * @param pos
	 *            the pos
	 * @param strength
	 *            the strength
	 * 
	 * @return the int
	 */
	@Override
	public int seismicPing(AIFloat3 pos, float strength) {
		msg("");
		return 0;
	}

	/**
	 * Send text msg.
	 * 
	 * @param msg
	 *            the msg
	 * 
	 * @return the int
	 */
	public int sendTextMsg(String msg) {
		SendTextMessageAICommand msgCmd = new SendTextMessageAICommand(msg,
				DEFAULT_ZONE);
		return handleEngineCommand(msgCmd);
	}

	/**
	 * Unit captured.
	 * 
	 * @param unit
	 *            the unit
	 * @param oldTeamId
	 *            the old team id
	 * @param newTeamId
	 *            the new team id
	 * 
	 * @return the int
	 */
	@Override
	public int unitCaptured(Unit unit, int oldTeamId, int newTeamId) {
		msg(unit.getDef().getName());
		if (teamId != newTeamId) {
			units.destroyed(unit, null);
			return 0;
		}
		AGUnit u = units.getUnit(unit);
		Task t = u.getTask();
		if ((u != null) && (t != null))
			t.unitCaptured();
		return 0;
	}

	/**
	 * Unit created.
	 * 
	 * @param unit
	 *            the unit
	 * @param builder
	 *            the builder
	 * 
	 * @return the int
	 */
	@Override
	public int unitCreated(Unit unit, Unit builder) {
		msg(unit.getDef().getName());
		// search builder and check if he has a task, when so, add unit to units
		// and set task from builder (if unit gets destroyed, only re-add to
		// tasklist...)
		AGUnit u = units.getUnit(unit);
		if (builder != null) {
			AGUnit b = units.getUnit(builder);
			if (b != null) {
				Task t = b.getTask();
				if (t != null) {
					t.unitCreated(b, u);
				}
			}
		}
		return 0;
	}

	/**
	 * Unit damaged.
	 * 
	 * @param unit
	 *            the unit
	 * @param attacker
	 *            the attacker
	 * @param damage
	 *            the damage
	 * @param dir
	 *            the dir
	 * @param weaponDef
	 *            the weapon def
	 * @param paralyzer
	 *            the paralyzer
	 * 
	 * @return the int
	 */
	@Override
	public int unitDamaged(Unit unit, Unit attacker, float damage,
			AIFloat3 dir, WeaponDef weaponDef, boolean paralyzer) {
		AGUnit u = units.getUnit(unit);
		Task t = u.getTask();
		if ((u != null) && (t != null))
			t.unitDamaged(u, damage, dir, weaponDef, paralyzer);
		else
			msg(unit.getDef().getName());
		infos.getSectors().unitDamaged(unit, attacker, damage);
		return 0;
	}

	/**
	 * Unit destroyed.
	 * 
	 * @param unit
	 *            the unit
	 * @param attacker
	 *            the attacker
	 * 
	 * @return the int
	 */
	@Override
	public int unitDestroyed(Unit unit, Unit attacker) {
		AGUnit u = units.getUnit(unit);
		Task t = u.getTask();
		if ((u != null) && (t != null))
			t.unitDestroyed(u);
		else
			msg(unit.getDef().getName());
		units.destroyed(unit, attacker);
		getInfos().getSectors().unitDestroyed(unit);
		return 0;
	}

	/**
	 * Unit built is complete.
	 * 
	 * @param unit
	 *            the unit
	 * 
	 * @return the int
	 */
	@Override
	public int unitFinished(Unit unit) {
		msg(unit.getDef().getName());
		AGUnit u = units.getUnit(unit);
		AGUnit b = u.getBuilder();
		if (b != null) {
			Task t = b.getTask();
			if (t != null) // call unitFinished for the builder
				t.unitFinished(b, u);
		}
		return 0;
	}

	/**
	 * Unit given.
	 * 
	 * @param unit
	 *            the unit
	 * @param oldTeamId
	 *            the old team id
	 * @param newTeamId
	 *            the new team id
	 * 
	 * @return the int
	 */
	@Override
	public int unitGiven(Unit unit, int oldTeamId, int newTeamId) {
		msg(unit.getDef().getName());
		AGUnit u = units.getUnit(unit);
		Task t = u.getTask();
		if ((u != null) && (t != null))
			t.unitGiven();
		else
			msg(unit.getDef().getName());
		return 0;
	}

	/**
	 * Unit idle.
	 * 
	 * @param unit
	 *            the unit
	 * 
	 * @return the int
	 */
	@Override
	public int unitIdle(Unit unit) {
		AGUnit u = units.getUnit(unit);
		Task t = u.getTask();
		if ((u != null) && (t != null))
			t.unitIdle(u);
		else
			msg("");
		return 0;
	}

	/**
	 * Unit move failed.
	 * 
	 * @param unit
	 *            the unit
	 * 
	 * @return the int
	 */
	@Override
	public int unitMoveFailed(Unit unit) {
		AGUnit u = units.getUnit(unit);
		Task t = u.getTask();
		if ((u != null) && (t != null))
			t.unitMoveFailed(u);
		else
			msg(unit.getDef().getName());
		return 0;
	}

	/**
	 * Update.
	 * 
	 * @param frame
	 *            the frame
	 * 
	 * @return the int
	 */
	@Override
	public int update(int frame) {
		this.frame = frame;
		controller.update(frame);
		return 0;
	}

	/**
	 * Weapon fired.
	 * 
	 * @param unit
	 *            the unit
	 * @param weaponDef
	 *            the weapon def
	 * 
	 * @return the int
	 */
	@Override
	public int weaponFired(Unit unit, WeaponDef weaponDef) {
		msg(unit.getDef().getName());
		AGUnit u = units.getUnit(unit);
		Task t = u.getTask();
		if ((u != null) && (t != null))
			t.unitWeaponFired();
		return 0;
	}
}
