package main.ui.custimization.values;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.prefs.Preferences;

public class IntStorageValue extends StorageValue<Integer>
{			
	/**
	 * setting up ints in preferences
	 * @param prefix class prefix
	 * @param key variable name
	 * @param changeAction should look like num -> myVar = num || functional interface to set variable
	 * @param retrieveAction should look like () -> myVar || functional interface to 
	 * Retrieve variable for storage
	 */
	public IntStorageValue(String prefix, String key, Consumer<Integer> changeAction, Supplier<Integer> retrieveAction)
	{
		super(prefix, key, changeAction, retrieveAction);
	}

	@Override
	public final void loadValue(Preferences prefs)
	{
		changeAction.accept(prefs.getInt(fullKey, defaultValue));
	}
	
	@Override
	public final void storeValue(Preferences prefs)
	{
		prefs.putInt(fullKey, retrieveAction.get());
	}
}