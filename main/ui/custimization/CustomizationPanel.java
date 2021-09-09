package main.ui.custimization;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import main.VisualSortingTool;
import main.sorters.Sorter;

@SuppressWarnings("serial")
public class CustomizationPanel extends JPanel
{
	protected GridBagConstraints c = new GridBagConstraints();
	protected int row = 0;
	protected VisualSortingTool sortingTool;
	protected CustomizationGUI customizer;
	protected Sorter sorter;
	
	public CustomizationPanel(VisualSortingTool sortingTool, Sorter sorter)
	{
		super(new GridBagLayout());
		this.sortingTool = sortingTool;
		this.sorter = sorter;
		c.insets = new Insets(1,5,1,3);
		addTitleSeperator(c, "Custimization", false);
		sorter.addCustomizationComponents(this);
		sorter.getVisualizer().addCustomizationComponents(this);
		fillBottom();
	}
		
	private final void fillBottom()
	{
		c.gridx = 0;
        c.gridy = row;
        c.weighty = 1;
        c.weightx = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.ipady = 0;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.fill = GridBagConstraints.VERTICAL;
        JLabel fill = new JLabel();
        fill.setBackground(Color.GRAY);
        fill.setOpaque(true);
        add(fill, c);
	}
	
	public void addTitleSeperator(GridBagConstraints c, String text, boolean hasVertGap)
	{
		if(hasVertGap)
		{
	        addRow(Box.createVerticalStrut(3), true);
			JSeparator vertGap = new JSeparator(SwingConstants.HORIZONTAL);
			c.gridx = 0;
	        c.gridy = row;
	        c.weighty = 0;
	        c.weightx = 1;
	        c.gridheight = 1;
	        c.gridwidth = 2;
	        c.ipady = 20;
	        c.anchor = GridBagConstraints.FIRST_LINE_START;
	        c.fill = GridBagConstraints.HORIZONTAL;
	        row++;
			add(vertGap, c);
		}
		JLabel label = new JLabel(text);
		addRow(label, true);
		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		sep.setPreferredSize(new Dimension(20,5));
		sep.setVisible(true);
		c.gridx = 0;
        c.gridy = row;
        c.weighty = 0;
        c.weightx = 1;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.ipady = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        row++;
        add(sep, c);
	}
	
	public void addRow(Component component, boolean center)
	{
		c.gridx = 0;
        c.gridy = row;
        c.weighty = 0;
        c.weightx = 1;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.ipady = 0;
        c.anchor = center ? GridBagConstraints.CENTER : GridBagConstraints.NORTHEAST;
        c.fill = GridBagConstraints.VERTICAL;
        add(component, c);
        row++;
	}
	
	public void addRow(String labelText, Component right)
	{
		c.gridx = 0;
        c.gridy = row;
        c.weighty = 0;
        c.weightx = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.ipady = 0;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.fill = GridBagConstraints.VERTICAL;
        add(new JLabel(labelText), c);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.gridx = 1;
        c.gridy = row;
        c.fill = GridBagConstraints.NONE;
        c.anchor =  GridBagConstraints.NORTHEAST;
        add(right, c);
        row++;
	}
}