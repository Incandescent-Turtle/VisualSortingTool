package main;

import java.awt.*;

import javax.swing.JPanel;

import main.ui.custimization.values.StorageValue;

public class VisualizationPanel extends JPanel
{
	private VisualSortingTool sortingTool;
	
	/**
	 * the panel the visualizers draw on
	 */
	public VisualizationPanel(VisualSortingTool sortingTool)
	{
		this.sortingTool = sortingTool;
		setBackground(new Color(102, 102, 102));
		final String prefix = VisualSortingTool.getPrefix(this.getClass());
		//stores the background color of the background
		StorageValue.addStorageValues(StorageValue.createColorStorageValue(prefix, "bgColor", this::setBackground, this::getBackground));
	}
	
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
	   // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_RESOLUTION_VARIANT, RenderingHints.VALUE_RESOLUTION_VARIANT_DPI_FIT);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    if(getWidth() > 0) sortingTool.getSorter().getVisualizer().drawArray((Graphics2D)g);
		g.dispose();
//		Window[] windows = Window.getWindows();
//		for (Window window : windows)
//			System.out.println(window.getName() + ": " + window.getClass());
	}
}