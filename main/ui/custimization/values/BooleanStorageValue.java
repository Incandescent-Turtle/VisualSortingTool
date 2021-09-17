package main.ui.custimization.values;

import java.util.prefs.Preferences;

import main.interfaces.OnChangeAction;
import main.interfaces.RetrieveAction;

public class BooleanStorageValue extends StorageValue<Boolean>
{

	public BooleanStorageValue(String prefix, String key, OnChangeAction<Boolean> changeAction, RetrieveAction<Boolean> retrieveAction)
	{
		super(prefix, key, changeAction, retrieveAction);
	}

	@Override
	public void loadValue(Preferences prefs)
	{
		changeAction.doStuff(prefs.getBoolean(fullKey, defaultValue));
	}

	@Override
	public void storeValue(Preferences prefs)
	{
		prefs.putBoolean(fullKey, retrieveAction.retrieve());
	}
}