package de.timmalbers.dbMan.modules;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import de.timmalbers.dbMan.db.DB;
import de.timmalbers.dbMan.db.Result;
import de.timmalbers.dbMan.scheme.TableScheme;

public abstract class AbstractModule {
	public Result get() throws IOException {
		DB db = getDB();
		Result r = db.get(getTableScheme());
		
		modifyHeader(r.getAttributes(), r.getLabels());
		
		for(LinkedHashMap<String, String> dataSet : r.getDataSets()) {
			modifyDataSet(dataSet);
		}
		
		return r;
	}
	
	public void modifyHeader(LinkedList<String> attributes, LinkedList<String> labels) {}
	
	public void modifyDataSet(LinkedHashMap<String, String> dataSet) {}
	
	public abstract String getLabel();
	public abstract DB getDB() throws IOException;
	public abstract TableScheme getTableScheme();
}
