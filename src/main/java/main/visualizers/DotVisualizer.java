package main.visualizers;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.sorters.DotSorter;
import main.sorters.Sorter;
import main.ui.custimization.ColorButton;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.StorageValue;
import main.vcs.VisualComponent;
import main.visualizers.bases.Visualizer;

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
		for (int i = 0; i < size; i++)
		{
			DotSorter sorter = (DotSorter) sortingTool.getSorter();
			double height = array[i].getValue()/sorter.getInitialHeight() * sortingTool.getVisualizerHeight();
			double section = sortingTool.getVisualizerWidth()/(double)size;
			g.setColor(confirmed ? Algorithm.confirmationColor : getHighlightAt(i));
			Shape circle = new Arc2D.Double(section*i, sortingTool.getVisualizerHeight()-height, componentWidth, componentWidth, 0, 360, Arc2D.CHORD);
			g.fill(circle);
		}
	}

	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		SpinnerNumberModel nm = new SpinnerNumberModel(componentWidth, .05, 20, 1);
		cp.addRow("Dot size:", CustomizationGUI.createNumberJSpinner(sortingTool, nm, n -> componentWidth = n, () -> componentWidth, false), false);
		cp.addRow(ColorButton.createDefaultColorPickingButton(sortingTool, sortingTool.getSorter(identifier)), true);
		cp.addRow(CustomizationGUI.createMakePinkButton(sortingTool), true);
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
