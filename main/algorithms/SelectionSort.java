package main.algorithms;

import java.awt.Color;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.ui.custimization.ColorButton;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.StorageValue;
import main.vcs.VisualComponent;

public class SelectionSort extends Algorithm
{	
	private Color startColor, minColor;
	
	public SelectionSort(VisualSortingTool sortingTool)
	{
		super("Selection Sort", sortingTool);
	}
	
	@Override
	public void setDefaultValues()
	{
		swapColor = Color.GREEN;
		compareColor = Color.RED;
		startColor = new Color(50, 129, 168);
		minColor = Color.ORANGE;
	}
	
	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		super.addCustomizationComponents(cp);
		cp.addRow(new ColorButton(sortingTool, c -> startColor = c, () -> startColor, "1st Index Color"), true);
		cp.addRow(new ColorButton(sortingTool, c -> minColor = c, () -> minColor, "Min Color"), true);
	}
	
	@Override
	public void addStorageValues()
	{
		super.addStorageValues();
		StorageValue.addStorageValues(
				StorageValue.createColorStorageValue(getPrefix(), "startColor", c -> startColor = c, () -> startColor),
				StorageValue.createColorStorageValue(getPrefix(), "minColor", c -> minColor = c, () -> minColor)
		);
	}

	@Override
	public void runAlgorithm()
	{
		Sorter sorter = sortingTool.getSorter();
		final VisualComponent[] array = sorter.getArray();
		final int size = array.length;
		  
		//i is first un-sorted item in the array
        for (int i = 0; i < size - 1; i++)  
        {
            int minIndex = i;  
            //loops through the array to find the smallest element, swaps with i 
            for (int j = i + 1; j < size; j++)
            {
				sorter.getVisualizer().resetHighlights();
				sorter.highlight(j, compareColor);
				sorter.highlight(minIndex, minColor);
				sorter.highlight(i, startColor);
				//new min value is found
                if(array[j].getValue() < array[minIndex].getValue())
                {
                	sorter.highlight(minIndex, swapColor);
                    sorter.highlight(j, swapColor);
                    minIndex = j;//searching for lowest index  
                }  
    			paintWithDelayAndStep();
            }  
            sorter.highlight(minIndex, swapColor);
            sorter.highlight(i, swapColor);
            sorter.swap(minIndex, i);
			paintWithDelayAndStep();
        }  
	}
}