package main.algorithms;

import javax.swing.JPanel;

import main.Sorter;
import main.SortingVisualizer;

/**
 * @author rorsm
 *	Abstract class to build algorithm classes with \n
 *	
 *	ran on a seperate thread by its button \n
 *	 
 *	
 *	the idea is to override run() in the subclasses
 */
public abstract class Algorithm 
{
	private String name;
	protected Sorter sorter;
	protected JPanel panel;
	
	public Algorithm(String name, SortingVisualizer visualizer)
	{
		this.name = name;
		sorter = visualizer.getSorter();
		panel = visualizer;
	}
	
	/**
	 * This gets called when its AlgorithmButton is pressed, gets run on a seperate thread \n
	 * this is where all the sorting logic/calls to repaint should go 
	 */
	public abstract void run();
		
	public String getAlgorithmName()
	{
		return name;
	}
}