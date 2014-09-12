package de.timmalbers.dbMan.gui;

import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 820651537166022872L;
	private final String TITLE = "DBMan";
	private final int DEFAULT_WIDTH = 800;
	private final int DEFAULT_HEIGHT = 600;
	private JPanel contentPane;
	private JList<String> list;

	public MainWindow() {
		initContentPane();
		initFrame();
		initList();
	}
	
	private void initContentPane() {
		contentPane = new JPanel();
	}
	
	private void initFrame() {
		setTitle(TITLE);
		setBounds(new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		setContentPane(contentPane);
	}
	
	private void initList() {
		list = new JList<>();
		
		list.setVisible(true);
		
		contentPane.add(list);
	}
	
	public void displayGUI() {
		setVisible(true);
	}
}
