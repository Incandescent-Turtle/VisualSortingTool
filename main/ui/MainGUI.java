package main.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import main.VisualSortingTool;
import main.algorithms.Algorithm;
import main.sorters.Sorter;

/**
 * On creation adds a top bar to the application with some options
 *
 */
public class MainGUI
{
	//the top UI bar holding the main UI components
	private JPanel topBar = new JPanel();
	
	//the button that shuffles the sorters array
	private JButton shuffleButton = new JButton("Shuffle");
	
	private JLabel algorithmLabel = new JLabel("Algorithm:");
	//drop down list of algorithms
	private JComboBox<Algorithm> algorithmList = new JComboBox<>();
	//runs the current algorithm
    private JButton runAlgorithmButton = new JButton("Run Selected");
    
	private JLabel delayLabel = new JLabel("Delay(ms):");
	//updates the sorters delay
    private JSpinner delaySpinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
    
	private JLabel sorterLabel = new JLabel("Method of Visualization:");
	//drop down list of sorters to pick visualization methods
    private JComboBox<Sorter> sorterList = new JComboBox<Sorter>();
    
    private VisualSortingTool sortingTool;
    
	public MainGUI(VisualSortingTool sortingTool)
	{
		this.sortingTool = sortingTool;
	}
	
	/**
	 * To call after sorters have been added to thei combobox
	 */
	public void setUp()
	{
		sortingTool.add(topBar, BorderLayout.PAGE_START); //top of the screen
		
		//Shuffle button
		shuffleButton.addActionListener(e -> {
			sortingTool.getSorter().tryShuffleArray();
			sortingTool.repaint();
		});
		
		//Delay Spinner
        delaySpinner.addChangeListener(e -> sortingTool.getSorter().setDelay((int) delaySpinner.getValue()));

        //sorter combobox. when switches it resizes/reloads/shuffles the sorter as well as carrying over the delay
        sorterList.addItemListener(e -> {
        	sortingTool.setSorter((Sorter) e.getItem());
        	sortingTool.getSorter().setDelay((int) delaySpinner.getValue());
        	sortingTool.getSorter().tryResizeArray();
        	sortingTool.getSorter().tryReloadArray();
        	sortingTool.getSorter().tryShuffleArray();
        	sortingTool.repaint();
        });
                        
        setUpRunButton(sortingTool);
        
        //in a specific order
		topBar.add(shuffleButton);
		topBar.add(algorithmLabel);
        topBar.add(algorithmList);
        topBar.add(runAlgorithmButton);
        topBar.add(delayLabel);
        topBar.add(delaySpinner);
        topBar.add(sorterLabel);
        topBar.add(sorterList);
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
		    	if(!Algorithm.isSorted(sortingTool, false) && sorter.getAlgorithm() == null)
		    	{
					System.out.println(algorithmList.getSelectedItem().toString() + " has been pushed");
					sorter.setAlgorithm((Algorithm)algorithmList.getSelectedItem());
		    		sorterList.setEnabled(false);
		    		//runs the current algorithm
				    Thread thread = new Thread(() -> ((Algorithm)algorithmList.getSelectedItem()).run());
		    		//runs logic on another thread so swing can update 
				    thread.start();
		    	}
			}
		});		
	}
	
	public void enableSorterPicker()
	{
		sorterList.setEnabled(true);
	}
	
	public int getTopBarHeight()
	{
		return topBar.getHeight();
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