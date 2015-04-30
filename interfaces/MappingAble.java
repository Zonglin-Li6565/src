package interfaces;

import MessageContainer.Box;
import MessageContainer.Get;
import MessageContainer.Request;
import MessageContainer.Update;

public interface MappingAble {
	/**
	 * Load the Handler class from the location designated
	 * The location should contain the jar file that contains
	 * the Handler
	 * @param handlerLocation
	 */
	abstract Handler loadHandler(String handlerLocation);
	
	/**
	 * Load the ViewController class from the location designated
	 * The location should contain the jar file that contains
	 * the ViewController
	 * @param viewLocation
	 */
	abstract ViewController loadView(String viewLocation);
	
	/**
	 * Load the Model class from the location designated
	 * The location should contain the jar file that contains
	 * the Model
	 * @param modelLocation
	 */
	abstract void loadModel(String modelLocation);
	
	/**
	 * Map request from View to Handler
	 * Return 0 if finished
	 * @param request
	 * @return
	 */
	abstract int mapRequest(Request<?, ?> request);
	
	/**
	 * Map Updates from Handler to View
	 * Return 0 if finished
	 * @param update
	 * @return
	 */
	abstract int mapUpdate(Update<?> update);
	
	/**
	 * Map Get from Handler to Model
	 * Return 0 if finished
	 * @param get
	 * @return
	 */
	abstract int mapGet(Get<?, ?> get);
	
	/**
	 * Map the resources container from Model to Handler
	 * Return 0 if finished
	 * @param box
	 * @return
	 */
	abstract int mapBox(Box<?> box);
}
