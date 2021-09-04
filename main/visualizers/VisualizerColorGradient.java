package main.visualizers;

import java.awt.Color;
import java.awt.Graphics;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.vcs.VisualComponent;

public class VisualizerColorGradient extends Visualizer
{

	public VisualizerColorGradient(VisualSortingTool sortingTool)
	{
		super(sortingTool);
		minMargin = 2;
		componentWidth = 15;
		//not used
		componentHeight = 0;
		componentGap = 2;
	}

	@Override
	public void drawArray(Graphics g, Sorter sorter, VisualComponent[] array, int size)
	{
		if(size > 0 && highlights != null)
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
			int maxHeight = sortingTool.getHeight() - sortingTool.getMainUI().getTopBarHeight() - 20;
			g.fillRect(getRealMargins(size) + i*(componentWidth + componentGap), sortingTool.getHeight()-maxHeight, componentWidth, maxHeight);
			highlights[i] = defaultColor; 
		}
	}
}