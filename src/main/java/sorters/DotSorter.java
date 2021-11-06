package sorters;

import main.VisualSortingTool;
import ui.custimization.CustomizationGUI;
import ui.custimization.CustomizationPanel;
import ui.custimization.values.IntStorageValue;
import ui.custimization.values.StorageValue;
import ui.tooltips.ToolTips;
import vcs.VisualComponent;
import visualizers.DotVisualizer;

import javax.swing.*;

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
		int height = sortingTool.getVisualizerHeight();
		double step = height/(double)size;
		//for scaling purposes on resize. always fills screen height
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
		SpinnerNumberModel nm = new SpinnerNumberModel(size, 100, 50000, 10);
		JSpinner amountSpinner = CustomizationGUI.createNumberJSpinner(sortingTool, nm, n -> size = n, () -> size);
		amountSpinner.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.AMOUNT, nm));
		cp.addRow("Amount:", amountSpinner);
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
