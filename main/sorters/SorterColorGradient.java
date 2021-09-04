package main.sorters;

import java.util.Random;

import main.VisualSortingTool;
import main.vcs.VisualComponent;
import main.visualizers.VisualizerColorGradient;

public class SorterColorGradient extends Sorter
{
	public SorterColorGradient(VisualSortingTool sortingTool)
	{
		super(sortingTool, new VisualizerColorGradient(sortingTool), "Color Gradient");
	}

	/**
	 * resizes based on window size and bar sizes/gaps
	 */
	@Override
	protected void resizeArray()
	{
		int barWidth = visualizer.getComponentWidth();
		int barGap = visualizer.getComponentGap();
		size = (sortingTool.getWidth() - visualizer.getMinMargin()*2 + barGap)/(barWidth+barGap);
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