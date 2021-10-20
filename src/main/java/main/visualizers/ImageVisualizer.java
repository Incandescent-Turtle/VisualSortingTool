package main.visualizers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import main.VisualSortingTool;
import main.sorters.Sorter.Sorters;
import main.ui.custimization.CustomizationPanel;
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
		componentGap = 0;
		minMargin = 30;
	}

	@Override
	protected void drawArray(Graphics2D g, VisualComponent[] array, int arraySize)
	{
		super.drawArray(g, array, arraySize);
		if(true) return;
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
	protected void drawComponent(Graphics2D g, VisualComponent[] array, int index, int arraySize, int x, int y)
	{
		componentGap = 0;
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
	//	g.drawImage(Util.shrinkImage(image, componentSize, componentSize), x, y, null);
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
		super.resize();
//		int x = minMargin;
//		componentSize = 10;
//
//		if(true) return;
//		VisualComponent[] array = sortingTool.getSorter().getArray();

		//doesnt work for images, but should for fixed-size, as total width is easier
//		while(true)
//		{
//			int totalWidth = 0;
//			for (int i = 0; i < array.length; i++)
//			{
//				BufferedImage img = ((ImageVisualComponent)array[i]).getOriginalImage();
//				totalWidth += Util.shrink(img.getWidth(),img.getHeight(), componentSize, componentSize).getWidth();
//			}
//			double rows = Math.ceil(totalWidth/sortingTool.getWidth());
//			totalWidth += componentSize*rows*2;
//			rows = Math.ceil(totalWidth/sortingTool.getWidth());
//
//			System.out.println("resized rows " + rows);
//			if((rows+1)*componentSize+componentSize > sortingTool.getHeight())
//			{
//				componentSize--;
//				break;
//			}
//			componentSize++;
//		}

		//works exactly as planned, but it induces seizures
//		while(true)
//		{
//			int row = 0;
//			for (int i = 0; i < array.length; i++)
//			{
//				BufferedImage img = ((ImageVisualComponent)array[i]).getOriginalImage();
//
//				Dimension dim = Util.shrink(img.getWidth(),img.getHeight(), componentSize, componentSize);
//				int width = (int) dim.getWidth();
//				int height = (int) dim.getHeight();
//
//				if(x+width+componentGap > sortingTool.getVisualizerWidth())
//				{
//					row++;
//					x = minMargin;
//				}
//
//				int y = row*componentSize;
//
//				if(y+height >= sortingTool.getVisualizerHeight())
//				{
//					componentSize -= 1;
//					System.out.println(componentSize);
//					System.out.println("rows" + row);
//					return;
//				}
//				x += width+componentGap;
//			}
//			componentSize++;
//		}
	}

	@Override
	public void resetHighlights()
	{
		Arrays.fill(highlights, null);
	}
}