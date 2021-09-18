package main.ui.custimization.values;

import java.awt.Color;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import main.interfaces.Closable;
import main.interfaces.OnChangeAction;
import main.interfaces.RetrieveAction;
import main.ui.BetterFrame;
import main.ui.GUIHandler;
import main.ui.custimization.CustomizationGUI;
import main.util.Util;

public abstract class StorageValue<T> implements Closable
{	
	//enum to identify storage action in certain methods
	public static enum StorageAction
	{
		LOAD, STORE, REMOVE;
	}
	
	//list that holds all storable objects for automatic loading/saving/deletion
	private static final ArrayList<StorageValue<?>> STORAGE_VALUES = new ArrayList<>();
	
	//the interface to help set a variable while passing in its stored value
	protected final OnChangeAction<T> changeAction;
	
	//the interface to get the current value to store
	protected final RetrieveAction<T> retrieveAction;

	protected final T defaultValue;
	//prefix + key
	protected final String fullKey;

	/**
	 * class to make storing data in preferences super easy. loads value on creation
	 * @param prefix the class prefix
	 * @param key variable name
	 * @param defaultValue if differing from retrieve action
	 * @param changeAction the interface to help set a variable while passing in its stored value
	 * @param retrieveAction the interface to get the current value to store
	 */
	protected StorageValue(String prefix, String key, T defaultValue, OnChangeAction<T> changeAction, RetrieveAction<T> retrieveAction)
	{
		this.fullKey = prefix + key;
		this.retrieveAction = retrieveAction;
		this.defaultValue = defaultValue;
		this.changeAction = changeAction;
		loadValue(CustomizationGUI.PREFS);
	}
	
	/**
	 * class to make storing data in preferences super easy. sets default value to current retrive
	 * @param prefix the class prefix
	 * @param key variable name
	 * @param changeAction the interface to help set a variable while passing in its stored value
	 * @param retrieveAction the interface to get the current value to store
	 */
	protected StorageValue(String prefix, String key, OnChangeAction<T> changeAction, RetrieveAction<T> retrieveAction)
	{
		this(prefix, key, retrieveAction.retrieve(), changeAction, retrieveAction);
	}
	
	/**
	 * called automatically to load this value to its variable
	 * @param prefs
	 */
	public abstract void loadValue(Preferences prefs);
	/**
	 * called automatically to store this value using its specified method
	 * @param prefs
	 */
	public abstract void storeValue(Preferences prefs);
	
	public final void removeValue(Preferences prefs)
	{
		prefs.remove(fullKey);
	}
	
	@Override
	public void close()
	{
		storeValue(CustomizationGUI.PREFS);
	}
	
	/**
	 * loads, stores, or removes preferences based on the actin thats passed in
	 * @param prefs
	 * @param action corresponding action
	 */
	public static void performStorageAction(Preferences prefs, StorageAction action)
	{
		for(StorageValue<?> sv : STORAGE_VALUES)
		{
			switch(action)
			{
				case LOAD:
					sv.loadValue(prefs);
					break;
					
				case STORE:
					sv.storeValue(prefs);
					break;
					
				case REMOVE:
					sv.removeValue(prefs);
					break;
			}
		}	
		//updates spinners etc
		if(action == StorageAction.LOAD) GUIHandler.update();
	}
	
	/**
	 * this adds it to a list to automitically load and store values
	 * @param values storables to add
	 */
	public static void addStorageValues(StorageValue<?>... values)
	{
		for(StorageValue<?> value : values)
		{
			STORAGE_VALUES.add(value);
			BetterFrame.addClosable(value);
		}
	}
	
	/**
	 * helper method to store colors. technically stores them as {@link IntStorageValue}s
	 * @param prefix the class prefix
	 * @param key the name of the color variable
	 * @param defaultColor the default color this should be
	 * @param changeAction the loaded color is passed into this param, c -> myColor = c is what it should look like
	 * @param retrieveAction this is used to fetch the current state of this color variable to store it
	 * @return the storagevalue equipped to load/save the color
	 */
	public static IntStorageValue createColorStorageValue(String prefix, String key, OnChangeAction<Color> changeAction, RetrieveAction<Color> retrieveAction)
	{
		return new IntStorageValue(prefix, key, num -> changeAction.doStuff(new Color(num)), () -> Util.colorToInt(retrieveAction.retrieve()));
	}
}