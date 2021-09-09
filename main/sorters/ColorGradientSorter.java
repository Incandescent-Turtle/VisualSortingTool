package main.sorters;

import java.util.Random;

import main.VisualSortingTool;
import main.ui.custimization.CustomizationPanel;
import main.vcs.VisualComponent;
import main.visualizers.ColorGradientVisualizer;

public class ColorGradientSorter extends Sorter
{
	public ColorGradientSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new ColorGradientVisualizer(sortingTool), "Color Gradient");
	}

	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		
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