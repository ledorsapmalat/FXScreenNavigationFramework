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

package rt.fx.sample;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import rt.fx.base.ScreenLoader;
import rt.fx.base.interfaces.Navigation;
import rt.fx.base.interfaces.Screen;

/**
 * 
 * @author rodel.talampas
 *
 */

/**
 * This '@Screen' line describes that this FX Controller is tied to a Screen (FXML) called 'main'
 * With a default FXML File /rt/fx/sample/mainScreen.fxml
 */
@Screen(id="main", fileContext="/rt/fx/sample/mainScreen.fxml")
public class MainScreenController extends AController{

	
	@FXML
	/**
	 * This '@Navigation' line describes that this FX Control Button is tied to a Navigation called 'btnMain'
	 * With a default target next screen to be 'screen02'
	 */
	@Navigation(id="btnMain", defaultTarget = "screen02")
	Button btnMain;
	
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void validate(ActionEvent event){
		Button btn = (Button)event.getSource();
		LOGGER.debug(btn.getId());
		/**
		 * Using Angela's Screen Framework, setting the next screen would be easy as its controlled 
		 * by the ScreenLoader Navigation Framework. Even if you have more than 1 Button,
		 * as long as you configured the Navigation Object using the Framework's annotation, 
		 * only 1 line of code is enough
		 */
		try {
			myController.setScreen(new ScreenLoader().getNavigation("main", btn.getId()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onShow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHide() {
		// TODO Auto-generated method stub
		
	}

}
