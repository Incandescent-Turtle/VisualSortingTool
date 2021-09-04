package main.sorters;

import main.VisualSortingTool;
import main.vcs.VisualComponent;
import main.visualizers.VisualizerNumber;

public class SorterNumber extends Sorter
{
	public SorterNumber(VisualSortingTool sortingTool)
	{
		super(sortingTool, new VisualizerNumber(sortingTool), "Numbers");
		size = 100;
	}
	
	@Override
	protected void reloadArray()
	{
		for(int i = 0; i < size; i++)
		{
			array[i] = new VisualComponent(i);
			visualizer.getHighlights()[i] = visualizer.getDefaultColor();
		}
	}
}
