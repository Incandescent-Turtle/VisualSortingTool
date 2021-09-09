package main.visualizers;

import java.awt.Color;
import java.awt.Graphics;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.ui.custimization.CustomizationPanel;
import main.vcs.VisualComponent;
import main.visualizers.bases.Visualizer;

public class ColorGradientVisualizer extends Visualizer
{

	public ColorGradientVisualizer(VisualSortingTool sortingTool)
	{
		super(sortingTool);
		minMargin = 2;
		componentWidth = 15;
		//not used
		componentHeight = 0;
		componentGap = 2;
	}

	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void drawArray(Graphics g, Sorter sorter, VisualComponent[] array, int size)
	{
		for(int i = 0; i < size; i++)
		{
			//honestly idk what this is, i just dont know how to make a gradient, but somehow i made this
			int red, green, blue;
			red = green = blue = 255;
			if(array[i].getValue() <= 255)
			{
				red = 255 - array[i].getValue();
			} else if(array[i].getValue() <= 255*2) {
				red = 0;
				green = 255 - (array[i].getValue() - 255);
			} else {
				red = 0;
				green = 0;
				blue = 255 - (array[i].getValue() - 255 - 255);
				if(blue < 0 || blue > 255) blue = 255;
			}
			g.setColor(highlights[i] == Color.RED ? Color.RED : new Color(red, green, blue));
			int maxHeight = sortingTool.getVisualizerHeight() - 20;
			g.fillRect(getRealHMargins(size) + i*(componentWidth + componentGap), sortingTool.getVisualizerHeight()-maxHeight, componentWidth, maxHeight);
			highlights[i] = defaultColor; 
		}
	}
}