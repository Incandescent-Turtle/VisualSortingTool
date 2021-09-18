package main.visualizers;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.SpinnerNumberModel;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.sorters.Sorter.Sorters;
import main.ui.custimization.ColorButton;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.StorageValue;
import main.vcs.VisualComponent;
import main.visualizers.bases.Visualizer;

public class BarHeightVisualizer extends Visualizer
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
		componentWidth = 15;
		componentGap = 2;
		minMargin = 2;
		defaultColor = new Color(144, 193, 215);
	}
	
	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		SpinnerNumberModel nm = new SpinnerNumberModel(componentWidth, 2, 100, 1);
		//spinner to change bar width
		cp.addRow("Bar Width:", CustomizationGUI.createJSpinner(sortingTool, nm, n -> componentWidth = n, () -> componentWidth));
		
		nm = new SpinnerNumberModel(componentGap, 0, 20, 1);
		//spinner to change gap between bars
		cp.addRow("Gap:", CustomizationGUI.createJSpinner(sortingTool, nm, n -> componentGap = n, () -> componentGap));
		
		nm = new SpinnerNumberModel(minMargin, 0, 100, 1);
		//spinner to change left/right margin
		cp.addRow("Margin:", CustomizationGUI.createJSpinner(sortingTool, nm, n -> minMargin = n, () -> minMargin));
		
		//default color button
		cp.addRow(ColorButton.createDefaultColorPickingButton(sortingTool, sortingTool.getSorter(identifier)), true);
		//change background button
		cp.addRow(ColorButton.createBackgroundColorPickingButton(sortingTool), true);
		//sets default color to pink button
		cp.addRow(CustomizationGUI.createMakePinkButton(sortingTool), true);
	}
	
	@Override
	public void addStorageValues()
	{	
		//setting up specified values with preferences
		StorageValue.addStorageValues(
				createWidthStorageValue(),
				createGapStorageValue(),
				createMarginStorageValue(),
				createDefaultColorStorageValue()
		);
	}
	
	/**
	 * draws bars of different heights using the values as the height input
	 */
	@Override
	public void drawArray(Graphics g, VisualComponent[] array, int size)
	{			
		for(int i = 0; i < size; i++)
		{
			//highlights in specified color
			g.setColor(confirmed ? Algorithm.confirmationColor : highlights[i]);
			g.fillRect(getRealHMargins(size) + i*(componentWidth+componentGap), sortingTool.getVisualizerHeight()-array[i].getValue(), componentWidth, array[i].getValue());
			//resets the highlights array
			highlights[i] = defaultColor; 
		}
	}
}