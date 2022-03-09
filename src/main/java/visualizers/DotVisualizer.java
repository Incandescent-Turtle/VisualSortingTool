package visualizers;

import algorithms.Algorithm;
import main.VisualSortingTool;
import sorters.DotSorter;
import sorters.Sorter;
import ui.custimization.ColorButton;
import ui.custimization.CustomizationGUI;
import ui.custimization.CustomizationPanel;
import ui.custimization.values.StorageValue;
import ui.tooltips.ToolTips;
import vcs.VisualComponent;
import visualizers.bases.Visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

public class DotVisualizer extends Visualizer
{
	/**
	 * a visualizer that uses scattered dots
	 * @param sortingTool
	 */
	public DotVisualizer(VisualSortingTool sortingTool)
	{
		super(sortingTool, Sorter.Sorters.DOT);
	}

	@Override
	public void setDefaultValues()
	{
		minMargin = 10;
		componentWidth = 10;
		defaultColor = new Color(144, 193, 215);
	}

	@Override
	protected void drawArray(Graphics2D g, VisualComponent[] array, int size)
	{
		DotSorter sorter = (DotSorter) sortingTool.getSorter();
		for (int i = 0; i < size; i++)
		{
			//scaled height so it fits the screen vertivally
			double height = array[i].getValue()/sorter.getInitialHeight() * sortingTool.getVisualizerHeight();
			//space inbetween dots
			double gap = sortingTool.getVisualizerWidth()/(double)size;
			g.setColor(confirmed ? Algorithm.confirmationColor : getHighlightAt(i));
			//the dot
			Shape circle = new Arc2D.Double(gap*i, sortingTool.getVisualizerHeight()-height, componentWidth, componentWidth, 0, 360, Arc2D.CHORD);
			g.fill(circle);
			//g.fillOval((int)(gap*i), (int)(sortingTool.getVisualizerHeight()-height), (int)componentWidth, (int)componentWidth);
		}
	}

	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		SpinnerNumberModel nm = new SpinnerNumberModel(componentWidth, .05, 20, 1);
		JSpinner dotSizeSpinner = CustomizationGUI.createNumberJSpinner(sortingTool, nm, n -> componentWidth = n, () -> componentWidth, false);
		dotSizeSpinner.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.SIZE, nm));
		cp.addRow("Dot size:", dotSizeSpinner, false);

		cp.addRow(ColorButton.createDefaultColorPickingButton(sortingTool, sortingTool.getSorter(identifier)), true);
		cp.addRow(CustomizationGUI.createMakePinkButton(sortingTool), true);
		super.addCustomizationComponents(cp);
	}

	@Override
	public void addStorageValues()
	{
		StorageValue.addStorageValues(
				createWidthStorageValue(),
				createDefaultColorStorageValue()
		);
	}

	@Override
	public double getComponentHeight()
	{
		return getComponentWidth();
	}

}