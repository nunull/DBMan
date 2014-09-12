package de.timmalbers.dbMan.modules;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import de.timmalbers.dbMan.db.DB;
import de.timmalbers.dbMan.db.HSQL;
import de.timmalbers.dbMan.scheme.TableScheme;

public class SchuelerModule extends AbstractModule {

	@Override
	public String getLabel() {
		return "Schüler";
	}
	
	@Override
	public DB getDB() throws IOException {
		return new HSQL("/Users/Timm 1/Programming/Java/DBMan/res/db", "SA", "");
	}
	
	@Override
	public TableScheme getTableScheme() {
		TableScheme bildungsgaengeScheme = new TableScheme("bildungsgaenge");
		bildungsgaengeScheme.bind("name").to("Bildungsgang");
		
		TableScheme klassenScheme = new TableScheme("klassen");
		klassenScheme.bind("jahrgang").to("Klasse");
		
		TableScheme schuelerScheme = new TableScheme("schueler");
		schuelerScheme.bind("name").to("Name");
		schuelerScheme.bind("vorname").to("Vorname");
		schuelerScheme.bind("geschlecht").to("Geschlecht");
		schuelerScheme.bind("geburtsdatum").to("Geburtsdatum");
		schuelerScheme.bind("bildungsgang").to(bildungsgaengeScheme).with("id");
		schuelerScheme.bind("klasse").to(klassenScheme).with("id");
		
		return schuelerScheme;
	}
	
	@Override
	public void modifyHeader(LinkedList<String> attributes, LinkedList<String> labels) {
		attributes.remove("bildungsgaenge.name");
		labels.remove("Bildungsgang");
		
		attributes.remove("klassen.jahrgang");
		labels.remove("Klasse");
		
		labels.add("Klasse");
		attributes.add("bildungsgaenge.name+klassen.jahrgang");
	}
	
	@Override
	public void modifyDataSet(LinkedHashMap<String, String> dataSet) {
		String klasse = dataSet.get("bildungsgaenge.name");
		klasse += dataSet.get("klassen.jahrgang");
		
		dataSet.remove("bildungsgaenge.name");
		dataSet.remove("klassen.jahrgang");
		
		dataSet.put("bildungsgaenge.name+klassen.jahrgang", klasse);
	}
}
