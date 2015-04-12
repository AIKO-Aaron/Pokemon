package ch.aiko.pokemon.settings;

import java.io.File;
import java.util.Map.Entry;

import ch.aiko.util.FileUtil;
import ch.aiko.util.PropertyUtil;

public class Settings extends PropertyUtil {
		
	public static final boolean CREATE_NEW_CONFIG_ON_START = false;
	
	public Settings(String path) {
		super(FileUtil.LoadFile(path));
	}

	public static boolean isUserFont = true;
	public static String font;
	public static boolean isFirstLaunch = false;
	
	public static Settings instance;
	public static float GAIN = -10F;
	
	public static Settings getInstance() {
		return instance;
	}
	
	public static void load() {
		if(CREATE_NEW_CONFIG_ON_START) new File(getPath() + "/settings/config.cfg").delete();
		if(!new File(getPath() + "/settings/config.cfg").exists()) FileUtil.CreateNewFile(getPath() + "/settings/config.cfg");
		instance = new Settings(getPath() + "/settings/config.cfg");
		
		isFirstLaunch = !instance.getBooleanValue("setupCorrect");
		
		if(isFirstLaunch) {
			instance.setValue("setupCorrect", "true");
		}
		
		for(Entry<String, String> entry : PropertyUtil.LoadFileInClassPath("/ch/aiko/pokemon/settings/fields").getEntrySet().entrySet()) {
			System.out.println("Checking for key: " + entry.getKey());
			if(!instance.exists(entry.getKey())) instance.setValue(entry.getKey(), entry.getValue());
		}		
		
		font = get("font");
		if(font.startsWith("user/")) {
			isUserFont = true;
			font = font.substring(5);
		}
		
		GAIN = getFloat("gain");
	}
	
	public static String getPath() {
		String s = System.getProperty("user.home") + "/";
		
		if(System.getProperty("os.name").contains("Mac")) s += "/Library/Application Support/Pokemon/";
		else if(System.getProperty("os.name").contains("Windows")) s += "/AppData/Roaming/Pokemon/";
		else s += "Pokemon/";
		
		return s;
	}
	
	public static String get(String key) {
		return instance.getValue(key);
	}
	
	public static int getInteger(String key) {
		return instance.getValue(key).equalsIgnoreCase(key) ? PropertyUtil.LoadFileInClassPath("/settings/fields").getIntegerValue(key) : instance.getIntegerValue(key);
	}
	
	public static float getFloat(String key) {
		return instance.getValue(key).equalsIgnoreCase(key) ? PropertyUtil.LoadFileInClassPath("/settings/fields").getFloatValue(key) : instance.getFloatValue(key);
	}

	public static double getDouble(String key) {
		return instance.getValue(key).equalsIgnoreCase(key) ? PropertyUtil.LoadFileInClassPath("/settings/fields").getDoubleValue(key) : instance.getDoubleValue(key);
	}
	
	public static boolean getBoolean(String key) {
		return instance.getValue(key).equalsIgnoreCase(key) ? PropertyUtil.LoadFileInClassPath("/settings/fields").getBooleanValue(key) : instance.getBooleanValue(key);
	}
	
	public static void set(String key, String value) {
		instance.setValue(key, value);
		
		if(key.equalsIgnoreCase("font")) {
			if(font.startsWith("user/")) {
				isUserFont = true;
				font = font.substring(5);
			}
			font = value;
		}
	}
}
