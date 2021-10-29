package visualizers;

import main.VisualSortingTool;
import algorithms.Algorithm;
import sorters.Sorter.Sorters;
import ui.custimization.ColorButton;
import ui.custimization.CustomizationGUI;
import ui.custimization.CustomizationPanel;
import ui.custimization.values.StorageValue;
import util.StringHelper;
import vcs.VisualComponent;
import visualizers.bases.FixedSizeVisualizer;

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