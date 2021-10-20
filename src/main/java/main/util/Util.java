package main.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import main.ui.custimization.CustomizationGUI;

public class Util
{
	private Util()
	{
	}

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
	 * to turn an image into a buffered image
	 *
	 * @param image the image to be converted
	 * @return bufferedimage representing input image
	 */
	public static BufferedImage imageToBufferedImage(Image image)
	{
		if (image instanceof BufferedImage)
			return (BufferedImage) image;
		BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics bg = bi.getGraphics();
		bg.drawImage(image, 0, 0, null);
		bg.dispose();
		return bi;
	}

	public static Dimension shrink(int originalWidth, int originalHeight, int boundWidth, int boundHeight)
	{
		int newWidth = originalWidth;
		int newHeight = originalHeight;

		// first check if we need to scale width
		if (originalWidth > boundWidth)
		{
			//scale width to fit
			newWidth = boundWidth;
			//scale height to maintain aspect ratio
			newHeight = (newWidth * originalHeight) / originalWidth;
		}

		// then check if we need to scale even with the new height
		if (newHeight > boundHeight)
		{
			//scale height to fit instead
			newHeight = boundHeight;
			//scale width to maintain aspect ratio
			newWidth = (newHeight * originalWidth) / originalHeight;
		}

		return new Dimension(newWidth,newHeight);
	}

	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary)
	{
		return shrink((int) imgSize.getWidth(), (int) imgSize.getHeight(), (int) boundary.getWidth(), (int) boundary.getHeight());
	}

//	public static Dimension shrinkDimension(Dimension original, Dimension boundary)
//	{
//		if(original.getWidth() <= boundary.getWidth() && original.getHeight() <= boundary.getHeight())
//			return original;
//
//		double widthScale = original.getWidth() / boundary.getWidth();
//		double heightScale = original.getHeight() / boundary.getHeight();
//
//		double scale = 1 - Math.min(widthScale, heightScale);
//
//		return new Dimension((int) (original.getWidth() * scale), (int) (original.getHeight() * scale));
//	}

	public static BufferedImage shrinkImage(Image image, int width, int height)
	{
		Dimension dim = shrink(image.getWidth(null), image.getHeight(null), width, height);
		//if it is smaller than the bound and a bufferedImage
		if(image instanceof BufferedImage && dim.getWidth() == image.getWidth(null) && dim.getHeight() == image.getHeight(null))
			return (BufferedImage) image;


		BufferedImage bi = new BufferedImage((int) dim.getWidth(), (int) dim.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics bg = bi.getGraphics();

		bg.drawImage(image, 0, 0, (int) dim.getWidth(), (int) dim.getHeight(), null);
		bg.dispose();
		return bi;
	}

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
	 * calculates the brightness of a color. 0-1, 0 is black 1 is white
	 * @param color the color to find the brightness of
	 * @return returns the brightness from 0-1
	 */
	public static float calculateBrightness(Color color)
	{
		return (color.getRed() * 0.2126f + color.getGreen() * 0.7152f + color.getBlue() * 0.0722f) / 255;
	}

	public static float[] getHSB(Color color)
	{
		return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
	}

//	public static float getDifferenceInHue(Color c1, Color c2)
//	{
//		float hue1 = Util.getHue(c1);
//		float hue2 = Util.getHue(c2);
//		return Math.min(Math.abs(hue1 - hue2), Math.min(hue1, 1-hue1) + Math.min(hue2, 1-hue2));
//	}

//	public static float compareHues(Color target, Color test)
//	{
//		//https://stackoverflow.com/questions/5347867/color-detection-algorithm-how-should-i-do-this
//		//bigger is closer
//		return (float) Math.cos(Util.getHue(test) - Util.getHue(target)) * getSaturation(test);
//	}

	/**
	 * this is used to see how similar colors are
	 * @param target the target color
	 * @param color2 the color to test
	 * @return a float representing the space between colors, the higher the closer
	 */
	public static float compareColors(Color target, Color color2)
	{
		float[] targetHSB = getHSB(target);
		float[] testHSB = getHSB(color2);

		float h1 = targetHSB[0]/1f * 2 * (float) Math.PI;
		float s1 = targetHSB[1];
		float v1 = targetHSB[2];

		float h2 = testHSB[0]/1f * 2 * (float) Math.PI;
		float s2 = testHSB[1];
		float v2 = testHSB[2];

		/*
			thanks Sean Gerrish on SO.
			https://stackoverflow.com/questions/35113979/calculate-distance-between-colors-in-hsv-space
		 */
		float diff = (float) (Math.pow(Math.sin(h1)*s1*v1 - Math.sin(h2)*s2*v2, 2)
							+ Math.pow(Math.cos(h1)*s1*v1 - Math.cos(h2)*s2*v2, 2)
							+ Math.pow(v1 - v2, 2));
		return diff;
	}
}