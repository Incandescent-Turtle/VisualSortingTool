package main.visualizers.bases;

import java.awt.*;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.sorters.Sorter.Sorters;
import main.vcs.VisualComponent;

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
		int limit = (sortingTool.getVisualizerWidth()-minMargin*2+componentGap)/((componentSize+componentGap));
		int numOfRows = arraySize/limit + 1;
		
		//space above/below everything
		int hMargins = getRealHMargins(limit);
		//space to the left/right of everything
		int vMargins = getRealVMargins(numOfRows);
		
		for(int i = 0; i < arraySize; i++)
		{
			int row = i/limit;
			//x and y are the coords of the top left of the component
			int x = hMargins + (i%limit)*(componentSize+componentGap);
			int y = vMargins + (row*(componentGap+componentSize));
			drawComponent(g, array, i, arraySize, x, y);
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
	public final int getRealVMargins(int numOfRows)
	{
		return (sortingTool.getVisualizerHeight() - (numOfRows*(componentSize+componentGap)-componentGap))/2;
	}
	
	/**
	 * sets componentSize to be as big as possible to fill up all the window space
	 */
	public void resize()
	{
		int arraySize = sortingTool.getSorter().getArraySize();
		int baseY = componentGap;
		int numOfRows = 1;
		int limit;
		componentSize = 1;
		//finds biggest possible size
		//bottom of the last row < the bottom of the screen
		while((baseY + numOfRows*(componentSize+componentGap)-componentGap) < (sortingTool.getVisualizerHeight() - componentGap))
		{
			componentSize++;
			//gap is a fraction of size
			//componentGap = (int) Math.ceil(componentSize/10f);
			//at current size how many VCs can fit in a single row
			limit = (sortingTool.getVisualizerWidth()-minMargin*2+componentGap)/((componentSize+componentGap));
			//with current size how many rows are needed
			numOfRows = arraySize/limit + 1;
		}
		componentSize-=1;
	}
	
	@Override
	public int getComponentWidth()
	{
		return componentSize;
	}
	
	@Override
	public int getComponentHeight()
	{
		return componentSize;
	}
}