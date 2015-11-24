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
package rt.fx.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.PauseTransition;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.util.Duration;
import screensframework.ControlledScreen;
import screensframework.ScreensController;

/**
 * @author rodel.talampas
 *
 */
public abstract class AController implements Initializable, ControlledScreen{

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());
	protected ScreensController myController;
	protected Scene scene;
	
	public void setScene(Scene scene) { this.scene = scene; }
	 
	public abstract void onShow();
	public abstract void onHide();
	
	protected boolean isRent;
	
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}
	
	protected Screen screen = Screen.getPrimary();
	protected Rectangle2D bounds = screen.getVisualBounds();
	
	protected PauseTransition blinkTransition(
			final ObjectProperty<String> colorBlinker, final String idStyle) {
		PauseTransition changeColor = new PauseTransition(new Duration(500));
		changeColor.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				colorBlinker.set(idStyle);
			}
		});
		return changeColor;
	}
}
