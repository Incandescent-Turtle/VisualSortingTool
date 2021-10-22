package main.vcs;

import java.awt.Color;

public class ColorVisualComponent extends VisualComponent
{
	private Color color;
	
	/**
	 * these are what the colorgradient sorter holds in its arrays, the things being sorted
	 * VC for short
	 * @param value the value for comparison agaisnt other VCs
	 * @param color the color to be stored in this vc
	 */
	public ColorVisualComponent(int value, Color color)
	{
		super(value);
		this.color = color;
	}
	
	public Color getColor()
	{
		return color;
	}
}