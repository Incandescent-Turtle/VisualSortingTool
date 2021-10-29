package ui.tooltips;

import org.json.simple.JSONArray;
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
	private static final Map<String, String> DESCRIPTIONS;

	static
	{
		var manager = ToolTipManager.sharedInstance();
		manager.setDismissDelay(manager.getDismissDelay()*2);

		DESCRIPTIONS = new HashMap<>();
		loadDescriptions();
	}

	private static final String MISC_PREFIX = "misc",
								TOP_BAR_PREFIX = "top bar",
								COLOR_BUTTONS_PREFIX = "color buttons",

								SORTER_PREFIX = "sorter",
								BAR_SORTER = "bar sorter",
								IMAGE_SORTER = "image sorter",

								COLOR_GRADIENT_SORTER_PREFIX = "color gradient",
								VISUALIZER_PREFIX = "visualizer",
								BAR_VISUALIZER_PREFIX = "bar visualizer";

	public enum Keys
	{
		/*
			misc
		 */
		MAKE_PINK(MISC_PREFIX, "make pink"),

		/*
			Top bar UI
		 */
		SHUFFLE(TOP_BAR_PREFIX, "shuffle"),
		ALGORITHMS(TOP_BAR_PREFIX, "algorithms"),
		RUN(TOP_BAR_PREFIX, "run"),
		DELAY(TOP_BAR_PREFIX, "delay"),
		STEP(TOP_BAR_PREFIX, "step"),
		VISUALIZATION_METHOD(TOP_BAR_PREFIX, "visualization"),

		/*
			Customizations for sorters
		 */
		//general
		AMOUNT(SORTER_PREFIX, "amount"),
		REVERSE(SORTER_PREFIX, "reverse"),

		//bar sorters
		BAR_AMOUNT(BAR_SORTER, "bar amount"),

		//color gradient
		LEFT_COLOR(COLOR_GRADIENT_SORTER_PREFIX, "left color"),
		RIGHT_COLOR(COLOR_GRADIENT_SORTER_PREFIX, "right color"),

		//image sorter
		SELECT_FOLER(IMAGE_SORTER, "select folder"),

		/*
			Customizations for all visualizers
		 */
		GAP(VISUALIZER_PREFIX, "gap"),
		MARGINS(VISUALIZER_PREFIX, "margins"),
		SIZE(VISUALIZER_PREFIX, "size"),

		//bar visualizers
		BAR_GAP(BAR_VISUALIZER_PREFIX, "gap"),

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

	private ToolTips(){}

	public static String getDescriptionFor(Keys key, SpinnerNumberModel nm)
	{
		String keyAsString = key.getKey();
		if(!DESCRIPTIONS.containsKey(keyAsString.toLowerCase()))
		{
			System.err.println("Error: invalid attempt at description access via key \"" + keyAsString + "\"");
			return "";
		}
		String spinnerModelString = nm != null ? '\n' + Util.spinnerNumberModelToString(nm) : "";
		return ("<html><p>" + DESCRIPTIONS.get(keyAsString)  + spinnerModelString + "</p></html>").replaceAll("\\n", "<br>");
	}

	public static String getDescriptionFor(Keys key)
	{
		return getDescriptionFor(key, null);
	}

	public static void loadDescriptions()
	{
		loadDescriptionsFrom(Paths.get("src/main/resources/tooltips").toFile());
		for (String key : DESCRIPTIONS.keySet())
		{
			System.out.println(key + " " + DESCRIPTIONS.get(key));
		}
	}

	public static void loadDescriptionsFrom(File folder)
	{
		try {
			for(File file : folder.listFiles())
			{
				if(file.isDirectory())
				{
					System.out.println("dir " + file.getName());
					loadDescriptionsFrom(file);
					continue;
				} else if(!Util.getFileExtension(file).equals(".json")) {
					continue;
				}
				JSONObject descDoc = (JSONObject) JSONValue.parse(Files.readString(Paths.get(file.toURI())));
				for(var descEntry : (Set<Map.Entry>) descDoc.entrySet())
				{
					String desc = "";
					if(descEntry.getValue() instanceof JSONArray array)
					{
						for (var entry:  (Set<Map.Entry>) ((JSONObject)array.get(0)).entrySet())
						{
							desc += entry.getKey();

							if(!entry.getKey().equals(""))
								desc += ": ";

							desc +=  entry.getValue() + "<br><br>";
						}
					}

					if(descEntry.getValue() instanceof String s)
					{
						desc = s;
					}
					DESCRIPTIONS.put(Util.getNameWithoutExtension(file).replaceAll("_", " ") + " " + ((String)descEntry.getKey()).toLowerCase(), desc.replaceAll("<br>$", ""));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}