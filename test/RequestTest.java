package test;

import interfaces.Request;

public class RequestTest implements Request {
	
	public String display = "hi~~ inside handler";
	
	@Override
	public String getHandlerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHandlerName(String handlerName) {
		// TODO Auto-generated method stub

	}

}

