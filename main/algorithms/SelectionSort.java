package main.algorithms;

import java.awt.Color;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.vcs.VisualComponent;

public class SelectionSort extends Algorithm
{	
	public SelectionSort(VisualSortingTool sortingTool)
	{
		super("Selection Sort", sortingTool);
		swapColor = Color.GREEN;
		 compareColor = Color.RED;
	}

	@Override
	public void runAlgorithm()
	{
		Sorter sorter = sortingTool.getSorter();
		final VisualComponent[] array = sorter.getArray();
		final int size = array.length;
		  
        for (int i = 0; i < size - 1; i++)  
        {
            int index = i;  
            for (int j = i + 1; j < size; j++)
            {
				sorter.getVisualizer().resetHighlights();
				sorter.highlight(j, compareColor);
				sorter.highlight(index, compareColor);
                if(array[j].getValue() < array[index].getValue())
                {
                	sorter.highlight(index, swapColor);
                    sorter.highlight(j, swapColor);
                    index = j;//searching for lowest index  
                }  
                delay(sorter);
                sortingTool.repaint();
            }  
            sorter.highlight(index, swapColor);
            sorter.highlight(i, swapColor);
            sorter.swap(index, i);
            delay(sorter);
            sortingTool.repaint();
        }  
	}
}