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
 * Implemented of the Configuration Loader should extend and override some
 * methods
 * 
 * @author rodel.talampas
 *
 */
public abstract class AConfigLoader {

	protected static AConfigLoader singleton = null;
	protected static Object singleton_mutex = new Object();

	protected static ApplicationContext beanFactory = null;
	protected static boolean loaded = false;
	protected String cfgPath;

	protected AConfigLoader getAppLoader() {
		return singleton;
	}

	/**
	 * You can make this a really configurable config by using
	 * FileSystemApplicationXMLContext
	 */
	protected void initSettings() {
		if (beanFactory == null) {
			beanFactory = new ClassPathXmlApplicationContext("config.xml");
		}
	}

	private void simpleInit() throws Exception {
		if (!loaded) {
			initialize();
		}
	}

	/** 
	 * Overwrite to initialize a full path
	 */
	protected synchronized void initialize() throws Exception {
		initialize("/"); // do nothing
	}

	protected void initialize(String cfgPath) {
		if (cfgPath == null || cfgPath.length() == 0) {
            cfgPath = "config";
        }
		this.cfgPath = cfgPath;
		this.getAppLoader().initSettings();
		loaded = true;
	}
	
	public void init() throws Exception{
		simpleInit();
	}

	@SuppressWarnings("unchecked")
	public Map<String, IDao<?>> getDao() throws Exception {
		simpleInit();
		return (Map<String, IDao<?>>) beanFactory.getBean(getDaoBeanName());
	}

	protected String getDaoBeanName() {
		return "daoMapping";
	}
}
