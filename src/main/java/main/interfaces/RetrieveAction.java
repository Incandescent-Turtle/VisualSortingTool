package main.interfaces;

/**
 * used for getting/retrieving values (for storing etc)
 * @param <T> the type of object to be returned
 */
public interface RetrieveAction<T>
{
	T retrieve();
}