package ui.custimization;

import main.VisualSortingTool;
import algorithms.Algorithm;
import sorters.Sorter;

/**
 *	for things such as {@link Sorter}s and {@link Algorithm}s that have their own {@link CustomizationPanel}s
 */
public interface Customizable
{
	/**
	 * to add components to the sidebar to change values
	 * @param cp the customization Panel
	 */
	void addCustomizationComponents(CustomizationPanel cp);
	void addStorageValues();
	void setDefaultValues();
	
	default String getPrefix()
	{
		return VisualSortingTool.getPrefix(this.getClass());
	}
}