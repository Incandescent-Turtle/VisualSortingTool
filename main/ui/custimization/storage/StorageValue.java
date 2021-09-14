package main.ui.custimization.storage;

import java.awt.Color;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import main.interfaces.Closable;
import main.interfaces.OnChangeAction;
import main.interfaces.RetrieveAction;
import main.ui.GUIHandler;
import main.ui.RoryFrame;
import main.ui.custimization.CustomizationGUI;
import main.util.Util;

public class StorageValue implements Closable
{	
	private static final ArrayList<StorageValue> STORAGE_VALUES = new ArrayList<>();
	
	private final RetrieveAction<Integer> retrieveAction;
	private final OnChangeAction<Integer> changeAction;
	private final int defaultValue;
	private final String fullKey;

	
	public StorageValue(String prefix, String key, int defaultValue, OnChangeAction<Integer> changeAction, RetrieveAction<Integer> retrieveAction)
	{
		this.fullKey = prefix + key;
		this.retrieveAction = retrieveAction;
		this.defaultValue = defaultValue;
		this.changeAction = changeAction;
	}
	
	public final void loadValue(Preferences prefs)
	{
		changeAction.doStuff(prefs.getInt(fullKey, defaultValue));
	}
	
	public final void storeValue(Preferences prefs)
	{
		prefs.putInt(fullKey, retrieveAction.retrieve());
	}
	
	public final void removeValue(Preferences prefs)
	{
		prefs.remove(fullKey);
	}
	
	@Override
	public void close()
	{
		storeValue(CustomizationGUI.PREFS);
	}
	
	public static void addStorageValues(StorageValue... values)
	{
		for(StorageValue value : values)
		{
			STORAGE_VALUES.add(value);
			RoryFrame.addClosable(value);
		}
	}
	
	public static void loadAll(Preferences prefs)
	{
		STORAGE_VALUES.stream().forEach(s -> {
			s.loadValue(prefs);
		});
		GUIHandler.update();
	}
	
	public static void storeAll(Preferences prefs)
	{
		STORAGE_VALUES.stream().forEach(s -> s.storeValue(prefs));
	}
	
	public static void removeAll(Preferences prefs)
	{
		STORAGE_VALUES.stream().forEach(s -> s.removeValue(prefs));
	}
	
	public static StorageValue createColorStorageValue(String prefix, String key, Color defaultColor, OnChangeAction<Color> changeAction, RetrieveAction<Color> retrieveAction)
	{
		return new StorageValue(prefix, key, Util.colorToInt(defaultColor), num -> changeAction.doStuff(new Color(num)), () -> Util.colorToInt(retrieveAction.retrieve()));
	}
	
	public class IntStorageValue extends StorageValue
	{

		public IntStorageValue(String prefix, String key, int defaultValue, OnChangeAction<Integer> changeAction,
				RetrieveAction<Integer> retrieveAction)
		{
			super(prefix, key, defaultValue, changeAction, retrieveAction);
		}
		
	}
}