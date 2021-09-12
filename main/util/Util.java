package main.util;

import java.awt.Color;

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
	 * @param key the key storing the int color value
	 * @param defaultColor if the key doesnt exist, the Color that should be returned instead
	 * @return either the stored color if it exists or the default color
	 */
	public static Color getColor(String key, String prefix, Color defaultColor)
	{
		return new Color(CustomizationGUI.PREFS.getInt(prefix + key, Util.colorToInt(defaultColor)));
	}
	
	public static void putColor(String key, String prefix, Color color)
	{
		CustomizationGUI.PREFS.putInt(prefix + key, Util.colorToInt(color));
	}
}