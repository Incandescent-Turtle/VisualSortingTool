package main.ui.custimization;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.interfaces.Closable;
import main.sorters.Sorter;
import main.visualizers.bases.Visualizer;

@SuppressWarnings("serial")
public class CustomizationGUI extends JPanel
{
	public static final Preferences PREFS = Preferences.userRoot().node(VisualSortingTool.class.getName());

	//these panels stack on top of eachother, each showing the respective sorter/algorithm
	private JPanel sorterPanels, algorithmPanels;
	
	private CardLayout sorterLayout = new CardLayout();
	private CardLayout algorithmLayout = new CardLayout();
	
	/**
	 * the right side bar that contains all the customization settings 
	 * for all sorters/visualizers/algorithms
	 */
	public CustomizationGUI() {}
	
	/**
	 * to be called right after instantiation 
	 */
	public void init(VisualSortingTool sortingTool)
	{
		//will stack panels on top of eachother, starting from top
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//panel to hold sorter/visualizer settings
		sorterPanels = new JPanel(sorterLayout);
		//panel to hold individual algorithm settings
		algorithmPanels = new JPanel(algorithmLayout);
		
		for(Sorter sorter : sortingTool.getSorters())
		{
			sorterPanels.add(new CustomizationPanel(sortingTool, sorter), sorter.toString());
		}
		
		for(Algorithm algorithm : sortingTool.getAlgorithms())
		{
			algorithmPanels.add(new CustomizationPanel(sortingTool, algorithm), algorithm.toString());
		}
	
		add(sorterPanels);
		add(algorithmPanels);
		
		//creates an invisible JLabel to push all the elemnents to the top....a little hacky
		JLabel fill = new JLabel("1");
		fill.setPreferredSize(new Dimension(3, 1000));
		fill.setMinimumSize(new Dimension(3, 0));
	//	fill.setBackground(Color.BLUE);
		//fill.setOpaque(true);
		add(fill);
		JButton reset = new JButton("Reset");
		reset.setAlignmentX(CENTER_ALIGNMENT);
		
		add(reset);
		
		sortingTool.add(this, BorderLayout.LINE_END);
	}
	
	/**
	 * this makes the specified {@link Sorter}s {@link CustomizationPanel} display
	 */
	public void changeSorterPanel(Sorter sorter)
	{
		sorterLayout.show(sorterPanels, sorter.toString());
	}
	
	/**
	 * this makes the specified {@link Algorithm}s {@link CustomizationPanel} display
	 */
	public void changeAlgorithmPanel(Algorithm algorithm)
	{
		algorithmLayout.show(algorithmPanels, algorithm.toString());
	}
	
	/**
	 * Helper method to easily create spinners to modify int values
	 * @param scl this will be called with the new value passed in on a change
	 * @return
	 */
	public static JSpinner createJSpinner(VisualSortingTool sortingTool, SpinnerNumberModel nm, SpinnerChangeAction scl)
	{
		JSpinner spinner = new JSpinner(nm);
		spinner.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				scl.changeAction((int) spinner.getValue());
				sortingTool.getSorter().recalculateAndRepaint();
			}
		});
		return spinner;
	}
	
	/**
	 * @return a button that switches the default color to pink
	 */
	public static JButton createMakePinkButton(VisualSortingTool sortingTool)
	{
		JButton makePink = new JButton("Make Pink");
		makePink.setBackground(new Color(222, 165,164));
		makePink.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Visualizer vis = sortingTool.getSorter().getVisualizer();
				
				//pastel pink
				vis.setDefaultColor(new Color(222, 165,164));
				ColorButton.recolorButtons();
				vis.resetHighlights();
				sortingTool.repaint();
			}
		});
		return makePink;
	}
	
	/**
	 *	for things such as {@link Sorter}s and {@link Algorithm}s that have their own
	 *	{@link CustomizationPanel}s <br>
	 */
	public interface Customizable extends Closable
	{
		/**
		 * to add components to the side bar to change values
		 * @param cp the customization Panel
		 */
		void addCustomizationComponents(CustomizationPanel cp);
		void loadValues(Preferences prefs, String prefix);
		void storeValues(Preferences prefs, String prefix);
		
		default String getPrefix()
		{
			return this.getClass().getSimpleName().toLowerCase() + "_";
		}
		
		@Override
		default void close()
		{
			storeValues(CustomizationGUI.PREFS, getPrefix());
		}
	}
	
	/**
	 *	used for easily setting values on a jspinner change <br>
	 *	for use as function interface
	 */
	public interface SpinnerChangeAction
	{
		void changeAction(int value);
	}
}