package main.visualizers;

import java.awt.*;
import java.awt.image.BufferedImage;

import main.VisualSortingTool;
import main.sorters.Sorter.Sorters;
import main.sorters.image.ImageSorter;
import main.ui.custimization.CustomizationPanel;
import main.util.Util;
import main.vcs.ImageVisualComponent;
import main.vcs.VisualComponent;
import main.visualizers.bases.FixedSizeVisualizer;

public class ImageVisualizer extends FixedSizeVisualizer
{

	public ImageVisualizer(VisualSortingTool sortingTool)
	{
		super(sortingTool, Sorters.IMAGE);
		highlights = new Color[5];
	}

	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		
	}

	@Override
	public void addStorageValues()
	{

	}

	@Override
	public void setDefaultValues()
	{
		super.setDefaultValues();
		componentGap = 1;
		minMargin = 30;
	}

	@Override
	protected void drawArray(Graphics g, VisualComponent[] array, int arraySize)
	{
		//super.drawArray(g, array, arraySize);
		resize();
		int x = minMargin;
		int row = 0;
		for (int i = 0; i < arraySize; i++)
		{
			BufferedImage image = ((ImageVisualComponent)array[i]).getOriginalImage();
			int width = image.getWidth();
			int height = image.getHeight();
			if(width > height)
			{
				height = (int) (height / ((float)width/componentSize));
				width = componentSize;

			} else {
				width = (int) (width / ((float)height/componentSize));
				height = componentSize;
			}
			if(x+width+2 > sortingTool.getVisualizerWidth())
			{
				row++;
				x = minMargin;
			}

			int y = row*componentSize + (componentSize - height)/2;
			g.drawImage(image, x, y, x + width, y + height, 0, 0, image.getWidth(), image.getHeight(), null);
			x += width+2;
		}
	}

	@Override
	protected void drawComponent(Graphics g, VisualComponent[] array, int index, int arraySize, int x, int y)
	{
		//custom resizing....kinda crappy
//		resize();
//		BufferedImage image = ((ImageVisualComponent)array[index]).getScaledImage();
//		int width = image.getWidth();
//		int height = image.getHeight();
//		x += (componentSize - width)/2;
//		y += (componentSize - height)/2;
//
//
//		g.drawImage(image, x, y, null);

		resize();
		BufferedImage image = ((ImageVisualComponent)array[index]).getOriginalImage();
		int width = image.getWidth();
		int height = image.getHeight();
		if(width > height)
		{
			height = (int) (height / ((float)width/componentSize));
			width = componentSize;

		} else {
			width = (int) (width / ((float)height/componentSize));
			height = componentSize;
		}
		//adding margins
		x += (componentSize - width)/2;
		y += (componentSize - height)/2;
		//draws auto-scaled image
		g.drawImage(image, x, y, x + width, y + height, 0, 0, image.getWidth(), image.getHeight(), null);

		//dont color in this case
		if(highlights[index] == null) return;

		//adding color overlay (translucent)
		Color c = highlights[index];
		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 150));
		if(!c.equals(defaultColor))
			g.fillRect(x, y, width, height);
	}
	
	@Override
	public void resize()
	{
		//super.resize();
		int x = minMargin;
		componentSize = 10;
		//if(true) return;
		VisualComponent[] array = sortingTool.getSorter().getArray();
		while(true)
		{
			for (int i = 0; i < array.length; i++)
			{
				int row = 0;

				BufferedImage image = Util.shrinkImage(((ImageVisualComponent)array[i]).getOriginalImage(), componentSize, componentSize);
				int width = image.getWidth();
				int height = image.getHeight();
				//System.out.println(width + " " + height + " " + componentSize);
				if(x+width+2 > sortingTool.getVisualizerWidth())
				{
					row++;
					x = minMargin;
				}

				int y = row*componentSize + (componentSize - height)/2;

				if(y+height >= sortingTool.getVisualizerHeight())
				{
					componentSize -= 1;
					return;
				}
				x += width+2;
			}
			componentSize++;
		}
	}

	@Override
	public void resetHighlights()
	{
		for(int i = 0; i < highlights.length; i++)
		{
			highlights[i] = null;
		}
	}
}
