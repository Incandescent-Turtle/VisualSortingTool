package main.visualizers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.prefs.Preferences;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.sorters.Sorter.Sorters;
import main.ui.custimization.ColorButton;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.CustomizationPanel;
import main.util.StringHelper;
import main.util.Util;
import main.vcs.VisualComponent;
import main.visualizers.bases.FixedSizeVisualizer;

public class NumberFixedVisualizer extends FixedSizeVisualizer
{
	public NumberFixedVisualizer(VisualSortingTool sortingTool)
	{
		super(sortingTool, Sorters.NUMBER);
	}

	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		cp.addRow(ColorButton.createDefaultColorPickingButton(sortingTool, sortingTool.getSorter(identifier)), true);
		cp.addRow(ColorButton.createBackgroundColorPickingButton(sortingTool), true);
		cp.addRow(CustomizationGUI.createMakePinkButton(sortingTool), true);
	}
	
	@Override
	public void loadValues(Preferences prefs, String prefix)
	{
		defaultColor = Util.getColor(DEFAULT_COLOR, prefix, new Color(144, 193, 215));
	}
	
	@Override
	public void storeValues(Preferences prefs, String prefix)
	{
		Util.putColor(DEFAULT_COLOR, prefix, defaultColor);
	}
	
	@Override
	protected void drawComponent(Graphics g , VisualComponent[] array, int i, int arraySize, int x, int y)
	{
		g.setColor(confirmed ? Algorithm.confirmationColor : highlights[i]);
		g.setFont(new Font("", 0, componentSize/2));
		String value = "" + array[i].getValue();
		//draws number centered around the passed in coors
		StringHelper.drawCenteredString(value, x+componentSize/2, y+componentSize/2, g);
		highlights[i] = defaultColor;
	}
}