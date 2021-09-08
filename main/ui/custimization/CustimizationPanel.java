package main.ui.custimization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import main.VisualSortingTool;
import main.util.ColorAction;
import main.util.ColorRetrieveAction;
import main.visualizers.bases.Visualizer;

@SuppressWarnings("serial")
public class CustimizationPanel extends JTabbedPane
{
	private final VisualSortingTool sortingTool;
	public final JButton defaultColorPickingButton;
	private  GroupLayout.ParallelGroup left;
	private  GroupLayout.ParallelGroup right;
	private  GroupLayout.SequentialGroup rows;

	public CustimizationPanel(VisualSortingTool sortingTool) 
	{
		this.sortingTool = sortingTool;
		sortingTool.add(this, BorderLayout.LINE_END);
		defaultColorPickingButton = createDefaultColorPickingButton();

		add("Bar Height", new SorterPanel(sortingTool, this));

		revalidate();
		JPanel p2 = new JPanel();
		add("Color Gradient", new SorterPanel(sortingTool, this));
		JPanel p3 = new JPanel();
		add("Numbers", p3);
	}
	
	private void placeComponents(GroupLayout gl)
	{	
		gl.setHorizontalGroup(
				gl.createSequentialGroup()
				.addGroup(left)
				.addGroup(right));
		
		gl.setVerticalGroup(rows);
	}
	
	private void addRow(GroupLayout gl, JComponent leftComponent, JComponent rightComponent)
	{
		GroupLayout.ParallelGroup row = gl.createParallelGroup();
		if(leftComponent != null)
		{
			row.addComponent(leftComponent);
			left.addComponent(leftComponent, GroupLayout.Alignment.CENTER);
		}
		if(rightComponent != null)
		{
			row.addComponent(rightComponent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
			right.addComponent(rightComponent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		}
		rows.addGroup(row);
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