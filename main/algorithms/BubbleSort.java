package main.algorithms;

import java.awt.Color;

import main.SortingVisualizer;

public class BubbleSort extends Algorithm
{	
	public BubbleSort(SortingVisualizer visualizer)
	{
		super("Bubble Sort", visualizer);
	}
	
	@Override
	public void run()
	{
		final int[] array = sorter.getArray();
		final int size = array.length;
		
		for(int i = 0; i < size-1; i++)
		{
			for(int j = 0; j < size-1-i; j++)
			{
				Color[] highlights = sorter.getHighlights();
				highlights[j] = Color.RED;
				highlights[j+1] = Color.RED;
				if(array[j] > array[j+1]) sorter.swap(j, j+1);
				delay(sorter);
				visualizer.repaint();
			}
		}
		finishRun();
		System.out.println("done check");
	}
}