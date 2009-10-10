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
		this.ai=ai;
	}
	
	/**
	 * Gets the income.
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
	 * Gets the current.
	 * 
	 * @param ResID the res id
	 * 
	 * @return the current
	 */
	public float getCurrent(int ResID){
		return res[current][ResID];
	}
	
	/**
	 * Gets the storage.
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
	 * Returns the resource
	 * 
	 * @param type the type
	 * @param resource the resource
	 * 
	 * @return the float
	 */
	public float get(int type, int resource){
		return res[type][resource];
	}

	/**
	 * Adds Resource to this res
	 * 
	 * @param resource the resource
	 */
	public void add(IResource resource) {
		for (int i=0; i<res.length; i++){
			for (int j=0; j<res[i].length; j++){
				res[i][j]=res[i][j]+resource.get(i,j);
			}
		}
	}

	/**
	 * Subtract resources from this resource
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
	 * Set all to Zero
	 */
	public void zero(){
		for (int i=0; i<res.length; i++){
			for (int j=0; j<res[i].length; j++){
				res[i][j]=0;
			}
		}
	}
	
	public void divide(int divider){
		if (divider==0){
			ai.msg("");
			return;
		}
		for (int i=0; i<res.length; i++){
			for (int j=0; j<res[i].length; j++){
				res[i][j]=res[i][j]/divider;
			}
		}
	}
	public boolean isZero(){
		for (int i=0; i<res.length; i++){
			for (int j=0; j<res[i].length; j++){
				if (res[i][j]!=0)
					return false;
			}
		}
		return true;
	}
	
}
