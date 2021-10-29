package sorters;

import main.VisualSortingTool;
import ui.custimization.CustomizationGUI;
import ui.custimization.CustomizationPanel;
import ui.custimization.values.IntStorageValue;
import ui.custimization.values.StorageValue;
import ui.tooltips.ToolTips;
import vcs.VisualComponent;
import visualizers.bases.Visualizer;

import javax.swing.*;
import java.util.function.Consumer;

public abstract class BarSorter extends Sorter
{
	//the JSpinner for the amount of bars
	private JSpinner barAmountSpinner;

	/**
	 * a sorter that is visualized with bars (bar height, gradient)
	 *
	 * @param sortingTool the sorting tool
	 * @param visualizer  the visualizer to use
	 * @param identifier  every sorter has their own identifier
	 */
	public BarSorter(VisualSortingTool sortingTool, Visualizer visualizer, Sorters identifier)
	{
		super(sortingTool, visualizer, identifier);
	}

	@Override
	public void setDefaultValues()
	{
		size = 15;
		array = new VisualComponent[size];
	}

	@Override
	public void addSorterCustomizationComponents(CustomizationPanel cp)
	{
		SpinnerNumberModel nm = new SpinnerNumberModel(size, 10, 9999, 1);

		//when a new number chosen via the spinner, this updates the array to the new size
		Consumer<Integer> setBarAmount = new Consumer<Integer>()
		{
			@Override
			public void accept(Integer n)
			{
				size = n;
				resizeArray();
				reloadArray();
			}
		};

		barAmountSpinner = CustomizationGUI.createNumberJSpinner(sortingTool, nm, setBarAmount, () -> size);
		cp.addRow("Max Amount:", barAmountSpinner);
		barAmountSpinner.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.BAR_AMOUNT, nm));
	}

	@Override
	public void addStorageValues()
	{
		StorageValue.addStorageValues(
				new IntStorageValue(getPrefix(), "barAmount", n -> size = n, () -> size)
		);
	}
}