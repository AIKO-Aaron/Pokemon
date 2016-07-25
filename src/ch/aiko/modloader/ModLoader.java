package ch.aiko.modloader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

public class ModLoader {

	private static final ArrayList<URL> ALL_MOD_FILES = new ArrayList<>();

	private static ArrayList<Method> preInits = new ArrayList<>();
	private static ArrayList<Method> Inits = new ArrayList<>();
	private static ArrayList<Method> postInits = new ArrayList<>();
	private static ArrayList<ModInfo> modInfoLists = new ArrayList<>();
	private static HashMap<String, Object> instances = new HashMap<>();

	private static int Status = 0;
	private static int FileIndex = 0;
	private static int CurrentFile = 0;
	private static int ClassIndex = 0;
	private static int CurrentClass = 0;
	private static int InitIndex = 0;

	private static String CoreInit = "Not known";
	private static String CurrentFileName;
	private static String CurrentClassName;

	public static final void loadMods(PrintStream ps, String dir, Runnable initCore) {
		new Thread(() -> displayStatus()).start();
		findMethods(ps, dir);
		initCore.run();
		start(ps);
	}

	private static void findMethods(PrintStream ps, String dir) {
		ArrayList<Class<?>> classesAnnotated = new ArrayList<>();

		File parent = new File(dir);
		File[] potentialMods = parent.listFiles((File pathname) -> pathname.getAbsolutePath().endsWith(".zip") || pathname.getAbsolutePath().endsWith(".jar"));

		if(!parent.exists()) {
			parent.mkdirs();
			return;
		}
		
		for (File f : potentialMods) {
			Status = 1;
			++CurrentFile;
			CurrentFileName = f.getAbsolutePath();
			ps.println("Loading File: " + CurrentFile + "/" + FileIndex + ": " + CurrentFileName);

			JarFile file = null;
			URLClassLoader cl = null;
			ClassPool pool = new ClassPool();

			try {
				cl = new URLClassLoader(new URL[] { new URL("jar:file:" + f.getAbsolutePath() + "!/") }, null);
				file = new JarFile(f);
			} catch (Throwable t) {
				t.printStackTrace();
			}

			boolean foundInfo = false;
			Enumeration<JarEntry> searchForInfo = file.entries();
			while (searchForInfo.hasMoreElements()) {
				JarEntry nextElement = searchForInfo.nextElement();
				if (nextElement.getName().endsWith(".amf")) {
					modInfoLists.add(new ModInfo(cl.getResourceAsStream(nextElement.getName())));
					foundInfo = true;
				}
			}
			if (!foundInfo) continue;

			try {
				ALL_MOD_FILES.add(new URL("jar:file:" + f.getAbsolutePath() + "!/"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			pool.appendClassPath(new ClassClassPath(Mod.class));
			pool.appendClassPath(new ClassClassPath(GameInit.class));
			pool.appendClassPath(new LoaderClassPath(cl));
			ArrayList<Class<?>> possibleClasses = new ArrayList<>();

			Enumeration<JarEntry> entries = file.entries();
			while (entries.hasMoreElements()) {
				JarEntry nextElement = entries.nextElement();
				if (!nextElement.isDirectory() && !nextElement.getName().startsWith("META-INF")) {
					if (nextElement.getName().endsWith(".class")) {
						String className = nextElement.getName().substring(0, nextElement.getName().length() - 6);
						className = className.replace("/", ".");
						try {
							possibleClasses.add(Class.forName(className, false, cl));
						} catch (Throwable e) {}
					}
				}
			}

			Status = 2;
			CurrentClass = 0;
			ClassIndex = possibleClasses.size();
			possibleClasses.stream().map((c) -> {
				++CurrentClass;
				CurrentClassName = c.getName();
				ps.println("Loading Class: " + CurrentClass + "/" + ClassIndex + ": " + CurrentClassName);
				return c;
			}).forEach((Class<?> c) -> {
				try {
					CtClass clazz = pool.get(c.getName());
					for (CtField field : clazz.getFields()) {
						if (field.getAnnotation(Instance.class) != null) {
							Object inst = null;
							try {
								inst = c.getField(field.getName()).get(null);
							} catch (IllegalArgumentException | IllegalAccessException e) {}
							if (inst != null) {
								instances.put(c.getName(), inst);
							}
						}
					}
					if (clazz.getAnnotation(Mod.class) != null) {
						for (CtMethod m : clazz.getMethods()) {
							Object anno = m.getAnnotation(GameInit.class);
							if (anno != null) {
								if (((GameInit) anno).type() == InitMethod.PRE_INIT) {
									preInits.add(c.getMethod(m.getName(), new Class[] {}));
								}
								if (((GameInit) anno).type() == InitMethod.INIT) {
									Inits.add(c.getMethod(m.getName(), new Class[] {}));
								}
								if (((GameInit) anno).type() == InitMethod.POST_INIT) {
									postInits.add(c.getMethod(m.getName(), new Class[] {}));
								}
							}
						}
					}
				} catch (NotFoundException | ClassNotFoundException | NoSuchFieldException | SecurityException | NoSuchMethodException e) {
					ps.println(e);
				}
			});

			try {
				cl.close();
				file.close();
			} catch (IOException e) {
				ps.println("Failed closing jar file: " + f.getAbsolutePath() + ".");
			}
		}

		Status = 3;

		for (Class<?> c : classesAnnotated) {
			for (Method m : c.getMethods()) {
				if (m.isAnnotationPresent(GameInit.class)) {
					if (((GameInit) m.getAnnotation(GameInit.class)).type() == InitMethod.PRE_INIT) {
						preInits.add(m);
					}
					if (((GameInit) m.getAnnotation(GameInit.class)).type() == InitMethod.INIT) {
						Inits.add(m);
					}
					if (((GameInit) m.getAnnotation(GameInit.class)).type() == InitMethod.POST_INIT) {
						postInits.add(m);
					}
				} else if (c.getSuperclass() == CoreLoader.class) {
					if (m.getName().equals("preInit")) {
						preInits.add(m);
					}
					if (m.getName().equals("init")) {
						Inits.add(m);
					}
					if (m.getName().equals("postInit")) {
						postInits.add(m);
					}
				}
			}
		}
	}

	private static void displayStatus() {
		final JFrame frame = new JFrame();

		final JProgressBar bar1 = new JProgressBar(0, 100);
		final JProgressBar bar2 = new JProgressBar(0, 700);

		final JTextPane area = new JTextPane();
		area.setPreferredSize(new Dimension(750, 150));
		area.setFont(new Font("Arial", 0, 40));
		area.setEditable(false);
		area.setText("1\n2\3\n4\n5\n6\n7\n8\n9\n10");

		SimpleAttributeSet attribs = new SimpleAttributeSet();
		StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
		area.setParagraphAttributes(attribs, true);

		frame.setLayout(new BorderLayout());
		frame.add(area, BorderLayout.NORTH);
		frame.add(bar1, BorderLayout.CENTER);
		frame.add(bar2, BorderLayout.SOUTH);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		frame.setVisible(true);

		while (true) {
			frame.requestFocus();

			int v1 = 0;
			switch (Status) {
				case 1:
					v1 = FileIndex == 0 ? 0 : 100 * CurrentFile / FileIndex;
					break;
				case 2:
					v1 = ClassIndex == 0 ? 0 : 100 * CurrentClass / ClassIndex;
					break;
				case 4:
					v1 = 100 * InitIndex / preInits.size();
					break;
				case 5:
					v1 = 100 * InitIndex / Inits.size();
					break;
				case 6:
					v1 = 100 * InitIndex / postInits.size();
					break;
				default:
					v1 = 0;
			}
			bar1.setValue(v1);
			bar2.setValue(Status * 100 + v1);

			String text = "Status: " + (Status == 0 ? "Loading Internal Files" : Status == 1 ? "Loading Jar" : Status == 2 ? "Loading Classes from Jar" : Status == 3 ? "Setup Core components" : Status == 4 ? "Pre-initialization" : Status == 5 ? "Initializing" : Status == 6 ? "Post-initializing" : Status + "");
			text += "\n\n";
			switch (Status) {
				case 1:
					text += "File " + CurrentFile + " from " + FileIndex + "\n" + CurrentFileName + "\nn";
					break;
				case 2:
					text += "Class " + CurrentClass + " from " + ClassIndex + "\n" + CurrentClassName + "\nfrom File: " + CurrentFile + "/" + FileIndex + ": " + CurrentFileName + "\n";
					break;
				case 3:
					text += "Loading Stuff: " + CoreInit;
					break;
				case 4:
					text += "Pre-initializing: " + InitIndex + "/" + preInits.size();
					break;
				case 5:
					text += "Initializing: " + InitIndex + "/" + Inits.size();
					break;
				case 6:
					text += "Post-initializing: " + InitIndex + "/" + postInits.size();
					break;
				default:
					text += "\n\n\n";
			}
			if (!text.equalsIgnoreCase(area.getText())) {
				area.setText(text);
			}
			if (Status == 7) {
				break;
			}
		}
		frame.setVisible(false);
		frame.dispose();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static Object getInstanceOf(PrintStream ps, Method m) {
		if (instances.containsKey(m.getDeclaringClass().getName())) { return instances.get(m.getDeclaringClass().getName()); }
		Object instance = null;
		try {
			instance = m.getDeclaringClass().newInstance();
		} catch (IllegalAccessException | IllegalArgumentException | InstantiationException e1) {
			ps.println("Couldn't read @instance in your class nor could it be instanziated. Mod will not be loaded...");
		}
		return instance;
	}

	private static void start(PrintStream ps) {
		if (preInits.size() > 0) {
			Status = 4;
			InitIndex = 0;
			preInits.stream().forEach((Method m) -> {
				try {
					Object instance = getInstanceOf(ps, m);
					try {
						++InitIndex;
						m.invoke(instance, new Object[] {});
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
				} catch (Throwable t) {}
			});
		}
		if (Inits.size() > 0) {
			Status = 5;
			InitIndex = 0;
			Inits.stream().forEach((Method m) -> {
				try {
					Object instance = getInstanceOf(ps, m);
					try {
						++InitIndex;
						m.invoke(instance, new Object[] {});
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
				} catch (Throwable t) {}
			});
		}
		if (postInits.size() > 0) {
			Status = 6;
			InitIndex = 0;
			postInits.stream().forEach((Method m) -> {
				try {
					Object instance = getInstanceOf(ps, m);
					try {
						++InitIndex;
						m.invoke(instance, new Object[] {});
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
				} catch (Throwable t) {}
			});
		}
		Status = 7;
	}
}
