package exceptions;

@SuppressWarnings("serial")
public class DBEngineException extends Exception {

	public DBEngineException()
	{
		
	}
	
	public DBEngineException(String m)
	{
		super(m);
	}
	
}
