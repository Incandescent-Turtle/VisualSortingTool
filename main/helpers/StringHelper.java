package main.helpers;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class StringHelper
{
	private StringHelper() {};

	//gets a string's width given the current font and such
	public static int getStringWidth(String message, Graphics g)
	{
		return (int) g.getFont().getStringBounds(message, ((Graphics2D)g).getFontRenderContext()).getWidth();
	}

	//gets a string's height given the current font and such
	public static int getStringHeight(Graphics g)
	{
		return g.getFontMetrics().getHeight();
	}
	
	//gets strings dimensions
	public static Dimension getStringDemensions(String message, Graphics g)
	{
		Rectangle rect = g.getFont().getStringBounds(message, ((Graphics2D)g).getFontRenderContext()).getBounds();
		return new Dimension((int)rect.getWidth(), (int)rect.getHeight());
	}
	
	//centers a string graphically on the x axis (given coord will be center)
	public static void drawXCenteredString(String message, int x, int y, Graphics g)
	{
		g.drawString(message, x-getStringWidth(message, g)/2, y);
	} 
	
	//draw a string centered on the Y axis (given coord will be in the middle of the height of the string)
	public static void drawYCenteredString(String message, int x, int y, Graphics g)
	{
		g.drawString(message, x, y+getStringHeight(g)/2);
	}
	
	//the given coords will be the dead center of the string
	public static void drawCenteredString(String message, int x, int y, Graphics g)
	{
		g.drawString(message, x-getStringWidth(message, g)/2, y+getStringHeight(g)/2-getStringHeight(g)/4);
	}
}