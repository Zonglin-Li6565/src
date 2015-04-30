package interfaces;

import MessageContainer.Request;


public interface Handler {
	
	public abstract int handleRequest(Request<?, ?> request);
	
	public abstract void setMapper(MappingAble mapper);
}
