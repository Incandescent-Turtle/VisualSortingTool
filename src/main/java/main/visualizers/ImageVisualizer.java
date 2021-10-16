package main.visualizers;

import java.awt.*;
import java.awt.image.BufferedImage;

import main.VisualSortingTool;
import main.sorters.Sorter.Sorters;
import main.sorters.image.ImageSorter;
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
		componentGap = 1;
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
		x += (componentSize - width)/2;
		y += (componentSize - height)/2;
		g.drawImage(image, x, y, x + width, y + height, 0, 0, image.getWidth(), image.getHeight(), null);

		Color c = highlights[index];
		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 150));
		if(!c.equals(defaultColor))
			g.fillRect(x, y, width, height);
	}
	
	@Override
	public void resize()
	{
		super.resize();
		//((ImageSorter)sortingTool.getSorter()).resizeImages(componentSize);
	}
}
