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

import agai.info.BuildTree;
import agai.info.PoIsMap;
import agai.info.Search;
import agai.info.TreatMap;

// TODO: Auto-generated Javadoc
/**
 * The Class Info.
 */
public class Info {

	/** The a gf. */
	private Search aGF;

	/** The a gp. */
	private PoIsMap aGP;

	/** The a gm. */
	private TreatMap aGM;

	/** The a gb. */
	private BuildTree aGB;

	/**
	 * Instantiates a new info.
	 *
	 * @param ai the ai
	 */
	Info(AGAI ai){
		this.aGF = new Search(ai);
		this.aGP = new PoIsMap(ai);
		this.aGM = new TreatMap(ai);
		this.aGB = new BuildTree(ai);
	}

	/**
	 * Gets the aGF.
	 *
	 * @return the aGF
	 */
	public Search getAGF() {
		return aGF;
	}

	/**
	 * Gets the aGP.
	 *
	 * @return the aGP
	 */
	public PoIsMap getAGP() {
		return aGP;
	}

	/**
	 * Gets the aGM.
	 *
	 * @return the aGM
	 */
	public TreatMap getAGM() {
		return aGM;
	}

	/**
	 * Gets the aGB.
	 *
	 * @return the aGB
	 */
	public BuildTree getAGB() {
		return aGB;
	}

}
