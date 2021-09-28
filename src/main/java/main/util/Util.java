package main.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import main.ui.custimization.CustomizationGUI;

public class Util
{
	private Util() {}
	
	/**
	 * converts a Color to an int that can be used to construct a Color
	 * @param c the desired color
	 * @return the int counterpart to the specified Color
	 */
	public static int colorToInt(Color c)
	{
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        return ((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
	}
	
	/**
	 * Retrieves the specified Color from its key using {@link CustomizationGUI#PREFS}
	 * @param prefix the prefix to be used
	 * @param key the key storing the int color value
	 * @param defaultColor if the key doesnt exist, the Color that should be returned instead
	 * @return either the stored color if it exists or the default color
	 */
	public static Color getColor(String fullKey, Color defaultColor)
	{
		return new Color(CustomizationGUI.PREFS.getInt(fullKey, Util.colorToInt(defaultColor)));
	}
	
	/**
	 * Stores the specified Color by its key using {@link CustomizationGUI#PREFS}
	 * @param prefix the prefix to be used
	 * @param key the key to store the int color value
	 * @param color the color to store
	 */
	public static void putColor(String fullKey, Color color)
	{
		CustomizationGUI.PREFS.putInt(fullKey, Util.colorToInt(color));
	}
	
	/**
	 * method to get file extensions!
	 * @param file the file whose extension you want
	 * @return the file extension including the "." or NULL if there is no extension
	 */
	public static String getFileExtension(File file)
	{
		String fileName = file.getName().toLowerCase();
		
		//the last index containing "."
		int i = fileName.lastIndexOf('.');
		//if . exists in a valid spot it returns all text after and including "."
		if (i > 0) return fileName.substring(i);
		
		return "";
	}
	
	/**
	 * to turn an image into a buffered image
	 * @param image the image to be converted
	 * @return bufferedimage representing input image
	 */
	public static BufferedImage imageToBufferedImage(Image image) 
	{
	     BufferedImage bi = new BufferedImage (image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
	     Graphics bg = bi.getGraphics();
	     bg.drawImage(image, 0, 0, null);
	     bg.dispose();
	     return bi;
	}
}