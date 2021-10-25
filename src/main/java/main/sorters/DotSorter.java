package main.sorters;

import main.VisualSortingTool;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.IntStorageValue;
import main.ui.custimization.values.StorageValue;
import main.vcs.VisualComponent;
import main.visualizers.DotVisualizer;

import javax.swing.*;
import java.util.Random;

public class DotSorter extends Sorter
{
	//initial height of the visualization panel when sorting starts
	private int initialHeight;

	/**
	 * dot sorter!
	 * @param sortingTool the sorting tool
	 */
	public DotSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new DotVisualizer(sortingTool), Sorters.DOT);
	}

	@Override
	public void setDefaultValues()
	{
		size = 2000;
	}

	@Override
	protected void reloadArray()
	{
		Random rand = new Random();
		int height = sortingTool.getVisualizerHeight();
		double step = height/(double)size;
		//for scaling purposes on resize
		initialHeight = height;
		for (int i = 0; i < size; i++)
		{
			//creates a diagonal line
			array[i] = new VisualComponent(step*i);
		}
	}

	@Override
	public void addSorterCustomizationComponents(CustomizationPanel cp)
	{
		SpinnerNumberModel nm = new SpinnerNumberModel(size, 100, 9999, 10);
		cp.addRow("Amount:", CustomizationGUI.createNumberJSpinner(sortingTool, nm, n -> size = n, () -> size));
	}

	@Override
	public void addStorageValues()
	{
		StorageValue.addStorageValues(
				new IntStorageValue(getPrefix(), "dotAmount", n -> size = n, () -> size)
		);
	}

	public int getInitialHeight()
	{
		return initialHeight;
	}
}
