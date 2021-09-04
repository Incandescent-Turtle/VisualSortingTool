package main.algorithms;

import java.awt.Color;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.vcs.VisualComponent;

/**
 * @author rorsm
 *	Abstract class to build algorithm classes with
 *	
 *	ran on a seperate thread by its button
 *	 
 *	
 *	the idea is to override run() in the subclasses
 */
public abstract class Algorithm 
{
	private String name;
	protected VisualSortingTool sortingTool;
	
	public Algorithm(String name, VisualSortingTool sortingTool)
	{
		this.name = name;
		this.sortingTool = sortingTool;
	}
	
	/**
	 * This gets called when its AlgorithmButton is pressed, gets run on a seperate thread \n
	 * this is where all the sorting logic/calls to repaint should go 
	 */
	public abstract void run();
		
	/**
	 * To be called at the end of the run() method to close some things
	 */
	protected final void finishRun()
	{
		System.out.println("Done " + name);
		sortingTool.getSorter().setAlgorithm(null);
		isSorted(sortingTool, true);
		sortingTool.getMainUI().enableSorterPicker();
	}
	
	/**
	 * Delays sort based on the spinner delay
	 */
	protected final static void delay(Sorter sorter)
	{
		VisualSortingTool.delay(sorter.getDelay());
	}
	
	/**
	 * <font color="red">Clears highlight array, dont run while algorithm active</font>
	 * Gives a little animation
	 * @param shouldPaint whether this should be run with painting/delay methods or not (visualized or not)
	 */
	public static boolean isSorted(VisualSortingTool sortingTool, boolean shouldPaint)
	{
		Sorter sorter = sortingTool.getSorter();
		VisualComponent[] array = sorter.getArray();
		Color[] highlights = sorter.getVisualizer().getHighlights();
		for(int i = 0; i<array.length; i++)
		{
			//if last element
			if(i == array.length-1)
			{
				for(int j = 0; j <= i; j++)
				{
					highlights[j] = Color.GREEN;
				}
				sortingTool.repaint();
				return true;
			}
			
			//returns false if incorrect
			if(i != array.length-1 && array[i].getValue()>array[i+1].getValue()) return false;
			if(shouldPaint)
			{
				for(int j = 0; j <= i+1; j++)
				{
					highlights[j] = Color.GREEN;
				}
				VisualSortingTool.delay(10);
				sortingTool.repaint();
			}
		}
		return true;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}