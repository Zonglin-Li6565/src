package interfaces;

import MessageContainer.Request;

/**
 * <b>important:<br></b>
 * It is important to know that the instance of Handler will be destroyed whenever it 
 * has finished its task. So, DO NOT save long-term data in this type of object. 
 * Instead, you should save them in other you defined objects.
 * @author Zonglin Li
 *
 */
public interface Handler extends Runnable{
	
	/**
	 * Set the <code>Request</code> to the Handler to handle. This should be called prior 
	 * to the run() method invoked.
	 * @param request
	 */
	public abstract void setRequest(Request<?, ?> request);
	
	/**
	 * The percentage of work finished.
	 * Return an integer in the interval [0, 100].
	 * @return
	 */
	public abstract int getPorgress();
	
	/**
	 * Let the Handler know the Mapper
	 * @param mapper
	 */
	public abstract void setMapper(Mapper mapper);
	
}
