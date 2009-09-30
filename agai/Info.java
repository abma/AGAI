package agai;

import agai.info.BuildTree;
import agai.info.PoIsMap;
import agai.info.Search;
import agai.info.TreatMap;

public class Info {
	private Search aGF;
	private PoIsMap aGP;
	private TreatMap aGM;
	private BuildTree aGB;

	Info(AGAI ai){
		this.aGF = new Search(ai);
		this.aGP = new PoIsMap(ai);
		this.aGM = new TreatMap(ai);
		this.aGB = new BuildTree(ai);
	}

	public Search getAGF() {
		return aGF;
	}

	public PoIsMap getAGP() {
		return aGP;
	}

	public TreatMap getAGM() {
		return aGM;
	}

	public BuildTree getAGB() {
		return aGB;
	}

}
