package main.vcs;

public abstract class VisualComponent
{
	private int value;
	
	public int getValue()
	{
		return value;
	}
	
	public VisualComponent setValue(int value)
	{
		this.value = value;
		return this;
	}
}