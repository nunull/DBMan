package de.timmalbers.dbMan.scheme;

public class BindingTarget {
	private TableScheme tableScheme;
	private String attribute;
	
	public BindingTarget(TableScheme tableScheme, String attribute) {
		this.tableScheme = tableScheme;
		this.attribute = attribute;
	}
	
	public void to(String displayName) {
		tableScheme.addAttribute(attribute, displayName);
	}
	
	public BindingTargetKey to(TableScheme scheme) {
		return new BindingTargetKey(tableScheme, scheme);
	}
}
