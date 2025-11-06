package ServerController;

import java.lang.Runnable;

public class Observer {
	private Runnable func;
	
	public void setFunc(Runnable newFunc) {
		func = newFunc;
	}
		
	public Runnable func() {
		return func;
	}
	
	public void runFunc() {
		func.run();
	}
}
