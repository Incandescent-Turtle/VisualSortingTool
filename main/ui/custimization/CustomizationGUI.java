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
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.interfaces.OnChangeAction;
import main.interfaces.RetrieveAction;
import main.sorters.Sorter;
import main.ui.GUIHandler;
import main.ui.custimization.values.StorageValue;
import main.ui.custimization.values.StorageValue.StorageAction;
import main.visualizers.bases.Visualizer;

@SuppressWarnings("serial")
public class CustomizationGUI extends JPanel
{
	//the only preferences instance to be used
	public static final Preferences PREFS = Preferences.userRoot().node(VisualSortingTool.class.getSimpleName());
	
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
		//main title
		addSectionTitle("Customization");
		//adds sorter panel right below title (gets pushed to the top)
		add(sorterPanels);
		
		//for values (like confirmationColor) that are static among algorithms
		JPanel generalAlgorithmPanel = new JPanel();
		//vertical box layout
		generalAlgorithmPanel.setLayout(new BoxLayout(generalAlgorithmPanel, BoxLayout.Y_AXIS));
		//adds the components
		Algorithm.addGeneralAlgorithmCustimizationComponents(sortingTool, generalAlgorithmPanel);
		//adds the panel after the sorter card panel
		addSectionTitle("All Algorithms");
		add(generalAlgorithmPanel);
		
		//adds algorithm card panel after general algorithms
		add(algorithmPanels);
		
		//creates an invisible JLabel to push all the elemnents to the top/bottom....a little hacky
		JLabel fill = new JLabel();
		fill.setPreferredSize(new Dimension(0, 1000));
		fill.setMinimumSize(new Dimension(0, 0));
		add(fill);
		
		//this button reverts all values to what they were on the previous saves(just loads them again)
		//centered on the botto
		JButton resetToSave = new JButton("Reset to Saved Values");
		resetToSave.setAlignmentX(CENTER_ALIGNMENT);
		resetToSave.addActionListener(e -> 
		{
			//reloads all values
			StorageValue.performStorageAction(PREFS, StorageAction.LOAD);
			//resets highlights (incase new default color)
			sortingTool.getSorter().getVisualizer().resetHighlights();
			//recolours all buttons according to their corrosponding color
			ColorButton.recolorButtons();
			//resizes, reloads, reshuffles, paints
			sortingTool.getSorter().recalculateAndRepaint();
			//updates all the spinner etc so their values match with reality
			GUIHandler.update();

		});
		//disabled when algo is running
		GUIHandler.addToggleable(resetToSave);
		add(resetToSave);
		
		//at the very bottom middle of panel, resets all values to their ORIGINAL defaults
		JButton resetToDefaultValues = new JButton("Reset to Default Values");
		resetToDefaultValues.setAlignmentX(CENTER_ALIGNMENT);
		resetToDefaultValues.addActionListener(e -> 
		{
			//removes ALL preferences 
			StorageValue.performStorageAction(PREFS, StorageAction.REMOVE);
			//loading fails so default values are used to load
			StorageValue.performStorageAction(PREFS, StorageAction.LOAD);
			//resets highlights (incase new default color)
			sortingTool.getSorter().getVisualizer().resetHighlights();
			//recolours all buttons according to their corrosponding color
			ColorButton.recolorButtons();
			//resizes, reloads, reshuffles, paints
			sortingTool.getSorter().recalculateAndRepaint();
			//updates all the spinner etc so their values match with reality
			GUIHandler.update();
		});
		GUIHandler.addToggleable(resetToDefaultValues);
		add(resetToDefaultValues);
		
		sortingTool.add(this, BorderLayout.LINE_END);
	}
	
	/**
	 * an underlined centered piece of text to denote category
	 * @param title
	 */
	public void addSectionTitle(String title)
	{
		JLabel label = new JLabel("All Algorithns");
		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		add(label);
		//adds space/underline below
		JSeparator line = new JSeparator(SwingConstants.HORIZONTAL);
		line.setPreferredSize(new Dimension(20,30));
		line.setVisible(true);
		add(line);
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
	 * @param changeAction this will be called with the new value passed in on a change
	 * @param onUpdate returns the value that when {@link #update()} is called will replace the spinners value
	 * @return
	 */
	public static JSpinner createJSpinner(VisualSortingTool sortingTool, SpinnerNumberModel nm, OnChangeAction<Integer> changeAction, RetrieveAction<Integer> onUpdate)
	{
		JSpinner spinner = new JSpinner(nm);
		GUIHandler.addUpdatables(() -> spinner.setValue(onUpdate.retrieve()));
		spinner.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				changeAction.doStuff((int) spinner.getValue());
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
}