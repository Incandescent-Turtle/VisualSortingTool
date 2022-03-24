package algorithms;

import main.VisualSortingTool;
import sorters.Sorter;
import vcs.VisualComponent;

public class InsertionSort extends Algorithm
{
	public InsertionSort(VisualSortingTool sortingTool)
	{
		super("Insertion Sort", sortingTool);
	}

	@Override
	public void runAlgorithm()
	{
		Sorter sorter = sortingTool.getSorter();
		final VisualComponent[] arr = sorter.getArray();
		final int size = arr.length;

		for (int j = 1; j < size; j++) {
			var key = arr[j];
			int i = j-1;
			while (i > -1 && arr[i].getValue() > key.getValue()) {
				arr[i+1] = arr[i];
				paintWithDelayAndStep();
				resetHighlightsAt(i+1);
				i--;
				System.out.println(i);
			}
			arr[i+1] = key;
			paintWithDelayAndStep();
			resetHighlightsAt(i+1);
		}
	}
}