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

import java.util.Map;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import rt.fx.base.ScreenLoader;

/**
 * @author rodel.talampas
 *
 */
public class MainClient extends Application {

	public static Map<String, String> screenMap = null;

	FXScreenController mainContainer = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		screenMap = ScreenLoader.getScreenMapping("rt.fx.sample");
		
		mainContainer = new FXScreenController(primaryStage);
		mainContainer.loadScreen(screenMap);

		mainContainer.setScreen("main");

		AnchorPane root = new AnchorPane();
		root.getChildren().addAll(mainContainer);
		AnchorPane.setTopAnchor(mainContainer, 0.0);
		AnchorPane.setRightAnchor(mainContainer, 0.0);
		AnchorPane.setBottomAnchor(mainContainer, 0.0);
		AnchorPane.setLeftAnchor(mainContainer, 0.0);

		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);

		primaryStage.setX(0);
		primaryStage.setY(0);

		primaryStage.setWidth(bounds.getWidth()/2);
		primaryStage.setHeight(bounds.getHeight()/2);

		root.setMaxHeight(bounds.getHeight());
		root.setMaxWidth(bounds.getWidth());
		root.autosize();
		root.prefHeight(bounds.getHeight());
		root.prefWidth(bounds.getWidth());

		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
