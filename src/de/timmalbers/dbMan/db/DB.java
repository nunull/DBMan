package de.timmalbers.dbMan.db;

import java.io.IOException;

import de.timmalbers.dbMan.scheme.TableScheme;

/**
 * Abstract class for database representation
 * 
 * @author Timm Albers
 */
public abstract class DB {
	private String path;
	private String username;
	private String password;
	
	public DB(String path, String username, String password) throws IOException {
		this.path = path;
		this.username = username;
		this.password = password;
		
		// Initialize the database when the object was created
		initDB();
	}
	
	/**
	 * Abstract method for initializing the database
	 * 
	 * @throws IOException
	 */
	protected abstract void initDB() throws IOException;
	
	/**
	 * Returns the path to the database
	 * 
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path to the database
	 * 
	 * @param path The database path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Returns the username used to log into the database 
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username used to log into the database
	 * 
	 * @param username The username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the password used to log into the database
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password used to log into the database
	 * 
	 * @param password The password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Selects data stored in the database
	 * 
	 * @param tableScheme The table scheme describing the table to read from (initially)
	 * @return the result
	 * @throws IOException if something went wrong
	 */
	public abstract Result get(TableScheme tableScheme) throws IOException;
	
	/**
	 * Inserts the given entry
	 * 
	 * @param tableScheme The table scheme describing the table to write to
	 * @param entry The entry to be written to the database
	 * @throws IOException if something went wrong
	 */
	public abstract void insert(TableScheme tableScheme, Entry entry) throws IOException;
	
	/**
	 * Updates the given entry
	 * 
	 * @param tableScheme The table scheme describing the table to write to
	 * @param entry The entry to write to the database
	 * @throws IOException if something went wrong
	 */
	public abstract void update(TableScheme tableScheme, Entry entry) throws IOException;
	
	/**
	 * Deletes the given entry
	 * 
	 * @param entry The entry to be deleted
	 * @throws IOException if something went wrong
	 */
	public abstract void delete(TableScheme tableScheme, Entry entry) throws IOException;
	
	/**
	 * Closes the database connection
	 * 
	 * @throws IOException if something went wrong
	 */
	public abstract void close() throws IOException;
}
