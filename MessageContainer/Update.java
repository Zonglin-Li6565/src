package MessageContainer;

import java.util.Enumeration;
import java.util.Hashtable;

import javafx.stage.StageStyle;

/**
 * The massage the <code>Handler</code> sends to the <code>ViewController</code>
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
	
	/**
	 * The Actions
	 */
	public static final short NEWSTAGE = 5, 	REPLACESCENE = 6,
							  DISPLAY = 7,		CLOSE = 8,
							  NEWDIALOG = 9;
	/**
	 * The Domains
	 */
	public static final short ALLSAMENAME = 1,	ALL = 2,
							  MASTERVIEW = 3;

	/**The action the Update Contains
	 * <hr><b>Always provide</b>*/
	private short Action;
	/**The application range of this Update*/
	private short Domain;
	/**The hash code of the view that the handler gets the request from
	 * <hr><b>Always provide</b>*/
	private int hashCodeofView;
	/**The name YOU GIVE to the combination of controller class and fxml document
	 * Used to locate the view controller by mapper. Usually used with <code>hashCodeofView</code> 
	 * to locate the exact object of view controller.
	 * <hr><b>Provide if the {@code}DOMIAN of Update is ALLSAMENAME</b>*/
	private String viewName;
	/**The name YOU GIVE to the combination of controller class and fxml document that will be used to
	 * create a new stage.
	 * <hr><b>Provide if the <code>Action</code> is <code><i>NEWSTAGE, REPLACESCENE</i> and<i> NEWDIALOG</i></code></b>*/
	private String newViewName;
	private Hashtable<E, Object> resources;
	
	/**Below are the window settings fields, if NEWSTAGE is the Action.*/
	private boolean isResizable;
	private int XLocation;
	private int YLocation;
	/**The stage size will be the same as the scene size
	 *The stage style. See Javafx API for detail*/
	private StageStyle stageStyle;
	/** Title of the new stage*/
	private String title;
	/**The name of the parent stage, if the Action is NEWDIALOG*/
	private String parentStageName;
	/**decides whether the stage will be set to full screen*/
	private Boolean fullScreen;
	
	/**Get the hash code of the <code>ViewController</code> that the handler gets the request from
	 * @return the hashCodeofView
	 */
	public int getHashCodeofView() {
		return hashCodeofView;
	}

	/**Set the hash code of the <code>ViewController</code> that the handler gets the request from
	 * <hr><b>Always provide if the domain is default or <code>MASTERVIEW</code></b>
	 * @param hashCodeofView the hashCodeofView to set
	 */
	public void setHashCodeofView(int hashCodeofView) {
		this.hashCodeofView = hashCodeofView;
	}

	/**
	 * Set the new view name. Will be called by the mapper when the Action is 
	 * <code>REPLACESECNE</code> and <code>NEWDIALOG</code>.
	 * @return the newViewName
	 */
	public String getNewViewName() {
		return newViewName;
	}

	/**
	 * For you to set the new scene name be replaced to. Only the newViewName will be 
	 * used when the Action is <code>REPLACESECNE</code> and <code>NEWDIALOG</code>.
	 * @param newViewName the newViewName to set
	 */
	public void setNewViewName(String newViewName) {
		this.newViewName = newViewName;
	}

	/**
	 * Will be called by the <code>mapper</code> to decide the domain of the application of this <code>Update</code>
	 * @return the domain
	 */
	public short getDomain() {
		return Domain;
	}

	/**
	 * Set the domain of this <code>Update</code>
	 * @param domain the domain to set
	 */
	public void setDomain(short domain) {
		Domain = domain;
	}

	/**
	 * Constructor<br>
	 * The <code>Action</code> should be the constant provided in this class<br>
	 * The <code>viewName</code> should be the name of the original <code>ViewController</code> if the 
	 * the action is <code>REPLACESCENE</code> and <code>NEWDIALOG</code>. The name of the new ViewController 
	 * should not be provided in this field. Instead, it should be 
	 * provided by calling method: <code>setNewViewName(newViewName)</code>. 
	 * @param viewName
	 * @param Action
	 */
	public Update(String viewName, short Action){
		this.viewName = viewName;
		this.Action = Action;
		resources = new Hashtable<E, Object>();
		isResizable = true;
	}
	
	/**
	 * The resource for view to display.<br>
	 * It will be called by View
	 * @param e
	 * @return
	 */
	public Object getResource(E e){
		return resources.get(e);
	}
	
	/**
	 * To add resource for view to display<br>
	 * It will be called by Handler
	 * @param e
	 * @param resource
	 */
	public void addResource(E e, Object resource){
		resources.put(e, resource);
	}
	
	/**
	 * Return all the keys of the hash table of <code>resources</code>.
	 * <br>For View to call, getting the keys to traverse all elements 
	 * contained by the hash table
	 * @return
	 */
	public Enumeration<E> getAllKeys(){
		return resources.keys();
	}
	
	/**
	 * Return all the elements of the hash table of <code>resources</code>.
	 * <br>For View to call, in order to traverse all elements 
	 * contained by the hash table
	 * @return
	 */
	public Enumeration<Object> getAllResources(){
		return resources.elements();
	}

	/**
	 * Get the Action the Handler want to do<br>
	 * Will be called by the <code>Mapper</code> for mapping and managing windows
	 * @return
	 */
	public short getAction() {
		// TODO Auto-generated method stub
		return Action;
	}

	/**
	 * Set the action to be performed
	 * <br>Would be called by the Handler to set the action
	 * @param Action
	 */
	public void setAction(short Action) {
		// TODO Auto-generated method stub
		this.Action = Action;
	}

	/**
	 * Get the information in the <code>viewName</code> field
	 * @return
	 */
	public String getViewName() {
		// TODO Auto-generated method stub
		return viewName;
	}

	/**
	 * Set the information in the <code>viewName</code> field
	 * @param viewName
	 */
	public void setViewName(String viewName) {
		// TODO Auto-generated method stub
		this.viewName = viewName;
	}

	/**
	 * Check whether the stage is resizable.
	 * It will only be called by <code>Mapper</code> when a new stage is created.
	 * @return
	 */
	public boolean isResizable() {
		// TODO Auto-generated method stub
		return this.isResizable;
	}

	/**
	 * Set whether the stage is resizable.
	 * @param isResizable
	 */
	public void setResizable(boolean isResizable) {
		// TODO Auto-generated method stub
		this.isResizable = isResizable;
	}

	/**
	 * Get the x location of the new stage.
	 * Only be called when a new stage is created.
	 * @return
	 */
	public int getXLocation() {
		// TODO Auto-generated method stub
		return XLocation;
	}

	/**
	 * Set the x location of the new stage.
	 * @param XLocation
	 */
	public void setXLocation(int XLocation) {
		// TODO Auto-generated method stub
		this.XLocation = XLocation;
	}

	/**
	 * Get the y location of the new stage.
	 * Only be called when a new stage is created.
	 * @return
	 */
	public int getYLocation() {
		// TODO Auto-generated method stub
		return YLocation;
	}

	/**
	 * Set the y location of the new stage.
	 * @param YLocation
	 */
	public void setYLocation(int YLocation) {
		// TODO Auto-generated method stub
		this.YLocation = YLocation;
	}

	/**
	 * Get the stage style when a new stage is created.
	 * Will be called by <code>Mapper</code>
	 * @return
	 */
	public StageStyle getStageStyle() {
		// TODO Auto-generated method stub
		return stageStyle;
	}

	/**
	 * Set the stage style when a new stage is created(the action is <code>NEWDIALOG</code> 
	 * or <code>NEWSTAGE</code>).
	 * @param stageStyle
	 */
	public void setStageStyle(StageStyle stageStyle) {
		// TODO Auto-generated method stub
		this.stageStyle = stageStyle;
	}

	/**
	 * Will be called by the <code>Mapper</code> to set the title of the new stage.
	 * Only be called when a new stage is created.
	 * @return
	 */
	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	/**
	 * Set the value of the field of <code>title</code>. 
	 * @param title
	 */
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		this.title = title;
	}
	
	/**
	 * 
	 * @param parentStageName
	 */
	public void setParentStageName(String parentStageName){
		this.parentStageName = parentStageName;
	}
	
	public String getParentStageName(){
		return parentStageName;
	}

	/**
	 * Will be called by the <code>mapper</code> is the actions are 
	 * <code> NEWSTAGE, DISPLAY and REPLACESCENE</code>.
	 * @return
	 */
	public Boolean getFullScreen() {
		return fullScreen;
	}

	/**
	 * Setting whether the stage will be set to full screen.
	 * Can be used when actions are<code> NEWSTAGE, DISPLAY and REPLACESCENE</code>.
	 * Do not forget to provide the hash code of the<code> ViewController</code> if 
	 * the action are<code> DISPLAY and REPLACESCENE</code>.
	 * @param fullScreen
	 */
	public void setFullScreen(Boolean fullScreen) {
		this.fullScreen = fullScreen;
	}

}
