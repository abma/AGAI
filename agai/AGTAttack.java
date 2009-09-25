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

import com.springrts.ai.AIFloat3;

// TODO: Auto-generated Javadoc
class AGTaskAttack extends AGTask{

	AGTaskAttack(AGAI ai) {
		super(ai);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void solve() {
		ai.getAGT().get(AGTAttack.class).solve(this);
		
	}

}
class AGTaskBuildAttacker extends AGTask{

	AGTaskBuildAttacker(AGAI ai) {
		super(ai);
	}

	@Override
	public void solve() {
		ai.msg("");
		ai.getAGT().get(AGTAttack.class).solve(this);
	}
	public void unitFinished(AGUnit unit){
		ai.msg("");
		this.setStatusFinished();
	}
}


// TODO: Auto-generated Javadoc
/**
 * The Class AGTAttack.
 * 
 * @author matze
 */
public class AGTAttack extends AGTaskManager{
	
	/**
	 * Instantiates a new aG task attack.
	 * 
	 * @param ai the ai
	 */
	AGTAttack(AGAI ai) {
		super(ai);
	}
	
	/**
	 * Sets the pos.
	 * 
	 * @param pos the new pos
	 */
	public void setPos(AIFloat3 pos){
		//this.pos=pos;
	}
	
	/* (non-Javadoc)
	 * @see antigeorgeai.AGTask#solveTodo()
	 */

	/**
	 * Dump.
	 */
	public void dump(){
		ai.msg("Task Attack");
	}

	/* (non-Javadoc)
	 * @see agai.AGTaskManager#solve(agai.AGTask)
	 */
	@Override
	public void solve(AGTask task) {
		// TODO Auto-generated method stub
		
	}

}