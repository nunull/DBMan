package de.timmalbers.dbMan;

import javax.swing.SwingUtilities;

import de.timmalbers.dbMan.gui.MainWindow;
import de.timmalbers.dbMan.modules.SchuelerModule;

/**
 * The main class
 */
public class Main {

	/**
	 * @param args ignored
	 */
	public static void main(String[] args) {
		// GUI-stuff, use invokeLater for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainWindow mainWindow = new MainWindow();
				mainWindow.initModule(new SchuelerModule());
				mainWindow.displayGUI();
			}
		});
	}
}
