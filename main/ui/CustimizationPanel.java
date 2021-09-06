package main.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

import main.VisualSortingTool;
import main.util.ColorAction;
import main.util.ColorRetrieveAction;
import main.visualizers.bases.Visualizer;

@SuppressWarnings("serial")
public class CustimizationPanel extends JTabbedPane
{
	private final VisualSortingTool sortingTool;
	private final JButton defaultColorPickingButton;
	
	public CustimizationPanel(VisualSortingTool sortingTool) 
	{
		this.sortingTool = sortingTool;
		defaultColorPickingButton = createDefaultColorPickingButton();
		setPreferredSize(new Dimension(200, sortingTool.getHeight()));
		sortingTool.add(this, BorderLayout.LINE_END);
		JPanel tab1 = new JPanel();
		tab1.setLayout(new BoxLayout(tab1, BoxLayout.Y_AXIS));
		add("Bar Height", tab1);
		tab1.add(createPanelWithLabelAndSpinner("Size:"));
		tab1.add(createPanelWithLabelAndSpinner("Amount:"));
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)) {
			@Override
			public Dimension getMaximumSize()
			{
				return new Dimension(super.getMaximumSize().width, getPreferredSize().height);
			}
		};
		JLabel label = new JLabel("Color:");
		label.setPreferredSize(new Dimension(40, label.getPreferredSize().height));
		panel.add(label);
		panel.add(defaultColorPickingButton);
		tab1.add(panel);		

		
		JButton makePink = new JButton("Make Pink");
		makePink.addActionListener(e -> {
			sortingTool.getSorter().getVisualizer().setDefaultColor(new Color(222, 165,164));
			sortingTool.getSorter().getVisualizer().resetHighlights();
			sortingTool.repaint();
		});
		tab1.add(createPanel(makePink));

		revalidate();
		JPanel p2 = new JPanel();
		add("Color Gradient", p2);
		JPanel p3 = new JPanel();
		add("Numbers", p3);
	}
	
	private JPanel createPanelWithLabelAndSpinner(String name)
	{
		JPanel panel = createPanelWithLabel(name);
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));
		panel.add(spinner);
		return panel;
	}
	
	private JPanel createPanelWithLabel(String label)
	{
		JPanel panel = createPanel();
		JLabel lbl = new JLabel(label) {
			@Override
			public Dimension getPreferredSize()
			{
				return new Dimension(40, super.getPreferredSize().height);
			}
		};
		panel.add(lbl);
		return panel;
	}
	
	private JPanel createPanel()
	{
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)) {
			@Override
			public Dimension getMaximumSize()
			{
				return new Dimension(super.getMaximumSize().width, getPreferredSize().height);
			}
		};
		return panel;
	}
	
	private JPanel createPanel(JComponent... components)
	{
		JPanel panel = createPanel();
		for(JComponent component : components)
		{
			panel.add(component);
		}
		return panel;
	}

	private JButton createColorPickingButton(ColorAction okAction, ColorRetrieveAction retrieveAction)
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
	
	private JButton createDefaultColorPickingButton()
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