package agai.loader;

import java.net.URL;
import java.net.URLClassLoader;

import com.springrts.ai.oo.AbstractOOAI;
import com.springrts.ai.oo.OOAI;
import com.springrts.ai.oo.OOAICallback;





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
				System.out.println(e.getMessage());
				return -1;
			}
		else
			subAI.message(player, message);
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
			System.out.println(e.getMessage());
			return -1;
		}
		return 0;
	}

/*	else if (argv[0].equalsIgnoreCase("reload")){
		doReload();
	}*/	
}
