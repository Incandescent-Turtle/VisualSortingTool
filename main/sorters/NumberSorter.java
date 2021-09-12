package main.sorters;

import java.util.prefs.Preferences;

import javax.swing.SpinnerNumberModel;

import main.VisualSortingTool;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.CustomizationPanel;
import main.vcs.VisualComponent;
import main.visualizers.NumberFixedVisualizer;

public class NumberSorter extends Sorter
{
	//key for getting/putting values in preferences
	private static final String AMOUNT = "amount";
	
	/**
	 * a {@link Sorter} to use actual numbers to visualize sorting
	 */
	public NumberSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new NumberFixedVisualizer(sortingTool), Sorters.NUMBER);
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
	public void loadValues(Preferences prefs, String prefix)
	{
		size = prefs.getInt(prefix + AMOUNT, 100);
	}
	
	@Override
	public void storeValues(Preferences prefs, String prefix)
	{
		prefs.putInt(prefix + AMOUNT, size);
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