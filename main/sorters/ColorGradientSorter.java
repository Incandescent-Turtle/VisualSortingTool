package main.sorters;

import java.util.Random;

import main.VisualSortingTool;
import main.vcs.VisualComponent;
import main.visualizers.ColorGradientVisualizer;

public class ColorGradientSorter extends Sorter
{
	/**
	 * a {@link Sorter} to use a blue color gradient to visualize sorting
	 */
	public ColorGradientSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new ColorGradientVisualizer(sortingTool), Sorters.COLOR_GRADIENT);
	}
	
	/**
	 * resizes based on window size and bar sizes/gaps
	 */
	@Override
	protected void resizeArray()
	{
		int barWidth = visualizer.getComponentWidth();
		int barGap = visualizer.getComponentGap();
		size = (sortingTool.getVisualizerWidth() - visualizer.getMinMargin()*2 + barGap)/(barWidth+barGap);
		//size was -1 when visualizer had 0 width...this solved it
		if(size <= 0) size = 10;
		array = new VisualComponent[size];
		visualizer.resizeHighlights(size);
		sortingTool.repaint();
	}
	
	/**
	 * fills array with random values
	 */
	@Override
	protected void reloadArray()
	{
		for(int i = 0; i < size; i++)
		{
			Random rand = new Random();
			array[i] = new VisualComponent(rand.nextInt(600) + 15);
			visualizer.getHighlights()[i] = visualizer.getDefaultColor();
		}
	}
}