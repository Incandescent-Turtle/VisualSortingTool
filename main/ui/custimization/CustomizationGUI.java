package main.ui.custimization;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.VisualSortingTool;
import main.sorters.Sorter;
import main.ui.custimization.interfaces.ColorAction;
import main.ui.custimization.interfaces.ColorRetrieveAction;
import main.ui.custimization.interfaces.SpinnerChangeAction;
import main.visualizers.bases.Visualizer;

@SuppressWarnings("serial")
public class CustomizationGUI extends JPanel
{
	private CardLayout cl = new CardLayout();
	
	public CustomizationGUI(VisualSortingTool sortingTool) 
	{
		setLayout(cl);
		for(Sorter s : sortingTool.getSorters())
		{
			s.setCustomizationPanel();
			add(s.getCustomizationPanel(), s.toString());
		}
		
	//	add(new SorterPanel(sortingTool), "Bar Heights");
	//	add(new SorterPanel(sortingTool), "Color Gradient");
		sortingTool.add(this, BorderLayout.LINE_END);
	}
	
	public void changePanel(Sorter sorter)
	{
		cl.show(this, sorter.toString());
	}
	
	public static JButton createMakePinkButton(VisualSortingTool sortingTool)
	{
		JButton makePink = new JButton("Make Pink");
		makePink.addActionListener(e -> {
			sortingTool.getSorter().getVisualizer().setDefaultColor(new Color(222, 165,164));
			sortingTool.getSorter().getVisualizer().resetHighlights();
			sortingTool.repaint();
		});
		return makePink;
	}
	
	public static JButton createColorPickingButton(VisualSortingTool sortingTool, ColorAction okAction, ColorRetrieveAction retrieveAction)
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
	
	public static JButton createDefaultColorPickingButton(VisualSortingTool sortingTool)
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
		
		return createColorPickingButton(sortingTool, okAction, retrieveAction);
	}
	
	public static JSpinner createJSpinner(VisualSortingTool sortingTool, SpinnerNumberModel nm, SpinnerChangeAction scl)
	{

		JSpinner spinner = new JSpinner(nm);
		spinner.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				scl.changeAction((int) spinner.getValue());
				if(sortingTool != null) sortingTool.getSorter().recalculateAndRepaint();
			}
		});
		return spinner;
	
	}
}