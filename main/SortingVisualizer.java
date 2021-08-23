package main;

import java.awt.Dimension;

import javax.swing.JFrame;

public class SortingVisualizer extends JFrame
{
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args)
	{
		new SortingVisualizer();
	}
	
	public SortingVisualizer()
	{
		setTitle("AP CSA Sorting Methods Visudal");
		
		Dimension dim = new Dimension(800, 800);
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMinimumSize(dim);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setLocationRelativeTo(null);
		add(new Sorter(this));
		setVisible(true);
		requestFocus();
	}
}