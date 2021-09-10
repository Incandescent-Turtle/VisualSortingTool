package main.visualizers.bases;

import java.awt.Graphics;

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
	protected void drawArray(Graphics g, VisualComponent[] array, int arraySize)
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
			int x = hMargins + (i%limit)*(componentSize+componentGap);
			int y = vMargins + (row*(componentGap+componentSize));
			drawComponent(g, array, i, arraySize, x, y);
		}
	}
	
	/**
	 * called from {@link #drawArray(Graphics, VisualComponent[], int)} for each component
	 */
	protected abstract void drawComponent(Graphics g, VisualComponent[] array, int index, int arraySize, int x, int y);

	/**
	 * is used to center the array vertically
	 * @param size # of rows
	 * @return the size of a single margin
	 */
	public final int getRealVMargins(int numOfRows)
	{
		return (sortingTool.getVisualizerHeight() - (numOfRows*(componentSize+componentGap)-componentGap))/2;
	}
	
	/**
	 * sets componentSize to be as big as possible to fill up all the window space
	 */
	public final void resize()
	{
		int arraySize = sortingTool.getSorter().getArraySize();
		int baseY = componentGap;
		int numOfRows = 1;
		int limit = 1;
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