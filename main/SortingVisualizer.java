package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.algorithms.BubbleSort;

/**
 * @author rorsm
 *	the window/panel for display
 */
public class SortingVisualizer extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private Sorter sorter;
	
	public SortingVisualizer()
	{
		sorter = new Sorter(this);
		setUpFrame();
		setUpButtons();
		//whenever a resize occurs it attempts to update the array size
		addComponentListener(new ComponentAdapter() {
				
			@Override
	        public void componentResized(ComponentEvent e) 
			{
				//only resizes when algorithm isnt running
				sorter.tryResizeArray();
	        }
		});
		validate();
		frame.setVisible(true);
		frame.requestFocus();
	}

	private void setUpButtons()
	{
		//shuffle button
		JButton shuffle = new JButton();
		shuffle.setText("Shuffle");
		shuffle.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				sorter.tryShuffleArray();
			}
		});
		add(shuffle);
		
		/*
		 * Algorithms
		 */
		
		//bubble sort
		add(new AlgorithmButton(sorter, new BubbleSort(this)));
		
	}

	private void setUpFrame()
	{
		frame = new JFrame("Sorting Methods Visudal");		
		Dimension dim = new Dimension(800, 800);
		frame.setPreferredSize(dim);
		frame.setMaximumSize(dim);
		frame.setMinimumSize(dim);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.add(this);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		sorter.drawArray(g);
	}
	
	public Sorter getSorter()
	{
		return sorter;
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
	public static void main(String[] args)
	{
		new SortingVisualizer();
	}
}