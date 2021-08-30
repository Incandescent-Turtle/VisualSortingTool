package main.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import main.Sorter;
import main.SortingVisualizer;
import main.algorithms.Algorithm;
import main.algorithms.BubbleSort;

/**
 * On creation adds a top bar to the application with some options
 *
 */
public class MainUI
{
	private JPanel topBar = new JPanel();
	private JButton shuffleButton = new JButton("Shuffle");
	private JComboBox<Algorithm> algorithmList = new JComboBox<>();
    private JButton runAlgorithmButton = new JButton("Run Selected");
    private JSpinner delaySpinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));

	public MainUI(SortingVisualizer visualizer)
	{
		Sorter sorter = visualizer.getSorter();
		visualizer.add(topBar, BorderLayout.PAGE_START); //top of the screen
		
		//Shuffle button
		shuffleButton.addActionListener(e -> sorter.tryShuffleArray());
		//Delay Spinner
        delaySpinner.addChangeListener(e -> sorter.setDelay((int) delaySpinner.getValue()));
		
        /*
         * Algorithms
         */
        algorithmList.addItem(new BubbleSort(visualizer));
        
        setUpRunButton(visualizer);
        
		topBar.add(shuffleButton);
        topBar.add(algorithmList);
        topBar.add(runAlgorithmButton);
        topBar.add(delaySpinner);
        visualizer.validate();
	}

	/*adds action listener to the button the runs the current algorithn
	 * I just dont like anonomyous classes
	 */
	private void setUpRunButton(SortingVisualizer visualizer)
	{
		runAlgorithmButton.addActionListener(new ActionListener() {
    	
			private Sorter sorter = visualizer.getSorter();
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
		    	//if there isn't an algorithm active and it isnt sorted
		    	if(!Algorithm.isSorted(visualizer, false) && sorter.getAlgorithm() == null)
		    	{
					System.out.println(algorithmList.getSelectedItem().toString() + " has been pushed");
		    		sorter.setAlgorithm((Algorithm)algorithmList.getSelectedItem());
		    		//runs the current algorithm
				    Thread thread = new Thread(() -> ((Algorithm)algorithmList.getSelectedItem()).run());
		    		//runs logic on another thread so swing can update 
				    thread.start();
		    	}
			}
		});		
	}
}