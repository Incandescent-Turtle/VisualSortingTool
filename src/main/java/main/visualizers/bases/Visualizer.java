package main.visualizers.bases;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.sorters.Sorter;
import main.sorters.Sorter.Sorters;
import main.ui.custimization.Customizable;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.DoubleStorageValue;
import main.ui.custimization.values.StorageValue;
import main.vcs.VisualComponent;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 *  Base class
 *  Visualizers are what draw the arrays and keep all the variables relating to the drawing
 */
public abstract class Visualizer implements Customizable
{	
	//in px the min distance from the borders of the screen
	protected double minMargin = 30;
	//the width of individual components
	protected double componentWidth = 15;
	//the height of individual components
	protected double componentHeight= 15;
	//the horiztonal/veritcal gap between components
	protected double componentGap = 10;
	
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
	 * This draws the Sorters array to the screen <br>
	 * <font color="red">you probably do not want to override this</font>
	 */
	public void drawArray(Graphics2D g)
	{
		Sorter sorter = sortingTool.getSorter();
		if(sorter.getArraySize() > 0 && highlights != null)
		{
			drawArray(g, sorter.getArray(), sorter.getArraySize());
			resetHighlights();
		}
	}
	
	//just a little helper method
	protected abstract void drawArray(Graphics2D g, VisualComponent[] array, int size);

	/**
	 * resets all indices to the default colour
	 */
	public void resetHighlights()
	{
		Arrays.fill(highlights, defaultColor);
	}

	@Override public void addCustomizationComponents(CustomizationPanel cp) {}
	@Override public void addStorageValues() {}

	/**
	 * This method takes in a value and spits out the width of that # of VCs including margins and 
	 * @param size # of VCs 
	 * @return how many pixels the specified number of VCS takes up
	 */
	public final double getTotalWidth(int size)
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
	public final double getRealHMargins(int size)
	{
		return (sortingTool.getVisualizerWidth() - getTotalWidth(size))/2f;
	}
	
	public double getComponentWidth()
	{
		return componentWidth;
	}
	
	public final double getComponentGap()
	{
		return componentGap;
	}
	
	public double getComponentHeight()
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
	protected final DoubleStorageValue createWidthStorageValue()
	{
		return new DoubleStorageValue(getPrefix(), "width", n -> componentWidth = n, () -> componentWidth);
	}
	
	/**
	 * @return the storagevalue all set up to load/save etc the componentGap variable
	 */
	protected final DoubleStorageValue createGapStorageValue()
	{
		return new DoubleStorageValue(getPrefix(), "gap", n -> componentGap = n, () -> componentGap);
	}
	
	/**
	 * @return the storagevalue all set up to load/save etc the minMargin variable
	 */
	protected final DoubleStorageValue createMarginStorageValue()
	{
		return new DoubleStorageValue(getPrefix(), "margin", n -> minMargin = n, () -> minMargin);
	}
	
	/**
	 * @return the storagevalue all set up to load/save etc the defaultColor variable
	 */
	protected final StorageValue<Integer> createDefaultColorStorageValue()
	{
		return StorageValue.createColorStorageValue(getPrefix(), "defaultColor", c -> defaultColor = c, () -> defaultColor);
	}

	protected final void addMarginSpinner(CustomizationPanel cp)
	{
		SpinnerNumberModel nm = new SpinnerNumberModel(minMargin, 0, 100, 1);
		//spinner to change left/right margin
		cp.addRow("Margin:", CustomizationGUI.createDoubleJSpinner(sortingTool, nm, n -> minMargin = n, () -> minMargin));
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
	
	public double getMinMargin()
	{
		return minMargin;
	}
	
	public void setConfirmed(boolean bool)
	{
		confirmed = bool;
	}
}