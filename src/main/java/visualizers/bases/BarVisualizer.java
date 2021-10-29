package visualizers.bases;

import main.VisualSortingTool;
import sorters.Sorter;
import ui.custimization.CustomizationGUI;
import ui.custimization.CustomizationPanel;
import ui.custimization.values.StorageValue;
import ui.tooltips.ToolTips;
import vcs.VisualComponent;

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
		JSpinner gapSpinner = CustomizationGUI.createNumberJSpinner(sortingTool, nm, n -> componentGap = n, () -> componentGap);
		gapSpinner.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.BAR_GAP, nm));
		cp.addRow("Gap:", gapSpinner);

		addMarginSpinner(cp);
		super.addCustomizationComponents(cp);
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
