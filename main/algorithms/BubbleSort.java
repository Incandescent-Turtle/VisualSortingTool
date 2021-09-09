package main.algorithms;

import java.awt.Color;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.vcs.VisualComponent;

public class BubbleSort extends Algorithm
{	
	public BubbleSort(VisualSortingTool sortingTool)
	{
		super("Bubble Sort", sortingTool);
	}
	
	@Override
	public void run()
	{
		Sorter sorter = sortingTool.getSorter();
		final VisualComponent[] array = sorter.getArray();
		final int size = array.length;
		
		for(int i = 0; i < size-1; i++)
		{
			for(int j = 0; j < size-1-i; j++)
			{
				Color[] highlights = sorter.getVisualizer().getHighlights();
				highlights[j] = Color.GREEN;
				highlights[j+1] = Color.GREEN;
				if(array[j].getValue() > array[j+1].getValue())
				{
					sorter.swap(j, j+1);
					highlights[j] = Color.RED;
					highlights[j+1] = Color.RED;
				}
				delay(sorter);
				sortingTool.repaint();
			}
		}
		finishRun();
	}
}