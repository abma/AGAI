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

import agai.info.IBuildTree;
import agai.info.IPoIs;
import agai.info.ISearchUnit;
import agai.info.ISectors;

import com.springrts.ai.AIFloat3;

// TODO: Auto-generated Javadoc
/**
 * The Class Info.
 */
public class AGInfos {

	/** The a gb. */
	private IBuildTree aGB;

	/** The a gf. */
	private ISearchUnit aGF;

	/** The a gm. */
	private ISectors aGM;

	/** The a gp. */
	private IPoIs aGP;

	/**
	 * Instantiates a new info.
	 * 
	 * @param ai
	 *            the ai
	 */
	AGInfos(AGAI ai) {
		this.aGF = new ISearchUnit(ai);
		this.aGP = new IPoIs(ai);
		this.aGM = new ISectors(ai);
		this.aGB = new IBuildTree(ai);
	}

	/**
	 * Gets the aGB.
	 * 
	 * @return the aGB
	 */
	public IBuildTree getAGB() {
		return aGB;
	}

	/**
	 * Gets the aGF.
	 * 
	 * @return the aGF
	 */
	public ISearchUnit getAGF() {
		return aGF;
	}

	/**
	 * Gets the aGM.
	 * 
	 * @return the aGM
	 */
	public ISectors getAGM() {
		return aGM;
	}

	/**
	 * Gets the aGP.
	 * 
	 * @return the aGP
	 */
	public IPoIs getAGP() {
		return aGP;
	}

	/**
	 * Gets the difference.
	 * 
	 * @param pos1
	 *            the pos1
	 * @param pos2
	 *            the pos2
	 * 
	 * @return the difference
	 */
	public double getDistance(AIFloat3 pos1, AIFloat3 pos2) {
		return Math.sqrt((Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.z
				- pos2.z, 2)));
	}

}
