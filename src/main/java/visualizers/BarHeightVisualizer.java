package visualizers;

import main.VisualSortingTool;
import algorithms.Algorithm;
import sorters.BarHeightSorter;
import sorters.Sorter.Sorters;
import ui.custimization.ColorButton;
import ui.custimization.CustomizationGUI;
import ui.custimization.CustomizationPanel;
import ui.custimization.values.StorageValue;
import vcs.VisualComponent;
import visualizers.bases.BarVisualizer;

import java.awt.*;

public class BarHeightVisualizer extends BarVisualizer
{	
	/**
	 * visualizes sorting via numbers corresponding to the height of bars
	 */
	public BarHeightVisualizer(VisualSortingTool sortingTool)
	{
		super(sortingTool, Sorters.BAR_HEIGHT);
	}
	
	@Override
	public void setDefaultValues()
	{
		super.setDefaultValues();
		defaultColor = new Color(144, 193, 215);
	}
	
	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		super.addCustomizationComponents(cp);
		cp.addRow(ColorButton.createDefaultColorPickingButton(sortingTool, sortingTool.getSorter(identifier)), true);
		cp.addRow(CustomizationGUI.createMakePinkButton(sortingTool), true);
		cp.addRow(ColorButton.createBackgroundColorPickingButton(sortingTool), true);
	}
	
	@Override
	public void addStorageValues()
	{
		super.addStorageValues();
		//setting up specified values with preferences
		StorageValue.addStorageValues(
				createDefaultColorStorageValue()
		);
	}

	//called for each bar
	@Override
	protected void drawComponent(Graphics2D g, double x, VisualComponent[] array, int i)
	{
		g.setColor(confirmed ? Algorithm.confirmationColor : getHighlightAt(i));
		BarHeightSorter sorter = (BarHeightSorter) sortingTool.getSorter(Sorters.BAR_HEIGHT);
		//drawing this bar
		drawBar(g, x, array[i].getValue()/sorter.getInitialHeight() * sortingTool.getVisualizerHeight());
	}
}