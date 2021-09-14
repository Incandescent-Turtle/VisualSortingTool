package main.ui.custimization.values;

import java.util.prefs.Preferences;

import main.interfaces.OnChangeAction;
import main.interfaces.RetrieveAction;

public class IntStorageValue extends StorageValue<Integer>
{				
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