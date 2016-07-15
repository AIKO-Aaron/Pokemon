package ch.aiko.pokemon.language;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import ch.aiko.pokemon.Pokemon;
import ch.aiko.util.FileUtil;
import ch.aiko.util.PropertyUtil;

public class Language extends PropertyUtil {

	public static final String defaultLang = "de_de";
	public static final String pathToLanguageFiles = "ch.aiko.pokemon.lang";
	private static HashMap<String, Language> languages = new HashMap<String, Language>();
	public static Language current;

	private String lang_id;
	private String __lang;

	public Language(String pathToFile) {
		super(FileUtil.ReadFileInClassPath((pathToFile.startsWith("/") ? "" : "/") + pathToFile), pathToFile);
		Pokemon.out.println("Successfully loaded: " + pathToFile + " as a Language File");
		lang_id = pathToFile.replace("lang", "").replace(".", "").replace("/", "");
		__lang = pathToFile.split("/")[pathToFile.split("/").length - 1].split("\\.")[0];
	}

	public static void setup() {
		/**
		 * Set<String> langFiles = new Reflections(pathToLanguageFiles, new ResourcesScanner()).getResources(new com.google.common.base.Predicate<String>() { public boolean apply(String arg0) { System.out.println("Found file: " + arg0); return true; } });
		 */

		File Me = null;
		try {
			Me = new File(Language.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e) {
			Pokemon.out.err("Error while searching for language files.");
			return;
		}
		Pokemon.out.println("Path to me: " + Me.getAbsolutePath());
		ArrayList<String> langFiles = new ArrayList<String>();
		if (Me.isDirectory()) {
			ArrayList<File> files = FileUtil.listFiles(Me.getAbsolutePath(), ".lang");
			for (File f : files)
				langFiles.add(f.getAbsolutePath().substring(Me.getAbsolutePath().length()));
		} else {
			try {
				JarFile file = new JarFile(Me);
				Enumeration<JarEntry> entries = file.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					if (entry.isDirectory()) continue;
					Pokemon.out.println("Found file:" + entry.getName());
					if (entry.getName().endsWith(".lang")) langFiles.add(entry.getName());
				}

				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Pokemon.out.println("Init Languages");
		languages.clear();
		for (String f : langFiles) {
			if (f.startsWith(".") || !f.endsWith(".lang")) continue;
			languages.put(f.replace("lang", "").replace(".", "").split("/")[f.replace("lang", "").replace(".", "").split("/").length - 1].replace("/", ""), new Language(f));
		}

		loadLanguage(defaultLang);
	}

	public static ArrayList<String> getLanguageNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (Entry<String, Language> l : languages.entrySet()) {
			names.add(l.getKey());
		}
		return names;
	}

	public static ArrayList<Language> getLanguages() {
		ArrayList<Language> langs = new ArrayList<Language>();
		for (Entry<String, Language> l : languages.entrySet()) {
			langs.add(l.getValue());
		}
		return langs;
	}

	public static void setCurrentLanguage(int index) {
		System.out.println("Setting language: " + getLanguageNames().get(index));
		current = languages.get(getLanguageNames().get(index));
	}

	public static void loadLanguage(String lang) {
		if (!languages.containsKey(lang)) return;

		current = languages.get(lang);
	}

	public static String translate(String s) {
		s = s.replace("/", " / ");
		String ret = "";
		for (String part : s.split(" ")) {
			ret += current.getValue(part) + " ";
		}
		ret = ret.substring(0, ret.length() - 1);
		ret = ret.replace(" / ", "/");
		ret = ret.substring(0, 1).toUpperCase() + ret.substring(1);
		return ret;
	}

	public String getName() {
		return (lang_id);
	}

	public String getLanguageName() {
		return getValue(__lang);
	}
}
