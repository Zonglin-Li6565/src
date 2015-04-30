package NotBeUsedNow;

import javafx.stage.StageStyle;

public interface Post {
	
	public static final int NEWSTAGE = 5, REPLACESCENE = 6, 
							DISPLAY = 7, CLOSE = 8;
	/**The ID of action must be those static final values
	 * @return the action
	 */
	public int getAction();
	
	/**
	 * The ID of action must be those static final values
	 * @param action the action to set
	 */
	public void setAction(int action);

	/**
	 * @return the fXfilename
	 */
	public String getFXfilename();

	/**
	 * @param fXfilename the fXfilename to set
	 */
	public void setFXfilename(String fXfilename);

	/**
	 * @return the viewName
	 */
	public String getViewName();

	/**
	 * @param viewName the viewName to set
	 */
	public void setViewName(String viewName);

	/**
	 * @return the resizable
	 */
	public boolean isResizable();

	/**
	 * @param resizable the resizable to set
	 */
	public void setResizable(boolean resizable);

	/**
	 * @return the xLocation
	 */
	public int getXLocation();

	/**
	 * @param xLocation the xLocation to set
	 */
	public void setXLocation(int xLocation);

	/**
	 * @return the yLocation
	 */
	public int getYLocation();

	/**
	 * @param yLocation the yLocation to set
	 */
	public void setYLocation(int yLocation);

	/**
	 * @return the stageStyle
	 */
	public StageStyle getStageStyle();

	/**
	 * @param stageStyle the stageStyle to set
	 */
	public void setStageStyle(StageStyle stageStyle);
	
	public String getTitle();
	
	public void setTitle(String title);
}