package interfaces;

import MessageContainer.Request;
import MessageContainer.Update;

public interface Mapper{
	/**
	 * Load the Handler class from the location designated
	 * The location should contain the jar file that contains
	 * the Handler
	 * @param handlerLocation
	 * @throws Exception 
	 */
	abstract Class<?> loadHandler(String handlerLocation) throws Exception;
	
	/**
	 * Load the ViewController class from the location designated
	 * The location should contain the jar file that contains
	 * the ViewController
	 * @param viewLocation
	 */
	abstract ViewController loadView(String viewLocation);
	
	/**
	 * Map request from View to Handler
	 * Return 0 if finished
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	abstract int mapRequest(Request<?, ?> request, int hashCode) throws Exception;
	
	/**
	 * Map Updates from Handler to View
	 * Return 0 if finished
	 * @param update
	 * @return
	 * @throws Exception 
	 */
	abstract int mapUpdate(Update<?> update) throws Exception;
}
