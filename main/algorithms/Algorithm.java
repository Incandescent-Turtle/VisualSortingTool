package main.algorithms;

import main.Sorter;

/**
 * @author rorsm
 *	Abstract class to build algorithm classes with
 */
public abstract class Algorithm
{
	private String name;
	protected Sorter sorter;
	
	public Algorithm(String name, Sorter sorter)
	{
		this.name = name;
		this.sorter = sorter;
	}
	
	public abstract void run();
	
	public String getName()
	{
		return name;
	}
}