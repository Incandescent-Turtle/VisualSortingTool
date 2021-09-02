package main.sorters;

import java.awt.Color;

import main.Sorter;
import main.VisualSortingTool;
import main.vcs.VisualComponent;
import main.visualizers.VisualizerBarHeight;

public class SorterBarHeight extends main.sorters.Sorter
{
	public static final int MIN_MARGIN = 30;
	private int barThickness = 15;
	private int barGap = 10;

	public SorterBarHeight(VisualSortingTool sortingTool)
	{
		super(sortingTool, new VisualizerBarHeight());
	}

	
	/**
	 * <ul>
	 * 	<li>changes the <b>size</b> variable based on window size</li>
	 *	<li>resizes <b>array</b> and <b>highlights</b> accordingly</li>
	 *	<li>repaints window</li>
	 * </ul>  
	 */
	@Override
	protected void resizeArray()
	{
		size = (sortingTool.getWidth() - Sorter.MIN_MARGIN*2 + barGap)/(barThickness+barGap);
		array = new VisualComponent[size];
		highlights = new Color[size];
		sortingTool.repaint();
	}


	@Override
	protected void shuffleArray()
	{
		
	}
}