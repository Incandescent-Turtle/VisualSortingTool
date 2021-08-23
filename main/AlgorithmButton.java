package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import main.algorithms.Algorithm;

public class AlgorithmButton extends JButton
{
	private static final long serialVersionUID = 1L;
		
	public AlgorithmButton(Sorter sorter, Algorithm algorithm)
	{
		setText(algorithm.getName());
		addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) 
		    {
		    	//if there isn't an algorithm active 
		    	if(sorter.getAlgorithm() == null)
		    	{
					System.out.println(algorithm.getName() + " has been pushed");
		    		sorter.setAlgorithm(algorithm);
		    		algorithm.run();
		    	}
		    }
		});
		sorter.add(this);
	}
}