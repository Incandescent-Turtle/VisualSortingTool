package main.util;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class StringHelper
{
	private StringHelper() {}
	
	/**
	 * gets a string's width given the current font and such
	 * @param message the text in question
	 * @param g current graphics object
	 * @return the width of the string in the current graphics context
	 */
	public static int getStringWidth(String message, Graphics g)
	{
		return (int) g.getFont().getStringBounds(message, ((Graphics2D)g).getFontRenderContext()).getWidth();
	}

	/**
	 * gets a string's height given the current font and such
	 * @param g current graphics object
	 * @return the height strings under this graphics context (doesnt work with lowercase??)
	 */
	public static int getStringHeight(Graphics g)
	{
		return g.getFontMetrics().getHeight();
	}
	
	/**
	 * gets strings dimensions
	 * @param message the message in question
	 * @param g current graphics object
	 * @return the dimensions of this string under the current graphics context
	 */
	public static Dimension getStringDimensions(String message, Graphics g)
	{
		Rectangle rect = g.getFont().getStringBounds(message, ((Graphics2D)g).getFontRenderContext()).getBounds();
		return new Dimension((int)rect.getWidth(), (int)rect.getHeight());
	}
	
	/**
	 * centers a string graphically on the x coord (given coord will be center)
	 * @param message message to be placed
	 * @param x middle x point
	 * @param y y coord representing the top of the string
	 * @param g current graphics object
	 */
	public static void drawXCenteredString(String message, int x, int y, Graphics g)
	{
		g.drawString(message, x-getStringWidth(message, g)/2, y);
	} 
	
	/**
	 * draw a string centered on the Y coord (given coord will be in the middle of the height of the string)
	 * @param message message to be placed
	 * @param x the left x coord of the message
	 * @param y the center y value of the message
	 * @param g current graphics object
	 */
	public static void drawYCenteredString(String message, int x, int y, Graphics g)
	{
		g.drawString(message, x, y+getStringHeight(g)/2);
	}
	
	/**
	 * the given coords will be the ~~dead center of the string
	 * @param message message to be placed
	 * @param x the center x value of the message
	 * @param y the center y value of the message
	 * @param g current graphics object
	 */
	public static void drawCenteredString(String message, int x, int y, Graphics g)
	{
		g.drawString(message, x-getStringWidth(message, g)/2, y+getStringHeight(g)/2-getStringHeight(g)/4);
	}
}