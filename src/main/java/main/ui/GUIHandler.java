package main.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;

import main.VisualSortingTool;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.Updatable;

public class GUIHandler
{
	//updated when customization values are reset
	private static final ArrayList<Updatable> UPDATABLES = new ArrayList<>();
	//all components across any GUI that need to be disabled when algorithm is running
	private static final ArrayList<Component> TOGGLEABLE = new ArrayList<>();
	
	private TopBarGUI topBar;
	private CustomizationGUI customizationGUI;
	
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
		TOGGLEABLE.addAll(Arrays.asList(components));
	}
	
	/**
	 * updates all updatables
	 */
	public static void update()
	{
		UPDATABLES.forEach(Updatable::update);
	}
	
	public static void addUpdatables(Updatable... updatables)
	{
		UPDATABLES.addAll(Arrays.asList(updatables));
	}
}