package main.sorters;

import java.awt.Color;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.vcs.VisualComponent;
import main.visualizers.Visualizer;

public abstract class Sorter
{
	protected VisualComponent[] array;
	protected Color[] highlights;
	
	protected Visualizer visualizer;
	protected Algorithm algorithm;
	protected VisualSortingTool sortingTool;
	
	protected int size = 200;
	protected int delay=10;
	
	public Sorter(VisualSortingTool sortingTool,Visualizer visualizer)
	{
		this.visualizer = visualizer;
		this.sortingTool = sortingTool;
	}
	
	/**
	 * Swaps these two indices with each other in <b>array</b> with <i>no animation</i>
	 * @param first first index to be swapped
	 * @param second second index to be swapped
	 */
	public final void swap(int first, int second)
	{
		VisualComponent temp = array[first];
		array[first] = array[second];
		array[second] = temp;
	}
	
	/**
	 * updates the <b>highlights</b> array at the specified index with the specified color
	 * @param index index of the number in <b>array</b> that is to be highlighted
	 */
	public void highlight(int index, Color color)
	{
		highlights[index] = color;
	}
	
	/**
	 * To be called when the window has changed size. If no algorithm is active calls resizeArray()
	 */
	public final void tryResizeArray()
	{
		if(algorithm == null)
		{
			resizeArray();
			reloadArray();
		}
	}
	
	protected abstract void resizeArray();
	
	/**
	 * Only nesscesary to override if the array is resizeable
	 */
	protected void reloadArray() {}

	/**
	 * If there is no active algorithm, shuffles the array AND resets colors in the highlights array
	 */
	public final void tryShuffleArray()
	{/*
		if(algorithm  == null)
		{
			tryResizeArray();
			int maxHeight = sortingTool.getHeight() - sortingTool.getMainUI().getTopBar().getHeight() - 20;
			for(int i = 0; i < size; i++)
			{
				Random rand = new Random();
				array[i] = rand.nextInt(maxHeight) + 15;
				highlights[i] = defaultColor;
			}
			sortingTool.repaint();
		}
		*/
	}
	
	protected abstract void shuffleArray();
}