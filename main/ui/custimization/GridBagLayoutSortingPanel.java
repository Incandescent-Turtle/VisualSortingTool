package main.ui.custimization;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import main.VisualSortingTool;

@SuppressWarnings("serial")
public class GridBagLayoutSortingPanel extends JPanel
{
	int rowNum = 0;
	public GridBagLayoutSortingPanel(VisualSortingTool sortingTool, CustimizationPanel custimizer)
	{
		super(new GridBagLayout());
		TitledBorder border = new TitledBorder("Custimization");
		border.setTitleJustification(TitledBorder.CENTER);
		setBorder(border);
		SpinnerNumberModel nm = new SpinnerNumberModel(10, 0, 100, 1); 
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(1,5,1,3);
		addRow(c, new JLabel("Size: "), new JSpinner(nm));
		addRow(c, new JLabel("Amount: "), new JSpinner(nm));
		addRow(c, new JLabel("BarHeight: "), new JSpinner(nm));
		addRow(c, custimizer.createDefaultColorPickingButton());
		JButton makePink = new JButton("Make Pink");
		makePink.addActionListener(e -> {
			sortingTool.getSorter().getVisualizer().setDefaultColor(new Color(222, 165,164));
			sortingTool.getSorter().getVisualizer().resetHighlights();
			sortingTool.repaint();
		});
		addRow(c, makePink);
       /* c.gridx = 0;
        c.gridy = rowNum;
        c.weighty = 0;
        c.weightx = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.fill = GridBagConstraints.VERTICAL;
        JLabel l = new JLabel("Size:");
        l.setBackground(Color.RED);
        l.setOpaque(true);
        add(l, c);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor =  GridBagConstraints.NORTHWEST;
        add(new JSpinner(nm), c);
        
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0;
        c.weightx = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.fill = GridBagConstraints.VERTICAL;
        JLabel l1 = new JLabel("Size:");
        l.setBackground(Color.RED);
        l.setOpaque(true);
        add(l1, c);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor =  GridBagConstraints.NORTHWEST;
        add(new JSpinner(nm), c);
        */
        c.gridx = 0;
        c.gridy = rowNum;
        c.weighty = 1;
        c.weightx = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.fill = GridBagConstraints.VERTICAL;
        JLabel fill = new JLabel();
        fill.setBackground(Color.GRAY);
        fill.setOpaque(true);
        add(fill, c);
	}
	
	private void addRow(GridBagConstraints c, JComponent component)
	{
		c.gridx = 0;
        c.gridy = rowNum;
        c.weighty = 0;
        c.weightx = 1;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.fill = GridBagConstraints.VERTICAL;
        add(component, c);
        rowNum++;
	}
	
	private void addRow(GridBagConstraints c, JComponent left, JComponent right)
	{
		c.gridx = 0;
        c.gridy = rowNum;
        c.weighty = 0;
        c.weightx = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.fill = GridBagConstraints.VERTICAL;
        add(left, c);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.gridx = 1;
        c.gridy = rowNum;
        c.fill = GridBagConstraints.NONE;
        c.anchor =  GridBagConstraints.NORTHWEST;
        add(right, c);
        rowNum++;
	}
}