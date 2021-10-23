package main.ui.custimization;

import main.VisualSortingTool;
import main.VisualizationPanel;
import main.sorters.Sorter;
import main.visualizers.bases.Visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ColorButton extends JButton implements ActionListener
{
	//used for recoloring/enabling/disabling color buttons
	private final static ArrayList<ColorButton> COLOR_BUTTONS = new ArrayList<>();
	
	private final VisualSortingTool sortingTool;
	private final Consumer<Color> okAction;
	private final Supplier<Color> retrieveAction;
	private final String text;
	
	/**
	 * A JButton fitted with a color chooser and custom ActionListener implementation
	 * for ease of use <br>
	 * all ColorButtons are automatically added to {@link #COLOR_BUTTONS} <br>
	 * these take on the color of their {@link ColorButton#retrieveAction}
	 *
	 * @param okAction the action to be completed when the ok button is hit
	 * @param retrieveAction action to get the default color
	 * @param text the text to appear on the button and frame
	 */
	public ColorButton(VisualSortingTool sortingTool, Consumer<Color> okAction, Supplier<Color> retrieveAction, String text)
	{
		super(text);
		//adds for recolouring etc
		COLOR_BUTTONS.add(this);
		this.sortingTool = sortingTool;
		this.okAction = okAction;
		this.retrieveAction = retrieveAction;
		this.text = text;
		
		setBackground(retrieveAction.get());
		addActionListener(this);	
	} 
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		//disables all color buttons until done
		ColorButton.enableColorButtons(false);
	    final JColorChooser colorChooser = CustomizationGUI.COLOR_CHOOSER;
		colorChooser.setColor(retrieveAction.get());

	    ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//re-enables all color buttons
				ColorButton.enableColorButtons(true);
				//sets the color to the chosen color
				okAction.accept(colorChooser.getColor());
				//sets the new button background to represent the new color
				setBackground(colorChooser.getColor());
			}
		};
		
	    JColorChooser.createDialog(
	    		sortingTool.getFrame(), 
	    		"Choose " + text, 
	    		false, 
	    		colorChooser, 
	    		al,
				e1 -> enableColorButtons(true)) //on cancel re-enables color buttons
	    .setVisible(true);
	}
	
	/**
	 * switches from black or white to make text more readable
	 */
	private void makeTextReadable()
	{
		Color bgColor = getBackground();
		
		/*
		 * https://stackoverflow.com/questions/4672271/reverse-opposing-colors
		 * brimborium on stackoverflow
		 * checks the brightness to see whether black or white is needed
		 */
		double y = (299 * bgColor.getRed() + 587 * bgColor.getGreen() + 114 * bgColor.getBlue()) / 1000;
		setForeground(y >= 128 ? Color.black : Color.white);
	}
	
	//overriding to make text readable
	@Override
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
		makeTextReadable();
	}
	
	/**
	 * enables/disables everything in the {@link ColorButton#COLOR_BUTTONS} array
	 */
	private static void enableColorButtons(boolean enable)
	{
		for(ColorButton colorButton : COLOR_BUTTONS)
		{
			colorButton.setEnabled(enable);
		}
	}
	
	/**
	 * recolours buttons in {@link ColorButton#COLOR_BUTTONS} using their {@link #retrieveAction}
	 */
	public static void recolorButtons()
	{
		for(ColorButton colorButton : COLOR_BUTTONS)
		{
			colorButton.setBackground(colorButton.retrieveAction.get());
		}
	}
	
	/**
	 * Helper to create a ColorButton that changes the default color of the {@link Visualizer}
	 * @param sortingTool the sorting tool
	 * @param sorter the sorter which the {@link Visualizer} is attached to
	 * @return new ColorButton which changes the default color of its {@link Visualizer}
	 */
	public static ColorButton createDefaultColorPickingButton(VisualSortingTool sortingTool, Sorter sorter)
	{
		Visualizer visualizer = sorter.getVisualizer();
		Consumer<Color> okAction = new Consumer<>() {
			
			@Override
			public void accept(Color color)
			{
				visualizer.setDefaultColor(color);
				if(sorter.getAlgorithm() == null)
				{
					visualizer.resetHighlights();
					sortingTool.repaint();
				}
			}
		};
		return new ColorButton(sortingTool, okAction, () -> visualizer.getDefaultColor(), "Default Color");
	}
	
	/**
	 * @return ColorButton that changes the background color of the {@link VisualizationPanel}
	 */
	public static ColorButton createBackgroundColorPickingButton(VisualSortingTool sortingTool)
	{
		Consumer<Color> okAction = new Consumer<>() {
			
			@Override
			public void accept(Color color)
			{
				sortingTool.getVisualizationPanel().setBackground(color);
				sortingTool.repaint();
			}
		};
		return new ColorButton(sortingTool, okAction, () -> sortingTool.getVisualizationPanel().getBackground(), "Background Color");
	}
}