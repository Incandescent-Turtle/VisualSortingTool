package visualizers;

import main.VisualSortingTool;
import sorters.Sorter.Sorters;
import ui.custimization.values.StorageValue;
import util.StringHelper;
import util.Util;
import vcs.ImageVisualComponent;
import vcs.VisualComponent;
import visualizers.bases.FixedSizeVisualizer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageVisualizer extends FixedSizeVisualizer
{
	public ImageVisualizer(VisualSortingTool sortingTool)
	{
		super(sortingTool, Sorters.IMAGE);
	}

	@Override
	public void setDefaultValues()
	{
		componentGap = 0;
		minMargin = 30;
	}

	@Override
	protected void drawComponent(Graphics2D g, VisualComponent[] array, int index, int arraySize, int x, int y)
	{
		componentGap = 0;
		//"full size" image
		BufferedImage image = ((ImageVisualComponent)array[index]).getOriginalImage();
		int width = image.getWidth();
		int height = image.getHeight();

		//preserving aspect ratio
		if(width > height)
		{
			height = (int) (height / ((float)width/componentSize));
			width = componentSize;

		} else {
			width = (int) (width / ((float)height/componentSize));
			height = componentSize;
		}

		//adding margins to center the image in its square
		x += (componentSize - width)/2;
		y += (componentSize - height)/2;

		//draws auto-scaled image
		g.drawImage(image, x, y, x + width, y + height, 0, 0, image.getWidth(), image.getHeight(), null);
		//dont color in this case
		if(getHighlightAt(index) == null) return;

		//adding color overlay (translucent)
		Color c = getHighlightAt(index);
		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 150));
		if(!c.equals(defaultColor))
			g.fillRect( x, y, width, height);
	}

	//to draw the error promt when no files available etc
	@Override
	public void drawArray(Graphics2D g)
	{
		VisualComponent[] array = sortingTool.getSorter().getArray();
		//for printing error messahe
		if(array == null || array.length == 0)
		{
			//the message to display on the screen
			String message = "Please pick a valid folder containing .png or .jpg images";
			float i = 1;
			//finding the largest possible font
			while(true)
			{
				g.setFont(g.getFont().deriveFont(i));
				int width = StringHelper.getStringWidth(message, g) + 20;
				if(width > sortingTool.getVisualizerWidth())
				{
					i--;
					break;
				}
				i++;
			}
			g.setFont(g.getFont().deriveFont(i));
			//sets the color so its always visible no matter the background
			g.setColor(Util.calculateLuminosity(sortingTool.getVisualizationPanel().getBackground()) >= 0.5 ? Color.BLACK : Color.WHITE);
			//draws message in the center of the panel
			StringHelper.drawCenteredString(message, sortingTool.getVisualizerWidth()/2, sortingTool.getVisualizerHeight()/2, g);
		} else {
			super.drawArray(g);
		}
	}

	@Override
	public void addStorageValues()
	{
		super.addStorageValues();
		StorageValue.addStorageValues(createMarginStorageValue());
	}
}