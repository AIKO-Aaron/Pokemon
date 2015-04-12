package ch.aiko.pokemon.language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Node;
import org.dom4j.rule.Pattern;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import ch.aiko.util.FileUtil;
import ch.aiko.util.PropertyUtil;

public class Language extends PropertyUtil {

	public static final String defaultLang = "de_de";
	public static final String pathToLanguageFiles = "ch.aiko.pokemon.lang";
	private static HashMap<String, Language> languages = new HashMap<String, Language>();
	public static Language current;
	
	private String lang_id;
	
	public Language(String pathToFile) {
		super(FileUtil.ReadFileInClassPath("/" + pathToFile), pathToFile);
		System.out.println("Successfully loaded: " + pathToFile + " as a Language File");
		lang_id = pathToFile.replace("lang", "").replace(".", "").replace("/", "");
	}
	
	public static void setup() {
		Set<String> langFiles = new Reflections(pathToLanguageFiles, new ResourcesScanner()).getResources(new com.google.common.base.Predicate<String>() {
			public boolean apply(String arg0) {
				System.out.println("Found file: " + arg0);
				return true;
			}
		});
		
		System.out.println("Init Languages");
		
		for(String f : langFiles) {
			if(f.startsWith(".") || !f.endsWith(".lang")) continue;
			languages.put(f.replace("lang", "").replace(".", "").split("/")[f.replace("lang", "").replace(".", "").split("/").length-1].replace("/", ""), new Language(f));
		}
		
		loadLanguage(defaultLang);
	}
	
	public static ArrayList<String> getLanguageNames() {
		ArrayList<String> names = new ArrayList<String>();
		for(Entry<String, Language> l : languages.entrySet()) {
			names.add(l.getKey());
		}
		return names;
	}
	
	public static ArrayList<Language> getLanguages() {
		ArrayList<Language> langs = new ArrayList<Language>();
		for(Entry<String, Language> l : languages.entrySet()) {
			langs.add(l.getValue());
		}
		return langs;
	}
	
	public static void setCurrentLanguage(int index) {
		System.out.println("Setting language: " + getLanguageNames().get(index));
		current = languages.get(getLanguageNames().get(index));
	}
	
	public static void loadLanguage(String lang) {
		if(!languages.containsKey(lang)) return;
		
		current = languages.get(lang);
	}
	
	public static String translate(String s) {
		s = s.replace("/", " / ");
		String ret = "";
		for(String part : s.split(" ")) {
			ret += current.getValue(part) + " ";
		}
		ret = ret.substring(0, ret.length()-1);
		ret = ret.replace(" / ", "/");
		return ret;
	}
	
	public static Pattern pat = new Pattern() {
		
		public boolean matches(Node arg0) {
			return true;
		}
		
		public Pattern[] getUnionPatterns() {
			return null;
		}
		
		public double getPriority() {
			return 0;
		}
		
		public String getMatchesNodeName() {
			return null;
		}
		
		public short getMatchType() {
			return 0;
		}
	};

	public String getName() {
		return (lang_id);
	}
	
	public String getLanguageName() {
		return getValue("lang_name");
	}
}
