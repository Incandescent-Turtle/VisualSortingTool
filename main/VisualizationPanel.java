package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class VisualizationPanel extends JPanel
{
	private VisualSortingTool sortingTool;
	
	public VisualizationPanel(VisualSortingTool sortingTool)
	{
		this.sortingTool = sortingTool;
		setBackground((Color.GRAY));
	}
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		if(getWidth() > 0)sortingTool.getSorter().getVisualizer().drawArray(g);
	}
}