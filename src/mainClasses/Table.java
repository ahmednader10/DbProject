package mainClasses;

import java.awt.List;
import java.io.Serializable;
import java.util.ArrayList;

public class Table implements Serializable{

	transient ArrayList<Page> pages; 
	ArrayList<String> refs;
	Page lastPage;
	int recordsNumber;
	
	public Table()
	{
		pages  = new ArrayList<Page>();
		refs = new ArrayList<String>(); 
	}
}
