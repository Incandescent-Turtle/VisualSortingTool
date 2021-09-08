package main.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
    private FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 5, 5);

	//the top UI bar holding the main UI components
	private JPanel topBar = new JPanel(layout);
	
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
    
    private ArrayList<JLabel> labels = new ArrayList<>();
    private ArrayList<JComponent> components = new ArrayList<>();

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
			sortingTool.getSorter().tryResizeArray();
			sortingTool.getSorter().tryReloadArray();
			sortingTool.getSorter().tryShuffleArray();
			sortingTool.repaint();
		});
		
		//Delay Spinner
        delaySpinner.addChangeListener(e -> sortingTool.getSorter().setDelay((int) delaySpinner.getValue()));

        //sorter combobox. when switches it resizes/reloads/shuffles the sorter as well as carrying over the delay
        sorterList.addItemListener(e -> {
        	sortingTool.setSorter((Sorter) e.getItem());
        	Sorter sorter = sortingTool.getSorter();
        	if(sortingTool.getCustimizationPanel() == null) System.out.println("true");
        	sortingTool.getCustimizationPanel().changePanel(sorter.toString());
        	sorter.setDelay((int) delaySpinner.getValue());
        	sorter.tryResizeArray();
        	sorter.tryReloadArray();
        	sorter.tryShuffleArray();
        	sortingTool.repaint();
        });
                        
        setUpRunButton(sortingTool);
        
        //in a specific order
        addToGUI(shuffleButton);
        addToGUI(algorithmLabel);
        addToGUI(algorithmList);
        addToGUI(runAlgorithmButton);
        addToGUI(delayLabel);
        addToGUI(delaySpinner);
        addToGUI(sorterLabel);
        addToGUI(sorterList);
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
		    		algorithmList.setEnabled(false);
		    		//runs the current algorithm
				    Thread thread = new Thread(() -> ((Algorithm)algorithmList.getSelectedItem()).run());
		    		//runs logic on another thread so swing can update 
				    thread.start();
		    	}
			}
		});		
	}
	
	private void addToGUI(JComponent component)
	{
		topBar.add(component);
		if(component instanceof JLabel) labels.add((JLabel) component);
		else components.add(component);
	}
	
	public void enableComboBoxes()
	{
		sorterList.setEnabled(true);
		algorithmList.setEnabled(true);
	}
	
	public void resizeGUI()
	{
		boolean isSmall = topBar.getWidth() < getGUIWidth(true);
		hideLabels(isSmall);
		layout.setAlignment(isSmall ? FlowLayout.LEFT : FlowLayout.CENTER);
	}
	
	private void hideLabels(boolean hide)
	{
		for(JLabel label : labels)
		{
			label.setVisible(!hide);
		}
	}
	
	public int getGUIWidth(boolean withLabels)
	{
		int width = 20;
		for(JComponent component : components)
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