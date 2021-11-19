package algorithms;

import main.VisualSortingTool;
import sorters.Sorter;
import ui.custimization.ColorButton;
import ui.custimization.CustomizationPanel;
import ui.custimization.values.StorageValue;
import ui.tooltips.ToolTips;
import vcs.VisualComponent;

import javax.swing.*;
import java.awt.*;

//
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
		swapColor = new Color(150, 210, 148); //green
		compareColor = new Color(255, 105, 97); //redd
		startColor = new Color(50, 129, 168); //blue
		minColor = Color.ORANGE;
	}
	
	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		super.addCustomizationComponents(cp);
		JButton firstIndexButton = new ColorButton(sortingTool, c -> startColor = c, () -> startColor, "1st Index Color");
		firstIndexButton.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.FIRST_INDEX_COLOR));
		cp.addRow(firstIndexButton, true);

		JButton minColorButton = new ColorButton(sortingTool, c -> minColor = c, () -> minColor, "Min Color");
		minColorButton.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.MIN_COLOR));
		cp.addRow(minColorButton, true);
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
				highlight(j, compareColor);
				highlight(minIndex, minColor);
				highlight(i, startColor);
				//new min value is found
				int initialMin = minIndex;
                if(array[j].getValue() < array[minIndex].getValue())
                {
					highlight(minIndex, swapColor);
					highlight(j, swapColor);
                    minIndex = j;//searching for lowest index  
                }  
    			paintWithDelayAndStep();
				resetHighlightsAt(j, i, initialMin);
			}
            swap(minIndex, i);
			paintWithDelayAndStep();
			resetHighlightsAt(minIndex, i);
		}
	}


}