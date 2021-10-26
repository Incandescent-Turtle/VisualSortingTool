package main.ui;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.sorters.Sorter;
import main.ui.custimization.ColorButton;
import main.ui.custimization.values.StorageValue;
import main.ui.custimization.values.StringStorageValue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
    private final JSpinner delaySpinner = new JSpinner(new SpinnerNumberModel(Algorithm.delay, 0, 10000, 1));
    
    //for adjusting the algorithm "step" size (how many runs are skipped)
	private final JLabel stepLabel = new JLabel("Step:");
    private final JSpinner stepSpinner = new JSpinner(new SpinnerNumberModel(Algorithm.stepSize, 1, 500, 1));

	private final JLabel sorterLabel = new JLabel("Visualization:");
	//drop down list of sorters to pick visualization methods
    private final JComboBox<Sorter> sorterList = new JComboBox<>();
    
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
		
		//Delay Spinner
        delaySpinner.addChangeListener(e -> Algorithm.delay = ((int) delaySpinner.getValue()));
        GUIHandler.addUpdatables(() -> delaySpinner.setValue(Algorithm.delay));
        
        //step spinner to adjust Algorithm.stepSize
        stepSpinner.addChangeListener(e -> Algorithm.stepSize = ((int) stepSpinner.getValue()));
        GUIHandler.addUpdatables(() -> stepSpinner.setValue(Algorithm.stepSize));
        
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
        
        //changes algorithm panel on algorithm change
        algorithmList.addItemListener(e -> sortingTool.getGUIHandler().getCustomizationGUI().changeAlgorithmPanel((Algorithm)e.getItem()));
        //sets up preferences for the algorithm open on program close. on reset nothing happens
        //a little hacky...uses toString on the algorithm to save/load it
        StorageValue.addStorageValues(new StringStorageValue(prefix, "algorithm", sortingTool.getAlgorithms()[0].toString(), name -> algorithmList.setSelectedItem(sortingTool.getAlgorithm(name)), () -> algorithmList.getSelectedItem().toString()).setResetable(false));
                        
        setUpRunButton(sortingTool);
        
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
        //adds all the things that need to be turned off while algorithm running
        GUIHandler.addToggleable(shuffleButton, algorithmList, runAlgorithmButton, sorterList);
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
		    		sorter.tryShuffleArray();
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