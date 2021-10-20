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
	//whether there should be an animation for the confirmation (all bars slowly turning green)
	private static boolean animateConfirmation;
	
	//delay is ms
	public static int delay;
	//whether it renders/delays for every run, every other, etc
	public static int stepSize;

	private final String name;
	protected VisualSortingTool sortingTool;
	
	protected Color swapColor, compareColor;
	//whether this is the first (0), second (1) etc. step to check if delay and render is needed
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
		//attempts to load confirmationColor, if can't its GREEN, sets up storage as well
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
	 * @param cp this Algorithm's own {@link CustomizationPanel} <br>
	 * <font color="red"> must call super() </font>
	 */
	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{		
		cp.addTitleSeparator(name, true);
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
	 * gets run on a separate thread.
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
	private void finishRun()
	{
		System.out.println("Done " + name);
		isSorted(sortingTool, true);
		sortingTool.getSorter().setAlgorithm(null);
		GUIHandler.setEnabled(true);
	}
	
	protected final void paintWithDelayAndStep()
	{
		currentStep++;
		if(currentStep >= stepSize)
		{
			delay();
			sortingTool.repaint();
			currentStep = 0;
		}
	}
	
	/**
	 * Delays sort based on the spinner delay
	 */
	protected static void delay()
	{
		VisualSortingTool.delay(delay);
	}

	/**
	 * adds customization for all the static values shared by algorithms, such as {@link #confirmationColor}
	 * @param sortingTool the sorting tool
	 * @param panel panel to add components to
	 */
	public static void addGeneralAlgorithmCustomizationComponents(VisualSortingTool sortingTool, JPanel panel)
	{
		RetrieveAction<Color> retrieveAction = () -> confirmationColor;
		JButton button = new ColorButton(sortingTool, c -> confirmationColor = c, retrieveAction, "Confirmation Color");
		button.setAlignmentX(JButton.CENTER_ALIGNMENT);
		panel.add(button);
		
		JCheckBox cb = new JCheckBox(" Animate Confirmation");
		cb.setAlignmentX(JButton.CENTER_ALIGNMENT);
		cb.setHorizontalTextPosition(SwingConstants.LEFT);
		cb.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,12));
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
				//printArray(sortingTool); //debug
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
	
	/*
	 * debugger for the arrays. prints the sorted list to a text file
	 */
	/*private static void printArray(VisualSortingTool sortingTool)
	{
		try
		{
			FileWriter pw = new FileWriter(new File("sorter.txt"));
			Stream.of(sortingTool.getSorter().getArray()).forEach(vc -> {
				try
				{
					pw.write("" + vc.getValue() + "\n");
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			pw.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	*/
	
	@Override
	public String toString()
	{
		return name;
	}
}