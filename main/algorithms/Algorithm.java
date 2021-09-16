package main.algorithms;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

import main.VisualSortingTool;
import main.interfaces.RetrieveAction;
import main.sorters.Sorter;
import main.ui.GUIHandler;
import main.ui.custimization.ColorButton;
import main.ui.custimization.Customizable;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.IntStorageValue;
import main.ui.custimization.values.StorageValue;
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
	public static int delay;

	private String name;
	protected VisualSortingTool sortingTool;
	
	protected Color swapColor, compareColor;
	
	//for setting up static values for all algorithms
	static
	{
		//class prefix
		final String prefix = VisualSortingTool.getPrefix(Algorithm.class);

		delay = 10;
        StorageValue.addStorageValues(new IntStorageValue(prefix, "delay", delay, n -> delay = n, () -> delay));
		//this line is needed because the delay spinner in TopBar will do weird stuff
        delay = CustomizationGUI.PREFS.getInt(prefix + "delay", delay);

		confirmationColor = new Color(162, 255, 143);
		//attemps to load confirmationColor, if cant its GREEN, sets up storage as well
		StorageValue.addStorageValues(StorageValue.createColorStorageValue(prefix, "confirmationColor", confirmationColor, c -> confirmationColor = c, () -> confirmationColor));
	}
	

	
	
	public Algorithm(String name, VisualSortingTool sortingTool)
	{
		this.name = name;
		this.sortingTool = sortingTool;
		swapColor = Color.RED;
		compareColor = Color.GREEN;
		setDefaultValues();
		addStorageValues();
	}
		
	/**
	 * from {@link Customizable}, populates its own {@link CustomizationPanel} with a title using
	 * the {@link Algorithm#name} variable and
	 * {@link ColorButton}s to change {@link Algorithm#swapColor} and {@link Algorithm#compareColor}
	 * @param cp this Algorithms own {@link CustomizationPanel} <br>
	 * <font color="red"> must call super() </font>
	 */
	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{		
		cp.addTitleSeperator(name, true);			
		cp.addRow(new ColorButton(sortingTool, c -> swapColor = c, () -> swapColor, "Swap Color"), true);
		cp.addRow(new ColorButton(sortingTool, c -> compareColor = c, () -> compareColor, "Compare Color"), true);
	}
	
	/**
	 * to override if this algorithm needs to set any default values before loading from preferences <br>
	 * called from constructor before values loaded
	 */
	@Override
	public void setDefaultValues() 
	{}
	
	/**
	 * to override if this algorithm needs to load/save any other values <br>
	 * by default all algorithms already save/store their swap color and compare color <br>
	 * called from constructor <br>
	 * <font color="red"> must call super() </font>
	 */
	@Override
	public void addStorageValues() 
	{
		//swap and compare colors getting set up for preferences
		StorageValue.addStorageValues(
				StorageValue.createColorStorageValue(getPrefix(), "swapColor", swapColor, c -> swapColor = c, () -> swapColor),
				StorageValue.createColorStorageValue(getPrefix(), "compareColor", compareColor, c -> compareColor = c, () -> compareColor)
		);
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
		isSorted(sortingTool, true);
		sortingTool.getSorter().setAlgorithm(null);
		GUIHandler.setEnabled(true);
	}
	
	/**
	 * Delays sort based on the spinner delay
	 */
	protected final static void delay(Sorter sorter)
	{
		VisualSortingTool.delay(delay);
	}
	
	/**
	 * adds customization for all the static values shared by algorithms, such as {@link #confirmationColor}
	 * @param cp this {@link CustomizationPanel} belongs to a sorter
	 */
	public static void addGeneralAlgorithmCustimizationComponents(VisualSortingTool sortingTool, JPanel panel)
	{
		//title placed with a little spacing under the sorter customization components
		//cp.addTitleSeperator("All Algorithms", true);
		RetrieveAction<Color> retrieveAction = new RetrieveAction<Color>() {
			
			@Override
			public Color retrieve()
			{
				return confirmationColor; 
			}
		};
		JButton button = new ColorButton(sortingTool, c -> confirmationColor = c, retrieveAction, "Confirmation Color");
		button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		panel.add(button);	
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
				sortingTool.repaint();
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
				sortingTool.repaint();
				VisualSortingTool.delay(10);
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