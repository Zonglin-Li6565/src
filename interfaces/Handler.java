package interfaces;

import MessageContainer.Request;


public interface Handler extends Runnable{
	
	/**
	 * The Handler will be instantiated as a <code>Runnable</code>
	 * object. Since the class implements this interface will be instantiated 
	 * with no arguments, the <code>Request<?, ?></code> could only be 
	 * passed in with this method.
 	 * @param request
	 */
	public abstract void toHandle(Request<?, ?> request);
	
	/**
	 * The reason to include this method is the same as 
	 * method <code>toHandle</code>.
	 * @param mapper
	 */
	public abstract void setMapper(Mapper mapper);
	
	/**
	 * The percentage of work finished.
	 * Return an integer in the interval [0, 100].
	 * @return
	 */
	public abstract int getPorgress();
}
