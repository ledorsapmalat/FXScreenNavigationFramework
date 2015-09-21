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

import java.util.HashMap;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import screensframework.ControlledScreen;
import screensframework.ScreensController;

/**
 * @author rodel.talampas
 *
 */
public class FXScreenController extends ScreensController {
	
	private static int MAIN_DURATION=100; 
	private static int FADE_IN_DURATION=50;
	private static int FADE_OUT_DURATION=150;
	
	private Stage stage = null;
	private AController controller;
	private String currentScreen; 
	
	private HashMap<String, AController> controllers = new HashMap<>();
	
	public FXScreenController(Stage stage){
		this.stage = stage;
	}
	
    public void addControllers(String name, AController controller) {
    	controllers.put(name, controller);
    }
    
    public AController getController(String name){
    	return controllers.get(name);
    }
    
    /**
     * Load screens in the map.
     * @param screenMap
     * @return
     */
    public boolean loadScreen(Map<String, String> screenMap){
    	boolean success=true;
    	for (String key: screenMap.keySet())
    		success&=loadScreen(key, screenMap.get(key));
    	return success;
    }
    
    /***
     * Load screens by Name
     */
	@Override
    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Node loadScreen = (Node) myLoader.load();

            Screen screen = Screen.getPrimary();
    		Rectangle2D bounds = screen.getVisualBounds();
    		
            ((StackPane)loadScreen).setMaxHeight(bounds.getHeight());
            ((StackPane)loadScreen).setMaxWidth(bounds.getWidth());
            
            controller = ((AController) myLoader.getController());
            controller.setScreenParent(this);
            
            addScreen(name, loadScreen);
            addControllers(name, controller);
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
    }
	
	@Override
	/**
	 * Change the duration to bit less for old systems
	 */
	public boolean setScreen(final String name) {
		if (currentScreen!=null && !currentScreen.isEmpty())
			 getController(currentScreen).onHide();
		
        if (screens.get(name) != null) {   //screen loaded
        	currentScreen = name;
            final DoubleProperty opacity = opacityProperty();

            if (!getChildren().isEmpty()) {    //if there is more than one screen
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(MAIN_DURATION), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        getChildren().remove(0);                    //remove the displayed screen
                        getChildren().add(0, screens.get(name));     //add the screen
                        Timeline fadeIn = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                new KeyFrame(new Duration(FADE_IN_DURATION), new KeyValue(opacity, 1.0)));
                        fadeIn.play();
                    }
                }, new KeyValue(opacity, 0.0)));
                fade.play();
            } else {
                setOpacity(0.0);
                getChildren().add(getScreen(name));       //no one else been displayed, then just show
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(FADE_OUT_DURATION), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            getController(name).onShow();
            return true;
        } else {
            System.out.println("Screen hasn't been loaded!!! \n");
            return false;
        }
    }
	
	public Stage getStage(){
		return stage;
	}
	
	public ControlledScreen getController(){
		return controller;
	}
	
}
