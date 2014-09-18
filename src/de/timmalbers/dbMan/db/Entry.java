package de.timmalbers.dbMan.db;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Represents a database entry
 * 
 * @author Timm Albers
 */
public class Entry {
	private String entryLabel;
	private LinkedList<String> labels;
	private LinkedHashMap<String, String> dataSet;
	
	public Entry(LinkedHashMap<String, String> dataSet, LinkedList<String> labels) {
		this.labels = labels;
		this.dataSet = dataSet;
	}
	
	/**
	 * Sets a value
	 * 
	 * @param key The key, at which the value will be stored 
	 * @param value The value to store
	 */
	public void set(String key, String value) {
		dataSet.put(key, value);
	}
	
	/**
	 * Returns a value
	 * 
	 * @param key The key, at which the value is stored
	 * @return The value
	 */
	public String get(String key) {
		return dataSet.get(key);
	}
	
	/**
	 * Returns the label, used for displaying the value, addressed with the given key
	 * 
	 * @param key The key
	 * @return The label
	 */
	public String getLabel(String key) {
		int index = 0;
		
		for(String curKey : getKeySet()) {
			if(curKey.equals(key)) {
				return labels.get(index);
			}
			
			index++;
		}
		
		return "";
	}
	
	/**
	 * Returns the whole set of keys
	 * 
	 * @return The set of keys
	 */
	public Set<String> getKeySet() {
		return dataSet.keySet();
	}
	
	/**
	 * Set the label for the whole entry
	 * 
	 * @param entryLabel The label
	 */
	public void setEntryLabel(String entryLabel) {
		this.entryLabel = entryLabel;
	}
	
	@Override
	public String toString() {
		return entryLabel;
	}
}
