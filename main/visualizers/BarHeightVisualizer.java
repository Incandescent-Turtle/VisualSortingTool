package main.visualizers;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.SpinnerNumberModel;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.ui.custimization.CustomizationGUI;
import main.ui.custimization.CustomizationPanel;
import main.vcs.VisualComponent;
import main.visualizers.bases.Visualizer;

public class BarHeightVisualizer extends Visualizer
{	
	public BarHeightVisualizer(VisualSortingTool sortingTool)
	{
		super(sortingTool);
		//a nice blue
		setDefaultColor(new Color(144, 193, 215));
		minMargin = 2;
		componentWidth = 15;
		componentGap = 2;
	}
	
	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		SpinnerNumberModel nm = new SpinnerNumberModel(componentWidth, 2, 100, 1);
		cp.addRow("Bar Width:", CustomizationGUI.createJSpinner(sortingTool, nm, n -> componentWidth = n));
		nm = new SpinnerNumberModel(componentGap, 1, 20, 1);
		cp.addRow("Gap:", CustomizationGUI.createJSpinner(sortingTool, nm, n -> componentGap = n));
		nm = new SpinnerNumberModel(componentGap, 0, 100, 1);
		cp.addRow("Margin:", CustomizationGUI.createJSpinner(sortingTool, nm, n -> minMargin = n));
		cp.addRow(CustomizationGUI.createDefaultColorPickingButton(sortingTool), true);
		cp.addRow(CustomizationGUI.createMakePinkButton(sortingTool), true);
	}
	
	/**
	 * draws bars of different heights using the values as the height input
	 */
	@Override
	public void drawArray(Graphics g, Sorter sorter, VisualComponent[] array, int size)
	{			
		for(int i = 0; i < size; i++)
		{
			if(highlights[i] == Color.RED) g.setColor(Color.RED);
			//highlights in specified color (default is white)
			g.setColor(highlights[i]);
			g.fillRect(getRealHMargins(size) + i*(componentWidth+componentGap), sortingTool.getVisualizerHeight()-array[i].getValue(), componentWidth, array[i].getValue());
			//resets the highlights array
			highlights[i] = defaultColor; 
		}
	}
}