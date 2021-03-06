package sorters;

import main.VisualSortingTool;
import ui.custimization.CustomizationGUI;
import ui.custimization.CustomizationPanel;
import ui.custimization.values.IntStorageValue;
import ui.custimization.values.StorageValue;
import ui.tooltips.ToolTips;
import vcs.VisualComponent;
import visualizers.NumberFixedVisualizer;

import javax.swing.*;

public class NumberSorter extends Sorter
{	
	/**
	 * a {@link Sorter} to use actual numbers to visualize sorting
	 */
	public NumberSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new NumberFixedVisualizer(sortingTool), Sorters.NUMBER);
	}
	
	/**
	 * adds customization spinner for the amount of numbers displayed
	 */
	@Override
	public void addSorterCustomizationComponents(CustomizationPanel cp)
	{
		SpinnerNumberModel nm = new SpinnerNumberModel(size, 10, 500, 1);
		JSpinner amountSpinner = CustomizationGUI.createNumberJSpinner(sortingTool, nm, n -> size = n, () -> size);
		amountSpinner.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.AMOUNT,nm));
		cp.addRow("# of Numbers:", amountSpinner);
	}
	
	@Override
	public void setDefaultValues()
	{
		size = 100;
	}
	
	@Override
	public void addStorageValues()
	{
		//sets up preferences for the amount of numbers
		StorageValue.addStorageValues(new IntStorageValue(getPrefix(), "amount", n -> size = n, () -> size));
	}
	
	@Override
	protected void reloadArray()
	{
		for(int i = 0; i < size; i++)
		{
			array[i] = new VisualComponent(i);
		}
	}
}