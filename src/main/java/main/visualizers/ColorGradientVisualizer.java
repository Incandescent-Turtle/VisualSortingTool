package main.visualizers;

import java.awt.*;

import javax.swing.SpinnerNumberModel;

import main.VisualSortingTool;
import main.sorters.Sorter.Sorters;
import main.ui.custimization.ColorButton;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.StorageValue;
import main.vcs.ColorVisualComponent;
import main.vcs.VisualComponent;
import main.visualizers.bases.Visualizer;

public class ColorGradientVisualizer extends Visualizer
{
	public ColorGradientVisualizer(VisualSortingTool sortingTool)
	{
		super(sortingTool, Sorters.COLOR_GRADIENT);
	}
	
	@Override
	public void setDefaultValues()
	{
		componentWidth = 15;
		componentGap = 0;
		minMargin = 0;		
	}

	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		SpinnerNumberModel nm = new SpinnerNumberModel(componentWidth, 1, 100, 1);
		
		//spinner to change bar width
		cp.addRow("Bar Width:", CustomizationGUI.createJSpinner(sortingTool, nm, n -> componentWidth = n, () -> componentWidth));
		
		//spinner to change gap between bars, min of 0 for flawless gradient
		nm = new SpinnerNumberModel(componentGap, 0, 20, 1);
		cp.addRow("Gap:", CustomizationGUI.createJSpinner(sortingTool, nm, n -> componentGap = n, () -> componentGap));
		
		//spinner to change left/right margin
		nm = new SpinnerNumberModel(minMargin, 0, 100, 1);
		cp.addRow("Margin:", CustomizationGUI.createJSpinner(sortingTool, nm, n -> minMargin = n, () -> minMargin));	
		
		//change background button
		cp.addRow(ColorButton.createBackgroundColorPickingButton(sortingTool), true);
	}
	
	@Override
	public void addStorageValues()
	{
		//setting up specified values with preferences
		StorageValue.addStorageValues(
				createWidthStorageValue(),
				createGapStorageValue(),
				createMarginStorageValue()
		);
	}
	
	@Override
	public void drawArray(Graphics2D g, VisualComponent[] array, int size)
	{
		for(int i = 0; i < size; i++)
		{
			//either proper gradient color or a highlight
			g.setColor(highlights[i]);
			int x = getRealHMargins(size) + i*(componentWidth + componentGap);
			//bars fill the height of the screen
			g.fillRect(x, 0, componentWidth, sortingTool.getVisualizerHeight());
		}
	}
	
	/**
	 * this is overriden to populate the highlights array with gradient values before real <br>
	 * highlights are set
	 */
	@Override
	public void resetHighlights()
	{
		for(int i = 0; i < highlights.length; i++)
		{
			highlights[i] = ((ColorVisualComponent) sortingTool.getSorter(identifier).getArray()[i]).getColor();
		}
	}
}