package de.timmalbers.dbMan.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.timmalbers.dbMan.db.Entry;
import de.timmalbers.dbMan.db.Result;
import de.timmalbers.dbMan.modules.AbstractModule;
import de.timmalbers.dbMan.scheme.TableScheme;

/**
 * The main window
 *
 * @author Timm Albers
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = 820651537166022872L;
	private final String TITLE = "DBMan";
	private final int DEFAULT_X = 100;
	private final int DEFAULT_Y = 100;
	private final int DEFAULT_WIDTH = 800;
	private final int DEFAULT_HEIGHT = 320;
	private ActionListener buttonActionListener;
	private KeyAdapter keyListener;
	private JMenuBar menuBar;
	private JPanel leftMenuPanel;
	private JPanel rightMenuPanel;
	private JButton newButton;
	private JButton saveButton;
	private JButton revertButton;
	private JButton deleteButton;
	private JButton searchButton;
	private JPanel contentPane;
	private GridBagConstraints constraints;
	private JList<Entry> list;
	private DefaultListModel<Entry> listModel;
	private JPanel entryContainer;
	private GridBagConstraints entryConstraints;
	private AbstractModule m;
	private Result r;

	public MainWindow() {
		initActionListener();
		initMenuBar();
		initContentPane();
		initFrame();
		initList();
		initEntryContainer();
	}
	
	private LinkedHashMap<String, String> createDefaultDataSet(TableScheme tableScheme) {
		LinkedHashMap<String, String> dataSet = new LinkedHashMap<>();
		
		String tableName = tableScheme.getTableName();
		LinkedHashMap<String, Object> attributes = tableScheme.getAttributes();
		Object[] attributeNames = attributes.keySet().toArray();
		
		for(int i = 0, j = attributeNames.length; i < j; i++) {
			String attributeName = (String) attributeNames[i];
			String[] attributeParts = attributeName.split("\\.");
			
			if(attributeParts.length > 0 && tableName.equals(attributeParts[0])) {
				dataSet.put(attributeName, tableScheme.getAttributeDefault(attributeName));
			} else {
				LinkedHashMap<String, String> tmp = createDefaultDataSet(tableScheme.getTableScheme(attributeParts[0]));
				Set<String> keys = tmp.keySet();
				
				for(String key : keys) {
					dataSet.put(key, tmp.get(key));
				}
			}
		}
		
		return dataSet;
	}
	
	private LinkedList<String> createDefaultLabels(TableScheme tableScheme) {
		LinkedList<String> labels = new LinkedList<>();
		
		String tableName = tableScheme.getTableName();
		LinkedHashMap<String, Object> attributes = tableScheme.getAttributes();
		Object[] attributeNames = attributes.keySet().toArray();
		
		for(int i = 0, j = attributeNames.length; i < j; i++) {
			String attributeName = (String) attributeNames[i];
			String[] attributeParts = attributeName.split("\\.");
			
			if(attributeParts.length > 0 && tableName.equals(attributeParts[0])) {
				labels.add(tableScheme.getAttributeName(attributeName));
			} else {
				LinkedList<String> tmp = createDefaultLabels(tableScheme.getTableScheme(attributeParts[0]));
				
				for(String label : tmp) {
					labels.add(label);
				}
			}
		}
		
		return labels;
	}
	
	/**
	 * Initializes all action listeners
	 */
	private void initActionListener() {
		buttonActionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String c = e.getActionCommand();
				
				if(c.equals("entry.new")) {
					Entry entry = new Entry(
							createDefaultDataSet(m.getTableScheme()),
							createDefaultLabels(m.getTableScheme()));
					
					try {
						m.getDB().insert(m.getTableScheme(), entry);
						r.getEntries(m).add(entry);
						
						listModel.clear();
						initModule(m);
						displayGUI();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "DBMan", JOptionPane.WARNING_MESSAGE);
					}
					
				} else if(c.equals("entry.save")) {
					Entry entry = r.getEntries(m).get(list.getSelectedIndex());
					Set<String> keys = entry.getKeySet();
					LinkedList<String> labels = new LinkedList<>();
					
					for(String key : keys) {
						labels.add(entry.getLabel(key));
					}
					
					Component[] comps = entryContainer.getComponents();
					
					String curKey = "";
					for(int i = 0, j = comps.length; i < j; i++) {
						if(i % 2 == 0) {
							int index = labels.indexOf(((JLabel) comps[i]).getText().replaceAll(":", ""));
							curKey = (String) keys.toArray()[index];
						} else {
							entry.set(curKey, ((JTextField) comps[i]).getText());
						}
					}
					
					m.initEntryLabel(entry);
					
					try {
						m.getDB().update(m.getTableScheme(), entry);
						list.updateUI();
						revertButton.setEnabled(false);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "DBMan", JOptionPane.WARNING_MESSAGE);
					}
				} else if(c.equals("entry.revert")) {
					displayEntry(r.getEntries(m).get(list.getSelectedIndex()));
				} else if(c.equals("entry.delete")) {
					Entry entry = r.getEntries(m).get(list.getSelectedIndex());
					
					try {
						m.getDB().delete(m.getTableScheme(), entry);
						
						r.getEntries(m).remove(list.getSelectedIndex());
						
						listModel.clear();
						initModule(m);
						displayGUI();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), "DBMan", JOptionPane.WARNING_MESSAGE);
					}
				} else if(c.equals("search")) {
					String searchString = JOptionPane.showInputDialog(null, "Syntax: name=value; Cancel to show all entries", "Search", JOptionPane.QUESTION_MESSAGE);
					filter(searchString);
				}
			}
		};
		
		keyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				revertButton.setEnabled(true);
			}
		};
	}
	
	/**
	 * Initializes the menu bar
	 */
	private void initMenuBar() {
		newButton = new JButton("New");
		saveButton = new JButton("Save");
		revertButton = new JButton("Revert");
		deleteButton = new JButton("Delete");
		searchButton = new JButton("Search");
		
		newButton.setActionCommand("entry.new");
		saveButton.setActionCommand("entry.save");
		revertButton.setActionCommand("entry.revert");
		deleteButton.setActionCommand("entry.delete");
		searchButton.setActionCommand("search");
		
		newButton.addActionListener(buttonActionListener);
		saveButton.addActionListener(buttonActionListener);
		revertButton.addActionListener(buttonActionListener);
		deleteButton.addActionListener(buttonActionListener);
		searchButton.addActionListener(buttonActionListener);
		
		revertButton.setEnabled(false);
		
		leftMenuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftMenuPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		leftMenuPanel.setPreferredSize(new Dimension(60, 40));
		leftMenuPanel.add(newButton);
		leftMenuPanel.add(searchButton);
		
		rightMenuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rightMenuPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		rightMenuPanel.setPreferredSize(new Dimension(440, 40));
		rightMenuPanel.add(saveButton);
		rightMenuPanel.add(revertButton);
		rightMenuPanel.add(deleteButton);
		rightMenuPanel.setVisible(false);
		
		menuBar = new JMenuBar();
		menuBar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
		menuBar.add(leftMenuPanel);
		menuBar.add(rightMenuPanel);
	}
	
	/**
	 * Initializes the content pane
	 */
	private void initContentPane() {
		constraints = new GridBagConstraints();
		
		contentPane = new JPanel(new GridBagLayout());
	}
	
	/**
	 * Initializes the main frame
	 */
	private void initFrame() {
		setTitle(TITLE);
		setBounds(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setResizable(false);
		setJMenuBar(menuBar);
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					m.getDB().close();
				} catch (IOException e1) {
				}
			}
		});
	}
	
	/**
	 * Initializes the list used to display entries
	 */
	private void initList() {
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		listModel = new DefaultListModel<>();
		list = new JList<>(listModel);
		list.setBorder(new MatteBorder(0, 0, 0, 1, new Color(200, 200, 200)));
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()) {
					rightMenuPanel.setVisible(true);
					displayEntry(list.getSelectedValue());
				}
			}
		});
		list.setCellRenderer(new ListCellRenderer<Entry>() {

			@Override
			public Component getListCellRendererComponent(
					JList<? extends Entry> list, Entry value, int index,
					boolean isSelected, boolean cellHasFocus) {
				
				JLabel label = new JLabel(value.toString());
				label.setOpaque(true);
				label.setBorder(new EmptyBorder(10, 10, 10, 10));
				label.setBackground(Color.WHITE);
				
				if(isSelected) {
					label.setBackground(new Color(240, 240, 240));
				}
				
				return label;
			}
		});
		
		scrollPane.setViewportView(list);
		contentPane.add(scrollPane, constraints);
	}
	
	/**
	 * Initializes the container used to display entries
	 */
	private void initEntryContainer() {
		constraints.weightx = 3;
		constraints.gridx = 1;
		
		entryContainer = new JPanel(new GridBagLayout());
		entryContainer.setBackground(Color.WHITE);
		
		JLabel label = new JLabel("Select an entry...");
		label.setForeground(Color.GRAY);
		entryContainer.add(label);
		
		entryConstraints = new GridBagConstraints();
		entryConstraints.fill = GridBagConstraints.HORIZONTAL;
		entryConstraints.anchor = GridBagConstraints.NORTH;
		entryConstraints.gridx = 0;
		entryConstraints.gridy = 0;
		entryConstraints.weightx = 3;
		entryConstraints.weighty = 1;
		entryConstraints.gridheight = 1;
		entryConstraints.insets = new Insets(10, 10, 10, 10);
		
		contentPane.add(entryContainer, constraints);
	}
	
	/**
	 * Displays the given entry
	 * 
	 * @param e The entry
	 */
	private void displayEntry(final Entry e) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				entryContainer.removeAll();
				revertButton.setEnabled(false);
				
				int index = 0;
				JLabel label;
				JTextField textField;
				for(String key : e.getKeySet()) {
					if(key.indexOf("hidden:") != 0) {
						label = new JLabel(e.getLabel(key) + ":");
						label.setBorder(new EmptyBorder(1, 1, 1, 1));
						
						entryConstraints.gridx = 0;
						entryConstraints.gridy = ++index;
						entryConstraints.weightx = 1;
						entryContainer.add(label, entryConstraints);
						
						textField = new JTextField(e.get(key));
						textField.setBorder(new MatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
						textField.addKeyListener(keyListener);
						
						entryConstraints.gridx = 1;
						entryConstraints.weightx = 3;
						entryContainer.add(textField, entryConstraints);
					}
				}
				
				entryContainer.updateUI();
			}
		});
	}
	
	/**
	 * Displays the window
	 */
	public void displayGUI() {
		setTitle(TITLE + ": " + m.getLabel());
		setVisible(true);
	}
	
	/**
	 * Initializes the given module
	 * 
	 * @param m The module
	 */
	public void initModule(AbstractModule m) {
		this.m = m;
		
		try {
			this.r = m.get();
			
			for(Entry e : r.getEntries(m)) {
				m.initEntryLabel(e);
			}
			
			for(Entry e : r.getEntries(m)) {
				listModel.addElement(e);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Filters the shown entries
	 * 
	 * @param search The search expression
	 */
	public void filter(String search) {
		LinkedList<Entry> entries = r.getEntries(m);
		listModel.clear();
		
		try {
			String label = search.split("=")[0].trim();
			String value = search.split("=")[1].trim();
			
			for(Entry e : entries) {
				boolean show = false;
				
				for(String key : e.getKeySet()) {
					if(key.indexOf("hidden:") != 0) {
						if(e.getLabel(key).toLowerCase().equals(label.toLowerCase()) &&
								e.get(key).toLowerCase().equals(value.toLowerCase())) {
							show = true;
						}
					}
				}
				
				if(show) {
					listModel.addElement(e);
				}
			}
		} catch(ArrayIndexOutOfBoundsException | NullPointerException e) {
			for(Entry entry : entries) {
				listModel.addElement(entry);
			}
		}
	}
}
