package main.ui;

import java.awt.Component;
import java.util.ArrayList;

import main.VisualSortingTool;
import main.ui.custimization.CustomizationGUI;

public class GUIHandler
{
	private TopBarGUI topBar;
	private CustomizationGUI customizationGUI;
	//all components across any GUI that need to be disabled when algorithm is running
	private static final ArrayList<Component> TOGGLEABLE = new ArrayList<>();
	
	/**
	 * holds the customization menus and the top utility bar
	 */
	public GUIHandler() {}
	
	/**
	 * creates and initializes the GUIS
	 */
	public void init(VisualSortingTool sortingTool)
	{
		topBar = new TopBarGUI(sortingTool);
		customizationGUI = new CustomizationGUI();
		customizationGUI.init(sortingTool);
		topBar.init();
	}
	
	public TopBarGUI getTopBarGUI()
	{
		return topBar;
	}
	
	public CustomizationGUI getCustomizationGUI()
	{
		return customizationGUI;
	}
	
	/**
	 * enables/disables all of {@link #TOGGLEABLE}
	 * @param enabled whether to enable or disable the components
	 */
	public static void setEnabled(boolean enabled)
	{
		for(Component c : TOGGLEABLE)
		{
			c.setEnabled(enabled);
		}
	}
	
	/**
	 * @param components all of the components to add to {@link #TOGGLEABLE}
	 */
	public static void addToggleable(Component... components)
	{
		for(Component c : components)
		{
			TOGGLEABLE.add(c);
		}
	}
}