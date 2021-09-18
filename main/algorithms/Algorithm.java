package main.algorithms;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import main.VisualSortingTool;
import main.interfaces.RetrieveAction;
import main.sorters.Sorter;
import main.ui.GUIHandler;
import main.ui.custimization.ColorButton;
import main.ui.custimization.Customizable;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.BooleanStorageValue;
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
	//whether a there should be a animation for the confirmation (all bars slowly turning green)
	private static boolean animateConfirmation;
	
	//delay is ms
	public static int delay;
	//whether it renders/delays for every every, every other, etc
	public static int stepSize;

	private String name;
	protected VisualSortingTool sortingTool;
	
	protected Color swapColor, compareColor;
	//whether this is the first (0), second (1) etc step to check if delay and render is needed
	protected int currentStep = 0;
	
	//for setting up static values for all algorithms
	static
	{
		//class prefix
		final String prefix = VisualSortingTool.getPrefix(Algorithm.class);

		delay = 10;
        StorageValue.addStorageValues(new IntStorageValue(prefix, "delay", n -> delay = n, () -> delay));

        stepSize = 1;
        StorageValue.addStorageValues(new IntStorageValue(prefix, "step", n -> stepSize = n, () -> stepSize));

        confirmationColor = new Color(162, 255, 143); //green
		//attemps to load confirmationColor, if cant its GREEN, sets up storage as well
		StorageValue.addStorageValues(StorageValue.createColorStorageValue(prefix, "confirmationColor", c -> confirmationColor = c, () -> confirmationColor));

		animateConfirmation = false;
		StorageValue.addStorageValues(new BooleanStorageValue(prefix, "animateConfirmation", b -> animateConfirmation = b, () -> animateConfirmation));
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
				StorageValue.createColorStorageValue(getPrefix(), "swapColor", c -> swapColor = c, () -> swapColor),
				StorageValue.createColorStorageValue(getPrefix(), "compareColor", c -> compareColor = c, () -> compareColor)
		);
	}
	
	/**
	 * This gets called when this is the selected algorithm and the run button is hit <br>
	 * gets run on a seperate thread. 
	 */
	public final void run()
	{
		currentStep = stepSize;
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
		//VisualSortingTool.delay(250);
		//sortingTool.getSorter().recalculateAndRepaint();
		//sortingTool.getSorter().setAlgorithm(this);
		//VisualSortingTool.delay(0);
		//run();
	}
	
	protected final void paintWithDelayAndStep()
	{
		currentStep++;
		if(currentStep >= stepSize)
		{
			delay(sortingTool.getSorter());
			sortingTool.repaint();
			currentStep = 0;
		}
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
		
		JCheckBox cb = new JCheckBox(" Animate Confirmation");
		cb.setAlignmentX(JButton.CENTER_ALIGNMENT);
		cb.setHorizontalTextPosition(SwingConstants.LEFT);
		cb.setFont(new Font(Font.SANS_SERIF,0,12));
		cb.setSelected(animateConfirmation);
		cb.addChangeListener(e -> animateConfirmation = cb.isSelected());
		panel.add(cb);
		GUIHandler.addUpdatables(() -> cb.setSelected(animateConfirmation));
	}

	/**
	 * Left to right turns components to {@link #confirmationColor} until done
	 * @param shouldPaint whether this should be run with painting/delay methods or not (visualized or not)
	 */
	public static boolean isSorted(VisualSortingTool sortingTool, boolean shouldPaint)
	{
		Sorter sorter = sortingTool.getSorter();
		VisualComponent[] array = sorter.getArray();
		Color[] highlights = sorter.getVisualizer().getHighlights().clone();
		for(int i = 0; i<array.length; i++)
		{
			//if last element
			if(i == array.length-1)
			{
				if(animateConfirmation) sorter.getVisualizer().setConfirmed(true);
				sorter.getVisualizer().resetHighlights();
				sortingTool.repaint();
				return true;
			}
			
			//returns false if incorrect
			if(i != array.length-1 && array[i].getValue()>array[i+1].getValue()) return false;
			//one by one colors them with confirmationColor
			if(shouldPaint && animateConfirmation)
			{
				for(int j = 0; j <= i+1; j++)
				{
					highlights[j] = confirmationColor;
				}
				sorter.getVisualizer().setHighlights(highlights);
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