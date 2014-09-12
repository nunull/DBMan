package de.timmalbers.dbMan;

import java.io.IOException;
import java.util.LinkedHashMap;

import de.timmalbers.dbMan.db.Result;
import de.timmalbers.dbMan.gui.MainWindow;
import de.timmalbers.dbMan.modules.AbstractModule;
import de.timmalbers.dbMan.modules.SchuelerModule;

public class Main {
	
	private static String fillString(String s) {
		for(int i = 24 - s.length(); i > 0; i--) s += " ";
		
		return s;
	}

	/**
	 * @param args ignored
	 */
	public static void main(String[] args) {
		AbstractModule m = new SchuelerModule();

		try {
			Result r = m.get();
			
			String moduleLabel = m.getLabel();
			System.out.println(moduleLabel);
			for(int i = 0, j = moduleLabel.length(); i < j; i++) {
				System.out.print("-");
			}
			System.out.println("\n");
			
			for(String label : r.getLabels()) {
				System.out.print(fillString(label.toUpperCase()));
			}
			System.out.println();
			
			int numCols = 0;
			for(String attribute : r.getAttributes()) {
				System.out.print(fillString("(" + attribute + ")"));
				numCols++;
			}
			System.out.println();
			
			for(int i = 0, j = numCols*24; i < j; i++) {
				System.out.print("-");
			}
			System.out.println("\n");
			
			for(LinkedHashMap<String, String> dataSet : r.getDataSets()) {
				for(String key : dataSet.keySet()) {
					System.out.print(fillString(dataSet.get(key)));
				}
				
				System.out.println();
			}
			
			MainWindow mainWindow = new MainWindow();
			mainWindow.displayGUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
