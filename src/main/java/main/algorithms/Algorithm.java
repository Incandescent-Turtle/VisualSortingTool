package main.algorithms;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.ui.GUIHandler;
import main.ui.custimization.ColorButton;
import main.ui.custimization.Customizable;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.BooleanStorageValue;
import main.ui.custimization.values.IntStorageValue;
import main.ui.custimization.values.StorageValue;
import main.vcs.VisualComponent;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

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

	public static boolean doHighlights;

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
		stepSize = 1;
		confirmationColor = new Color(162, 255, 143);
		animateConfirmation = false;
		doHighlights = true;

        StorageValue.addStorageValues(
				new IntStorageValue(prefix, "delay", n -> delay = n, () -> delay),
				new IntStorageValue(prefix, "step", n -> stepSize = n, () -> stepSize),
				StorageValue.createColorStorageValue(prefix, "confirmationColor", c -> confirmationColor = c, () -> confirmationColor),
				new BooleanStorageValue(prefix, "animateConfirmation", b -> animateConfirmation = b, () -> animateConfirmation),
				new BooleanStorageValue(prefix, "doHighlights", b -> doHighlights = b, () -> doHighlights)
		);
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
	public void setDefaultValues() {}
	
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
		long start =System.currentTimeMillis();
		currentStep = stepSize;
		runAlgorithm();
		finishRun();
		System.out.println((System.currentTimeMillis() - start)/1000f);
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

	/*
			methods to abstract some functionality
	 */

	/**
	 * calls {@link #delay()} and repaint on the sorter. also accounts for steps
	 */
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
	 * if highlights are enabled, it will highlight the specified index witht the color
	 * @param index the index of the VC array to highlight
	 * @param color the color to highlight it
	 */
	protected final void highlight(int index, Color color)
	{
		if(doHighlights)
			sortingTool.getSorter().getVisualizer().highlight(index, color);
	}

	/**
	 * calls {@link main.visualizers.bases.Visualizer#resetHighlightAt(int)} to reset the colors at
	 * the specified indices
	 * @param indices the indices to reset
	 */
	protected final void resetHighlightsAt(int... indices)
	{
		for (int index : indices)
		{
			sortingTool.getSorter().getVisualizer().resetHighlightAt(index);
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
		Supplier<Color> retrieveAction = () -> confirmationColor;
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

		//button to disable/enable highlighting (speeds up)
		JCheckBox highlightBox = new JCheckBox("Highlight");
		//highlightBox.setAlignmentX(JButton.CENTER_ALIGNMENT);
		highlightBox.setHorizontalTextPosition(SwingConstants.LEFT);
		highlightBox.addChangeListener(e -> {
			doHighlights = (highlightBox.isSelected());
		});

		panel.add(highlightBox);
		GUIHandler.addUpdatables(() -> cb.setSelected(animateConfirmation), () -> highlightBox.setSelected(doHighlights));
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
		//seconds for the animation
		int seconds = 2;
		//the delay needed for it to take 2 seconds
		int delay = (seconds*1_000_000_000)/sortingTool.getSorter().getArraySize();
		for(int i = 0; i<array.length; i++)
		{
			//if last element
			if(i == array.length-1)
			{
				
				if(animateConfirmation) sorter.getVisualizer().setConfirmed(true);
				sorter.getVisualizer().reloadHighlights();
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
				sortingTool.repaint();
				VisualSortingTool.delay(0, delay);
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