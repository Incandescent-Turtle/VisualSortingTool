package main.vcs;

public class VisualComponent
{
	//the value that is compared to other VCs
	private float value;
	
	/**
	 * these are what the sorters hold in their arrays, the things being sorted
	 * VC for short
	 */
	public VisualComponent(float value)
	{
		this.value = value;
	}
	
	public final float getValue()
	{
		return value;
	}
	
	public final void setValue(float value)
	{
		this.value = value;
	}
}