package main.visualizers;

import java.awt.Color;
import java.awt.Graphics;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.vcs.VisualComponent;

public class VisualizerBarHeight extends Visualizer
{	
	public VisualizerBarHeight(VisualSortingTool sortingTool)
	{
		super(sortingTool);
		//a nice blue
		setDefaultColor(new Color(144, 193, 215));
		
		minMargin = 2;
		componentWidth = 15;
		componentGap = 2;
	}
	
	/**
	 * draws bars of different heights using the values as the height input
	 */
	@Override
	public void drawArray(Graphics g, Sorter sorter, VisualComponent[] array, int size)
	{			
		for(int i = 0; i < size; i++)
		{
			if(highlights[i] == Color.RED) g.setColor(Color.RED);
			//highlights in specified color (default is white)
			g.setColor(highlights[i]);
			g.fillRect(getRealMargins(size) + i*(componentWidth+componentGap), sortingTool.getHeight()-array[i].getValue(), componentWidth, array[i].getValue());
			//resets the highlights array
			highlights[i] = defaultColor; 
		}
	}
}