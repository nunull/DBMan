package de.timmalbers.dbMan.modules;

import java.io.IOException;

import de.timmalbers.dbMan.db.DB;
import de.timmalbers.dbMan.db.Entry;
import de.timmalbers.dbMan.db.Result;
import de.timmalbers.dbMan.scheme.TableScheme;

/**
 * Represents a module
 * 
 * @author Timm Albers
 */
public abstract class AbstractModule {
	/**
	 * Queries the database and returns the result
	 *  
	 * @return The result
	 * @throws IOException if something went wrong
	 */
	public Result get() throws IOException {
		DB db = getDB();
		Result r = db.get(getTableScheme());
		
		return r;
	}
	
	/**
	 * Returns the database
	 * 
	 * @return The database
	 * @throws IOException if something went wrong
	 */
	public abstract DB getDB() throws IOException;
	
	/**
	 * Returns the label
	 * 
	 * @return the label
	 */
	public abstract String getLabel();
	
	/**
	 * Initializes the label of the given entry
	 * 
	 * @param e The entry
	 */
	public abstract void initEntryLabel(Entry e);
	
	/**
	 * Returns the table scheme used by this module
	 * 
	 * @return The scheme
	 */
	public abstract TableScheme getTableScheme();
}
