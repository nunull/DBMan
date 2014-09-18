package de.timmalbers.dbMan.db;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import de.timmalbers.dbMan.modules.AbstractModule;

/**
 * Represents a result of a database query
 * 
 * @author Timm Albers
 */
public class Result {
	private LinkedList<String> attributes;
	private LinkedList<String> labels;
	private LinkedList<LinkedHashMap<String, String>> dataSets;
	private LinkedList<Entry> entries;
	
	public Result() {
		this.attributes = new LinkedList<>();
		this.labels = new LinkedList<>();
		this.dataSets = new LinkedList<>();
	}
	
	/**
	 * Adds an attribute
	 * 
	 * @param attribute The attribute
	 */
	public void addAttribute(String attribute) {
		attributes.add(attribute);
	}
	
	/**
	 * Adds a label
	 * 
	 * @param label The label
	 */
	public void addLabel(String label) {
		labels.add(label);
	}
	
	/**
	 * Adds a set of data
	 * 
	 * @param dataSet The set
	 */
	public void addDataSet(LinkedHashMap<String, String> dataSet) {
		dataSets.add(dataSet);
	}
	
	/**
	 * Returns all attributes
	 * 
	 * @return the attributes
	 */
	public LinkedList<String> getAttributes() {
		return attributes;
	}
	
	/**
	 * Returns all labels
	 * 
	 * @return the labels
	 */
	public LinkedList<String> getLabels() {
		return labels;
	}
	
	/**
	 * Returns all sets of data
	 * 
	 * @return the sets
	 */
	public LinkedList<LinkedHashMap<String, String>> getDataSets() {
		return dataSets;
	}
	
	/**
	 * Returns all entries
	 * 
	 * @param m The module describing the database structure
	 * @return the entries
	 */
	public LinkedList<Entry> getEntries(AbstractModule m) {
		if(entries == null) {
			entries = new LinkedList<>();
			
			for(LinkedHashMap<String, String> dataSet : getDataSets()) {
				entries.add(new Entry(dataSet, getLabels()));
			}
		}
		
		return entries;
	}
}
