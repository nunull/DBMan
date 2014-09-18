package de.timmalbers.dbMan.scheme;

/**
 * Represents the default of a value 
 * 
 * @author Timm Albers
 */
public class BindingDefault {
	private TableScheme tableScheme;
	private String attribute;
	
	public BindingDefault(TableScheme tableScheme, String attribute) {
		this.tableScheme = tableScheme;
		this.attribute = attribute;
	}
	
	public void def(String def) {
		tableScheme.addAttributeDefault(attribute, def);
	}
}
