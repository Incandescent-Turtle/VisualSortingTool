package main.algorithms;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.vcs.VisualComponent;

import java.awt.*;

public class BubbleSort extends Algorithm
{		
	public BubbleSort(VisualSortingTool sortingTool)
	{
		super("Bubble Sort", sortingTool);
	}
	
	@Override
	public void setDefaultValues()
	{
		swapColor = new Color(255, 105, 97); //red
		compareColor = new Color(150, 210, 148); //green
	}

	@Override
	public void runAlgorithm()
	{
		Sorter sorter = sortingTool.getSorter();
		final VisualComponent[] array = sorter.getArray();
		final int size = array.length;
		//loops through array (final element is always going to be sorted by the end)
		for(int i = 0; i < size-1; i++)
		{
			//loops through what hasn't already been sorted
			for(int j = 0; j < size-1-i; j++)
			{
				resetHighlights();
				highlight(j, compareColor);
				highlight(j+1, compareColor);
				//comparing adjacent components
				if(array[j].getValue() > array[j+1].getValue())
				{
					sorter.swap(j, j+1);
					highlight(j, swapColor);
					highlight(j+1, swapColor);
				}
				paintWithDelayAndStep();
			}
		}
	}
}