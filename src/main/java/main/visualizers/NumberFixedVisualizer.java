package main.visualizers;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.sorters.Sorter.Sorters;
import main.ui.custimization.ColorButton;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.StorageValue;
import main.util.StringHelper;
import main.vcs.VisualComponent;
import main.visualizers.bases.FixedSizeVisualizer;

import java.awt.*;

public class NumberFixedVisualizer extends FixedSizeVisualizer
{
	public NumberFixedVisualizer(VisualSortingTool sortingTool)
	{
		super(sortingTool, Sorters.NUMBER);
	}
	
	@Override
	public void setDefaultValues()
	{
		defaultColor = new Color(144, 193, 215);		
	}

	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		super.addCustomizationComponents(cp);
		cp.addRow(ColorButton.createDefaultColorPickingButton(sortingTool, sortingTool.getSorter(identifier)), true);
		cp.addRow(ColorButton.createBackgroundColorPickingButton(sortingTool), true);
		cp.addRow(CustomizationGUI.createMakePinkButton(sortingTool), true);
	}
	
	@Override
	public void addStorageValues()
	{
		super.addStorageValues();
		//setting up default color with preferences
		StorageValue.addStorageValues(createDefaultColorStorageValue());
	}
	
	@Override
	protected void drawComponent(Graphics2D g , VisualComponent[] array, int i, int arraySize, int x, int y)
	{
		g.setColor(confirmed ? Algorithm.confirmationColor : getHighlightAt(i));
		g.setFont(new Font("", Font.PLAIN, componentSize/2));
		String value = "" + (int) array[i].getValue();
		//draws number centered around the passed in coors
		StringHelper.drawCenteredString(value, x+componentSize/2, y+componentSize/2, g);
	}
}