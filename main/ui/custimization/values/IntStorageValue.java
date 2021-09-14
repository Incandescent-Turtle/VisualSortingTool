package main.ui.custimization.values;

import java.util.prefs.Preferences;

import main.interfaces.OnChangeAction;
import main.interfaces.RetrieveAction;

public class IntStorageValue extends StorageValue<Integer>
{			
	/**
	 * setting up ints in preferences
	 * @param prefix class prefix
	 * @param key variable name
	 * @param defaultValue if load fails it will set the variable to this value
	 * @param changeAction should look like num -> myVar = num || functional interface to set variable
	 * @param retrieveAction should look like () -> myVar || functional interface to 
	 * Retrieve variable for storage
	 */
	public IntStorageValue(String prefix, String key, int defaultValue, OnChangeAction<Integer> changeAction, RetrieveAction<Integer> retrieveAction)
	{
		super(prefix, key, defaultValue, changeAction, retrieveAction);
	}

	@Override
	public final void loadValue(Preferences prefs)
	{
		changeAction.doStuff(prefs.getInt(fullKey, defaultValue));
	}
	
	@Override
	public final void storeValue(Preferences prefs)
	{
		prefs.putInt(fullKey, retrieveAction.retrieve());
	}
}