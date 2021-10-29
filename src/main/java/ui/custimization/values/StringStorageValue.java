package ui.custimization.values;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.prefs.Preferences;

public class StringStorageValue extends StorageValue<String>
{
	/**
	 * setting up Strings in preferences. sets default to retrieve action on construction
	 * @param prefix class prefix
	 * @param key variable name
	 * @param changeAction should look like str -> myVar = str || functional interface to set variable
	 * @param retrieveAction should look like () -> myVar || functional interface to 
	 * Retrieve variable for storage
	 */
	public StringStorageValue(String prefix, String key, Consumer<String> changeAction, java.util.function.Supplier<String> retrieveAction)
	{
		super(prefix, key, changeAction, retrieveAction);
	}
	
	/**
	 * setting up Strings in preferences
	 * @param prefix class prefix
	 * @param key variable name
	 * @param defaultValue if load fails it will set the variable to this value
	 * @param changeAction should look like str -> myVar = str || functional interface to set variable
	 * @param retrieveAction should look like () -> myVar || functional interface to 
	 * Retrieve variable for storage
	 */
	public StringStorageValue(String prefix, String key, String defaultValue, Consumer<String> changeAction, Supplier<String> retrieveAction)
	{
		super(prefix, key, defaultValue, changeAction, retrieveAction);
	}

	@Override
	public void loadValue(Preferences prefs)
	{
		changeAction.accept(prefs.get(fullKey, defaultValue));
	}

	@Override
	public void storeValue(Preferences prefs)
	{
		prefs.put(fullKey, retrieveAction.get());
	}
}