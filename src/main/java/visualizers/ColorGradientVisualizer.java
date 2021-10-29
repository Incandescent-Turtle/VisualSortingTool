package visualizers;

import main.VisualSortingTool;
import sorters.Sorter.Sorters;
import vcs.ColorVisualComponent;
import vcs.VisualComponent;
import visualizers.bases.BarVisualizer;

import java.awt.*;

public class ColorGradientVisualizer extends BarVisualizer
{
	public ColorGradientVisualizer(VisualSortingTool sortingTool)
	{
		super(sortingTool, Sorters.COLOR_GRADIENT);
	}
	
	@Override
	public void setDefaultValues()
	{
		super.setDefaultValues();
		componentGap = 0;
		minMargin = 0;		
	}

	//called for each bar
	@Override
	protected void drawComponent(Graphics2D g, double x, VisualComponent[] array, int i)
	{
		g.setColor(getHighlightAt(i));
		//draws this bar
		drawBar(g, x, sortingTool.getVisualizerHeight());
	}

	@Override
	public void resetHighlightAt(int index)
	{
		highlight(index, ((ColorVisualComponent) sortingTool.getSorter(identifier).getArray()[index]).getColor());
	}
}