package util;

import ui.custimization.CustomizationGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Util
{
	private Util() {}

	/**
	 * converts a Color to an int that can be used to construct a Color
	 *
	 * @param c the desired color
	 * @return the int counterpart to the specified Color
	 */
	public static int colorToInt(Color c)
	{
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		return ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
	}

	/**
	 * Retrieves the specified Color from its key using {@link CustomizationGUI#PREFS}
	 *
	 * @param fullKey          the key storing the int color value
	 * @param defaultColor if the key doesnt exist, the Color that should be returned instead
	 * @return either the stored color if it exists or the default color
	 */
	public static Color getColor(String fullKey, Color defaultColor)
	{
		return new Color(CustomizationGUI.PREFS.getInt(fullKey, Util.colorToInt(defaultColor)));
	}

	/**
	 * Stores the specified Color by its key using {@link CustomizationGUI#PREFS}
	 *
	 * @param fullKey    the key to store the int color value
	 * @param color  the color to store
	 */
	public static void putColor(String fullKey, Color color)
	{
		CustomizationGUI.PREFS.putInt(fullKey, Util.colorToInt(color));
	}

	/**
	 * method to get file extensions!
	 *
	 * @param file the file whose extension you want
	 * @return the file extension including the "." or NULL if there is no extension
	 */
	public static String getFileExtension(File file)
	{
		String fileName = file.getName().toLowerCase();

		//the last index containing "."
		int i = fileName.lastIndexOf('.');
		//if . exists in a valid spot it returns all text after and including "."
		if (i > 0)
			return fileName.substring(i);

		return "";
	}

	/**
	 * restrains a dimension to a new dimension while maintaining aspect ratio. does nothing if smaller than restraints
	 * @param originalWidth the width to restrain
	 * @param originalHeight the width to restrain
	 * @param widthRestraint the maximum width
	 * @param heightRestraint the maximum width
	 * @return a dimension representing the new values
	 */
	public static Dimension restrainDimensions(int originalWidth, int originalHeight, int widthRestraint, int heightRestraint)
	{
		int newWidth = originalWidth;
		int newHeight = originalHeight;

		//if width needs scaling
		if (originalWidth > widthRestraint)
		{
			newWidth = widthRestraint;
			//scales height to match aspect ratio
			newHeight = (newWidth * originalHeight) / originalWidth;
		}

		//see if the new height is within bounds
		if (newHeight > heightRestraint)
		{
			newHeight = heightRestraint;
			//scales width to match
			newWidth = (newHeight * originalWidth) / originalHeight;
		}
		return new Dimension(newWidth,newHeight);
	}

	/**
	 * restricts an image to a certain dimension, keeping aspect ratio intact.
	 * scales down if it is bigger, leaves it if it is smaller.
	 * @param image the image to restrain
	 * @param width new width restraint
	 * @param height new height restraint
	 * @return the scales (or unscaled) image
	 */
	public static BufferedImage restrainImageSize(Image image, int width, int height)
	{
		Dimension dim = restrainDimensions(image.getWidth(null), image.getHeight(null), width, height);
		//if it is smaller than the bound and a bufferedImage
		if(image instanceof BufferedImage && dim.getWidth() == image.getWidth(null) && dim.getHeight() == image.getHeight(null))
			return (BufferedImage) image;


		BufferedImage bi = new BufferedImage((int) dim.getWidth(), (int) dim.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics bg = bi.getGraphics();

		bg.drawImage(image, 0, 0, (int) dim.getWidth(), (int) dim.getHeight(), null);
		bg.dispose();
		return bi;
	}

	/**
	 * calculates the brightness of a color. 0-1, 0 is black 1 is white
	 * @param color the color to find the brightness of
	 * @return returns the brightness from 0-1
	 */
	public static float calculateLuminosity(Color color)
	{
		return (color.getRed() * 0.2126f + color.getGreen() * 0.7152f + color.getBlue() * 0.0722f) / 255;
	}

	/**
	 * returns the average color of the pixels in the image. adds up all rgb and averages them
	 * @param img the image to get the color of
	 * @return returns the average color (no opacity)
	 */
	public static Color getAverageColor(BufferedImage img)
	{
		long r = 0;
		long g = 0;
		long b = 0;
		for(int i = 0; i < img.getWidth(); i++)
		{
			for(int j = 0; j < img.getHeight(); j++)
			{
				Color color = new Color(img.getRGB(i, j));
				r+=color.getRed();
				g+=color.getGreen();
				b+=color.getBlue();
			}
		}
		int pixels = img.getWidth()*img.getHeight();
		return new Color((int) (r/pixels),(int) (g/pixels), (int) (b/pixels));
	}

	/**
	 * calculates the size squares must be to fit within the specified bounds <br>
	 * with the specified parameters
	 * @param amount the amount of squares/components
	 * @param height the height of the area to fit them in
	 * @param width the width of the area to fit them in
	 * @param gap the gap between the squares/components
	 * @param xMargin the margin on the x axis
	 * @return the maximum size the componets can be in said area
	 */
	public static double getMaxComponentSize(int amount, double height, double width, double gap, double xMargin)
	{
		double size = 1;
		while(true)
		{
			//total length if all components were lined up horizontally
			double totalWidth = amount * (size+gap) - gap;

			//how many visualizer widths can you fit into the total length? round up!
			int rows = (int) Math.ceil((float)totalWidth/(width-xMargin*2));

			//amount of rows, checking to see if this component would be out of bounds. exits and reverts to prev
			if(rows*(size+gap)+size-gap > height)
			{
				size--;
				break;
			}
			size++;
		}
		return size;
	}

	public static String spinnerNumberModelToString(SpinnerNumberModel nm)
	{
		boolean allowsDecimals = nm.getValue() instanceof Double || nm.getValue() instanceof Float;
		return String.join(" | ", "Max: " + nm.getMaximum(), "Min: " + nm.getMinimum(), "Step size: " + nm.getStepSize()) + (allowsDecimals ? " | Allows Decimals" : "");
	}

	public static String getNameWithoutExtension(File file)
	{
		String fileName = file.getName();
		return fileName.substring(0, fileName.indexOf('.'));
	}
}