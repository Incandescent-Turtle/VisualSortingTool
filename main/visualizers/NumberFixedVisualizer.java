package main.visualizers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.ui.custimization.CustomizationPanel;
import main.util.StringHelper;
import main.vcs.VisualComponent;
import main.visualizers.bases.FixedSizeVisualizer;

public class NumberFixedVisualizer extends FixedSizeVisualizer
{

	public NumberFixedVisualizer(VisualSortingTool sortingTool)
	{
		super(sortingTool);
		setDefaultColor(new Color(144, 193, 215));
	}

	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void drawComponent(Graphics g, Sorter sorter, VisualComponent[] array, int i, int arraySize, int x, int y)
	{
		g.setColor(highlights[i]);
		g.setFont(new Font("", 0, componentSize/2));
		String value = "" + array[i].getValue();
		StringHelper.drawCenteredString(value, x+componentSize/2, y+componentSize/2, g);
		//g.fillRect(x, y, componentSize, componentSize);
		highlights[i] = defaultColor;
	}
}