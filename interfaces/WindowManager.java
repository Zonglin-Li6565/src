package interfaces;

import java.io.IOException;

/**
 * <hr>
 * Implementing notes:<br>
 * The support for dialog window has not been implemented yet.
 * <br>
 * @author Zonglin Li
 *
 */
public interface WindowManager {
	
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
	 * @param viewName
	 */
	abstract void shutdownStage(String viewName);
}
