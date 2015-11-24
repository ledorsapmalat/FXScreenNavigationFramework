/*
 * Copyright (c) 2015 Rodel M. Talampas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


/**
 * 
 */
package rt.fx.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.springframework.util.StopWatch;

import rt.fx.base.config.CConfigLoader;
import rt.fx.base.db.IDao;
import rt.fx.base.db.ScreenNavigatorEntity;
import rt.fx.base.interfaces.Navigation;
import rt.fx.base.interfaces.Screen;

/**
 * @author rodel.talampas
 *
 */
public class ScreenLoader {

	private static boolean loaded = false;
	private static String packageName = "rt.fx";

	private static Map<String, String> screens = null;
	private static Map<String, Map<String, String>> screensNavigation = null;

	private static Map<?,?> dbMap = null;
	private static IDao<ScreenNavigatorEntity> daoNavigator = null;
	
	private static ScreenLoader singleton = null;
	private static Object singleton_mutex = new Object();

	private static ScreenLoader getLoader() {
		if (singleton == null) {
			System.out.println("Retrieving Loader for the first time...");
			synchronized (singleton_mutex) {
				if (singleton == null) {
					singleton = new ScreenLoader();
					System.out.println("Loader has been created...");
				}
			}
		}
		return singleton;
	}

	private static void simpleInit(String pkgName) throws Exception {
		if (!loaded) {
			packageName = pkgName;
			getLoader().initialize();
		}
	}

	@SuppressWarnings("unchecked")
	private void initialize() {
		loaded = true;

		Map<String, String> map = new HashMap<String, String>();
		Map<String, Map<String, String>> mapNav = new HashMap<String, Map<String, String>>();

		final StopWatch sw = new StopWatch();
		sw.start();
		final Reflections reflections = new Reflections(packageName,
				new TypeAnnotationsScanner());
		Set<Class<?>> theScreens = reflections
				.getTypesAnnotatedWith(Screen.class);
		sw.stop();
		System.out.println("Classpath scanning took: "+ sw.getTotalTimeMillis() + "ms");
		for (final Class<?> theScreen : theScreens) {
			final Screen annotation = theScreen.getAnnotation(Screen.class);
			System.out.println(annotation.id() + " -- " + theScreen.getName());
			map.put(annotation.id(), annotation.fileContext());

			Map<String, String> nav = new HashMap<String, String>();
			List<Field> fieldList = Arrays.asList(theScreen.getDeclaredFields());
			List<Navigation> theButtons = new ArrayList<Navigation>();
			for (Field f : fieldList) 
				if (f.getType().equals(javafx.scene.control.Button.class)) {
					theButtons.add(f.getAnnotation(Navigation.class));
				}
			for (Navigation btn : theButtons) {
				System.out.println("\t\t"+btn.id()+" -- " + btn.defaultTarget());
				nav.put(btn.id(), btn.defaultTarget());
			}
			mapNav.put(annotation.id(), nav);

		}
		
		try {
			dbMap = CConfigLoader.getDao();
			daoNavigator = (IDao<ScreenNavigatorEntity>) dbMap.get("SCREEN_NAVIGATOR");
			
			if (daoNavigator!=null){
				for (String s: mapNav.keySet()){
					Map<String, String> nav = mapNav.get(s);
					for (String k: nav.keySet()){
						System.out.println(k);
						ScreenNavigatorEntity entity = daoNavigator.getByPrimary(k);
						if (entity!=null)
							nav.put(k,entity.getTarget().getId());
					}
					mapNav.put(s, nav);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		screens = map;
		screensNavigation = mapNav;

	}

	/**
	 * 
	 * @param pkgName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getScreenMapping(String pkgName)
			throws Exception {

		if (!loaded) {
			simpleInit(pkgName);
		}

		return screens;

	}

	/**
	 * Returns a list of Navigation Objects with its ID and Target Screen Id
	 * 
	 * @param screenId
	 * @param pkgName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getNavigations(String screenId,
			String pkgName){
		if (!loaded) {
			try {
				simpleInit(pkgName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return screensNavigation.get(screenId);
	}

	/**
	 * returns the Next Screen Id to be Navigated
	 * 
	 * @param screenId
	 * @param navigationId
	 * @param pkgName
	 * @return
	 * @throws Exception
	 */
	public static String getNavigation(String screenId, String navigationId,
			String pkgName){
		if (!loaded) {
			try {
				simpleInit(pkgName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return screensNavigation.get(screenId).get(navigationId);
	}
	
	/**
	 * returns the Next Screen Id to be Navigated
	 * 
	 * @param screenId
	 * @param navigationId
	 * @param pkgName
	 * @return
	 * @throws Exception
	 */
	public static String getNavigation(String screenId, String navigationId){
		if (!loaded) {
			try {
				simpleInit(packageName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return screensNavigation.get(screenId).get(navigationId);
	}
	
	public static void main(String[] args){
		try {
			ScreenLoader.getScreenMapping("rt.fx.sample");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
