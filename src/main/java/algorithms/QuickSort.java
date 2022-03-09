package algorithms;

import main.VisualSortingTool;
import vcs.VisualComponent;

//recursion! divides array into 2 repetitively
public class QuickSort extends Algorithm
{
	public QuickSort(VisualSortingTool sortingTool)
	{
		super("Quick Sort", sortingTool);
	}

	@Override
	public void runAlgorithm()
	{
		VisualComponent[] array = sortingTool.getSorter().getArray();
		quicksort(array, 0, array.length-1);
	}

	private void quicksort(VisualComponent[] array, int low, int high)
	{
		int i = low, j = high;
		//	middle pivot
		int pivotIndex = low + (high-low);

		// Divide into two lists
		while (i <= j)
		{
			while(array[i].getValue() < array[pivotIndex].getValue())
			{
				compare(i, pivotIndex);
				i++;
				paintWithDelayAndStep();
				resetHighlightsAt(i-1, pivotIndex);
			}

			while (array[j].getValue() > array[pivotIndex].getValue())
			{
				compare(j, pivotIndex);
				j--;
				paintWithDelayAndStep();
				resetHighlightsAt(j+1, pivotIndex);
			}

			boolean swap = false;
			if (i <= j)
			{
				swap = true;
				swap(i, j);
				i++;
				j--;
			}
			if(swap)
			{
				paintWithDelayAndStep();
				resetHighlightsAt((i-1), (j+1));
			}
		}
		// Recursion
		if (low < j)
			quicksort(array, low, j);
		if (i < high)
			quicksort(array, i, high);
	}
}