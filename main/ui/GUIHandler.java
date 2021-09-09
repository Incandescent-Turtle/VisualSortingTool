package main.ui;

import java.awt.Component;
import java.util.ArrayList;

import main.VisualSortingTool;
import main.ui.custimization.CustomizationGUI;

/**
 * On creation adds a top bar to the application with some options
 *
 */
public class GUIHandler
{
	private TopBarGUI topBar;
	private CustomizationGUI customizationGUI;
	private ArrayList<Component> toggleable = new ArrayList<>();
	
	public GUIHandler(VisualSortingTool sortingTool)
	{
		topBar = new TopBarGUI(sortingTool);
		customizationGUI = new CustomizationGUI(sortingTool);
	}
	
	public void setUp()
	{
		topBar.setUp();
	}
	
	public TopBarGUI getTopBarGUI()
	{
		return topBar;
	}
	
	public CustomizationGUI getCustimizationPanel()
	{
		return customizationGUI;
	}
	
	public void setEnabled(boolean enabled)
	{
		for(Component c : toggleable)
		{
			c.setEnabled(enabled);
		}
	}
	
	public void addToggleable(Component... components)
	{
		for(Component c : components)
		{
			toggleable.add(c);
		}
	}
}