package main.ui.custimization.values;

import java.util.prefs.Preferences;

import main.interfaces.OnChangeAction;
import main.interfaces.RetrieveAction;

public class StringStorageValue extends StorageValue<String>
{
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