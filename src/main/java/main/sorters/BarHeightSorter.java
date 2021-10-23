package main.sorters;

import main.VisualSortingTool;
import main.vcs.VisualComponent;
import main.visualizers.BarHeightVisualizer;

import javax.swing.*;

public class BarHeightSorter extends BarSorter
{

	private JSpinner barAmountSpinner;
	/**
	 * a {@link Sorter} to use different height of bars as a visualizer
	 */
	public BarHeightSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new BarHeightVisualizer(sortingTool), Sorters.BAR_HEIGHT);
	}

	/*
	 * based on the array size and height of window creates values that create a linear slope
	 */
	@Override
	protected void reloadArray()
	{
		int maxHeight = sortingTool.getVisualizerHeight() - 20;
		int minHeight = 15;
		//difference between two adjacent bars
		double step = (maxHeight - minHeight)/(float)size;

		for(int i = 0; i < size; i++)
		{
			array[i] = new VisualComponent(maxHeight - i*(step));
		}
	}
}