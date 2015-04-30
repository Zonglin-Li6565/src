package MessageContainer;

import java.util.Enumeration;
import java.util.Hashtable;

import javafx.stage.StageStyle;

/**
 * The information will be updated on the graphic user interface.
 * Support create new stage, just update the information on the one 
 * designated
 * <hr>
 * Implementing notes:<br>
 * The support for dialog window has not been implemented.
 * <br>
 * @author Zonglin Li
 *
 * @param <E> The type of the keys(i.e.String, Integer) 
 * for the resources hash table. 
 */
public class Update<E>{
	
	public static final short NEWSTAGE = 5, 	REPLACESCENE = 6,
							  DISPLAY = 7,		CLOSE = 8,
							  NEWDIALOG = 9;
	private short Action;
	private String viewName;
	private Hashtable<E, Object> resources;
	
	//Below are the window settings fields, if NEWSTAGE is the Action.
	private String FXMLfilename;
	private boolean isResizable;
	private int XLocation;
	private int YLocation;
	//The stage size will be the same as the scene size
	//The stage style. See Javafx API for detail
	private StageStyle stageStyle;
	//Title of the new stage
	private String title;
	//The name of the parent stage, if the Action is NEWDIALOG
	private String parentStageName;
	
	public Update(String viewName, short Action){
		this.viewName = viewName;
		this.Action = Action;
		resources = new Hashtable<E, Object>();
	}
	
	public Object getResource(E e){
		return resources.get(e);
	}
	
	public void addResource(E e, Object resource){
		resources.put(e, resource);
	}
	
	public Enumeration<E> getAllKeys(){
		return resources.keys();
	}
	
	public Enumeration<Object> getAllResources(){
		return resources.elements();
	}

	public short getAction() {
		// TODO Auto-generated method stub
		return Action;
	}

	public void setAction(short Action) {
		// TODO Auto-generated method stub
		this.Action = Action;
	}

	public String getFXMLfilename() {
		// TODO Auto-generated method stub
		return FXMLfilename;
	}

	public void setFXMLfilename(String FXMLfilename) {
		// TODO Auto-generated method stub
		this.FXMLfilename = FXMLfilename;
	}

	public String getViewName() {
		// TODO Auto-generated method stub
		return viewName;
	}

	public void setViewName(String viewName) {
		// TODO Auto-generated method stub
		this.viewName = viewName;
	}

	public boolean isResizable() {
		// TODO Auto-generated method stub
		return this.isResizable;
	}

	public void setResizable(boolean isResizable) {
		// TODO Auto-generated method stub
		this.isResizable = isResizable;
	}

	public int getXLocation() {
		// TODO Auto-generated method stub
		return XLocation;
	}

	public void setXLocation(int XLocation) {
		// TODO Auto-generated method stub
		this.XLocation = XLocation;
	}

	public int getYLocation() {
		// TODO Auto-generated method stub
		return YLocation;
	}

	public void setYLocation(int YLocation) {
		// TODO Auto-generated method stub
		this.YLocation = YLocation;
	}

	public StageStyle getStageStyle() {
		// TODO Auto-generated method stub
		return stageStyle;
	}

	public void setStageStyle(StageStyle stageStyle) {
		// TODO Auto-generated method stub
		this.stageStyle = stageStyle;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	public void setTitle(String title) {
		// TODO Auto-generated method stub
		this.title = title;
	}
	
	public void setParentStageName(String parentStageName){
		this.parentStageName = parentStageName;
	}
	
	public String getParentStageName(){
		return parentStageName;
	}

}
