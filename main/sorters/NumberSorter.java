package main.sorters;

import javax.swing.SpinnerNumberModel;

import main.VisualSortingTool;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.CustomizationPanel;
import main.vcs.VisualComponent;
import main.visualizers.NumberFixedVisualizer;

public class NumberSorter extends Sorter
{
	/**
	 * a {@link Sorter} to use actual numbers to visualize sorting
	 */
	public NumberSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new NumberFixedVisualizer(sortingTool), Sorters.NUMBER);
		size = 100;
	}
	
	/**
	 * adds customization spinner for the amount of numbersdisplayed
	 */
	@Override
	public void addSorterCustomizationComponents(CustomizationPanel cp)
	{
		SpinnerNumberModel nm = new SpinnerNumberModel(size, 10, 500, 1);
		cp.addRow("# of Numbers:", CustomizationGUI.createJSpinner(sortingTool, nm, n -> size = n));	
	}
	
	@Override
	protected void reloadArray()
	{
		for(int i = 0; i < size; i++)
		{
			array[i] = new VisualComponent(i);
			visualizer.getHighlights()[i] = visualizer.getDefaultColor();
		}
	}
}