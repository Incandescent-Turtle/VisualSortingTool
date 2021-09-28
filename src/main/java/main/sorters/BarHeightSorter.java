package main.sorters;

import main.VisualSortingTool;
import main.vcs.VisualComponent;
import main.visualizers.BarHeightVisualizer;

public class BarHeightSorter extends Sorter
{
	/**
	 * a {@link Sorter} to use different height of bars as a visualizer
	 */
	public BarHeightSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new BarHeightVisualizer(sortingTool), Sorters.BAR_HEIGHT);
	}
	
	/**
	 * <ul>
	 * 	<li>changes the <b>size</b> variable based on window size</li>
	 *	<li>resizes <b>array</b> and <b>highlights</b> accordingly</li>
	 *	 * </ul>  
	 */
	@Override
	protected void resizeArray()
	{
		int barWidth = visualizer.getComponentWidth();
		int barGap = visualizer.getComponentGap();
		size = (sortingTool.getVisualizerWidth() - visualizer.getMinMargin()*2 + barGap)/(barWidth+barGap);
		if(size <=0 ) size = 10;
		super.resizeArray();
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
		int step = (maxHeight - minHeight)/size;
		/* 
		 	steps have to be whole numbers, so its not possible to have a proper range between min and max
		 	so this counts down from max height so at least the screen is filled
		 */
		for(int i = 0; i < size; i++)
		{
			array[i] = new VisualComponent(maxHeight - i*(step));
		}
	}
}