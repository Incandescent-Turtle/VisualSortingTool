package sorters;

import main.VisualSortingTool;
import vcs.VisualComponent;
import visualizers.BarHeightVisualizer;

public class BarHeightSorter extends BarSorter
{
	//initial height of the visualization panel when sorting starts
	private int initialHeight;

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
		int height = sortingTool.getVisualizerHeight();
		int maxHeight = height - 20;
		//for height scaling purposes
		initialHeight = height;
		int minHeight = 15;
		//difference between two adjacent bars
		double step = (maxHeight - minHeight)/(float)size;

		for(int i = 0; i < size; i++)
		{
			array[i] = new VisualComponent(maxHeight - i*(step));
		}
	}

	public int getInitialHeight()
	{
		return initialHeight;
	}
}