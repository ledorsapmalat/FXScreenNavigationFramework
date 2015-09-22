RT FX's Screen Navigation Framework
===================================
- Author: Rodel M. Talampas
- Date: Sept 18, 2015
- Version: 0.0.1-Beta

Project status
-----------------
Release: Beta-Version

Summary
-----------------
The idea of making application screens' navigation button configurable, e.g. the NEXT Button's target screen can change, seems to be a problem that can't be solved. The idea behind this project is to create such framework and making it configurable into the extent that there will be less or no code changes on the actual FX Controller's Button's next screen method.

This basically assumes that each Screen has its own FXML file and each having its own Controller API. The classpath location of each FXML file will be stored on a Configuration DB.

The framework is not limited to Buttons though. Every navigation object such as HyperLink or DropDown can be configured as such. 

Technologies / Components
-------------------------
1. Java Annotations (Java 8)
	- Java Annotations will make it easy to track down Screen Properties
2. Database Tables (Derby)
	- The annotated properties will be linked into column values in tables
3. Spring/Hibernate ORM
	- The configuration is using Spring ORM with Hibernate Entity Management
	- You can remove this part for simplicity but needs to do some code changes

	
Resources
---------
1. The project uses Screen Management Framework of Ms. Angela Caicedo of Oracle 
	- https://blogs.oracle.com/acaicedo/entry/managing_multiple_screens_in_javafx1
	- https://github.com/acaicedo/JFX-MultiScreen
2. The project also used some concepts of Spring-Reflection
	- http://techo-ecco.com/blog/spring-custom-annotations/
		
The Code
---------
The `ScreenFlowSample` Maven Module contains the basic Java FX Screen / Controller pairing that uses the RT FX's Screen Navigation Framework. 

It is compose of 3 FXML files:
- mainScreen.fxml
- nextScreen01.fxml
- nextScreen02.fxml

With their corresponding FX Controllers:
- MainScreenController.java
- NextScreen01Controller.java
- NextScreen02Controller.java


```java
@Screen(id="main", fileContext="/rt/fx/sample/mainScreen.fxml")
public class MainScreenController extends AController
{

	@FXML
	@Navigation(id="btnMain", defaultTarget = "screen02")
	Button btnMain;

	@FXML
	private void validate(ActionEvent event){
		Button btn = (Button)event.getSource();
		System.out.println(btn.getId());
		myController.setScreen(ScreenLoader.getNavigation("main", btn.getId()));
	}

}














