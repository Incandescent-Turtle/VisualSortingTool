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
		setText(algorithm.getAlgorithmName());
		addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) 
		    {
		    	//if there isn't an algorithm active 
		    	if(sorter.getAlgorithm() == null)
		    	{
					System.out.println(algorithm.getAlgorithmName() + " has been pushed");
		    		sorter.setAlgorithm(algorithm);
		    		//runs logic on another thread so swing can update 
				    new Thread(new Runnable()
					{
						
						@Override
						public void run()
						{
						    algorithm.run();

						}
					}).start();
		    	}
		    }
		});
	}
}