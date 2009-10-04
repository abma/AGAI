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

package agai.loader;

//import com.springrts.ai.oo.*;

import com.springrts.ai.oo.OOAI;
import com.springrts.ai.oo.OOAICallback;
import com.springrts.ai.oo.OOAIFactory;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating AGAI objects.
 */
public class AGAIFactory extends OOAIFactory {

	/**
	 * Checks if is debugging.
	 * 
	 * @return true, if is debugging
	 */
	public static boolean isDebugging() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.springrts.ai.oo.OOAIFactory#createAI(int,
	 * com.springrts.ai.oo.OOAICallback)
	 */
	@Override
	public OOAI createAI(int teamId, OOAICallback callback) {
		return new AGLoader();
	}
}
