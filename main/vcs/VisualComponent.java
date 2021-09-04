package main.vcs;

/**
 * these are what the sorters hold in their arrays, the things being sorted
 * VC for short
 */
public class VisualComponent
{
	//the value that is compared to other VCs
	private final int value;
	
	public VisualComponent(int value)
	{
		this.value = value;
	}
	
	public final int getValue()
	{
		return value;
	}
}