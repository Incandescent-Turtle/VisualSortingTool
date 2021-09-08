package main.ui.custimization;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import main.VisualSortingTool;

@SuppressWarnings("serial")
public class GroupLayoutSorterPanel extends JPanel
{
	private final GroupLayout.ParallelGroup left;
	private final GroupLayout.ParallelGroup right;
	private final GroupLayout.SequentialGroup rows;
	private final GroupLayout gl;
	private final VisualSortingTool sortingTool;

	public GroupLayoutSorterPanel(VisualSortingTool sortingTool, CustimizationPanel custimizer)
	{
		this.sortingTool = sortingTool;
		setLayout(gl = new GroupLayout(this));
		left = gl.createParallelGroup();
		right = gl.createParallelGroup();
		rows = gl.createSequentialGroup();
		SpinnerNumberModel nm = new SpinnerNumberModel(10, 0, 100, 1); 
		
		JButton makePink = new JButton("Make Pink");
		makePink.addActionListener(e -> {
			sortingTool.getSorter().getVisualizer().setDefaultColor(new Color(222, 165,164));
			sortingTool.getSorter().getVisualizer().resetHighlights();
			sortingTool.repaint();
		});
		
		gl.setAutoCreateContainerGaps(true);
	    gl.setAutoCreateGaps(true);
		addRow(gl, new JLabel("Size:"), new JSpinner(nm));
		addRow(gl, new JLabel("Amount:"), new JSpinner(nm));
		addRow(gl, new JLabel("Gap:"), new JSpinner(nm));
		addRow(gl, new JLabel("Width:"), new JSpinner(nm));
		addRow(gl, new JLabel("Height Scaling:"), new JSpinner(nm));
		addRow(gl, custimizer.createDefaultColorPickingButton(), null);
		addRow(gl, makePink, null);
	//	addRow(gl, null, defaultColorPickingButton);
		//addRow(gl, null, makePink);
		placeComponents(gl);
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
	
	private void placeComponents(GroupLayout gl)
	{	
		gl.setHorizontalGroup(
				gl.createSequentialGroup()
				.addGroup(left)
				.addGroup(right));
		
		gl.setVerticalGroup(rows);
	}
}