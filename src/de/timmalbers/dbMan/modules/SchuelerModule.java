package de.timmalbers.dbMan.modules;

import java.io.IOException;

import de.timmalbers.dbMan.db.DB;
import de.timmalbers.dbMan.db.Entry;
import de.timmalbers.dbMan.db.HSQL;
import de.timmalbers.dbMan.scheme.TableScheme;

/**
 * A module representing students
 * 
 * @author Timm Albers
 */
public class SchuelerModule extends AbstractModule {

	/**
	 * Returns the database
	 * 
	 * @return The database
	 * @throws IOException if something went wrong
	 */
	@Override
	public DB getDB() throws IOException {
		return new HSQL("/Users/Timm 1/Programming/Java/DBMan/res/dbszut", "SA", "");
	}
	
	/**
	 * Returns the label
	 * 
	 * @return the label
	 */
	@Override
	public String getLabel() {
		return "Schüler";
	}
	
	/**
	 * Initializes the label of the given entry
	 * 
	 * @param e The entry
	 */
	@Override
	public void initEntryLabel(Entry e) {
		e.setEntryLabel(e.get("schueler.name") + ", " + e.get("schueler.vorname"));
	}
	
	/**
	 * Returns the table scheme used by this module
	 * 
	 * @return The scheme
	 */
	@Override
	public TableScheme getTableScheme() {
		TableScheme bildungsgaengeScheme = new TableScheme("bildungsgaenge");
		bildungsgaengeScheme.bind("name").to("Bildungsgang");
		
		TableScheme klassenScheme = new TableScheme("klassen");
		klassenScheme.bind("jahrgang").to("Klasse");
		
		TableScheme schuelerScheme = new TableScheme("schueler");
		
		// Hidden
		schuelerScheme.bind("id");
		
		// Visible
		schuelerScheme.bind("name").to("Name").def("Name");;
		schuelerScheme.bind("vorname").to("Vorname").def("Vorname");
		schuelerScheme.bind("geschlecht").to("Geschlecht").def("0");
		schuelerScheme.bind("geburtsdatum").to("Geburtsdatum").def("1970-01-01");
		schuelerScheme.bind("bildungsgang").to(bildungsgaengeScheme).with("id");
		schuelerScheme.bind("klasse").to(klassenScheme).with("id");
		
		return schuelerScheme;
	}
}
