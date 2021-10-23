package main.visualizers.bases;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.sorters.Sorter;
import main.ui.custimization.ColorButton;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.StorageValue;
import main.vcs.VisualComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class BarVisualizer extends Visualizer
{

	public BarVisualizer(VisualSortingTool sortingTool, Sorter.Sorters identifier)
	{
		super(sortingTool, identifier);
	}

	@Override
	public void setDefaultValues()
	{
		componentWidth = 15;
		componentGap = 2;
		minMargin = 2;
	}

	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		SpinnerNumberModel nm = new SpinnerNumberModel(componentGap, 0, 20, 1);
		//spinner to change gap between bars
		cp.addRow("Gap:", CustomizationGUI.createDoubleJSpinner(sortingTool, nm, n -> componentGap = n, () -> componentGap));

		addMarginSpinner(cp);

		//change background button
		cp.addRow(ColorButton.createBackgroundColorPickingButton(sortingTool), true);
	}

	@Override
	public void addStorageValues()
	{
		//setting up specified values with preferences
		StorageValue.addStorageValues(
				createGapStorageValue(),
				createMarginStorageValue()
		);
	}

	/**
	 * draws bars of different heights using the values as the height input
	 */
	@Override
	public void drawArray(Graphics2D g, VisualComponent[] array, int size)
	{
		componentWidth = (sortingTool.getVisualizerWidth()-minMargin*2-componentGap*(size-1))/size;
		for(int i = 0; i < size; i++)
		{
			//highlights in specified color
			g.setColor(confirmed ? Algorithm.confirmationColor : highlights[i]);
		}

		for(int i = 0; i < size; i++)
		{
			double x = getRealHMargins(size) + i*(componentWidth + componentGap);
			//bars fill the height of the screen
			drawComponent(g, x, array, i);
		}
	}

	/**
	 * this method is called for every bar that needs to be rendered.
	 * use {@link #drawBar(Graphics2D, double, double)} to actually draw them
	 * @param g the graphics object to draw with
	 * @param x the x pos the bar is located
	 * @param array the VC array from the sorter
	 * @param index the index of this bar in the VC array
	 */
	protected abstract void drawComponent(Graphics2D g, double x, VisualComponent[] array, int index);

	/**
	 * for drawing individual bars at the specified x coord
	 * @param g graphics object that will be used to draw
	 * @param x the x pos to put the bar at
	 * @param height the height of the bar
	 */
	protected final void drawBar(Graphics2D g, double x, double height)
	{
		g.fill(new Rectangle2D.Double(x, sortingTool.getVisualizerHeight()-height, componentWidth, height));
	}
}
