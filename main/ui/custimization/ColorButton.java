package main.ui.custimization;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JColorChooser;

import main.VisualSortingTool;
import main.VisualizationPanel;
import main.sorters.Sorter;
import main.visualizers.bases.Visualizer;

@SuppressWarnings("serial")

/**
 * 
 */
public class ColorButton extends JButton implements ActionListener
{
	private final static ArrayList<ColorButton> COLOR_BUTTONS = new ArrayList<>();
	
	private VisualSortingTool sortingTool;
	private ColorAction okAction;
	private ColorRetrieveAction retrieveAction;
	private String text;
	
	/**
	 * A JButton fitted with a color chooser and custom ActionListener implementation
	 * for ease of use <br>
	 * all ColorButtons are automatically added to {@link COLOR_BUTTONS} <br>
	 * these take on the color of their {@link ColorButton#retrieveAction}
	 *
	 * @param okAction the action to be completed when the ok button is hit
	 * @param retrieveAction action to get the default color
	 * @param text the text to appear on the button and frame
	 */
	public ColorButton(VisualSortingTool sortingTool, ColorAction okAction, ColorRetrieveAction retrieveAction, String text)
	{
		super(text);
		COLOR_BUTTONS.add(this);
		this.sortingTool = sortingTool;
		this.okAction = okAction;
		this.retrieveAction = retrieveAction;
		this.text = text;
		
		setBackground(retrieveAction.retrieveColor());
		addActionListener(this);	
	} 
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		//disables all color buttons until done
		enableColorButtons(false);
	    final JColorChooser colorChooser = new JColorChooser(retrieveAction.retrieveColor());
	    ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				enableColorButtons(true);
				//sets the color to the chosen color
				okAction.doStuff(colorChooser.getColor());
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
			colorButton.setBackground(colorButton.retrieveAction.retrieveColor());
		}
	}
	
	/**
	 * Helper to create a ColorButton that changes the default color of the {@link Visualizer}
	 * @param sortingTool
	 * @param sorter the sorter which the {@link Visualizer} is attached to
	 * @return new ColorButton which changes the default color of its {@link Visualizer}
	 */
	public static ColorButton createDefaultColorPickingButton(VisualSortingTool sortingTool, Sorter sorter)
	{
		Visualizer visualizer = sorter.getVisualizer();
		ColorAction okAction = new ColorAction() {
			
			@Override
			public void doStuff(Color color)
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
		ColorAction okAction = new ColorAction() {
			
			@Override
			public void doStuff(Color color)
			{
				sortingTool.getVisualizationPanel().setBackground(color);
				sortingTool.repaint();
			}
		};
		return new ColorButton(sortingTool, okAction, () -> sortingTool.getVisualizationPanel().getBackground(), "Background Color");
	}
	
	/**
	  * when a new color is picked, this action is carried out <br>
	  *	for use as function interface
	 */
	public interface ColorAction
	{
		void doStuff(Color color);	
	}
	
	/**
	 *	for setting the default choose color in case no color is selected <br>
	 *	for use as function interface
	 */
	public interface ColorRetrieveAction
	{
		Color retrieveColor();
	}
}