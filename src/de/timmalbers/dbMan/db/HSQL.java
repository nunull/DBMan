package de.timmalbers.dbMan.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import de.timmalbers.dbMan.scheme.TableScheme;

public class HSQL extends DB {
	private Connection con;
	private String joinsCache;
	
	public HSQL(String path, String username, String password) throws IOException {
		super(path, username, password);
	}
	
	@Override
	protected void initDB() throws IOException {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			
			con = DriverManager.getConnection("jdbc:hsqldb:file:" + getPath() + ";shutdown=true", getUsername(), getPassword());
			
			clearJoinsCache();
		} catch (ClassNotFoundException e) {
			throw new IOException(e);
		} catch (SQLException e) {
			throw new IOException(e);
		}
	}
	
	public void ensureConnection() throws IOException {
		if(con == null) initDB();
	}
	
	public void clearJoinsCache() {
		joinsCache = "";
	}

	private String getAttributeList(TableScheme tableScheme) {
		LinkedHashMap<String, Object> attributes = tableScheme.getAttributes();
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
		
		attributeList = attributeList.substring(0, attributeList.length() - 2);
			
		return attributeList;
	}
	
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
						if(!tableName.equals(tableScheme.getTableName())) {
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
			
			s.close();
		} catch (SQLException e) {
			throw new IOException(e);
		}
		
		clearJoinsCache();
		
		return result;
	}

	@Override
	public void update(TableScheme tableScheme) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
