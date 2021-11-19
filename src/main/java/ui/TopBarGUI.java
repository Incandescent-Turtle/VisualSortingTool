package ui;

import main.VisualSortingTool;
import algorithms.Algorithm;
import sorters.Sorter;
import ui.custimization.ColorButton;
import ui.custimization.values.BooleanStorageValue;
import ui.custimization.values.StorageValue;
import ui.custimization.values.StringStorageValue;
import ui.tooltips.ToolTips;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TopBarGUI extends JPanel
{
	private final FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 5, 5);
	
	//the button that shuffles the sorters array
	private final JButton shuffleButton = new JButton("Shuffle");
	
	private final JLabel algorithmLabel = new JLabel("Algorithm:");
	//drop down list of algorithms
	private final JComboBox<Algorithm> algorithmList = new JComboBox<>();
	//runs the current algorithm
    private final JButton runAlgorithmButton = new JButton("Run Selected");
    
	private final JLabel delayLabel = new JLabel("Delay(ms):");
	//updates the sorters delay
	private final SpinnerNumberModel delaySpinnerNumberModel = new SpinnerNumberModel(Algorithm.delay, 0, 10000, 1);
    private final JSpinner delaySpinner = new JSpinner(delaySpinnerNumberModel);
    
    //for adjusting the algorithm "step" size (how many runs are skipped)
	private final JLabel stepLabel = new JLabel("Step:");
	private final SpinnerNumberModel stepSpinnerNumberModel = new SpinnerNumberModel(Algorithm.stepSize, 1, 10000, 1);
    private final JSpinner stepSpinner = new JSpinner(stepSpinnerNumberModel);

	private final JLabel sorterLabel = new JLabel("Visualization:");
	//drop down list of sorters to pick visualization methods
    private final JComboBox<Sorter> sorterList = new JComboBox<>();

	//	to toggle the tool tips on/off
	private final JCheckBox tooltipsCheckbox = new JCheckBox("Toggle Tooltips", true);
    
    private final ArrayList<JLabel> labels = new ArrayList<>();
    private final ArrayList<Component> components = new ArrayList<>();

    private final VisualSortingTool sortingTool;
    
    private final String prefix = VisualSortingTool.getPrefix(getClass());
    
    public TopBarGUI(VisualSortingTool sortingTool)
	{
    	setLayout(layout);
		this.sortingTool = sortingTool;
	}
    
	/**
	 * To call after sorters have been added to thei combobox
	 */
	public void init()
	{
		sortingTool.add(this, BorderLayout.PAGE_START); //top of the screen
		//Shuffle button
		shuffleButton.addActionListener(e -> sortingTool.getSorter().recalculateAndRepaint());
		shuffleButton.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.SHUFFLE));

		//Delay Spinner
        delaySpinner.addChangeListener(e -> Algorithm.delay = ((int) delaySpinner.getValue()));
        GUIHandler.addUpdatables(() -> delaySpinner.setValue(Algorithm.delay));
		delaySpinner.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.DELAY, delaySpinnerNumberModel));

		//step spinner to adjust Algorithm.stepSize
        stepSpinner.addChangeListener(e -> Algorithm.stepSize = ((int) stepSpinner.getValue()));
        GUIHandler.addUpdatables(() -> stepSpinner.setValue(Algorithm.stepSize));
		stepSpinner.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.STEP, stepSpinnerNumberModel));

		//sorter combobox. when switches it resizes/reloads/shuffles the sorter as well as carrying over the delay
        sorterList.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
	    		ColorButton.recolorButtons();
	        	sortingTool.setSorter((Sorter) e.getItem());
				//tells the sorter its been switched to
				sortingTool.getSorter().switchedTo();
	        	Sorter sorter = sortingTool.getSorter();
	        	sortingTool.getGUIHandler().getCustomizationGUI().changeSorterPanel(sorter);
	        	Algorithm.delay = ((int) delaySpinner.getValue());
	        	sorter.recalculateAndRepaint();
			}
		});
        //sets up preferences for the sorter open on program close. on reset nothing happens
        //a little hacky...uses id string name to find the sorter
        StorageValue.addStorageValues(new StringStorageValue(prefix, "sorter", Sorter.Sorters.BAR_HEIGHT.toString(), name -> sorterList.setSelectedItem(sortingTool.getSorter(name)), () -> sorterList.getSelectedItem().toString()).setResetable(false));
		sorterList.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.VISUALIZATION_METHOD));
        //changes algorithm panel on algorithm change
        algorithmList.addItemListener(e -> sortingTool.getGUIHandler().getCustomizationGUI().changeAlgorithmPanel((Algorithm)e.getItem()));
        //sets up preferences for the algorithm open on program close. on reset nothing happens
        //a little hacky...uses toString on the algorithm to save/load it
        StorageValue.addStorageValues(new StringStorageValue(prefix, "algorithm", sortingTool.getAlgorithms()[0].toString(), name -> algorithmList.setSelectedItem(sortingTool.getAlgorithm(name)), () -> algorithmList.getSelectedItem().toString()).setResetable(false));
		algorithmList.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.ALGORITHMS));



		tooltipsCheckbox.addActionListener(e -> ToolTips.MANAGER.setEnabled(tooltipsCheckbox.isSelected()));
		StorageValue.addStorageValues(new BooleanStorageValue(prefix, "enableToolTips", b -> tooltipsCheckbox.setSelected(b), () -> tooltipsCheckbox.isSelected()).setResetable(false));

		//always displaying tooltip whether disabled or not
		JToolTip toolTip = new JToolTip();
		toolTip.setTipText(ToolTips.getDescriptionFor(ToolTips.Keys.TOGGLE_TOOLTIPS));
		tooltipsCheckbox.addMouseListener(new MouseAdapter(){

			Popup popup = PopupFactory.getSharedInstance().getPopup(tooltipsCheckbox, toolTip, tooltipsCheckbox.getX(), tooltipsCheckbox.getY());

			@Override
			public void mouseEntered(MouseEvent e)
			{
				if(e.getSource() == tooltipsCheckbox)
				{
					Point screenPos = tooltipsCheckbox.getLocationOnScreen();
					popup = PopupFactory.getSharedInstance().getPopup(tooltipsCheckbox, toolTip, (int)screenPos.getX()-toolTip.getWidth(), (int)screenPos.getY()+tooltipsCheckbox.getHeight());
					popup.show();
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				popup.hide();
			}
		});

		setUpRunButton(sortingTool);
		runAlgorithmButton.setToolTipText(ToolTips.getDescriptionFor(ToolTips.Keys.RUN));
        
        //in a specific order
        addToGUI(shuffleButton);
        addToGUI(algorithmLabel);
        addToGUI(algorithmList);
        addToGUI(runAlgorithmButton);
        addToGUI(delayLabel);
        addToGUI(delaySpinner);
        addToGUI(stepLabel);
        addToGUI(stepSpinner);
        addToGUI(sorterLabel);
        addToGUI(sorterList);
		addToGUI(tooltipsCheckbox);
        //adds all the things that need to be turned off while algorithm running
        GUIHandler.addToggleable(shuffleButton, algorithmList, runAlgorithmButton, sorterList);
		//	for tooltips
		GUIHandler.addUpdatables(() -> ToolTips.MANAGER.setEnabled(tooltipsCheckbox.isSelected()));
        sortingTool.validate();
	}

	/** adds action listener to the button the runs the current algorithn
	 * I just dont like anonomyous classes
	 */
	private void setUpRunButton(VisualSortingTool sortingTool)
	{
		runAlgorithmButton.addActionListener(new ActionListener() {
    				
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Sorter sorter = sortingTool.getSorter();
		    	//if there isn't an algorithm active and it isnt sorted
		    	if(sorter.getAlgorithm() == null)
		    	{
					if(sorter.getArray() == null)
					{
						System.out.println("Can't run because the array is null");
						return;
					}
		    		if(Algorithm.isSorted(sortingTool, false)) sorter.tryShuffleArray();
					System.out.println(algorithmList.getSelectedItem().toString() + " has been pushed");
					sorter.setAlgorithm((Algorithm)algorithmList.getSelectedItem());
		    		//disables resizing components etc
					GUIHandler.setEnabled(false);
		    		//runs the current algorithm
				    Thread thread = new Thread(() -> ((Algorithm)algorithmList.getSelectedItem()).run());
					sorter.run();
					//runs logic on another thread so swing can update
				    thread.start();
		    	}
			}
		});		
	}
	
	/**
	 * auto-adds all non-JButton components to {@link GUIHandler}'s toggleable array
	 * @param component the component to add
	 */
	private void addToGUI(Component component)
	{
		add(component);
		if(component instanceof JLabel) labels.add((JLabel) component);
			else components.add(component);
	}
	
	/**
	 * called when frame resized <br>
	 * if bar size is smaller than the GUI width, labels are hidden, opposite if it it larger
	 */
	public void resizeGUI()
	{
		hideLabels(getWidth() < getGUIWidth(true));
	}
	
	private void hideLabels(boolean hide)
	{
		for(JLabel label : labels)
		{
			label.setVisible(!hide);
		}
	}
	
	/**
	 * returns the horizontal width of this GUI
	 * @param withLabels whether the lengths of the labels are included in the return value
	 * @return the length the topBar GUI takes up
	 */
	public int getGUIWidth(boolean withLabels)
	{
		int width = 20;
		for(Component component : components)
		{
			width += component.getWidth() + layout.getHgap();
		}
		width += layout.getHgap();
		
		if(withLabels)
		{
			for(JLabel label : labels)
			{
				width += label.getWidth() + layout.getHgap();
			}
		}
		return width;
	}
	
	public void addAlgorithm(Algorithm algorithm)
	{
        algorithmList.addItem(algorithm);
	}
	
	public void addSorter(Sorter sorter)
	{
		sorterList.addItem(sorter);
	}
}