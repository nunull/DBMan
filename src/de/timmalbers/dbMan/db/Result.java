package de.timmalbers.dbMan.db;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Result {
	private LinkedList<String> attributes;
	private LinkedList<String> labels;
	private LinkedList<LinkedHashMap<String, String>> dataSets;
	
	public Result() {
		this.attributes = new LinkedList<>();
		this.labels = new LinkedList<>();
		this.dataSets = new LinkedList<>();
	}
	
	public void addAttribute(String attribute) {
		attributes.add(attribute);
	}
	
	public void addLabel(String label) {
		labels.add(label);
	}
	
	public void addDataSet(LinkedHashMap<String, String> dataSet) {
		dataSets.add(dataSet);
	}
	
	public LinkedList<String> getAttributes() {
		return attributes;
	}
	
	public LinkedList<String> getLabels() {
		return labels;
	}
	
	public LinkedList<LinkedHashMap<String, String>> getDataSets() {
		return dataSets;
	}
}
