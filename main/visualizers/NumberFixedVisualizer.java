package main.visualizers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.VisualSortingTool;
import main.helpers.StringHelper;
import main.sorters.Sorter;
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
	protected void drawComponent(Graphics g, Sorter sorter, VisualComponent[] array, int i, int arraySize, int x, int y)
	{
		g.setColor(highlights[i]);
		g.setFont(new Font("", 0, componentSize/2));
		String value = "" + array[i].getValue();
		StringHelper.drawCenteredString(value, x+componentSize/2, y+componentSize/2, g);
		//g.fillRect(x, y, componentSize, componentSize);
		highlights[i] = defaultColor;
	}
	
	/*@Override
	protected void drawArray(Graphics g, Sorter sorter, VisualComponent[] array, int arraySize)
	{
		super.drawArray(g, sorter, array, arraySize);
		int baseY = sortingTool.getMainGUI().getTopBarHeight() + componentSize;
		int limit = (sortingTool.getWidth()-minMargin*2+componentGap)/((componentSize+componentGap));
		int realMargin = getRealHMargins(limit);
		
		g.setColor(Color.BLACK);
	    ((Graphics2D)g).setStroke(new BasicStroke(3));
		for(int i = 0; i <= limit; i++)
		{
			int x = realMargin + i*(componentGap+componentSize) - componentGap/2;
			g.drawLine(x, 0, x, baseY + ((arraySize/limit)*(componentGap+componentSize))+componentGap);
		}
		for(int i = 0; i <= arraySize/limit+1; i++)
		{
			int y = -20 + componentSize/2 + sortingTool.getMainGUI().getTopBarHeight() + (i*(componentGap+componentSize));
			g.drawLine(realMargin, y, sortingTool.getWidth()-realMargin, y);
		}
	}*/
}