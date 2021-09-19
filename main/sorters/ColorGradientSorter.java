package main.sorters;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.ui.GUIHandler;
import main.ui.custimization.ColorButton;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.BooleanStorageValue;
import main.ui.custimization.values.StorageValue;
import main.vcs.ColorVisualComponent;
import main.vcs.VisualComponent;
import main.visualizers.ColorGradientVisualizer;

public class ColorGradientSorter extends Sorter
{
	//the two colors represented in the gradient
	private Color color1, color2;
	//clicking this reverses the order. doesnt really matter which value means what though?
	private boolean order;
	
	/**
	 * a {@link Sorter} to use a blue color gradient to visualize sorting
	 */
	public ColorGradientSorter(VisualSortingTool sortingTool)
	{
		super(sortingTool, new ColorGradientVisualizer(sortingTool), Sorters.COLOR_GRADIENT);
	}
	
	//setting up the values for the gradient sorter
	@Override
	public void setDefaultValues()
	{
		color1 = Color.CYAN;
		color2 = Color.BLUE;
		order = false;
	}
	
	//we calling this function to place these buttons after the other sorter customization options
	@SuppressWarnings("serial")
	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		/*
		 * calls super cause this isnt the proper method to add sorter components
		 * places all these componenets after all other necesary components
		 */
		super.addCustomizationComponents(cp);
		
		//color buttons that cannot be hit mid run
		
		//button to change color1
		ColorButton button1 = new ColorButton(sortingTool, c -> {color1 = c; recalculateAndRepaint();}, () -> color1, "First Color");
		cp.addRow(button1, true);
		
		//button to change color2
		ColorButton button2 = new ColorButton(sortingTool, c -> {color2 = c; recalculateAndRepaint();}, () -> color2, "Second Color");
		cp.addRow(button2, true);
		
		//button to change order
		JButton orderButton = new JButton("Reverse Order") 
		{
			@Override
			protected void paintComponent(Graphics g) 
			{
				super.paintComponent(g);
				//sets the button/text colors to represent the order. bg is right
				setBackground(order ? color1 : color2);
				setForeground(!order ? color1 : color2);
			}
		};
		//on press reverses order and if no algo running re-organizes and re-paints to show it 
		orderButton.addActionListener(e -> reverseOrder());
		cp.addRow(orderButton, true);
		
		GUIHandler.addToggleable(button1, button2, orderButton);
		//for when its set to default etc recolours it
		GUIHandler.addUpdatables(() -> orderButton.repaint());
	}
	
	@Override
	public void addStorageValues()
	{
		StorageValue.addStorageValues(
				StorageValue.createColorStorageValue(getPrefix(), "color1", c -> color1 = c, () -> color1),
				StorageValue.createColorStorageValue(getPrefix(), "color2", c -> color2 = c, () -> color2),
				new BooleanStorageValue(getPrefix(), "order", b -> order = b, () -> order)
		);
	}
	
	/**
	 * resizes based on window size and bar sizes/gaps
	 */
	@Override
	protected void resizeArray()
	{
		int barWidth = visualizer.getComponentWidth();
		int barGap = visualizer.getComponentGap();
		size = (sortingTool.getVisualizerWidth() - visualizer.getMinMargin()*2 + barGap)/(barWidth+barGap);
		//size was -1 when visualizer had 0 width...this solved it
		if(size <= 0) size = 10;
			super.resizeArray();
	}
	
	/**
	 * fills array with ColorVCs that form a gradient
	 */
	@Override
	protected void reloadArray()
	{
		for(int i = 0; i < size; i++)
		{
	    	float ratio =  i / (float) size;
	        int red = (int) (color2.getRed() * ratio + color1.getRed() * (1 - ratio));
	        int green = (int) (color2.getGreen() * ratio + color1.getGreen() * (1 - ratio));
	        int blue = (int) (color2.getBlue() * ratio + color1.getBlue() * (1 - ratio));
	        //whether it fills from the back or front
	        int index = order ? size-1-i : i;
	        //loads the VC with the color
			array[i] = new ColorVisualComponent(index, new Color(red, green, blue));
		}
	}
	
	/**
	 * this method swaps the internal orders of the VCs so the colors sort in a reverse direction,
	 * and also re-orders the list and re-prints to display properly
	 */
	private void reverseOrder()
	{
		order=!order;
		//if un-sorted, returns after switch
		if(!Algorithm.isSorted(sortingTool, false)) return;
		
		//if sorted
		
		//changes the value of the VCs to switch switch color is left or right
		VisualComponent[] tempArray = array.clone();
		for(int i = 0; i < size; i++)
		{
			//the new value/index for it to take once reversed
			int newValue = (size-1) - tempArray[i].getValue();
			tempArray[i].setValue(newValue);
			//this ends up reversing the list
			array[newValue] = tempArray[i];
		}
		visualizer.resetHighlights();
		sortingTool.repaint();
	}
}