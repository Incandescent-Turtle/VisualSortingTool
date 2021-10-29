package vcs;

public class VisualComponent
{
	//the value that is compared to other VCs
	private double value;
	
	/**
	 * these are what the sorters hold in their arrays, the things being sorted
	 * VC for short
	 */
	public VisualComponent(double value)
	{
		this.value = value;
	}
	
	public final double getValue()
	{
		return value;
	}
	
	public final void setValue(float value)
	{
		this.value = value;
	}
}