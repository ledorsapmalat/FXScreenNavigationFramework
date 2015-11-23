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

This framework was designed for a Desktop Application. If in case for web, I believe there are a bunch of Web UI Framework out there suitable for the same task. But if this is useful in someone's case, I am glad that I can help.

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
		
The Code and the Default Navigation
-----------------------------------
The `ScreenFlowSample` Maven Module contains the basic Java FX Screen / Controller pairing that uses the RT FX's Screen Navigation Framework. 

It is compose of 3 FXML files:
- mainScreen.fxml
- nextScreen01.fxml
- nextScreen02.fxml

With their corresponding FX Controllers:
- MainScreenController.java
- NextScreen01Controller.java
- NextScreen02Controller.java

A typical code for an FX Controller will be like the following below:

```java
/**
 * This '@Screen' line describes that this FX Controller is tied to a Screen (FXML) called 'main'
 * With a default FXML File /rt/fx/sample/mainScreen.fxml
 */
@Screen(id="main", fileContext="/rt/fx/sample/mainScreen.fxml")
public class MainScreenController extends AController
{

	@FXML
	/**
	 * This '@Navigation' line describes that this FX Control Button is tied to a Navigation called 'btnMain'
	 * With a default target next screen to be 'screen02'
	 */
	@Navigation(id="btnMain", defaultTarget = "screen02")
	Button btnMain;

	@FXML
	private void validate(ActionEvent event){
		Button btn = (Button)event.getSource();
		System.out.println(btn.getId());
		/**
		 * Using Angela's Screen Framework, setting the next screen would be easy as its controlled 
		 * by the ScreenLoader Navigation Framework. Even if you have more than 1 Button,
		 * as long as you configured the Navigation Object using the Framework's annotation, 
		 * only 1 line of code is enough
		 */
		myController.setScreen(ScreenLoader.getNavigation("main", btn.getId()));
	}

}

```
Without the ScreenLoader (RT FX's Screen Navigation Framework), the transition screen code would look like this

```java 
private void validate(ActionEvent event) {
	Button btn = (Button) event.getSource();
	switch (btn.getId()) {
	case "btnMain":
		myController.setScreen("screen02");
		break;
	// if you have other buttons to declare
	// you need to add the some piece of code below
	case "btnCancel":
		myController.setScreen("cancel");
		break;
	case "btnBack":
		myController.setScreen("back");
		break;
	default:
		break;
	}
}
```

Overriding the Default Navigation
---------------------------------
There are some instances that the default next screen wont be the next screen the customer (operator of the app) wants. E.g. Instead of going to the default Item Selection Screen first, the customer wants to have another screen before that, Season Screen [Summer, Winter, Fall, Autumn]. Instead of modifying the Screen Controller that redirect to Item Selection Screen, overrite the Controller's Navigation defaultTarget through a series SQL Scripts.

```sql
'This lines of SQL Script will Override the defaultTarget screen id of btnMain navigation object. The default
'is screen02. Now this script will make it as screen01
insert into "APP"."SCREEN_CONTROLLER" ("ID", "FILE_CONTEXT") values('main', '/rt/fx/sample/mainScreen.fxml')
insert into "APP"."SCREEN_NAVIGATOR" ("ID", "OWNER_ID", "TARGET_ID") values('btnMain', 'main', 'screen01')
```

The Object Loader
---------------------------------
The Singleton Class ScreenLoader is brain of the framework. Its purpose is simple: Scan your packages with the framework's annotations and load and in case the default navigation is overriden by the DAO Navigation, update and load them in Memory to be ACCESS later.

The line of code should be called on the very first load of your application.
```java 
   ScreenLoader.getScreenMapping("your.package.com");
```
This will override the framework's package loading. 




