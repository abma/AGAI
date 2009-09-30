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

import agai.AGAI.ElementType;
import agai.task.TaskAttack;
import agai.task.TaskScout;

// TODO: Auto-generated Javadoc
/*

Pseudocode für den Algorithmus

1. Sortiere nach Priorität
2. Versuche Alle Tasks zu lösen
	Um Task zu lösen, füge notwendige zwischenschritte ein,
	Sollte ein zwischenschritt ein zweites mal hinzugefügt werden,
	wird wird die Priorität des ersten erhöht und der zwischenschritt nicht hinzugefügt
3. Entferne gelöste Tasks
4. Gehe zu 1



 Tasks + Algorithmus

 Baue Energie
 Hinzufügen: Ist ein Idle-Task schon vorhanden, wenn ja erhöhe Priorität, sonst:
	Filtere nach möglichen Gebäude die mit dem vorhandenen Techlevel möglich sind
	Filtere nach Gebäude nach wieviel Energie ist vorhanden
	Filtere nach wieviel Metall ist vorhanden
	Filtere nach möglichen Baupunkten
	Füge Task hinzu

 Baue Metall
 Siehe Baue Energie

 Scout
 Gibt es einen Scout der nichts zu tun hat?
	Ja: Teile Karte in Raster mit der grösse des Scoutradius ein und fahre jedes Raster ab, markiere Raster mit dem letzten Besuch
	Nein: Baue "beste" Scouteinheit:
		Filtere nach möglichen Einheiten die gebaut werden können
		Nimm die Schnellste + billigste + grösster Suchradius
		Baue Einheit

 Einheit bauen
 Suche idle Einheit die die Einheit bauen kann
 baue Einheit
 Wenn Priorität hoch ist, beschleunige bau mit weiteren Baueinheiten

 Gebäude bauen
 Suche idle Einheit, die das Gebäude bauen kann und möglichst Nahe an dem Punkt ist
 baue Gebäude
 Wenn Priorität hoch ist, beschleunige bau mit weiteren Baueinheiten

 Metall bauen
 Suche leere Metallpunkte
	ja:Suche Einheit die (besten) Metallabbauer bauen kann
		ja: Baue Metallabbauer
		nein: Baue Einheit die Metallabbauer bauen kann
	nein:
		ist genüg Energie vorhanden:
			ja: Baue Energie->Metallwandler
			nein: Baue Energie

 Energie bauen
	Suche idle Einheit die Energieproduktion bauen kann
		ja: Baue Energieproduktion in der Nähe
		nein: baue Einheit die Energie bauen kann

 Angriff vorbereiten
	Suche idle Angriffseinheit
		Wenn vorhanden: füge Einheit in Angriffsformation hinzu

 Angreifen:
	Suche idle Angriffsformation:
		ja: Suche lohnende Ziele und versuche dies über ungefährliche Positionen anzugreifen
		nein: baue angriffseinheit

 KartenMatrixen zur Entscheidungshilfe
		Land-passierbar
		Wasser-passierbar
		Feind-sichtungen
		Verlorene Einheiten
		Feindliche Gebäudesichtungen
		Feindliche Einheitensichtungen

 Einheitenfilter
	Suche Einheiten mit Folgenden Eigenschafte:...

		Suche Liste mit Einheiten nach Eigenschaft 1 aus und füge dies in die Liste hinzu (+sortiere)
	Nimm nächste Eigenschaft und wiederhole



wishlist:

einheit
resource (?)
gebäude
verteidigung
einheit

struktur:
	notwendige resourcen
	(position?)
	dringlichkeit
	was bringt das wenn der wunsch erfüllt wird? (gegnerische einheit tot? schutz? mehr resourcen?)

*/

/**
 * The Controller, the big boss :-)
 * verteilt die resourcen an die mananger, die verfügbar sind und passt diese bei änderungen an (mehr oder weniger)
 * sagt dem angriffsmanager wann er angreifen soll
 * gibt dem resourcenmanager resourcen um gebäude zu bauen
 */
public class Controller{
	
	/** The ai. */
	private AGAI ai;

	/**
	 * Instantiates a new controller.
	 * 
	 * @param ai the ai
	 */
	Controller(AGAI ai) {
		this.ai=ai;
	}
	
	/**
	 * Stop.
	 */
	public void stop() {
		ai.getAGT().clear();
	}

	/**
	 * Start.
	 */
	public void start() {
		ai.getAGT().addTask(new TaskScout(ai));
		ai.getAGT().addTask(new TaskAttack(ai, ElementType.unitLand));
	}
	
	/**
	 * Update.
	 * 
	 * @param frame the frame
	 */
	public void update(int frame){
		ai.getAGT().DoSomething(frame);
	}

}
