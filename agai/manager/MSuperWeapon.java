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

package agai.manager;

import agai.AGAI;
import agai.task.Task;

// TODO: Auto-generated Javadoc
/**
 * The Class AGTBuildSuperWeapon.
 */
public class MSuperWeapon extends Manager {
	/*
	 * Algorithm: get all units with attack-damage calculate sum/n get the
	 * biggest values and try to build this units
	 */

	/**
	 * Instantiates a new aGT build super weapon.
	 * 
	 * @param ai
	 *            the ai
	 */
	MSuperWeapon(AGAI ai) {
		super(ai);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agai.AGTaskManager#solve(agai.AGTask)
	 */
	@Override
	public void solve(Task task) {
		// TODO Auto-generated method stub

	}

}
