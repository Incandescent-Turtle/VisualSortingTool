package main.ui.custimization;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import main.VisualSortingTool;
import main.util.ColorAction;
import main.util.ColorRetrieveAction;
import main.visualizers.bases.Visualizer;

@SuppressWarnings("serial")
public class CustimizationPanel extends JPanel
{
	private final VisualSortingTool sortingTool;
	private CardLayout cl = new CardLayout();
	
	public CustimizationPanel(VisualSortingTool sortingTool) 
	{
		setLayout(cl);
		this.sortingTool = sortingTool;
		add(new GridBagLayoutSortingPanel(sortingTool, this), "Bar Heights");
		add(new GroupLayoutSorterPanel(sortingTool, this), "Color Gradient");
		//add("Bar Height", new GroupLayoutSorterPanel(sortingTool, this));
		revalidate();
		sortingTool.add(this, BorderLayout.LINE_END);
	}
	
	public void changePanel(String name)
	{
		cl.show(this, name);
	}
	protected JButton createColorPickingButton(ColorAction okAction, ColorRetrieveAction retrieveAction)
	{
		JButton button = new JButton("Choose Color");
		button.addActionListener(e -> {
		    final JColorChooser colorChooser = new JColorChooser(retrieveAction.retrieveColor());
		    ActionListener al = new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					okAction.doStuff(colorChooser.getColor());
				}
			};
			
		    JColorChooser.createDialog(
					sortingTool.getFrame(), 
					"Choose Background Color", 
					false,
					colorChooser, 
					al,
					null).setVisible(true);
		});
		return button;
	}
	
	protected JButton createDefaultColorPickingButton()
	{
		ColorAction okAction = new ColorAction() {
			
			@Override
			public void doStuff(Color color)
			{
				Visualizer visualizer = sortingTool.getSorter().getVisualizer();
				visualizer.setDefaultColor(color);
				if(sortingTool.getSorter().getAlgorithm() == null)
				{
					visualizer.resetHighlights();
					sortingTool.repaint();
				}
			}
	};
		
		ColorRetrieveAction retrieveAction = new ColorRetrieveAction() {
			
			@Override
			public Color retrieveColor()
			{
				return sortingTool.getSorter().getVisualizer().getDefaultColor(); 
			}
		};
		
		return createColorPickingButton(okAction, retrieveAction);
	}
}