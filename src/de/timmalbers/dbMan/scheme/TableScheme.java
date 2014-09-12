package de.timmalbers.dbMan.scheme;

import java.util.LinkedHashMap;

public class TableScheme {
	private String tableName;
	private String bindingKey;
	private LinkedHashMap<String, Object> attributes;
	
	public TableScheme(String tableName) {
		this.tableName = tableName;
		
		attributes = new LinkedHashMap<>();
	}
	
	public BindingTarget bind(String attribute) {
		return new BindingTarget(this, attribute);
	}
	
	public void setBindingKey(String bindingKey) {
		this.bindingKey = bindingKey;
	}
	
	public String getBindingKey() {
		return getTableName() + "." + bindingKey;
	}
	
	protected void addAttribute(String attribute, String displayName) {
		attributes.put(this.getTableName() + "." + attribute, displayName);
	}
	
	protected void addAttribute(String attribute, TableScheme tableScheme) {
		attributes.put(tableScheme.getTableName() + "." + attribute, tableScheme);
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public LinkedHashMap<String, Object> getAttributes() {
		return attributes;
	}
	
	public String getAttributeName(String attributeString) {
		for(String attribute : attributes.keySet()) {
			if(attribute.equals(attributeString)) {
				if(attributes.get(attribute) instanceof String) {
					return (String) attributes.get(attribute);
				}
			}
		}
		
		return null;
	}
	
	public TableScheme getTableScheme(String tableName) {
		for(String attribute : attributes.keySet()) {
			if(attributes.get(attribute) instanceof TableScheme) {
				TableScheme scheme = (TableScheme) attributes.get(attribute);
				
				if(scheme.getTableName().equals(tableName)) {
					return scheme;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		String s = tableName + "\n";
		
		for(String attribute : attributes.keySet()) {
			String value = attributes.get(attribute).toString();
			value = value.replace("\t", "\t\t");
			
			s += "\t" + attribute + ": " + value + "\n"; 
		}
					
		return s;
	}
}
