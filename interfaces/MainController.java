package interfaces;

import java.io.IOException;

import javafx.scene.Node;

public interface MainController {

	/**
	 * 
	 * @param handlerLocation
	 */
	abstract void loadHandler(String handlerLocation);
	
	/**
	 * 
	 * @param viewLocation
	 */
	abstract void loadView(String viewLocation);
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	abstract int mapRequest(Request request);
	
	/**
	 * 
	 * @param post
	 * @return
	 */
	abstract int mapPost(Post post);
	
	/**
	 * Same center as the previous scene
	 * @param FXlocation
	 * @return
	 * @throws Exception 
	 */
	abstract ViewController replaceSceneContent(String FXlocation, boolean resizeAble) throws Exception;
	
	/**
	 * At the indicated location
	 * @param FXlocation
	 * @param x
	 * @param y
	 * @param width
	 * @param hight
	 * @return
	 * @throws Exception 
	 */
	abstract ViewController replaceSceneContent(String FXlocation, int x, int y, 
			boolean resizeAble) throws Exception;
	
	/**
	 * Default window location. Default fxml file location.
	 * @param title
	 * @return
	 */
	abstract ViewController showStartWindow(String title, boolean resizeable);
	
	/**
	 * Default window location. Specified fxml file location.
	 * @param location
	 * @param title
	 * @return
	 */
	abstract ViewController showStartWindow(String location, String title, 
			boolean resizeable);
	
	/**
	 * 
	 * @param FXlocation
	 * @return
	 */
	abstract ViewController showNewStage(String FXlocation, String title, 
			boolean resizeAble) throws IOException;
	
	/**
	 * 
	 * @param dialogName
	 */
	abstract void shutdownStage(String viewName);
}
