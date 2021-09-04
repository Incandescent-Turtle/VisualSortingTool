package main.sorters;

import java.awt.Color;
import java.util.Random;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.vcs.VisualComponent;
import main.visualizers.Visualizer;

public abstract class Sorter
{
	protected VisualComponent[] array;
	
	protected Visualizer visualizer;
	protected Algorithm algorithm;
	protected VisualSortingTool sortingTool;
	
	protected int size = 200;
	protected int delay = 10;
	
	private String name;
	
	private boolean hasGenerated = false;
		
	public Sorter(VisualSortingTool sortingTool, Visualizer visualizer, String name)
	{
		this.visualizer = visualizer;
		this.sortingTool = sortingTool;
		this.name = name;
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
	 * Called on start up to populate array
	 * default imp just calls reloadArray()
	 * override if there is some special initial loading (and probably if this isnt resizeable)
	 */
	public void generateValues()
	{
		reloadArray();
	}
	
	/**
	 * updates the <b>highlights</b> array at the specified index with the specified color
	 * @param index index of the number in <b>array</b> that is to be highlighted
	 */
	public final void highlight(int index, Color color)
	{
		visualizer.getHighlights()[index] = color;
	}
	
	/**
	 * To be called when the window has changed size. If no algorithm is active calls resizeArray()
	 */
	public final void tryResizeArray()
	{
		if(algorithm == null)
			resizeArray();
	}
	
	/**
	 * used to resize the array if applicable. never to be called directly outside of tryResizeArray()
	 */
	protected void resizeArray()
	{
		array = new VisualComponent[size];
		visualizer.resizeHighlights(size);
	}
	
	/**
	 * if no active algorithm, will either generate new values via generateValues() or reload via reloadArray()
	 */
	public final void tryReloadArray()
	{
		if(algorithm == null)
		{
			if(hasGenerated) 
			{
				reloadArray();
			} else {
				hasGenerated = true;
				generateValues();
			}
		}
	}
	
	/**
	 * Only nesscesary to override if the array is resizeable and non-random
	 * re-populates array with values
	 */
	protected void reloadArray() {}

	/**
	 * If there is no active algorithm, shuffles the array AND resets colors in the highlights array to default
	 */
	public final void tryShuffleArray()
	{
		if(algorithm  == null)
		{
			shuffleArray();
			visualizer.resetHighlights();
		}
	}
	
	/**
	 * shuffles the values of the array around randomly
	 */
	protected final void shuffleArray()
	{
		for(int i = 0; i < size; i++)
		{
			Random rand = new Random();
			int first, second;
			first = rand.nextInt(size);
			second = rand.nextInt(size);
			VisualComponent temp = array[first];
			array[first] = array[second];
			array[second] = temp;
		}
	}
	
	public Visualizer getVisualizer()
	{
		return visualizer;
	}
	
	/**
	 * <font color="red">Only to be used by <b>AlgorithmButton</b>s upon click</font color="green">
	 * @param algorithm the algorithm that the button represents
	 */
	public void setAlgorithm(Algorithm algorithm)
	{
		this.algorithm = algorithm;
	}
	
	public Algorithm getAlgorithm()
	{
		return algorithm;
	}
	
	/**
	 * <font color="red"> Only to be used when the spinner value in <b>SortingAlgorithm</b> changes</font color="red">
	 * @param delay delay in ms for the animations
	 */
	public void setDelay(int delay)
	{
		this.delay = delay;
	}
	
	public int getDelay()
	{
		return delay;
	}
	
	public int getArraySize()
	{
		return size;
	}
	
	public VisualComponent[] getArray()
	{
		return array;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}