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
		System.out.println("now bubble sorting");
		final int[] array = sorter.getArray();
		final int size = array.length;
		
		for(int i = 0; i < size-1; i++)
		{
			for(int j = 0; j < size-1-i; j++)
			{
				if(array[j] > array[j+1])
				{
					sorter.swap(j, j+1);
					Color[] highlights = sorter.getHighlights();
					highlights[j] = Color.RED;
					highlights[j+1] = Color.RED;
					try
					{
						Thread.sleep(sorter.getDelay());
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					panel.repaint();
				}
			}
		}
		for(int i = 0; i<size; i++)
		{
			if(i != size-1 && array[i]>array[i+1]) 
			{
				System.out.println("didnt sort properly");
			}
		}
		panel.repaint();
		System.out.println("done bubble sort");
		sorter.setAlgorithm(null);
	}
}