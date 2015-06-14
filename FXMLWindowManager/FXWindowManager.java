package FXMLWindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Hashtable;

import XMLService.Configuration;
import XMLService.XMLService;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import interfaces.Mapper;
import interfaces.ViewController;

/**
 * 
 * @author Zonglin Li
 *
 */
public class FXWindowManager{
	
	private Hashtable<String, ViewController> views;
	private Hashtable<String, Class<?>> loadedViewClasses;
	private Hashtable<String, Stage> stages;
	private URI viewinfoxmlLocation;
	private Mapper mapper;
	public static FXWindowManager instance;
	
	/**
	 * The singleton constructor
	 * @param viewinfoxmlLocation
	 * @param mapper
	 */
	private FXWindowManager(URI viewinfoxmlLocation, Mapper mapper){
		this.viewinfoxmlLocation = viewinfoxmlLocation;
		this.mapper = mapper;
		stages = new Hashtable<String, Stage>();
		views = new Hashtable<String, ViewController>();
		loadedViewClasses = new Hashtable<String, Class<?>>();
	}
	
	/**
	 * Singleton design pattern. For only one instance to be created
	 * @param viewinfoxmlLocation
	 * @param mapper
	 * @return
	 */
	public static synchronized FXWindowManager getInstance(URI viewinfoxmlLocation, 
			Mapper mapper){
		if(instance == null){
			instance = new FXWindowManager(viewinfoxmlLocation, mapper);
		}
		return instance;
	}
	
	/**
	 * Set the mapper cooperates with this FXWindowManager
	 * @param mapper
	 */
	public void setMapper(Mapper mapper){
		this.mapper = mapper;
	}
	
	/**
	 * Replace the Scene content of the Stage(window) specified. 
	 * The stage will be centered at the original location of the stage.
	 * The size of the stage will be the same as the new scene.
	 * @param stageName	The name of the stage will be replaced with new scene
	 * (the <code>stageName</code> is the same as the name of the scene currently 
	 * in that <code>stage</code>)
	 * @param newSceneName the name of the scene that will be displayed in the 
	 * specified stage
	 * @param resizeAble define whether the stage is resizable after replacing the 
	 * scene
	 * @return
	 * @throws Exception
	 */
	public ViewController replaceSceneContent(String stageNameandHashcode, String newSceneName,
			boolean resizeAble) throws Exception {
		// TODO Auto-generated method stub
		return replaceSceneContent(stageNameandHashcode,newSceneName,-1,-1,resizeAble, null);
	}
	
	/**
	 * Replace the Scene content of the Stage(window) specified.
	 * The stage will be centered at the original location of the stage.
	 * The size of the stage will be the same as the new scene.
	 * @param stageName The name of the stage will be replaced with new scene 
	 * (the stageName is the same as the name of the scene currently in that stage)
	 * @param newSceneName the name of the scene that will be displayed in the 
	 * specified stage
	 * @param resizeAble define whether the stage is resizable after replacing the 
	 * scene
	 * @param title The title of the stage after replacing the scene
	 * @return
	 * @throws Exception
	 */
	public ViewController replaceSceneContent(String stageNameandHashcode, String newSceneName,
			boolean resizeAble, String title) throws Exception {
		// TODO Auto-generated method stub
		return replaceSceneContent(stageNameandHashcode,newSceneName,-1,-1,resizeAble, title);
	}
	
	/**
	 * Replace the Scene content of the Stage(window) specified.
	 * The size of the stage will be the same as the new scene.
	 * @param stageName The name of the stage will be replaced with new scene 
	 * (the stageName is the same as the name of the scene currently in that stage)
	 * @param newSceneName the name of the scene that will be displayed in the 
	 * specified stage
	 * @param x the X location of the stage after replacing the scene.
	 * @param y the Y location of the stage after replacing the scene.
	 * @param resizeAble define whether the stage is resizable after replacing the 
	 * scene
	 * @param title The title of the stage after replacing the scene
	 * @return
	 * @throws Exception
	 */
	public ViewController replaceSceneContent(String stageNameandHashcode, String newSceneName,
			int x, int y,boolean resizeAble, String title) throws Exception {
		// TODO Auto-generated method stub
		Stage stage = stages.remove(stageNameandHashcode);
		Path Location = viewLocationFetcher(newSceneName);
		if(Location == null){
			throw new Exception("No such view with name: " + newSceneName);
		}
		String jarLocation = Location.toString(); 
		XMLService xmlSevice = XMLService.getInstance();
		URL location = new URL("jar:file:" + jarLocation +"!/" + "config.xml");
		InputStream is = location.openStream();
		Configuration cg = xmlSevice.parseXML(is).getChild("Views$1");
		String viewName = cg.getChild(newSceneName + "$1").getText();
		String controller = cg.getChild(newSceneName + "$1").getAttributeValue("controller");
		/**********************Load the controller class**********************/
		Class<?> controllerClass = loadController(controller,new URL("jar:file:" + jarLocation +"!/"));
		URL fxmlLocation = new URL("jar:file:" + jarLocation +"!/" + viewName);
		InputStream in = fxmlLocation.openStream();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(new URL("jar:file:" + jarLocation+"!/"));
		loader.setController(controllerClass.newInstance());
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
		} finally {
			in.close();
        }
        if(x < 0 || y < 0){
        	double nowWidth = stage.getWidth();
        	double nowHight = stage.getHeight();
        	double nowX = stage.getX();
        	double nowY = stage.getY();
        	double futureWidth = stage.getWidth();
        	double futureHight = stage.getHeight();
        	double futureX = nowX - (futureWidth - nowWidth) / 2;
        	double futureY = nowY - (futureHight - nowHight) / 2;
        	if(nowX >=0 && nowY >= 0){
        		stage.setX(futureX);
        		stage.setY(futureY);
        	}
        }else{
        	stage.setX(x);
        	stage.setY(y);
        }
        stage.setResizable(resizeAble);
        Scene scene = new Scene(page);
        stage.setScene(scene);        
        stage.sizeToScene();
        if(title != null){
        	stage.setTitle(title);
        }
        ViewController n = (ViewController)loader.getController();
        stages.put(newSceneName + n.hashCode(), stage);
        this.views.put(newSceneName + n.hashCode(), n);
        this.views.remove(stageNameandHashcode);
        n.setMapper(mapper);
        n.initialize(scene, stage);
        is.close();
		return n;
	}
	
	/**
	 * Start a new Stage(window).
	 * @param viewName The name of the view YOU GIVE. Will be the name of the NewViewName of 
	 * Update or the ViewName of Update
	 * @param title The title of the new Stage
	 * @param resizeAble Whether the new Stage is resizable
	 * @param stageStyle The StageStyle object of that Stage
	 * @return
	 * @throws Exception
	 */
	public ViewController showNewStage(String viewName, String title,
			boolean resizeAble, StageStyle stageStyle) throws Exception {
		// TODO Auto-generated method stub
		Stage newOne = new Stage();
		stages.put(viewName, newOne);
		ViewController control = replaceSceneContent(viewName, viewName,resizeAble, title);
		if(stageStyle != null){
			newOne.initStyle(stageStyle);
		}
		newOne.show();
		return control;
	}
	
	/**
	 * Start a new stage.
	 * @param viewName The name of the view YOU GIVE. Will be the name of the NewViewName of 
	 * Update or the ViewName of Update
	 * @param title The title of the new Stage
	 * @param resizeAble Whether the new Stage is resizable
	 * @param stageStyle The StageStyle object of that Stage
	 * @param x The x coordinate of the new started stage
	 * @param y The y coordinate of the new started stage
	 * @return
	 * @throws Exception
	 */
	public ViewController showNewStage(String viewName, String title,
			boolean resizeAble, StageStyle stageStyle, int x, int y) throws Exception {
		// TODO Auto-generated method stubStage newOne = new Stage();
		Stage newOne = new Stage();
		stages.put(viewName, newOne);
		ViewController control = replaceSceneContent(viewName, viewName,resizeAble, title);
		stages.remove(viewName);
		if(stageStyle != null){
			newOne.initStyle(stageStyle);
		}
		newOne.setX(x);
		newOne.setY(y);
		newOne.show();
		return control;
	}
	
	
	/**
	 * Create a new dialog window with the specified parent and at the default location
	 * @param viewName The viewName of the View of the new Dialog stage. It could only be the 
	 * field of the newViewName in Update
	 * @param title The title of the new dialog Stage
	 * @param resizeAble Whether the new dialog Stage can be resizable
	 * @param stageStyle The StageStyle of the new Stage
	 * @param parentViewNameandhashCode It will be used to locate the exact Object of the parent Stage.
	 * You do not need to provide this in Update. Instead, you should provide the parent View Name and 
	 * the hash code of the parent ViewController.
	 * @return
	 * @throws Exception
	 */
	public ViewController showNewDialog(String viewName, String title,
			boolean resizeAble, StageStyle stageStyle, String parentViewNameandhashCode) throws Exception{
		System.out.println(parentViewNameandhashCode);
		System.out.println(viewName);
		Stage parent = stages.get(parentViewNameandhashCode);
		Stage newOne = new Stage();
		stages.put(viewName, newOne);
		ViewController control = replaceSceneContent(viewName, viewName,resizeAble, title);
		if(stageStyle != null){
			newOne.initStyle(stageStyle);
		}
		newOne.initModality(Modality.APPLICATION_MODAL);
		newOne.initOwner(parent);
		newOne.show();
		return control;
	}
	
	/**
	 * Create a new dialog stage with the specified parent and location.
	 * @param viewName
	 * @param title
	 * @param resizeAble
	 * @param stageStyle
	 * @param x
	 * @param y
	 * @param parentViewNameandhashCode
	 * @return
	 * @throws Exception
	 */
	public ViewController showNewDialog(String viewName, String title,
			boolean resizeAble, StageStyle stageStyle, int x, int y,
			String parentViewNameandhashCode) throws Exception{
		Stage parent = stages.get(parentViewNameandhashCode);
		Stage newOne = new Stage();
		stages.put(viewName, newOne);
		ViewController control = replaceSceneContent(viewName, viewName,resizeAble, title);
		if(stageStyle != null){
			newOne.initStyle(stageStyle);
		}
		newOne.setX(x);
		newOne.setY(y);
		newOne.initModality(Modality.APPLICATION_MODAL);
		newOne.initOwner(parent);
		newOne.show();
		return control;
	}
	
	/**
	 * Close the stage as specified as the String <code>viewName</code> and the hash code of the parent 
	 * ViewController.
	 * @param viewName
	 */
	public void shutdownStage(String viewNameandHashCode) {
		// TODO Auto-generated method stub
		Stage remove = stages.remove(viewNameandHashCode);
		views.remove(viewNameandHashCode);
		if(remove != null){ remove.close(); }
	}
	
	/**
	 * Change the setting of the stage indicated by the String <code>stageName</code>.
	 * @param stageName
	 * @param resizAble
	 * @param title
	 * @param x
	 * @param y
	 */
	public void changingSettingofStage(String viewNameandHashCode,
			boolean resizAble, String title, int x, int y) {
		// TODO Auto-generated method stub
		Stage toChange = stages.get(viewNameandHashCode);
		if(toChange == null){
			return;
		}
		toChange.setResizable(resizAble);
		if(title != null){
			toChange.setTitle(title);
		}
		if(x >= 0 && y >= 0){
			toChange.setX(x);
			toChange.setY(y);
		}
	}
	
	/**
	 * Find the path of the FXML file of the scene specified by the 
	 * String <code>viewName</code>.
	 * @param viewName
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public Path viewLocationFetcher(String viewName) 
			throws MalformedURLException, IOException{
		Path location = Paths.get("");
		XMLService xml = XMLService.getInstance();
		InputStream in = viewinfoxmlLocation.toURL().openStream();
		Configuration pluginfo = xml.parseXML(in);
		Enumeration<Configuration> directories = pluginfo.getAllChildren();
		while(directories != null && directories.hasMoreElements()){
			Configuration directory = directories.nextElement();
			Path get = directoryScanner(location, directory, viewName);
			if(get != null){
				return get;
			}
		}
		return null;
	}
	
	/**
	 * Scanning the directory for specific FXML file. Functions like the 
	 * <code>directoryScanner</code> in <code>JarClassLoader</code>
	 * @param parentPath
	 * @param node
	 * @param viewName
	 * @return
	 */
	public Path directoryScanner(Path parentPath, 
			Configuration node, String viewName){
		Path currentPath = parentPath.resolve(node.getAttributeValue("path"));
		Enumeration<Configuration> pluginsOrDirectories = 
				node.getAllChildren();
		while(pluginsOrDirectories != null && pluginsOrDirectories.hasMoreElements()){
			Configuration child = pluginsOrDirectories.nextElement();
			String nodeName = child.getName();
			if(nodeName.indexOf("$") != -1){
				nodeName = nodeName.substring(0, nodeName.indexOf("$"));
			}
			if(nodeName.equals("plugin")){
				Enumeration<Configuration> classes = child.getAllChildren();
				while(classes != null && classes.hasMoreElements()){
					Configuration clazz = classes.nextElement();
					if(clazz.getText().equals(viewName) && !clazz.getName().startsWith("class")){ 
						return currentPath.resolve(child.getAttributeValue("jarName"));
					}
				}
			}else if(nodeName.endsWith("directory")){
				directoryScanner(currentPath, child, viewName);
			}
		}
		return null;
	}
	
	public void setStartStage(Stage primaryStage, String stageName){
		this.stages.put(stageName, primaryStage);
	}
	
	public Class<?> loadController(String viewClassPath, URL location) throws ClassNotFoundException{
		if(loadedViewClasses.get(viewClassPath) != null){
			return loadedViewClasses.get(viewClassPath);
		}else{
			URL[] urls = { location };
			URLClassLoader cl = URLClassLoader.newInstance(urls);
			Class<?> controllerClass;
			controllerClass = cl.loadClass(viewClassPath);
			loadedViewClasses.put(viewClassPath, controllerClass);
			return controllerClass;
		}
	}
	
}
