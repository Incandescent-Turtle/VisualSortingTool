package main.visualizers.bases;

import java.awt.Graphics;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.vcs.VisualComponent;

public abstract class FixedSizeVisualizer extends Visualizer
{
	protected int componentSize = 0;
	
	public FixedSizeVisualizer(VisualSortingTool sortingTool)
	{
		super(sortingTool);
		minMargin = 5;
		componentGap = 10;
		componentSize = 10;
	}

	/**
	 * calls resize(), then calls drawComponent() for every VC with the x and y of its upper 
	 * left hand position
	 * dont override this 
	 */
	@Override
	protected void drawArray(Graphics g, Sorter sorter, VisualComponent[] array, int arraySize)
	{				
		resize();
		int limit = (sortingTool.getWidth()-minMargin*2+componentGap)/((componentSize+componentGap));
		int numOfRows = arraySize/limit + 1;
		
		int hMargins = getRealHMargins(limit);
		int vMargins = getRealVMargins(numOfRows);
		
		for(int i = 0; i < arraySize; i++)
		{
			int row = i/limit;
			int x = hMargins + (i%limit)*(componentSize+componentGap);
			int y = sortingTool.getMainGUI().getTopBarHeight() + vMargins + (row*(componentGap+componentSize));
			drawComponent(g, sorter, array, i, arraySize, x, y);
		}
	}
	
	/**
	 * is used to center the array vertically
	 * @param size # of rows
	 * @return the size of a single margin
	 */
	public final int getRealVMargins(int numOfRows)
	{
		return (sortingTool.getHeight() - sortingTool.getMainGUI().getTopBarHeight() - (numOfRows*(componentSize+componentGap)-componentGap))/2;
	}
	
	/**
	 * sets componentSize and componentGap to be as big as possible to fill up all the window space
	 */
	public final void resize()
	{
		int arraySize = sortingTool.getSorter().getArraySize();
		int basey = sortingTool.getMainGUI().getTopBarHeight() + componentGap;
		int numOfRows = 1;
		int limit = 1;
		componentSize = 1;
		componentGap = 1;
		//finds biggest possible size
		//bottom of the last row < the bottom of the screen
		while((basey + numOfRows*(componentSize+componentGap)-componentGap) < (sortingTool.getHeight() - componentGap))
		{
			componentSize++;
			//gap is a fraction of size
			componentGap = (int) Math.ceil(componentSize/10f);
			//at current size how many VCs can fit in a single row
			limit = (sortingTool.getWidth()-minMargin*2+componentGap)/((componentSize+componentGap));
			//with current size how many rows are needed
			numOfRows = arraySize/limit + 1;
		}
		componentSize-=1;
		//finds biggest possible gap size
		//bottom of the last row < the bottom of the screen
		while((basey + numOfRows*(componentSize+componentGap)-componentGap) < (sortingTool.getHeight() - componentGap))
		{
			componentGap++;
			//at current size how many VCs can fit in a single row
			limit = (sortingTool.getWidth()-minMargin*2+componentGap)/((componentSize+componentGap));
			//with current size how many rows are needed
			numOfRows = arraySize/limit + 1;
		}
		componentGap -= 1;
		System.out.println(componentSize);
	}
	
	protected abstract void drawComponent(Graphics g, Sorter sorter, VisualComponent[] array, int index, int arraySize, int x, int y);

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