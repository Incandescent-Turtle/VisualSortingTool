package main.visualizers;

import java.awt.Color;
import java.awt.Graphics;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaultValues()
	{

	}

	@Override
	protected void drawComponent(Graphics g, VisualComponent[] array, int index, int arraySize, int x, int y)
	{

		//index = array.length - 1 - index;
		resize();
		componentGap = 1;
		BufferedImage image = ((ImageVisualComponent)array[index]).getScaledImage();
		int width = image.getWidth();
		int height = image.getHeight();
		x += (componentSize - width)/2;
		y += (componentSize - height)/2;
		
		
	//	g.drawImage(image, x, y, null);
	//	g.drawImage(10, 10, 30, 30)
		Color c = highlights[index];
		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 150));
		if(!c.equals(defaultColor))
			g.fillRect(x, y, width, height);
	}
	
	@Override
	public void resize()
	{
		super.resize();
		((ImageSorter)sortingTool.getSorter()).resizeImages(componentSize);
	}
}
