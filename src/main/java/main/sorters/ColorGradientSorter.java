package main.sorters;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.ui.GUIHandler;
import main.ui.custimization.ColorButton;
import main.ui.custimization.CustomizationPanel;
import main.ui.custimization.values.BooleanStorageValue;
import main.ui.custimization.values.StorageValue;
import main.vcs.ColorVisualComponent;
import main.visualizers.ColorGradientVisualizer;

import javax.swing.*;
import java.awt.*;

public class ColorGradientSorter extends BarSorter
{
	//the two colors represented in the gradient
	private Color leftColor, rightColor;
	//clicking this reverses the order. doesn't really matter which value means what though?
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
		super.setDefaultValues();
		leftColor = Color.CYAN;
		rightColor = Color.BLUE;
		order = false;
	}
	
	//calling this function to place these buttons after the other sorter customization options
	@Override
	public void addCustomizationComponents(CustomizationPanel cp)
	{
		/*
		 * calls super cause this isn't the proper method to add sorter components
		 * places all these components after all other necessary components
		 */
		super.addCustomizationComponents(cp);

		//button to change which color is on the left
		ColorButton leftColorButton = new ColorButton(sortingTool, c -> {
			this.leftColor = c; recalculateAndRepaint();}, () -> this.leftColor, "Left Color");
		cp.addRow(leftColorButton, true);
		
		//button to change which color is on the right
		ColorButton rightColorButton = new ColorButton(sortingTool, c -> {
			rightColor = c; recalculateAndRepaint();}, () -> rightColor, "Right Color");
		cp.addRow(rightColorButton, true);
		
		//button to change order
		JButton orderButton = new JButton("Reverse Order") 
		{
			@Override
			protected void paintComponent(Graphics g) 
			{
				super.paintComponent(g);
				//sets the button/text colors to represent the order. bg is right
				setBackground(order ? ColorGradientSorter.this.leftColor : rightColor);
				setForeground(!order ? ColorGradientSorter.this.leftColor : rightColor);
			}
		};
		//on press reverses order and if no algo running re-organizes and re-paints to show it 
		orderButton.addActionListener(e -> reverseOrder());
		cp.addRow(orderButton, true);

		cp.addRow(ColorButton.createBackgroundColorPickingButton(sortingTool), true);

		GUIHandler.addToggleable(leftColorButton, rightColorButton, orderButton);
		//for when it's set to default etc. recolours it
		GUIHandler.addUpdatables(orderButton::repaint);
	}
	
	@Override
	public void addStorageValues()
	{
		super.addStorageValues();
		StorageValue.addStorageValues(
				StorageValue.createColorStorageValue(getPrefix(), "leftColor", c -> leftColor = c, () -> leftColor),
				StorageValue.createColorStorageValue(getPrefix(), "rightColor", c -> rightColor = c, () -> rightColor),
				new BooleanStorageValue(getPrefix(), "order", b -> order = b, () -> order)
		);
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
	        int red = (int) (rightColor.getRed() * ratio + leftColor.getRed() * (1 - ratio));
	        int green = (int) (rightColor.getGreen() * ratio + leftColor.getGreen() * (1 - ratio));
	        int blue = (int) (rightColor.getBlue() * ratio + leftColor.getBlue() * (1 - ratio));
	        //loads the VC with the color
			array[i] = new ColorVisualComponent(i, new Color(red, green, blue));
		}
	}
	
	/**
	 * this method swaps the internal orders of the VCs so the colors sort in a reverse direction,
	 * and also re-orders the list and re-prints to display properly
	 */
	private void reverseOrder()
	{
		order=!order;
		//switching the colors and repainting the relevant buttons
		Color temp = leftColor;
		leftColor = rightColor;
		rightColor = temp;
		ColorButton.recolorButtons();
		reloadArray();
		//if un-sorted, returns after switch
		if(!Algorithm.isSorted(sortingTool, false)) return;
		visualizer.resetHighlights();
		sortingTool.repaint();
	}
}