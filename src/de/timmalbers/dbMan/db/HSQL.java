package de.timmalbers.dbMan.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import de.timmalbers.dbMan.scheme.TableScheme;

/**
 * Represents a HSQL database connection
 * 
 * @author Timm Albers
 */
public class HSQL extends DB {
	private Connection con;
	private String joinsCache;
	
	public HSQL(String path, String username, String password) throws IOException {
		super(path, username, password);
	}
	
	/**
	 * Method for initializing the database
	 * 
	 * @throws IOException
	 */
	@Override
	protected void initDB() throws IOException {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			
//			con = DriverManager.getConnection("jdbc:hsqldb:file:" + getPath() + ";shutdown=true", getUsername(), getPassword());
			con = DriverManager.getConnection("jdbc:hsqldb:file:" + getPath(), getUsername(), getPassword());
			
			clearJoinsCache();
//			createTableIfNotExists();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * Checks if the connection is alive, reconnects to the database if necessary
	 * 
	 * @throws IOException if something went wrong
	 */
	public void ensureConnection() throws IOException {
		try {
			if(con == null || con.isClosed()) initDB();
		} catch (SQLException e) {
			initDB();
		}
	}
	
	/**
	 * Clear the cache used for SQL-join statements
	 */
	public void clearJoinsCache() {
		joinsCache = "";
	}

	/**
	 * Returns the list of attributes for the given table scheme
	 * 
	 * @param tableScheme The scheme
	 * @return The list as a SQL string
	 */
	private String getAttributeList(TableScheme tableScheme) {
		LinkedHashMap<String, Object> attributes = tableScheme.getAttributes();
		LinkedList<String> hiddenAttributes = tableScheme.getHiddenAttributes();
		Object[] attributeNames = attributes.keySet().toArray();
		String attributeList = "";
			
		for(int i = 0, j = attributeNames.length; i < j; i++) {
			Object attribute = attributes.get(attributeNames[i]);
			
			if(!(attribute instanceof TableScheme)) {
				attributeList += (String) attributeNames[i];
				attributeList += " AS \"" + (String) attributeNames[i] + "\", ";
			} else {
				TableScheme subScheme = (TableScheme) attribute;
				attributeList += getAttributeList(subScheme) + ", ";
				
				String join = " JOIN " + subScheme.getTableName() + " ON ";
				join += "(" + attributeNames[i] + " = ";
				join += subScheme.getBindingKey() + ")";
				
				joinsCache += join;
			}
		}
		
		for(String attribute : hiddenAttributes) {
			attributeList += attribute;
			attributeList += " AS \"hidden:" + attribute + "\", ";
		}
		
		attributeList = attributeList.substring(0, attributeList.length() - 2);
			
		return attributeList;
	}
	
	/**
	 * Selects data stored in the database
	 * 
	 * @param tableScheme The table scheme describing the table to read from (initially)
	 * @return the result
	 * @throws IOException if something went wrong
	 */
	@Override
	public Result get(TableScheme tableScheme) throws IOException {
		ensureConnection();
		
		String attributeList = getAttributeList(tableScheme);
		Result result = new Result();
		
		try {
			PreparedStatement s = con.prepareStatement("SELECT " + attributeList + " FROM " + tableScheme.getTableName() + joinsCache);
			ResultSet r = s.executeQuery();
			
			ResultSetMetaData rMeta = r.getMetaData(); 
			
			int index = 0;
			while(r.next()) {
				LinkedHashMap<String, String> dataSet = new LinkedHashMap<>();
				
				for(int i = 1, j = rMeta.getColumnCount(); i < j + 1; i++) {
					String fullName = rMeta.getColumnLabel(i);
					
					if(index == 0) {
						String[] columnLabelParts = fullName.split("\\.");
						String tableName = columnLabelParts[0];
						
						TableScheme tmpScheme = tableScheme;
						
						if(tableName.indexOf("hidden:") != 0 &&
								!tableName.equals(tableScheme.getTableName())) {
							tmpScheme = tableScheme.getTableScheme(tableName);
						}
						
						result.addAttribute(fullName);
						result.addLabel(tmpScheme.getAttributeName(fullName));
					}
					
					dataSet.put(fullName, r.getString(i));
				}
				result.addDataSet(dataSet);
				
				index++;
			}
			
			r.close();
			s.close();
		} catch (SQLException e) {
			throw new IOException(e);
		}
		
		clearJoinsCache();
		
		return result;
	}

	/**
	 * Inserts the given entry
	 * 
	 * @param tableScheme The table scheme describing the table to write to
	 * @param entry The entry to be written to the database
	 * @throws IOException if something went wrong
	 */
	public void insert(TableScheme tableScheme, Entry entry) throws IOException {
		ensureConnection();
		
		String sqlKeys = "";
		String sqlValues = "";
		
		for(String key : entry.getKeySet()) {
			if(key.indexOf(tableScheme.getTableName() + ".") == 0) {
				sqlKeys += key.replaceFirst(tableScheme.getTableName() + ".", "") + ",";
				sqlValues += "'" + entry.get(key) + "',";
			}
		}
		
		sqlKeys = sqlKeys.substring(0, sqlKeys.length()-1);
		sqlValues = sqlValues.substring(0, sqlValues.length()-1);
		
		String sql = "INSERT INTO " + tableScheme.getTableName() + "(" + sqlKeys + ")"
			+ " VALUES (" + sqlValues + ")";
		
		try {
			con.createStatement().execute(sql);
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * Updates the given entry
	 * 
	 * @param tableScheme The table scheme describing the table to write to
	 * @param entry The entry to write to the database
	 * @throws IOException if something went wrong
	 */
	@Override
	public void update(TableScheme tableScheme, Entry entry) throws IOException {
		ensureConnection();
		
		LinkedHashMap<String, Object> attributes = tableScheme.getAttributes();
		Set<String> attributeKeySet = attributes.keySet();
		LinkedList<String> hiddenAttributes = tableScheme.getHiddenAttributes();
		String sql = "UPDATE " + tableScheme.getTableName() + " SET ";
		
		for(String key : attributeKeySet) {
			if(attributes.get(key) instanceof TableScheme) {
				
				// TODO recursive
//				update((TableScheme) attributes.get(key), entry);
			} else if(key.indexOf(tableScheme.getTableName() + ".") == 0) {
				sql += key.replaceFirst(tableScheme.getTableName() + ".", "") + "='" + entry.get(key) + "', ";
			}
		}
		
		sql = sql.substring(0, sql.length() - 2);
		sql += " WHERE ";
		
		for(String key : hiddenAttributes) {
			sql += key.replaceFirst(tableScheme.getTableName() + ".", "") + "=" + entry.get("hidden:" + key);
		}
		
		try {
			con.createStatement().execute(sql);
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Closes the database connection
	 * 
	 * @throws IOException if something went wrong
	 */
	@Override
	public void close() throws IOException {
		try {
			con.commit();
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Deletes the given entry
	 * 
	 * @param entry The entry to be deleted
	 * @throws IOException if something went wrong
	 */
	@Override
	public void delete(TableScheme tableScheme, Entry entry) throws IOException {
		ensureConnection();
		
		try {
			con.createStatement().execute("DELETE FROM " + tableScheme.getTableName() + " WHERE id=" + entry.get("hidden:schueler.id"));
		} catch (SQLException e) {
			throw new IOException(e);
		}
		
	}
}
