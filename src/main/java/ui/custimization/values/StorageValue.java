package ui.custimization.values;

import interfaces.Closable;
import ui.BetterFrame;
import ui.GUIHandler;
import ui.custimization.CustomizationGUI;
import util.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.prefs.Preferences;

public abstract class StorageValue<T> implements Closable
{	
	//enum to identify storage action in certain methods
	public enum StorageAction
	{
		LOAD, STORE, REMOVE, RESET_TO_SAVE, RESET_TO_DEFAULTS
	}
	
	//list that holds all storable objects for automatic loading/saving/deletion
	private static final ArrayList<StorageValue<?>> STORAGE_VALUES = new ArrayList<>();
	
	//the interface to help set a variable while passing in its stored value
	protected final Consumer<T> changeAction;
	
	//the interface to get the current value to store
	protected final Supplier<T> retrieveAction;

	protected final T defaultValue;
	//prefix + key
	protected final String fullKey;

	private boolean canDefaultReset = true;
	private boolean canSaveReset = true;

	/**
	 * class to make storing data in preferences super easy. loads value on creation
	 * @param prefix the class prefix
	 * @param key variable name
	 * @param defaultValue if differing from retrieve action
	 * @param changeAction the interface to help set a variable while passing in its stored value
	 * @param Supplier the interface to get the current value to store
	 */
	protected StorageValue(String prefix, String key, T defaultValue, Consumer<T> changeAction, Supplier<T> Supplier)
	{
		this.fullKey = prefix + key;
		this.retrieveAction = Supplier;
		this.defaultValue = defaultValue;
		this.changeAction = changeAction;
		loadValue(CustomizationGUI.PREFS);
	}
	
	/**
	 * class to make storing data in preferences super easy. sets default value to current retrieve
	 * @param prefix the class prefix
	 * @param key variable name
	 * @param changeAction the interface to help set a variable while passing in its stored value
	 * @param Supplier the interface to get the current value to store
	 */
	protected StorageValue(String prefix, String key, Consumer<T> changeAction, Supplier<T> Supplier)
	{
		this(prefix, key, Supplier.get(), changeAction, Supplier);
	}
	
	/**
	 * called automatically to load this value to its variable
	 * @param prefs the preferences object to use
	 */
	public abstract void loadValue(Preferences prefs);
	/**
	 * called automatically to store this value using its specified method
	 * @param prefs the preferences object to use
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
	 * if this value will be reset when either reset button is pressed
	 * @param canReset whether it can be reset
	 * @return this instance
	 */
	public StorageValue<T> setResetable(boolean canReset)
	{
		canDefaultReset = canReset;
		canSaveReset = canReset;
		return this;
	}

	/**
	 * if this value will be reset when the reset to save button is pressed
	 * @param canSaveReset whether it can be reset to save
	 * @return this instance
	 */
	public StorageValue<T> setSaveResetable(boolean canSaveReset)
	{
		this.canSaveReset = canSaveReset;
		return this;
	}

	/**
	 * if this value will be reset when the default button is pressed
	 * @param canDefaultReset whether it can be reset to default
	 * @return this instance
	 */
	public StorageValue<T> setDefaultResetable(boolean canDefaultReset)
	{
		this.canDefaultReset = canDefaultReset;
		return this;
	}
	
	/**
	 * loads, stores, or removes preferences based on the action that's passed in
	 * @param prefs the preferences object to use
	 * @param action corresponding action
	 */
	public static void performStorageAction(Preferences prefs, StorageAction action)
	{
		for(StorageValue<?> sv : STORAGE_VALUES)
		{
			switch (action)
			{
				case LOAD -> sv.loadValue(prefs);
				case STORE -> sv.storeValue(prefs);
				case REMOVE -> sv.removeValue(prefs);

				case RESET_TO_SAVE -> {
					if (!sv.canSaveReset)
						break;
					sv.loadValue(prefs);
				}
				case RESET_TO_DEFAULTS -> {
					if (!sv.canDefaultReset)
						break;
					sv.removeValue(prefs);
					sv.loadValue(prefs);
				}
			}
		}	
		//updates spinners etc
		if(action == StorageAction.LOAD) GUIHandler.update();
	}
	
	/**
	 * this adds it to a list to automatically load and store values
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
	 * @param changeAction the loaded color is passed into this param, c -> myColor = c is what it should look like
	 * @param Supplier this is used to fetch the current state of this color variable to store it
	 * @return the {@link StorageValue} equipped to load/save the color
	 */
	public static IntStorageValue createColorStorageValue(String prefix, String key, Consumer<Color> changeAction, Supplier<Color> Supplier)
	{
		return new IntStorageValue(prefix, key, num -> changeAction.accept(new Color(num)), () -> Util.colorToInt(Supplier.get()));
	}
}