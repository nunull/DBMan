package de.timmalbers.dbMan.scheme;

/**
 * The target to bind an attribute to (See {@link TableScheme.bind(String attribute)} for further information)
 *
 * @author Timm Albers
 */
public class BindingTarget {
	private TableScheme tableScheme;
	private String attribute;
	
	public BindingTarget(TableScheme tableScheme, String attribute) {
		this.tableScheme = tableScheme;
		this.attribute = attribute;
	}
	
	public BindingDefault to(String displayName) {
		tableScheme.addAttribute(attribute, displayName);
		
		return new BindingDefault(tableScheme, attribute);
	}
	
	public BindingTargetKey to(TableScheme scheme) {
		tableScheme.removeHiddenAttribute(attribute);
		return new BindingTargetKey(tableScheme, scheme);
	}
}
