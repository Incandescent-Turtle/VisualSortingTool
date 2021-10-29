package ui.custimization.values;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.prefs.Preferences;

public class DoubleStorageValue extends StorageValue<Double>
{
	/**
	 * for storing double types via preferences
	 * @param prefix the class prefix
	 * @param key variable name
	 * @param changeAction the interface to help set a variable while passing in its stored value
	 * @param Supplier the interface to get the current value to store
	 */
	public DoubleStorageValue(String prefix, String key, Consumer<Double> changeAction, Supplier<Double> Supplier)
	{
		super(prefix, key, changeAction, Supplier);
	}

	@Override
	public void loadValue(Preferences prefs)
	{
		changeAction.accept(prefs.getDouble(fullKey, defaultValue));

	}

	@Override
	public void storeValue(Preferences prefs)
	{
		prefs.putDouble(fullKey, retrieveAction.get());
	}
}
