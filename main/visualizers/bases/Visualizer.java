package main.visualizers.bases;

import java.awt.Color;
import java.awt.Graphics;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.ui.custimization.CustomizationPanel;
import main.vcs.VisualComponent;

/**
 *  Base class
 *  Visualizers are what draw the arrays and keep all the variables relating to the drawing
 */
public abstract class Visualizer
{
	//in px the min distance from the borders of the screen
	protected int minMargin = 30;
	//the width of individual components
	protected int componentWidth = 15;
	//the height of individual components
	protected int componentHeight= 15;
	//the horiztonal/veritcal gap between components
	protected int componentGap = 10;
	
	//this could be, for example, what unsorted bars are coloured
	protected Color defaultColor = Color.WHITE;
	//this is used to colour certain elements, the index corrosponds to that of array in sorter
	protected Color[] highlights;
	
	
	protected VisualSortingTool sortingTool;

	public Visualizer(VisualSortingTool sortingTool)
	{
		this.sortingTool = sortingTool;
	}
	
	public abstract void addCustomizationComponents(CustomizationPanel cp);
	/**
	 * This draws the Sorters array to the screen
	 */
	public final void drawArray(Graphics g)
	{
		Sorter sorter = sortingTool.getSorter();
		if(sorter.getArraySize() > 0 && highlights != null)
			drawArray(g, sorter, sorter.getArray(), sorter.getArraySize());
	}
	
	protected abstract void drawArray(Graphics g, Sorter sorter, VisualComponent[] array, int size);

	/**
	 * resets all indices to the default colour
	 */
	public void resetHighlights()
	{
		for(int i = 0; i < highlights.length; i++)
		{
			highlights[i] = defaultColor;
		}
	}
	
	/**
	 * This method takes in a value and spits out the width of that # of VCs including margins and 
	 * @param size # of VCs 
	 * @return how many pixels the specified number of VCS takes up
	 */
	public final int getTotalWidth(int size)
	{
		return size*(getComponentWidth()+componentGap) - componentGap;
	}
	
	/**
	 * always bigger than min_margin, is calculated taking into account how much space the drawn array actually
	 * takes
	 * is used to center the array
	 * @param size # of VCs 
	 * @return the size of a single margin
	 */
	public final int getRealHMargins(int size)
	{
		return (sortingTool.getVisualizerWidth() - getTotalWidth(size))/2;
	}
	
	public int getComponentWidth()
	{
		return componentWidth;
	}
	
	public final int getComponentGap()
	{
		return componentGap;
	}
	
	public int getComponentHeight()
	{
		return componentHeight;
	}
	
	/**
	 * used to resise the highlights array
	 * @param size the new length of the higlights array
	 */
	public final void resizeHighlights(int size)
	{
		highlights = new Color[size];
	}
	
	public final Color[] getHighlights()
	{
		return highlights;
	}
	
	public final Color getDefaultColor()
	{
		return defaultColor;
	}
	
	public final void setDefaultColor(Color defaultColor)
	{
		this.defaultColor = defaultColor;
	}
	
	public int getMinMargin()
	{
		return minMargin;
	}
}