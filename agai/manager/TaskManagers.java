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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import agai.AGAI;
import agai.unit.AGTask;

// TODO: Auto-generated Javadoc
/**
 * The Class AGTasks.
 */


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

public class TaskManagers {
	
	/** The ai. */
	AGAI ai;
	
	/** The list with all Task managers. */
	LinkedList <TaskManager> list;
	
	/** The tasks. */
	LinkedList<AGTask> tasks;
	
	/**
	 * Instantiates a new aG tasks.
	 * 
	 * @param ai the ai
	 */
	public TaskManagers(AGAI ai){
		this.ai=ai;
		tasks=new LinkedList<AGTask>();
		list=new LinkedList<TaskManager>();

		list.add(new ResourceManager(ai));
		list.add(new ScoutManager(ai));
		list.add(new AttackManager(ai));
		list.add(new BuildManager(ai));
	}
	
	/**
	 * Walk through the Task list und try to solve the "problems".
	 *
	 * @param frame the frame
	 */
	public void DoSomething(int frame){
		sort();
		for(int i=tasks.size()-1; i>=0; i--){
			if (tasks.get(i).getLastrun()+tasks.get(i).getRepeat()<=frame){
				tasks.get(i).solve();
				tasks.get(i).lastrun=frame;
				if (tasks.get(i).getRepeat()==0)
					tasks.remove(i);
			}
		}
	}
	
	/**
	 * Gets the list.
	 * 
	 * @param classname the classname
	 * 
	 * @return the list
	 */
	public TaskManager get(Class <?> classname){
		for(int i=0; i<list.size();i++){
			if (list.get(i).getClass()==classname)
				return list.get(i);
		}
		ai.msg("Couldn't find required class!"+classname.getCanonicalName());
		return null;
	}
	
	/**
	 * Adds the Task.
	 * 
	 * @param task the task
	 */
	public void addTask(AGTask task) {
		ai.msg("");
		tasks.add(task);
	}
	
	/**
	 * The Class AGTaskcompare.
	 */
	private class AGTaskcompare implements Comparator<AGTask>{
		
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(AGTask o1, AGTask o2) {
			if (o1.getPriority()>o2.getPriority())
				return 1;
			return 0;
		}
	
	}
	
	/**
	 * Sort list bye priorities.
	 */
	public void sort(){
		Collections.sort(tasks, new AGTaskcompare());
	}

	/**
	 * Dump all Tasks.
	 */
	public void dump() {
		ai.msg("Tasks: ");
		for (int i=0; i<tasks.size();i++){
			ai.msg(i+" "+tasks.get(i).getClass().getName()+" Priority "+tasks.get(i).getPriority()+" "+tasks.get(i).toString());
		}
	}

	public void clear() {
		tasks.clear();
	}
}
