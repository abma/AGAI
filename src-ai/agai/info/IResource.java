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
package agai.info;

import java.util.List;

import agai.AGAI;
import agai.Str;

import com.springrts.ai.oo.Resource;

// TODO: Auto-generated Javadoc
/**
 * The Class IResource.
 */
public class IResource{
	
	/** The res. */
	private float [][] res;
	
	/** The resources. */
	private List <Resource> resources;

	/** The ai. */
	private AGAI ai;

	/** The Constant current. */
	final static int current=0;
	
	/** The Constant useage. */
	final static int useage=1;
	
	/** The Constant income. */
	final static int income=2;
	
	/** The Constant storage. */
	final static int storage=3;
	
	/** The Constant count. */
	final static int count=4;
	
	/**
	 * Instantiates a new i resource.
	 * 
	 * @param ai the ai
	 */
	public IResource(AGAI ai){
		resources=ai.getClb().getResources();
		res=new float[count][resources.size()];
		for (int i=0; i<res.length; i++){
			for (int j=0; j<res[i].length; j++){
				res[i][j]=0;
			}
		}
		this.ai=ai;
	}
	
	/**
	 * Gets the current income.
	 * 
	 * @param ResID the res id
	 * 
	 * @return the income
	 */
	public float getIncome(int ResID){
		return res[income][ResID];
	}
	
	/**
	 * Gets the useage.
	 * 
	 * @param ResID the res id
	 * 
	 * @return the useage
	 */
	public float getUseage(int ResID){
		return res[useage][ResID];
	}
	
	/**
	 * Gets the current value in storage
	 * 
	 * @param ResID the res id
	 * 
	 * @return the current
	 */
	public float getCurrent(int ResID){
		return res[current][ResID];
	}
	
	/**
	 * Gets the size of storage
	 * 
	 * @param ResID the res id
	 * 
	 * @return the storage
	 */
	public float getStorage(int ResID){
		return res[storage][ResID];
	}

	/**
	 * Sets the storage.
	 * 
	 * @param ResID the res id
	 * @param value the value
	 */
	public void setStorage(int ResID, float value){
		res[storage][ResID]=value;
	}

	/**
	 * Sets the current.
	 * 
	 * @param ResID the res id
	 * @param value the value
	 */
	public void setCurrent(int ResID, float value){
		res[current][ResID]=value;
	}

	/**
	 * Sets the useage.
	 * 
	 * @param ResID the res id
	 * @param value the value
	 */
	public void setUseage(int ResID, float value){
		res[useage][ResID]=value;
	}

	/**
	 * Sets the income.
	 * 
	 * @param ResID the res id
	 * @param value the value
	 */
	public void setIncome(int ResID, float value){
		res[income][ResID]=value;
	}

	/**
	 * Returns the resource.
	 * 
	 * @param type the type (current, usage, income, storage)
	 * @param resource the resource
	 * 
	 * @return the float
	 */
	public float get(int resource, int type){
		return res[resource][type];
	}

	/**
	 * Adds Resource to this res.
	 * 
	 * @param resource the resource
	 * 
	 * @return the resource
	 */
	public IResource add(IResource resource) {
		if (resource!=null)
			for (int i=0; i<res.length; i++){
				for (int j=0; j<res[i].length; j++){
					res[i][j]=res[i][j]+resource.get(i,j);
				}
			}
		return this;
	}

	/**
	 * Subtract resources from this resource.
	 * 
	 * @param resource the resource
	 */
	public void sub(IResource resource) {
		for (int i=0; i<res.length; i++){
			for (int j=0; j<res[i].length; j++){
				res[i][j]=res[i][j]-resource.get(i,j);
			}
		}
	}

	/**
	 * Set all to Zero.
	 */
	public void zero(){
		for (int i=0; i<res.length; i++){
			for (int j=0; j<res[i].length; j++){
				res[i][j]=0;
			}
		}
	}
	
	/**
	 * Divide.
	 *
	 * @param divider the divider the resource will be divided
	 */
	public void divide(int divider){
		if (divider==0){
			ai.logError("");
			return;
		}
		for (int i=0; i<res.length; i++){
			for (int j=0; j<res[i].length; j++){
				res[i][j]=res[i][j]/divider;
			}
		}
	}

	/**
	 * Checks if is zero.
	 *
	 * @return true, if is zero
	 */
	public boolean isZero(){
		for (int i=0; i<res.length; i++){
			for (int j=0; j<res[i].length; j++){
				if (res[i][j]!=0)
					return false;
			}
		}
		return true;
	}

	/**
	 * Gets the total Resources available (seen over time).
	 *
	 * @param ResID the res id
	 * @param time the time
	 *
	 * @return the total
	 */
	public float getTotal(int ResID, int time){
		return getCurrent(ResID) + (time)*(getIncome(ResID) - getUseage(ResID));
	}

	/**
	 * Checks if the resources produced are less cost
	 *
	 * @param cost the resource
	 * @param time the time
	 *
	 * @return true, if successful
	 */
	public boolean lessOrEqual(IResource cost, int time) {
		for (int i=0; i<resources.size(); i++){
			float current, less;
			current=getTotal(i, time);
			less=cost.getTotal(i, 0);
			if (current-less>=0){
				ai.logWarning("" +resources.get(i).getName()+" is missing: " + new Str(current) + new Str(less) + new Str(this.toString()) + new Str(cost.toString() + new Str(time)));
				return false;
			}
		}
		ai.logDebug("Enough resources!");
		return true;
	}
	
	private String getType(int i){
		switch (i){
			case 0: return "cur";
			case 1: return "use";
			case 2: return "inc";
			case 3: return "sto";
		}
		return "Unknown type!";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String str="";
		int i=0;
		for (int j=0; j<res[0].length; j++){
			if (j>0)
				str=str+"                              ";
			str = str + resources.get(j).getName();
			for (i=0; i<res.length; i++){
				str=str+new Str(getType(i),8)+new Str(res[i][j],8);
			}
			if (j<res[0].length-1)
				str = str+  "\n";
		}
		return str;
	}

	/**
	 * Size.
	 *
	 * @return the count of resources (in default Metal + Energy)
	 */
	public int size(){
		return resources.size();
	}
	
	public void setFrom(IResource ress){
		for (int i=0; i<res.length; i++){
			for (int j=0; j<res[i].length; j++){
				res[i][j]=ress.get(i,j);
			}
		}
	}
}
