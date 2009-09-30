package agai.info;

import com.springrts.ai.AIFloat3;

public class PoIMap{ //Point of Interest
	private AIFloat3 pos;
	private boolean visited;
	private int type = -1;
	private boolean built;
	PoIMap(AIFloat3 pos, int type){
		this.pos=pos;
		this.type=type;
		built=false;
		visited=false;
	}
	
	/**
	 * Gets if the PoI was already visited (or if a building is built there, visiting isn't necessary)
	 * 
	 * @return the visited
	 */
	public boolean isVisited(){
		return visited;
	}
	
	/**
	 * Sets the visited.
	 * 
	 * @param visited the new visited
	 */
	public void setVisited(boolean visited){
		this.visited=visited;
	}
	
	/**
	 * Gets the pos.
	 * 
	 * @return the pos
	 */
	public AIFloat3 getPos(){
		return this.pos;
	}
	
	/**
	 * Type.
	 * 
	 * @return the int
	 */
	public int getType(){
		return this.type;
	}
	
	/**
	 * Checks if is built.
	 * 
	 * @return true, if is built
	 */
	public boolean isBuilt() {
		return built;
	}
	
	/**
	 * Sets the built.
	 * 
	 * @param built the new built
	 */
	public void setBuilt(boolean built) {
		this.built = built;
	}
}