package main.visualizers.bases;

import java.awt.Color;
import java.awt.Graphics;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.sorters.Sorter;
import main.sorters.Sorter.Sorters;
import main.ui.custimization.Customizable;
import main.ui.custimization.values.IntStorageValue;
import main.ui.custimization.values.StorageValue;
import main.vcs.VisualComponent;

/**
 *  Base class
 *  Visualizers are what draw the arrays and keep all the variables relating to the drawing
 */
public abstract class Visualizer implements Customizable
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
	
	/**
	 * whether should render components with {@link Visualizer#defaultColor} or {@link Algorithm#confirmationColor}
	 */
	protected boolean confirmed = false;	
	
	protected VisualSortingTool sortingTool;
	
	protected Sorters identifier;

	public Visualizer(VisualSortingTool sortingTool, Sorters identifier)
	{
		this.sortingTool = sortingTool;
		this.identifier = identifier;
		setDefaultValues();
		addStorageValues();
	}

	/**
	 * This draws the Sorters array to the screen
	 */
	public final void drawArray(Graphics g)
	{
		Sorter sorter = sortingTool.getSorter();
		if(sorter.getArraySize() > 0 && highlights != null)
		{
			drawArray(g, sorter.getArray(), sorter.getArraySize());
			resetHighlights();
		}
	}
	
	//just a little helper method
	protected abstract void drawArray(Graphics g, VisualComponent[] array, int size);

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
	
	/**
	 * @return the storagevalue all set up to load/save etc the componentWidth variable
	 */
	protected StorageValue<Integer> createWidthStorageValue()
	{
		return new IntStorageValue(getPrefix(), "width", n -> componentWidth = n, () -> componentWidth);
	}
	
	/**
	 * @return the storagevalue all set up to load/save etc the componentGap variable
	 */
	protected StorageValue<Integer> createGapStorageValue()
	{
		return new IntStorageValue(getPrefix(), "gap", n -> componentGap = n, () -> componentGap);
	}
	
	/**
	 * @return the storagevalue all set up to load/save etc the minMargin variable
	 */
	protected StorageValue<Integer> createMarginStorageValue()
	{
		return new IntStorageValue(getPrefix(), "margin", n -> minMargin = n, () -> minMargin);
	}
	
	/**
	 * @return the storagevalue all set up to load/save etc the defaultColor variable
	 */
	protected StorageValue<Integer> createDefaultColorStorageValue()
	{
		return StorageValue.createColorStorageValue(getPrefix(), "defaultColor", c -> defaultColor = c, () -> defaultColor);

	}
	
	public final Color[] getHighlights()
	{
		return highlights;
	}
	
	public void setHighlights(Color[] highlights)
	{
		this.highlights = highlights;
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
	
	public void setConfirmed(boolean bool)
	{
		confirmed = bool;
	}
}