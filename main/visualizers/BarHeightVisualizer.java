package main.visualizers;

import java.awt.Color;
import java.awt.Graphics;
import java.util.prefs.Preferences;

import javax.swing.SpinnerNumberModel;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.sorters.Sorter.Sorters;
import main.ui.custimization.ColorButton;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.CustomizationPanel;
import main.util.Util;
import main.vcs.VisualComponent;
import main.visualizers.bases.Visualizer;

public class BarHeightVisualizer extends Visualizer
{	
	/**
	 * visualizes sorting via numbers corresponding to the height of bars
	 */
	public BarHeightVisualizer(VisualSortingTool sortingTool)
	{
		super(sortingTool, Sorters.BAR_HEIGHT);
	}
	
	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		SpinnerNumberModel nm = new SpinnerNumberModel(componentWidth, 2, 100, 1);
		//spinner to change bar width
		cp.addRow("Bar Width:", CustomizationGUI.createJSpinner(sortingTool, nm, n -> componentWidth = n));
		
		nm = new SpinnerNumberModel(componentGap, 2, 20, 1);
		//spinner to change gap between bars
		cp.addRow("Gap:", CustomizationGUI.createJSpinner(sortingTool, nm, n -> componentGap = n));
		
		nm = new SpinnerNumberModel(componentGap, 0, 100, 1);
		//spinner to change left/right margin
		cp.addRow("Margin:", CustomizationGUI.createJSpinner(sortingTool, nm, n -> minMargin = n));
		
		//default color button
		cp.addRow(ColorButton.createDefaultColorPickingButton(sortingTool, sortingTool.getSorter(identifier)), true);
		//change background button
		cp.addRow(ColorButton.createBackgroundColorPickingButton(sortingTool), true);
		//sets default color to pink button
		cp.addRow(CustomizationGUI.createMakePinkButton(sortingTool), true);
	}
	
	@Override
	public void loadValues(Preferences prefs, String prefix)
	{
		componentWidth = prefs.getInt(prefix + WIDTH, 15);
		componentGap = prefs.getInt(prefix + GAP, 2);
		minMargin = prefs.getInt(prefix + MARGIN, 2);
		defaultColor = Util.getColor(DEFAULT_COLOR, getPrefix(), new Color(144, 193, 215));
	}
	
	@Override
	public void storeValues(Preferences prefs, String prefix)
	{
		System.out.println(prefix);
		prefs.putInt(prefix + WIDTH, componentWidth);
		prefs.putInt(prefix + GAP, componentGap);
		prefs.putInt(prefix + MARGIN, minMargin);
		Util.putColor(DEFAULT_COLOR, getPrefix(), defaultColor);
	}
	
	/**
	 * draws bars of different heights using the values as the height input
	 */
	@Override
	public void drawArray(Graphics g, VisualComponent[] array, int size)
	{			
		for(int i = 0; i < size; i++)
		{
			if(highlights[i] == Color.RED) g.setColor(Color.RED);
			//highlights in specified color
			g.setColor(confirmed ? Algorithm.confirmationColor : highlights[i]);
			g.fillRect(getRealHMargins(size) + i*(componentWidth+componentGap), sortingTool.getVisualizerHeight()-array[i].getValue(), componentWidth, array[i].getValue());
			//resets the highlights array
			highlights[i] = defaultColor; 
		}
	}
}