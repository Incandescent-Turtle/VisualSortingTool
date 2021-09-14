package main.interfaces;

/**
 * general functional interface whenever a value is going to change and you want to set a variable to it
 * @param <T> the type of object that is changing, the one that is passed in on the doStuff method
 */
public interface OnChangeAction<T>
{
	/**
	 * @param value the new value
	 */
	void doStuff(T value);
}