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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import rt.fx.base.config.AConfigLoader;
import rt.fx.base.config.CConfigLoader;
import rt.fx.base.db.IDao;
import rt.fx.base.db.ScreenNavigatorEntity;
import rt.fx.base.interfaces.Navigation;
import rt.fx.base.interfaces.Screen;

/**
 * @author rodel.talampas
 *
 */
public abstract class AScreenLoader {
	
	private static Logger LOGGER = LoggerFactory.getLogger(AScreenLoader.class);

	protected static boolean sloaded = false;
	protected static String packageName = "rt.fx";

	protected static Map<String, String> screens = null;
	protected static Map<Object, String> navigation = null;
	protected static Map<String, Map<String, String>> screensNavigation = null;

	protected static Map<?,?> dbMap = null;
	protected static IDao<ScreenNavigatorEntity> daoNavigator = null;
	
	protected static AScreenLoader singleton = null;
	protected static Object singleton_mutex = new Object();

	protected AScreenLoader getLoader() {
		return singleton;
	}

	private void simpleInit(String pkgName) throws Exception {
		LOGGER.debug("Loading Screen Mapping ....");
		if (!sloaded) {
			packageName = pkgName;
			this.getLoader().initialize();
			LOGGER.debug("Loaded Screen Mapping!!!");
		}
	}

	@SuppressWarnings({ "unchecked"})
	protected void initialize() throws Exception {
		sloaded = true;

		Map<String, String> map = new HashMap<String, String>();
		Map<String, Map<String, String>> mapNav = new HashMap<String, Map<String, String>>();

		final StopWatch sw = new StopWatch();
		sw.start();
		final Reflections reflections = new Reflections(packageName,
				new TypeAnnotationsScanner());
		Set<Class<?>> theScreens = reflections
				.getTypesAnnotatedWith(Screen.class);
		sw.stop();
		Map<Object, String> navObj = new HashMap<Object, String>();
		LOGGER.debug("Classpath scanning took: "+ sw.getTotalTimeMillis() + "ms");
		for (final Class<?> theScreen : theScreens) {
			final Screen annotation = theScreen.getAnnotation(Screen.class);
			map.put(annotation.id(), annotation.fileContext());
			LOGGER.debug(annotation.id() + " : "+ annotation.fileContext());

			Map<String, String> nav = new HashMap<String, String>();
			List<Field> fieldList = Arrays.asList(theScreen.getDeclaredFields());
			for (Field f : fieldList) 
				if (f.getType().equals(javafx.scene.control.Button.class) && f.isAnnotationPresent(Navigation.class)) {
					Navigation navBtn = f.getAnnotation(Navigation.class); 
					nav.put(navBtn.id(), navBtn.defaultTarget());
				}
			mapNav.put(annotation.id(), nav);

		}
		
		try {
			dbMap = getConfigLoader().getDao();
			daoNavigator = (IDao<ScreenNavigatorEntity>) dbMap.get("SCREEN_NAVIGATOR");
			
			if (daoNavigator!=null){
				for (String s: mapNav.keySet()){
					Map<String, String> nav = mapNav.get(s);
					for (String k: nav.keySet()){
						ScreenNavigatorEntity entity = daoNavigator.getByPrimary(k);
						if (entity!=null)
							nav.put(k,entity.getTarget().getId());
					}
					mapNav.put(s, nav);
				}
			}
			
			
		} catch (Exception e) {
			throw e;
		}
		
		screens = map;
		navigation=navObj;
		screensNavigation = mapNav;

	}
	
	/**
	 * 
	 * @return
	 */
	protected AConfigLoader getConfigLoader(){
		return new CConfigLoader();
	}

	/**
	 * 
	 * @param pkgName
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getScreenMapping(String pkgName)
			throws Exception {
		LOGGER.debug("Retrieving Screen Mapping!!!");
		if (!sloaded) {
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
	public Map<String, String> getNavigations(String screenId,
			String pkgName){
		if (!sloaded) {
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
	public String getNavigation(String screenId, String navigationId,
			String pkgName){
		if (!sloaded) {
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
	public String getNavigation(String screenId, String navigationId) throws Exception{
		if (!sloaded) {
			try {
				simpleInit(packageName);
			} catch (Exception e) {
				throw e;
			}
		}
		return screensNavigation.get(screenId).get(navigationId);
	}
	
	public static String getNavigationId(Object obj){
		return navigation.get(obj);
	}

}
