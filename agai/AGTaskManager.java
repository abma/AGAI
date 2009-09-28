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


// TODO: Auto-generated Javadoc
//TODO: Auto-generated Javadoc
/**
 * The Class AGTaskManager.
 */
abstract class AGTaskManager{
	
	/** The ai. */
	protected AGAI ai;
	
	
	/**
	 * Instantiates a new aG task manager.
	 * 
	 * @param ai the ai
	 */
	AGTaskManager(AGAI ai){
		ai.msg("Initialized AGTaskManager "+this.getClass()+" "+ai);
		this.ai=ai;
	}

	/**
	 * Solve.
	 * 
	 * @param task the task
	 */
	public abstract void solve(AGTask task);
}
