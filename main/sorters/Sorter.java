package main.sorters;

import java.awt.Color;
import java.util.Random;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.ui.TopBarGUI;
import main.ui.custimization.Customizable;
import main.ui.custimization.CustomizationPanel;
import main.vcs.VisualComponent;
import main.visualizers.bases.Visualizer;

public abstract class Sorter implements Customizable
{
	protected VisualComponent[] array;
	
	protected Visualizer visualizer;
	protected Algorithm algorithm;
	protected VisualSortingTool sortingTool;
	
	protected int size = 200;
	
	//whether the values have been generated yet (used while reloading)
	private boolean hasGenerated = false;
		
	protected Sorter.Sorters identifier;
	
	/**
	 * has an array of {@link VisualComponent}s and uses algorithms to sort them <br>
	 * handles the loading, shuffling, and resizing of said array <br>
	 * has a {@link Visualizer} which uses the array to animate the sorting process <br>
	 * each Sorter must have its own {@link Sorter.Sorters} entry
	 * @param visualizer the visualizer to use
	 * @param identifier every sorter has their own identifier
	 */
	public Sorter(VisualSortingTool sortingTool, Visualizer visualizer, Sorter.Sorters identifier)
	{
		this.sortingTool = sortingTool;
		this.identifier = identifier;
		this.visualizer = visualizer;
		setDefaultValues();
		addStorageValues();
	}
	
	/**
	 * components added to this sorters personal {@link CustomizationPanel} <br>
	 * adds title "Customization", adds componenets for the subclass, visualizer, and general
	 * {@link Algorithm} components <br>
	 * 
	 */
	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		addSorterCustomizationComponents(cp);
		visualizer.addCustomizationComponents(cp);
	}
	
	/**
	 * to be overriden if the sorter needs customization 
	 * @param cp the sorters {@link CustomizationPanel}
	 */
	public void addSorterCustomizationComponents(CustomizationPanel cp) {}
	
	
	/**
	 * to override if the sorter needs to set any default values before loading from preferences <br>
	 * called from constructor before values loaded <br>
	 * no super() needed
	 */
	@Override
	public void setDefaultValues() {}
	
	@Override
	/**
	 * Only needs to be overridden if this sorter has customization settings to be stored <br>
	 * called from constructor
	 */
	public void addStorageValues() {}
	
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
	 * Called on start up to populate array <br>
	 * default imp just calls reloadArray() <br>
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
	 * attempts to change the size variable
	 */
	protected final void tryResizeArray()
	{
		if(algorithm == null)
		{
			visualizer.setConfirmed(false);
			resizeArray();
		}
	}
	
	/**
	 * used to resize the array if applicable. never to be called outside of tryResizeArray()
	 * changes array lengths based on size variable
	 */
	protected void resizeArray()
	{
		array = new VisualComponent[size];
		visualizer.resizeHighlights(size);
	}
	
	/**
	 * to fill the array with new values (when size has changed for example)
	 * if no active algorithm, will either generate new values via generateValues() or reload via reloadArray()
	 */
	protected final void tryReloadArray()
	{
		if(algorithm == null)
		{
			visualizer.setConfirmed(false);
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
	 * If there is no active algorithm, shuffles the array AND resets 
	 * colors in the highlights array to default
	 */
	public final void tryShuffleArray()
	{
		if(algorithm  == null)
		{
			visualizer.setConfirmed(false);
			shuffleArray();
			visualizer.resetHighlights();
		}
	}
	
	/**
	 * when no algorithms are active this method will attempt to: 
	 * 1. Resize 2. reload 3. shuffle 4. repaint
	 */
	public void recalculateAndRepaint()
	{
		//whether everything is set up to go and if sorting isnt happening
		if(sortingTool.isInitialized() && algorithm == null)
		{
			tryResizeArray();
			tryReloadArray();
			tryShuffleArray();
			sortingTool.repaint();
		}
	}
	
	/**
	 * shuffles the values of the array around randomly
	 */
	protected void shuffleArray()
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
	 * <font color="red">Only to be used by the run button in {@link TopBarGUI} </font>
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
		return identifier.toString();
	}
	
	public Sorter.Sorters getIdentifier()
	{
		return identifier;
	}
	
	/**
	 * Enum for identifying sorters (used for finding in getSorter(Sorters identfier) method is VisualSortingTool
	 * an object should exist for each sorter
	 */
	public enum Sorters
	{
		BAR_HEIGHT("Bar Height"),
		COLOR_GRADIENT("Color Gradient"),
		NUMBER("Number Grid"),
		IMAGE("Image");
		
		private final String name;
		
		private Sorters(String name)
		{
			this.name = name;
		}
		@Override
		public String toString()
		{
			return name;
		}
	}
}