package main.algorithms;

import java.awt.Color;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.ui.GUIHandler;
import main.ui.custimization.ColorButton;
import main.ui.custimization.ColorButton.ColorRetrieveAction;
import main.ui.custimization.CustomizationGUI.Customizable;
import main.ui.custimization.CustomizationPanel;
import main.vcs.VisualComponent;

/**
 * @author rorsm
 *	Abstract class to build algorithm classes with<br>
 *	
 *	ran on a seperate thread by its button<br>
 *	 
 *	the idea is to override {@link Algorithm#runAlgorithm()} in the subclasses to implement behaviour
 */
public abstract class Algorithm implements Customizable
{
	//all algorithms have the same color for ending/confirming an algorithm run
	public static Color confirmationColor;

	private String name;
	protected VisualSortingTool sortingTool;
	protected Color swapColor, compareColor;
	
	static
	{
		confirmationColor = Color.GREEN;
	}
	
	public Algorithm(String name, VisualSortingTool sortingTool)
	{
		this.name = name;
		this.sortingTool = sortingTool;
		swapColor = Color.RED;
		compareColor = Color.GREEN;
	}
		
	/**
	 * from {@link Customizable}, populates its own {@link CustomizationPanel} with a title using
	 * the {@link Algorithm#name} variable and
	 * {@link ColorButton}s to change {@link Algorithm#swapColor} and {@link Algorithm#compareColor}
	 * @param cp this Algorithms own {@link CustomizationPanel}
	 */
	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{		
		cp.addTitleSeperator(name, true);			
		cp.addRow(new ColorButton(sortingTool, c -> swapColor = c, () -> swapColor, "Swap Color"), true);
		cp.addRow(new ColorButton(sortingTool, c -> compareColor = c, () -> compareColor, "Compare Color"), true);
	}
	/**
	 * This gets called when this is the selected algorithm and the run button is hit <br>
	 * gets run on a seperate thread. 
	 */
	public final void run()
	{
		runAlgorithm();
		finishRun();
	}
	
	/**
	 * <font color="red">never call this directly</font> <br>
	 * this is where all the sorting logic/calls to repaint should go 
	 */
	public abstract void runAlgorithm();
		
	/**
	 * called at the end of the run() method to close some things
	 */
	private final void finishRun()
	{
		System.out.println("Done " + name);
		sortingTool.getSorter().setAlgorithm(null);
		isSorted(sortingTool, true);
		GUIHandler.setEnabled(true);
	}
	
	/**
	 * Delays sort based on the spinner delay
	 */
	protected final static void delay(Sorter sorter)
	{
		VisualSortingTool.delay(Sorter.delay);
	}
	
	/**
	 * adds customization for all the static values shared by algorithms, such as {@link #confirmationColor}
	 * @param cp this {@link CustomizationPanel} belongs to a sorter
	 */
	public static void addGeneralAlgorithmCustimizationComponents(VisualSortingTool sortingTool, CustomizationPanel cp)
	{
		//title placed with a little spacing under the sorter customization components
		cp.addTitleSeperator("All Algorithms", true);
		ColorRetrieveAction retrieveAction = new ColorRetrieveAction() {
			
			@Override
			public Color retrieveColor()
			{
				return confirmationColor; 
			}
		};
		cp.addRow(new ColorButton(sortingTool, c -> confirmationColor = c, retrieveAction, "Confirmation Color"), true);	
	}

	/**
	 * Left to right turns components to {@link #confirmationColor} until done
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
				sorter.getVisualizer().setConfirmed(true);
				return true;
			}
			
			//returns false if incorrect
			if(i != array.length-1 && array[i].getValue()>array[i+1].getValue()) return false;
			//one by one colors them with confirmationColor
			if(shouldPaint)
			{
				for(int j = 0; j <= i+1; j++)
				{
					highlights[j] = confirmationColor;
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