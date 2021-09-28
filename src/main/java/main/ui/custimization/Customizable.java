package main.ui.custimization;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.sorters.Sorter;

/**
 *	for things such as {@link Sorter}s and {@link Algorithm}s that have their own
 *	{@link CustomizationPanel}s <br>
 */
public interface Customizable
{
	/**
	 * to add components to the side bar to change values
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