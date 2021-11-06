package ui.tooltips;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import util.Util;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ToolTips
{
	public final static ToolTipManager MANAGER = ToolTipManager.sharedInstance();

	//map that holds all the descriptions for tooltips
	private static final Map<String, String> DESCRIPTIONS;

	static
	{
		//a lot of time to read the tooltips
		MANAGER.setDismissDelay(MANAGER.getDismissDelay()*15);
		//tooltips popup immediately
		MANAGER.setInitialDelay(0);

		DESCRIPTIONS = new HashMap<>();

		//	loading descriptions recursively
		loadDescriptionsFrom(Paths.get("src/main/resources/tooltips").toFile());
	}

	private ToolTips(){}

	/**
	 * this retrieves the description for the specified key, as well as support to show info
	 * on spinner number models
	 * @param key the key—this corresponds to a specific description in a json file
	 * @param nm the spinner number model to display min, max, step size, allowance of decimal values
	 * @return returns the string holding the description
	 */
	public static String getDescriptionFor(Keys key, SpinnerNumberModel nm)
	{
		//	the key as a string (thats how theyre stored internally)
		String keyAsString = key.getKey();

		//	if the description doesnt exist
		if(!DESCRIPTIONS.containsKey(keyAsString.toLowerCase()))
		{
			System.err.println("Error: invalid attempt at description access via key \"" + keyAsString + "\"");
			return "";
		}
		//	string giving info on the spinner number model
		String spinnerModelString = nm != null ? '\n' + Util.spinnerNumberModelToString(nm) : "";
		/*
			uses html formatting which allows for <br> to be properly utilized, as well as other html things
			such as color support, bolding, etc.

			gets the description from the corresponding description file using the key and adds the
			spinner information
		 */
		return ("<html><p>" + DESCRIPTIONS.get(keyAsString)  + spinnerModelString + "</p></html>").replaceAll("\\n", "<br>");
	}

	/**
	 * to get the description for the given key, when number spinner model info is not needed
	 * @param key the key to be used
	 * @return returns the formatted string containing the requested description
	 */
	public static String getDescriptionFor(Keys key)
	{
		return getDescriptionFor(key, null);
	}

	/**
	 * only folders should be passed in
	 * adds all the descriptions from all .json files, and recursively checks any sub-folders
	 * @param folder
	 */
	private static void loadDescriptionsFrom(File folder)
	{
		try {
			//	for every file within this folder
			for(File file : folder.listFiles())
			{
				//	will recursively check this folder for .json descriptions and sub-folders
				if(file.isDirectory())
				{
					loadDescriptionsFrom(file);
					continue;
					//	if the file isnt a folder or json
				} else if(!Util.getFileExtension(file).equals(".json")) {
					continue;
				}
				//	the whole description document
				JSONObject descDoc = (JSONObject) JSONValue.parse(Files.readString(Paths.get(file.toURI())));
				//	for every entry (or "Key")
				for(var descEntry : (Set<Map.Entry>) descDoc.entrySet())
				{
					String desc = "";
					/*
						for things like algorithms, or sorters/visualizers, arrays are used
						to cleanly organize how descriptions are stored. each different
						element goes on a different line automatically, with one line in-between
					 */
					if(descEntry.getValue() instanceof JSONObject object)
					{
						for (var entry:  (Set<Map.Entry<String, String>>) object.entrySet())
						{
							String entryName = entry.getKey();

							//	if the entry is named like | "Quick Sort": "Quick sort..."
							if(!entryName.equals(""))
							{
								desc += entryName + ": ";
							}

							//	adds description for this entry and double spacing
							desc +=  entry.getValue() + "<br><br>";
						}
					}

					//	if this is a single entry
					if(descEntry.getValue() instanceof String s)
					{
						desc = s;
					}
					//	using the file name, uniquely stores the descriptions, and removes trailing whitespace from <br>
					DESCRIPTIONS.put(Util.getNameWithoutExtension(file).replaceAll("_", " ") + " " + ((String)descEntry.getKey()).toLowerCase(), desc.replaceAll("<br>$", ""));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//	prefixes for keys
	private static final String MISC_PREFIX = "misc",
								TOP_BAR_PREFIX = "top bar",
								COLOR_BUTTONS_PREFIX = "color buttons",

								SORTER_PREFIX = "sorter",
								BAR_SORTER = "bar sorter",
								IMAGE_SORTER = "image sorter",
								COLOR_GRADIENT_SORTER_PREFIX = "color gradient",

								VISUALIZER_PREFIX = "visualizer",
								BAR_VISUALIZER_PREFIX = "bar visualizer",

								ALGORITHM_PREFIX = "algorithm",
								SELECTION_SORT_PREFIX = "selection sort";

	//	the keys for tooltips!
	public enum Keys
	{
		/*
			misc
		 */
		MAKE_PINK(MISC_PREFIX, "make pink"),
		RESET_TO_SAVE(MISC_PREFIX, "reset to save"),
		RESET_TO_DEFAULTS(MISC_PREFIX, "reset to defaults"),

		/*
			Top bar UI
		 */
		SHUFFLE(TOP_BAR_PREFIX, "shuffle"),
		ALGORITHMS(TOP_BAR_PREFIX, "algorithms"),
		RUN(TOP_BAR_PREFIX, "run"),
		DELAY(TOP_BAR_PREFIX, "delay"),
		STEP(TOP_BAR_PREFIX, "step"),
		VISUALIZATION_METHOD(TOP_BAR_PREFIX, "visualization"),
		TOGGLE_TOOLTIPS(TOP_BAR_PREFIX, "toggle tooltips"),


		/*
			Customizations for sorters
		 */
		//general
		AMOUNT(SORTER_PREFIX, "amount"),
		REVERSE_SORT_ORDER(SORTER_PREFIX, "reverse"),

		//bar sorters
		BAR_AMOUNT(BAR_SORTER, "bar amount"),

		//color gradient
		LEFT_COLOR(COLOR_GRADIENT_SORTER_PREFIX, "left color"),
		RIGHT_COLOR(COLOR_GRADIENT_SORTER_PREFIX, "right color"),

		//image sorter
		SELECT_FOLDER(IMAGE_SORTER, "select folder"),
		RELOAD_IMAGES(IMAGE_SORTER, "reload"),

		/*
			Customizations for all visualizers
		 */
		GAP(VISUALIZER_PREFIX, "gap"),
		MARGINS(VISUALIZER_PREFIX, "margins"),
		SIZE(VISUALIZER_PREFIX, "size"),

		//bar visualizers
		BAR_GAP(BAR_VISUALIZER_PREFIX, "gap"),

		/*
			Algorithms
		 */
		CONFIRMATION_COLOR(ALGORITHM_PREFIX,"confirmation color"),
		ANIMATE_CONFIRMATION(ALGORITHM_PREFIX, "animate confirmation"),
		HIGHLIGHTS(ALGORITHM_PREFIX, "highlights"),
		SWAP_COLOR(ALGORITHM_PREFIX, "swap color"),
		COMPARE_COLOUR(ALGORITHM_PREFIX, "compare color"),

		//selection sort
		FIRST_INDEX_COLOR(SELECTION_SORT_PREFIX, "1st index color"),
		MIN_COLOR(SELECTION_SORT_PREFIX, "min color"),

		/*
			Color buttons
		 */
		BACKGROUND_COLOR(COLOR_BUTTONS_PREFIX, "background"),
		DEFAULT_COLOR(COLOR_BUTTONS_PREFIX, "default");

		private final String key;

		Keys(String prefix, String key)
		{
			this.key = prefix + " " + key;
		}

		public String getKey()
		{
			return key;
		}
	}
}