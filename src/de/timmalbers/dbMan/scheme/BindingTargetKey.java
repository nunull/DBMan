package de.timmalbers.dbMan.scheme;

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
