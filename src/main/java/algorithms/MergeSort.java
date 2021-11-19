package algorithms;

import main.VisualSortingTool;
import vcs.VisualComponent;

public class MergeSort extends Algorithm
{
	public MergeSort(VisualSortingTool sortingTool)
	{
		super("Merge Sort", sortingTool);
	}

	@Override
	public void runAlgorithm()
	{
		VisualComponent[] array = sortingTool.getSorter().getArray();
		mergeSort(array, array.clone(), 0);
	}

	public void mergeSort(VisualComponent[] mainArray, VisualComponent[] array, int startIndex)
	{
		if (array.length < 2) return;

		int mid = array.length / 2;

		VisualComponent[] leftArray = new VisualComponent[mid];
		VisualComponent[] rightArray = new VisualComponent[array.length - mid];

		for (int i = 0; i < mid; i++)
			leftArray[i] = array[i];

		for (int i = mid; i < array.length; i++)
			rightArray[i - mid] = array[i];

		mergeSort(mainArray, leftArray, startIndex);
		mergeSort(mainArray, rightArray, mid+startIndex);

		merge(mainArray, array, leftArray, rightArray, startIndex);
	}

	public void merge(VisualComponent[] mainArray, VisualComponent[] array, VisualComponent[] leftArray, VisualComponent[] rightArray, int startIndex)
	{
		int leftStart = 0;
		int rightStart = leftArray.length;

		int i = 0, j = 0, k = 0;
		while (i < leftArray.length && j < rightArray.length)
		{
			final int iInit = i + startIndex;
			final int jInit = j + startIndex;
			final int kInit = k + startIndex;

			compare(iInit, jInit);
			if (leftArray[i].getValue() <= rightArray[j].getValue())
			{
				array[k] = leftArray[i];
				highlight(iInit, swapColor);
				mainArray[kInit] = leftArray[i];
				k++;
				i++;
			} else {
				array[k] = rightArray[j];
				highlight(jInit + rightStart, swapColor);
				mainArray[kInit] = rightArray[j];
				k++;
				j++;
			}
			paintWithDelayAndStep();
			resetHighlightsAt(iInit, jInit, jInit+rightStart);
		}

		while (i < leftArray.length)
		{
			final int kInit = k + startIndex;
			final int iInit = i + startIndex;
			highlight(iInit, swapColor);
			highlight(kInit, swapColor);
			array[k] = leftArray[i];
			mainArray[kInit] = leftArray[i];
			paintWithDelayAndStep();
			resetHighlightsAt(iInit, kInit);
			k++;
			i++;
		}

		while (j < rightArray.length)
		{
			final int jInit = j + startIndex;
			final int kInit = k + startIndex;
			highlight(kInit, swapColor);
			highlight(jInit, swapColor);
			array[k] = rightArray[j];
			mainArray[kInit] = rightArray[j];
			paintWithDelayAndStep();
			resetHighlightsAt(jInit, kInit);
			k++;
			j++;
		}
	}
}