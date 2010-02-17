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

import java.util.ArrayList;

import com.springrts.ai.oo.OptionValues;

public class AGLogger {
	private int lastframe=-1;
	private String lastclass="";
	private AGAI ai;

	public static final int error = 0;
	public static final int warning = 1;
	public static final int normal = 2;
	public static final int info = 3;
	public static final int debug = 4;
	
	private int debuglevel=debug;
	private ArrayList<String> debuginfo;
	private boolean filter;
	
	AGLogger(AGAI ai){
		this.ai=ai;
		this.filter=false;
		loadSettings();
	}
	
	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
		this.debuglevel=debug;
	}

	/**
	 * Print a debug message on console, print only messages where
	 * level is < debuglevel and the calling class is in debuginfo
	 *
	 * @param str the str
	 * @param level the level
	 */
	private void msg(String str, int level) {
		if (level>debuglevel)
			return;
		try {
			throw new Exception();
		} catch (Exception e) {
			String cur=e.getStackTrace()[3].getFileName();
			cur=cur.substring(0, cur.length()-5); //remove .java
			boolean found=false;
			if (filter){
				for (int i=0; i<debuginfo.size(); i++){
					if (debuginfo.get(i).equals(cur)){
						found=true;
					}
				}
				if (!found)
					return;
			}
			if (lastclass.contains(cur)){
				cur="";
				for (int i=0; i<lastclass.length(); i++)
					cur=cur+" ";
			}else
				lastclass=cur;
			str =  cur + "."
					+ e.getStackTrace()[3].getMethodName() + ":"
					+ e.getStackTrace()[3].getLineNumber() + " " + str;
		}
		String fstr="";
		if (lastframe==ai.getFrame()){
			int tmp=ai.getFrame();
			while(tmp>0){
				fstr=fstr+" ";
				tmp=tmp/10;
			}
		}else{
			lastframe=ai.getFrame();
			fstr=""+ai.getFrame();
		}
			
		System.out.println(fstr+" "+str);
	}

	public void addLogger(String classname){
		for (int i=0; i<debuginfo.size(); i++){
			if (classname.compareTo(debuginfo.get(i))==0){
				System.out.println("addLogger: already there "+classname);
				return;
			}
		}
		debuginfo.add(classname);
	}

	public void delLogger(String classname){
		for (int i=0; i<debuginfo.size(); i++){
			if (classname.compareTo(debuginfo.get(i))==0){
				debuginfo.remove(i);
				return;
			}
		}
		System.out.println("delLogger: String not found "+classname);
	}
	
	private void loadSettings(){
		OptionValues opt = ai.getClb().getSkirmishAI().getOptionValues();
		System.out.println("Dumping options "+opt.getSize());
		for (int i=0; i<opt.getSize(); i++){
			System.out.println(opt.getKey(i) +"="+opt.getValue(i));
		}
		String debug=null;
		debug = opt.getValueByKey("debuginfos");
		if (debug==null){
			debug="IResource:MExpensiveBuild:MAttack:Manager";
			System.out.println("using default values: "+debug);
		}
		String[] list = debug.split(":");
		debuginfo=new ArrayList<String>();
		for (int i=0; i<list.length; i++){
			addLogger(list[i]);
		}
	}
	public void error(String msg){
		msg(msg, error);
	}
	public void warning(String msg){
		msg(msg, warning);
	}
	public void normal(String msg){
		msg(msg, normal);
	}
	public void info(String msg){
		msg(msg, info);
	}
	public void debug(String msg){
		msg(msg, debug);
	}

	public void clear() {
		debuginfo.clear();
	}
	
}
