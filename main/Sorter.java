package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.algorithms.Algorithm;
import main.algorithms.BubbleSort;

/**
 * @author rorsm
 *	JPanel class that handles the main array used, the rendering, and the buttons \n
 */
public class Sorter extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	public static final int MARGIN = 30;

	private JFrame frame;

	//main array that holds all the numbers to be sorted
	public int[] array;
	//array that holds which color the corresponding index in <b>array</b> should be
	private Color[] highlights;
	//array size aka length
	private int size = 200;
	
	//current algorithm being used (changed via buttons)
	private Algorithm algorithm;
	
	public Sorter(JFrame frame)
	{
		this.frame = frame;
		//JPanel was being slow and for some reason <b>getWidth()</b> was returning 0 on startup
		new AlgorithmButton(this, new BubbleSort(this));
		JButton shuffle = new JButton();
		shuffle.setText("Shuffle");
		shuffle.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				shuffleArray();
				repaint();
			}
		});
		add(shuffle);
		resizeArray();
		repaint(); 
	}
	
	/**
	 * To have no logic, renders the bars with their colors and clears <b>highlights</b>
	 */
	@Override
	protected void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);
		System.out.println("repainting");
        Graphics2D g = (Graphics2D) graphics.create();
		System.out.println("painting");
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		
		for(int i = 0; i < size; i++)
		{
			g.setColor(highlights[i]);
			g.fillRect(MARGIN + i*4, 0, 2, array[i]);
			highlights[i] = Color.WHITE; 
		}
		g.dispose();
		//Arrays.stream(array).forEach(i -> System.out.print(i + " "));		
	} 
	
	/**
	 * To be called when the window has changed size and isn't in the process of a shuffle
	 * <ul>
	 * 	<li>changes the <b>size</b> variable</li>
	 *	<li>resizes <b>array</b> and <b>highlights</b> accordingly</li>
	 * 	<li>shuffles <b>array</b></li>
	 * </ul>  
	 */
	public void resizeArray()
	{
		System.out.println(getWidth());
		setBounds(frame.getBounds());
		size = (getWidth() - Sorter.MARGIN*2)/4;
		array = new int[size];
		highlights = new Color[size];
		shuffleArray();
	}
	
	/**
	 * <font color="red">Only to be used by <b>AlgorithmButton</b>s upon click</font color="green">
	 * @param algorithm the algorithm that the button represents
	 */
	public void setAlgorithm(Algorithm algorithm)
	{
		this.algorithm = algorithm;
	}
	
	public Algorithm getAlgorithm()
	{
		return algorithm;
	}
	
	public int getArraySize()
	{
		return size;
	}
	
	public int[] getArray()
	{
		return array;
	}
	
	public Color[] getHighlights()
	{
		return highlights;
	}
	
	/**
	 * Shuffles the array AND resets colors in the highlights array
	 * 
	 */
	private void shuffleArray()
	{
		for(int i = 0; i < size; i++)
		{
			Random rand = new Random();
			array[i] = rand.nextInt(600) + 15;
			highlights[i] = Color.WHITE;
		}
	}
}