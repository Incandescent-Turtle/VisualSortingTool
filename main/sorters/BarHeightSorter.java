package main.sorters;

import main.VisualSortingTool;
import main.vcs.VisualComponent;
import main.visualizers.BarHeightVisualizer;

public class BarHeightSorter extends Sorter
{
	public BarHeightSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new BarHeightVisualizer(sortingTool), "Bar Heights");
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
		array = new VisualComponent[size];
		visualizer.resizeHighlights(size);
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
		for(int i = 0; i < size; i++)
		{
			array[i] = new VisualComponent(minHeight + i*(step));
		}
	}
}