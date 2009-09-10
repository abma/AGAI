package agai.loader;

import java.net.URL;
import java.net.URLClassLoader;

import com.springrts.ai.AIFloat3;
import com.springrts.ai.oo.AbstractOOAI;
import com.springrts.ai.oo.OOAI;
import com.springrts.ai.oo.OOAICallback;
import com.springrts.ai.oo.Unit;
import com.springrts.ai.oo.WeaponDef;


public class AGLoader extends AbstractOOAI implements OOAI{

	private IAGAI subAI;
	private OOAICallback clb;
	private int teamId;
	private URLClassLoader getURLClassLoader(URL jarURL) {
		ClassLoader baseClassLoader=AGLoader.class.getClassLoader();
		if (baseClassLoader==null)
			baseClassLoader = ClassLoader.getSystemClassLoader();
		return new URLClassLoader(new URL[]{jarURL}, baseClassLoader);
	}
	
	public IAGAI reloadTheClass() throws Exception {
		subAI=null;
		URLClassLoader urlLoader = getURLClassLoader(new URL("file", null, "/home/matze/Projects/agai/SkirmishAIReal.jar"));
		Class <?>cl=urlLoader.loadClass("agai.AGAI");
		if (!IAGAI.class.isAssignableFrom(cl)) {
			throw new RuntimeException("Invalid class");
		}
		Object newInstance=cl.newInstance();
		return (IAGAI)newInstance;
	}
	
	public void doReload(int teamId, OOAICallback clb) throws Exception {
		subAI=null;
		System.runFinalization();
		System.gc();
		System.gc();
		this.subAI=reloadTheClass();
		subAI.init(teamId, clb);
	}
	
	@Override
	public int message(int player, String message) {
		if (message.equalsIgnoreCase("reload"))
			try{
				doReload(teamId, clb);
			} catch (Exception e) {
				e.printStackTrace();
			}
		else{
			try{
				subAI.message(player, message);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	@Override
	public int init(int teamId, OOAICallback callback) {
		this.clb=callback;
		this.teamId=teamId;
		try{
			if (subAI==null)
				doReload(teamId, callback);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	
	@Override
	public int unitCreated(Unit unit, Unit builder) {
		try {
			subAI.unitCreated(unit, builder);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	} 

	@Override
	public int unitFinished(Unit unit) {
		try {
			subAI.unitFinished(unit);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int unitIdle(Unit unit) {
		try {
			subAI.unitIdle(unit);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int unitMoveFailed(Unit unit) {
		try {
			subAI.unitMoveFailed(unit);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int unitDamaged(Unit unit, Unit attacker, float damage, AIFloat3 dir, WeaponDef weaponDef, boolean paralyzer) {
		try {
			subAI.unitDamaged(unit, attacker, damage, dir, weaponDef, paralyzer);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int unitDestroyed(Unit unit, Unit attacker) {
		try {
			subAI.unitDestroyed(unit, attacker);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int enemyEnterLOS(Unit enemy) {
		try {
			subAI.enemyEnterLOS(enemy);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int enemyLeaveLOS(Unit enemy) {
		try {
			subAI.enemyLeaveLOS(enemy);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int enemyEnterRadar(Unit enemy) { //never call enemy.getDef().getName()!!
		try {
			subAI.enemyEnterRadar(enemy);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int enemyLeaveRadar(Unit enemy) { //never call enemy.getDef().getName()!!
		try {
			subAI.enemyLeaveRadar(enemy);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int enemyDamaged(Unit enemy, Unit attacker, float damage, AIFloat3 dir, WeaponDef weaponDef, boolean paralyzer) {
		try {
			subAI.enemyDamaged(enemy, attacker, damage, dir, weaponDef, paralyzer);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int enemyDestroyed(Unit enemy, Unit attacker) {
		try {
			subAI.enemyDestroyed(enemy, attacker);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int commandFinished(Unit unit, int commandId, int commandTopicId) {
		try {
			subAI.commandFinished(unit, commandId, commandTopicId);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int update(int frame) {
		try {
			subAI.update(frame);
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
}
