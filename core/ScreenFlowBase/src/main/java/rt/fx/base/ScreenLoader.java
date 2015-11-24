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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rodel.talampas
 *
 */
public class ScreenLoader extends AScreenLoader {

	private static Logger LOGGER = LoggerFactory.getLogger(ScreenLoader.class);
	
	public ScreenLoader(){
		if (singleton == null) {
			LOGGER.debug("Retrieving Screens for the first time...");
			synchronized (singleton_mutex) {
				if (singleton == null) {
					singleton = this;
					LOGGER.debug("Screen Loader has been created...");
				}
			}
		}
		//LOGGER.debug(singleton);
	}
	
	
	public static void main(String[] args){
		try {
			new ScreenLoader().getScreenMapping("rt.fx.sample");
			new ScreenLoader().getScreenMapping("rt.fx.sample");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
