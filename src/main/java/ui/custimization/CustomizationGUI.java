package ui.custimization;

import algorithms.Algorithm;
import main.VisualSortingTool;
import sorters.Sorter;
import ui.GUIHandler;
import ui.custimization.values.StorageValue;
import ui.custimization.values.StorageValue.StorageAction;
import ui.tooltips.ToolTips;
import visualizers.bases.Visualizer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.prefs.Preferences;

public class CustomizationGUI extends JPanel
{
	//the only preferences instance to be used
	public static final Preferences PREFS = Preferences.userRoot().node(VisualSortingTool.class.getSimpleName());
	public static final JColorChooser COLOR_CHOOSER = new JColorChooser();

	static
	{
		COLOR_CHOOSER.getChooserPanels();
	}
	//these panels stack on top of each other, each showing the respective sorter/algorithm
	private JPanel sorterPanels, algorithmPanels;

	private final CardLayout sorterLayout = new CardLayout();
	private final CardLayout algorithmLayout = new CardLayout();

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
			//also inserts fill at the bottom (inside) the cp, to push everything to the top
			sorterPanels.add(new CustomizationPanel(sortingTool, sorter).fill(), sorter.toString());
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
		Algorithm.addGeneralAlgorithmCustomizationComponents(sortingTool, generalAlgorithmPanel);
		//adds the panel after the sorter card panel
		addSectionTitle("All Algorithms");
		add(generalAlgorithmPanel);

		//adds algorithm card panel after general algorithms
		add(algorithmPanels);

		//creates an invisible JLabel to push all the elemnents to the top/bottom....a little hacky
//		JLabel fill = new JLabel();
//		fill.setPreferredSize(new Dimension(0, 1000));
//		fill.setMinimumSize(new Dimension(0, 0));
//		add(fill);

		//to push everything up and to push the reset buttons down
		add(Box.createVerticalGlue());

		//this button reverts all values to what they were on the previous saves(just loads them again)
		//centered on the botto
		JButton resetToSave = new JButton("Reset to Saved Values");
		resetToSave.setAlignmentX(CENTER_ALIGNMENT);
		resetToSave.addActionListener(e ->
		{
			//reloads all values
			StorageValue.performStorageAction(PREFS, StorageAction.RESET_TO_SAVE);
			//resets highlights (incase new default color)
			sortingTool.getSorter().getVisualizer().reloadHighlights();
			//recolours all buttons according to their corrosponding color
			ColorButton.recolorButtons();
			//resizes, reloads, reshuffles, paints
			sortingTool.getSorter().recalculateAndRepaint();
			//updates all the spinner etc so their values match with reality
			GUIHandler.update();
		});

		resetToSave.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.RESET_TO_SAVE));
		//disabled when algo is running
		GUIHandler.addToggleable(resetToSave);
		add(resetToSave);

		//at the very bottom middle of panel, resets all values to their ORIGINAL defaults
		JButton resetToDefaultValues = new JButton("Reset to Default Values");
		resetToDefaultValues.setAlignmentX(CENTER_ALIGNMENT);
		resetToDefaultValues.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.RESET_TO_DEFAULTS));
		resetToDefaultValues.addActionListener(e ->
		{
			//removes ALL preferences and reloads them - everything goes to defaults
			StorageValue.performStorageAction(PREFS, StorageAction.RESET_TO_DEFAULTS);
			//resets highlights (incase new default color)
			sortingTool.getSorter().getVisualizer().reloadHighlights();
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
	 * @param title the title of the section
	 */
	public void addSectionTitle(String title)
	{
		JLabel label = new JLabel(title);
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
	 * Helper method to easily create spinners to modify number values
	 * @param sortingTool the sorting tool
	 * @param changeAction this will be called with the new value passed in on a change
	 * @param onUpdate returns the value that the spinner will fetch when updated
	 * @param recalc whether this spinner should recalculate, or just repainted
	 * @return returns the new JSpinner
	 */
	public static <T extends Number> JSpinner createNumberJSpinner(VisualSortingTool sortingTool, SpinnerNumberModel nm, Consumer<T> changeAction, Supplier<T> onUpdate, boolean recalc)
	{
		JSpinner spinner = new JSpinner(nm);
		GUIHandler.addUpdatables(() -> spinner.setValue(onUpdate.get()));
		spinner.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				changeAction.accept((T)spinner.getValue());
				if(recalc) sortingTool.getSorter().recalculateAndRepaint();
				else sortingTool.repaint();
			}
		});
		return spinner;
	}

	/**
	 * Helper method to easily create spinners to modify number values <br>
	 * doesnt recalculate sorting on spinner update (used for solely visual things)
	 * @param sortingTool the sorting tool
	 * @param consumer this will be called with the new value passed in on a change
	 * @param supplier returns the value that the spinner will fetch when updated
	 * @return returns the new JSpinner
	 */
	public static <T extends Number> JSpinner createNumberJSpinner(VisualSortingTool sortingTool, SpinnerNumberModel nm, Consumer<T> consumer, Supplier<T> supplier)
	{
		return createNumberJSpinner(sortingTool, nm, consumer, supplier, true);
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
				vis.reloadHighlights();
				sortingTool.repaint();
			}
		});
		makePink.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.MAKE_PINK));
		return makePink;
	}
}