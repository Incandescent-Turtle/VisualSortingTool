package main.algorithms;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import main.Sorter;

public class BubbleSort extends Algorithm
{
	Timer timer;
	
	public BubbleSort(Sorter sorter)
	{
		super("Bubble Sort", sorter);
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
					int temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
					Color[] highlights = sorter.getHighlights();
					highlights[j] = Color.RED;
					highlights[j+1] = Color.RED;
					try
					{
						Thread.sleep(2);
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sorter.repaint();
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
		sorter.repaint();
		System.out.println("done bubble sort");
		sorter.setAlgorithm(null);
	}

	@Override
	public String getName()
	{
		return "BubbleSort";
	}
}