package visualizers.bases;

import main.VisualSortingTool;
import sorters.Sorter;
import sorters.Sorter.Sorters;
import ui.custimization.ColorButton;
import ui.custimization.CustomizationPanel;
import ui.custimization.values.StorageValue;
import util.Util;
import vcs.VisualComponent;

import java.awt.*;

public abstract class FixedSizeVisualizer extends Visualizer
{
	//used instead of componentWidth
	protected int componentSize = 0;
	
	/**
	 * Used to change {@link FixedSizeVisualizer#componentSize} to fill the screen
	 * with a fixed number of components <br>
	 * this differs from how some sorter/visualizer pairs function where the array size 
	 * on a frame resize
	 * @param identifier identifier for the corresponding {@link Sorter}
	 */
	public FixedSizeVisualizer(VisualSortingTool sortingTool, Sorters identifier)
	{
		super(sortingTool, identifier);
	}

	@Override
	public void setDefaultValues()
	{
		minMargin = 5;
		componentGap = 10;
		componentSize = 10;
	}

	/**
	 * calls resize(), then calls drawComponent() for every VC with the x and y of its upper 
	 * left hand position <br>
	 * dont override this 
	 */
	@Override
	protected void drawArray(Graphics2D g, VisualComponent[] array, int arraySize)
	{
		//maxes componentSize
		this.resize();
		//limit per row
		int limit = (int) Math.floor((sortingTool.getVisualizerWidth()-minMargin*2+componentGap)/((componentSize+componentGap)));
		int numOfRows = arraySize/limit + 1;
		
		//space to the left/right of everything
		double hMargins = getRealHMargins(limit);

		//space above/below everything
		double vMargins = getRealVMargins(numOfRows);
		
		for(int i = 0; i < arraySize; i++)
		{
			int row = i/limit;
			//centers last row
			if(row == numOfRows-1) hMargins = getRealHMargins(arraySize%limit);
			//x and y are the coords of the top left of the component
			double x = hMargins + (i%limit)*(componentSize+componentGap);
			double y = vMargins + (row*(componentGap+componentSize));
			drawComponent(g, array, i, arraySize, (int) x, (int) y);
		}
	}

	/**
	 * called from {@link #drawArray(Graphics2D, VisualComponent[], int)} for each component
	 * @param g graphics object to paint with
	 * @param array the VC array
	 * @param index the current index
	 * @param arraySize the size of the VC array
	 * @param x pos of the left side of the component
	 * @param y pos of the top of the component
	 */
	protected abstract void drawComponent(Graphics2D g, VisualComponent[] array, int index, int arraySize, int x, int y);

	/**
	 * is used to center the array vertically
	 * @param numOfRows # of rows
	 * @return the size of a single margin
	 */
	public final double getRealVMargins(int numOfRows)
	{
		return (sortingTool.getVisualizerHeight() - (numOfRows*(componentSize+componentGap)-componentGap))/2;
	}
	
	/**
	 * sets componentSize to be as big as possible to fill up all the window space
	 */
	public void resize()
	{
		int arraySize = sortingTool.getSorter().getArraySize();
		componentSize = (int) Util.getMaxComponentSize(arraySize, sortingTool.getVisualizerHeight(), sortingTool.getVisualizerWidth(), componentGap, 0) - 2;
	}

	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		addMarginSpinner(cp);
		cp.addRow(ColorButton.createBackgroundColorPickingButton(sortingTool), true);
	}

	@Override
	public void addStorageValues()
	{
		StorageValue.addStorageValues(
				createMarginStorageValue()
		);
	}

	@Override
	public double getComponentWidth()
	{
		return componentSize;
	}
	
	@Override
	public double getComponentHeight()
	{
		return componentSize;
	}
}