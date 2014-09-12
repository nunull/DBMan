package de.timmalbers.dbMan.controller;

import de.timmalbers.dbMan.gui.MainWindow;

public class MainController {
	private MainWindow mainWindow;
	
	public MainController(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		
		initGUI();
	}
	
	private void initGUI() {
		mainWindow.setVisible(true);
	}
}
