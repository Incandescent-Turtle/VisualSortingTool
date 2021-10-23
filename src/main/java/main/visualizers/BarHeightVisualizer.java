package main.visualizers;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.sorters.Sorter.Sorters;
import main.ui.custimization.ColorButton;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.StorageValue;
import main.vcs.VisualComponent;
import main.visualizers.bases.BarVisualizer;

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
		g.setColor(confirmed ? Algorithm.confirmationColor : highlights[i]);
		//drawing this bar
		drawBar(g, x, array[i].getValue());
	}
}