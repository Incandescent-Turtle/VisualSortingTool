package main.ui.custimization.values;

import java.util.prefs.Preferences;

import main.interfaces.OnChangeAction;
import main.interfaces.RetrieveAction;

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
	public StringStorageValue(String prefix, String key, OnChangeAction<String> changeAction, RetrieveAction<String> retrieveAction)
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
	public StringStorageValue(String prefix, String key, String defaultValue, OnChangeAction<String> changeAction, RetrieveAction<String> retrieveAction)
	{
		super(prefix, key, defaultValue, changeAction, retrieveAction);
	}

	@Override
	public void loadValue(Preferences prefs)
	{
		changeAction.doStuff(prefs.get(fullKey, defaultValue));
	}

	@Override
	public void storeValue(Preferences prefs)
	{
		prefs.put(fullKey, retrieveAction.retrieve());
	}
}