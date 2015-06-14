package interfaces;

import java.io.IOException;

import javafx.stage.StageStyle;

/**
 * <hr>
 * Implementing notes:<br>
 * The support for dialog window has not been implemented yet.
 * <br>
 * @author Zonglin Li
 *
 */
public interface WindowManager {
	
	abstract ViewController replaceSceneContent(String stageName, boolean resizeAble) throws Exception;
	
	abstract ViewController replaceSceneContent(String stageName, boolean resizeAble, String title) 
			throws Exception;

	abstract ViewController replaceSceneContent(String stageName, int x, int y, 
			boolean resizeAble, String title) throws Exception;
	
	abstract ViewController showNewStage(String viewName, String title, 
			boolean resizeAble, StageStyle stageStyle) throws IOException, Exception;
	
	abstract ViewController showNewStage(String viewName, String title, 
			boolean resizeAble, StageStyle stageStyle, int x,
			int y) throws IOException, Exception;

	abstract void shutdownStage(String viewName);
	
	abstract void changingSettingofStage(String stageName, 
			boolean resizAble, String title, int x, int y);
}
