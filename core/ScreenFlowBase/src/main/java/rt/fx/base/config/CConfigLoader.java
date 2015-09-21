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
package rt.fx.base.config;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import rt.fx.base.db.IDao;


/**
 * @author rodel.talampas
 *
 */
public class CConfigLoader {

	private static CConfigLoader singleton   	= null;
	private static Object singleton_mutex 			= new Object();
	
	private static ApplicationContext beanFactory = null;
	private static boolean loaded = false;
	
	private static CConfigLoader getAppLoader() {
		if (singleton == null) {
			System.out.println("Retrieving Config Loader for the first time...");
			synchronized (singleton_mutex) {
				if (singleton == null) {
					singleton = new CConfigLoader();
					System.out.println("Config Loader has been created...");
				}
			}
		}
		return singleton;
	}

	/**
	 * You can make this a really configurable config by using FileSystemApplicationXMLContext
	 */
	private void initSettings() {
		if (beanFactory == null) {
			beanFactory = new ClassPathXmlApplicationContext("config.xml");
			System.out.println("BEAN FACTORY: " + beanFactory);
		}
	}
	
	private static void simpleInit() throws Exception{
		if (!loaded){
			initialize();
		}
	}
	
	public static void initialize() {
        getAppLoader().initSettings();
        loaded = true;
    }
	
	@SuppressWarnings("unchecked")
	public static Map<String, IDao<?>> getDao() throws Exception{
    	simpleInit();
    	System.out.println((Map<String, IDao<?>>)beanFactory.getBean("daoMapping"));
    	return (Map<String, IDao<?>>)beanFactory.getBean("daoMapping");
    }
	
}
