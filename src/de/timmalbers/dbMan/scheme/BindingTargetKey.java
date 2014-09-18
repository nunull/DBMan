package de.timmalbers.dbMan.scheme;

/**
 * Used for binding an attribute to a table scheme (See {@link TableScheme.bind(String attribute)} for further information)
 *
 * @author Timm Albers
 */
public class BindingTargetKey {
	private TableScheme tableScheme;
	private TableScheme goalScheme;
	
	public BindingTargetKey(TableScheme tableScheme, TableScheme goalScheme) {
		this.tableScheme = tableScheme;
		this.goalScheme = goalScheme;
	}
	
	public void with(String attribute) {
		goalScheme.setBindingKey(attribute);
		tableScheme.addAttribute(attribute, goalScheme);
	}
}
