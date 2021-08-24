package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

import main.algorithms.Algorithm;

/**
 * @author rorsm
 *	handles the main array used \n
 *	
 */
public class Sorter
{	
	public static final int MARGIN = 30;

	
	//current algorithm being used (changed via buttons)
	private Algorithm algorithm;
	private Color defaultColor;
	private JPanel panel;
	
	//the delay between anmimations
	private int delay=10;
	//main array that holds all the numbers to be sorted
	public int[] array;
	//array that holds which color the corresponding index in array should be
	private Color[] highlights;
	//array size aka length
	private int size = 200;
	
	public Sorter(SortingVisualizer visualizer)
	{
		defaultColor = new Color(144, 193, 215);
		panel = visualizer;
	}
	
	/**
	 * Swaps these two indices with each other in <b>array</b> with <i>no animation</i>
	 * @param first first index to be swapped
	 * @param second second index to be swapped
	 */
	public void swap(int first, int second)
	{
		int temp = array[first];
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
	 * To be called when the window has changed size. Wont run if an algorithm is active
	 * <ul>
	 * 	<li>changes the <b>size</b> variable</li>
	 *	<li>resizes <b>array</b> and <b>highlights</b> accordingly</li>
	 * 	<li>shuffles <b>array</b></li>
	 * </ul>  
	 */
	public void tryResizeArray()
	{
		if(algorithm == null)
		{
			size = (panel.getWidth() - Sorter.MARGIN*2)/4;
			array = new int[size];
			highlights = new Color[size];
			panel.repaint();
		}
	}
	
	/**
	 * If there is no active algorithm, shuffles the array AND resets colors in the highlights array
	 */
	public void tryShuffleArray()
	{
		if(algorithm  == null)
		{
			tryResizeArray();
			for(int i = 0; i < size; i++)
			{
				Random rand = new Random();
				array[i] = rand.nextInt(600) + 15;
				highlights[i] = defaultColor;
			}
			panel.repaint();
		}
	}
	
	/**
	 * To have no logic, renders the bars with their colors and clears <b>highlights</b>
	 */
	protected void drawArray(Graphics g)
	{
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
		
		//int[] arrayCopy = array.clone();
		//Color[] highlightsCopy = highlights.clone();
		if(size > 0 && highlights != null)
		for(int i = 0; i < size; i++)
		{
			//highlights in specified color (default is white)
			g.setColor(highlights[i]);
			g.fillRect(MARGIN + i*4, panel.getHeight()-array[i], 2, array[i]);
			//resets the highlights array
			highlights[i] = defaultColor; 
		}
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
	
	public int[] getArray()
	{
		return array;
	}
	
	public Color[] getHighlights()
	{
		return highlights;
	}
}