package de.timmalbers.dbMan.scheme;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Represents a scheme of a table
 * 
 * @author Timm Albers
 */
public class TableScheme {
	private String tableName;
	private String bindingKey;
	private LinkedHashMap<String, Object> attributes;
	private LinkedList<String> hiddenAttributes;
	private LinkedHashMap<String, String> attributeDefaults;
	
	public TableScheme(String tableName) {
		this.tableName = tableName;
		
		attributes = new LinkedHashMap<>();
		hiddenAttributes = new LinkedList<>();
		attributeDefaults = new LinkedHashMap<>();
	}
	
	/**
	 * Bind the given attribute<br><br>
	 * 
	 * <b>First example</b>: <code>scheme.bind("id").to("ID");</code><br>
	 * (This would bind the attribute called "id" to "ID".
	 * It would then be displayed as "ID".)<br><br>
	 * 
	 * <b>Second example</b>: <code>scheme.bind("carId").to(carScheme).with("id");</code><br>
	 * (This would bind the attribute called "carId" to the
	 * previously initialized "carScheme" using the attribute
	 * "id" of the table of "carScheme" to map the entries.)<br><br>
	 * 
	 * <b>Third example</b>: <code>scheme.bind("secret");</code><br>
	 * (This would bind the attribute called "secret". The
	 * attribute will not be displayed to the user in this case,
	 * but it will be available for internal use.) 
	 * 
	 * @param attribute The attribute
	 * @return The target (e.g. the object implementing the .to()-method)
	 */
	public BindingTarget bind(String attribute) {
		addHiddenAttribute(attribute);
		
		return new BindingTarget(this, attribute);
	}
	
	public void setBindingKey(String bindingKey) {
		this.bindingKey = bindingKey;
	}
	
	public String getBindingKey() {
		return getTableName() + "." + bindingKey;
	}
	
	protected void addAttribute(String attribute, String displayName) {
		removeHiddenAttribute(attribute);
		attributes.put(this.getTableName() + "." + attribute, displayName);
	}
	
	protected void addAttribute(String attribute, TableScheme tableScheme) {
		attributes.put(tableScheme.getTableName() + "." + attribute, tableScheme);
	}
	
	protected void addAttributeDefault(String attribute, String def) {
		attributeDefaults.put(attribute, def);
	}
	
	public String getAttributeDefault(String attribute) {
		return attributeDefaults.get(attribute.split("\\.")[1]);
	}
	
	public LinkedHashMap<String, String> getAttributeDefaults() {
		return attributeDefaults;
	}
	
	private void addHiddenAttribute(String attribute) {
		hiddenAttributes.add(this.getTableName() + "." + attribute);
	}
	
	protected void removeHiddenAttribute(String attribute) {
		hiddenAttributes.remove(this.getTableName() + "." + attribute);
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public LinkedHashMap<String, Object> getAttributes() {
		return attributes;
	}
	
	public LinkedList<String> getHiddenAttributes() {
		return hiddenAttributes;
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
