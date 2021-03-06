package rt.fx.sample;

import java.net.URL;
import java.util.ResourceBundle;

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
@Screen(id="screen01", fileContext="/rt/fx/sample/nextScreen01.fxml")
public class NextScreen01Controller extends AController{

	@FXML
	@Navigation(id="btnScreen01", defaultTarget = "main")
	Button btnScreen01;
	
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void validate(ActionEvent event){
		Button btn = (Button)event.getSource();
		LOGGER.debug(btn.getId());
		try {
			myController.setScreen(new ScreenLoader().getNavigation("screen01", btn.getId()));
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
