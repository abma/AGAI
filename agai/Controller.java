package agai;

import agai.AGAI.ElementType;
import agai.manager.ResourceManagerTask;
import agai.unit.AttackTask;
import agai.unit.ScoutTask;

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

*/

public class Controller{
	private AGAI ai;

	Controller(AGAI ai) {
		this.ai=ai;
	}
	public void stop() {
		ai.getAGT().clear();
	}

	public void start() {
		ai.getAGT().addTask(new ResourceManagerTask(ai));
		ai.getAGT().addTask(new ScoutTask(ai));
		ai.getAGT().addTask(new AttackTask(ai, ElementType.unitLand));
	}
	
	public void update(int frame){
		ai.getAGT().DoSomething(frame);
	}

}
