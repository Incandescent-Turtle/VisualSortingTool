package main.visualizers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import main.VisualSortingTool;
import main.helpers.StringHelper;
import main.sorters.Sorter;
import main.vcs.VisualComponent;

public class VisualizerNumber extends Visualizer
{

	public VisualizerNumber(VisualSortingTool sortingTool)
	{
		super(sortingTool);
		componentGap = 10;
		minMargin = 5;
	}

	@Override
	protected void drawArray(Graphics g, Sorter sorter, VisualComponent[] array, int size)
	{
		g.setFont(new Font("", 0, 40));
		componentHeight = StringHelper.getStringHeight(g);
		int baseY = sortingTool.getMainGUI().getTopBarHeight() + componentHeight;

		componentWidth = StringHelper.getStringWidth("111", g);
		int limit = (sortingTool.getWidth()-minMargin*2+componentGap)/((componentWidth+componentGap));
		int realMargin = getRealMargins(limit);
		g.setColor(Color.BLACK);
	    ((Graphics2D)g).setStroke(new BasicStroke(3));
		for(int i = 0; i <= limit; i++)
		{
			int x = realMargin + i*(componentGap+componentWidth) - componentGap/2;
			g.drawLine(x, 0, x, baseY + ((size/limit)*(componentGap+componentHeight))+componentGap);
		}
		for(int i = 0; i <= size/limit+1; i++)
		{
			int y = -20 + componentHeight/2 + sortingTool.getMainGUI().getTopBarHeight() + (i*(componentGap+componentHeight));
			g.drawLine(realMargin, y, sortingTool.getWidth()-realMargin, y);
		}

		g.setColor(Color.WHITE);
		for(int i = 0; i < size; i++)
		{
			int row = i/limit;
			int x = realMargin + (i%limit)*(componentWidth+componentGap);
			int y = baseY + (row*(componentGap+componentHeight));
			
			g.setColor(highlights[i]);
			
			String value = "" + array[i].getValue();
			StringHelper.drawXCenteredString(value, x+componentWidth/2, y, g);
			highlights[i] = defaultColor;
		}
	}
	
	
}