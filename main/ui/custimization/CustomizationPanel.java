package main.ui.custimization;

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
import main.ui.GUIHandler;
import main.ui.custimization.CustomizationGUI.Customizable;

@SuppressWarnings("serial")
public class CustomizationPanel extends JPanel
{
	protected VisualSortingTool sortingTool;

	protected GridBagConstraints c = new GridBagConstraints();
	protected CustomizationGUI customizer;

	//the current row this panel is on
	protected int row = 0;
	
	/**
	 * 
	 * @param sortingTool
	 * @param customizable
	 */
	public CustomizationPanel(VisualSortingTool sortingTool, Customizable customizable)
	{
		super(new GridBagLayout());
		this.sortingTool = sortingTool;
		//padding
		c.insets = new Insets(1,5,1,3);
		//adds components to the frame
		customizable.addCustomizationComponents(this);
	}
	
	/**
	 * creates an "underlined" label to denote category
	 * @param text the title to display
	 * @param hasVertGap whether this should be spaced out from whatever is above it
	 */
	public void addTitleSeperator(String text, boolean hasVertGap)
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
		//adds space/underline below
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
	
	/**
	 * creates a new row and adds a singular component to it
	 * @param component probably a button - something self explanatory
	 * @param center whether it should be centered or right aligned
	 */
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
        //sortingTool.getGUIHandler().addToggleable(component);
        row++;
	}
	
	/**
	 * creates a new row and adds a right-aligned componenet with a label to its left
	 * @param labelText the text to be on the label
	 * @param right whatever the label is describing
	 */
	public void addRow(String labelText, Component right)
	{
		//placing label
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
        //placing right-aligned componenet
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.gridx = 1;
        c.gridy = row;
        c.fill = GridBagConstraints.NONE;
        c.anchor =  GridBagConstraints.NORTHEAST;
        add(right, c);
        //so it can be disabled when an algoroithm is running
        GUIHandler.addToggleable(right);
        row++;
	}
}