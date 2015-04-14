package mainClasses;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import exceptions.DBAppException;
import exceptions.DBEngineException;



public class DBApp {
	
	public void init()
	{
		
	}
	
	
	
	public static void createTable(String strTableName, Hashtable<String, String> htblColNameType, //remove static when not needed 
			Hashtable<String, String> htblColNameRefs, String strKeyColName) throws DBAppException
	{
		Table.createTable(strTableName, htblColNameType, htblColNameRefs, strKeyColName);
	}
	
	public static void createIndex(String strTableName, String strColName) throws DBAppException
	{
		//indexCreation.createIndex(strTableName, strColName);
	}
	
		
	public static void insertIntoTable(String strTableName, Hashtable<String, String> htblColNameValue) throws 
		DBAppException, IOException
	{
		Table.insertIntoTable(strTableName, htblColNameValue);
	}
	
	
	public static void deleteFromTable(String strTableName, Hashtable<String, String> htblColNameValue, 
			String strOperator) throws DBEngineException
	{
		//deletion.deleteFromTable(strTableName, htblColNameValue,  strOperator);
	}
	
	/*public Iterator selectValueFromTable(String strTable, Hashtable<String, String> htblColNameValue, 
			String strOperator) throws DBEngineException
	{
		
	}
	
	public Iterator selectRangeFromTable(String strTable, Hashtable<String, String> htblColNameRange, 
			String strOperator) throws DBEngineException
	{
		
	}*/
	
	public void saveAll() throws DBEngineException
	{
		
	}
	
	public static void main(String[]args) throws DBAppException, IOException , ClassNotFoundException
	{
		Hashtable h1 = new Hashtable();
		h1.put("ID", "java.lang.Integer");
		h1.put("Name", "java.lang.String");
		h1.put("Dept", "java.util.String");
		h1.put("Start_Date", "java.util.Date");
		h1.put("Country", "java.util.String");
		
		Hashtable h2 = new Hashtable();
		h2.put("Dept", "Department.ID");
		
		
		h2.put("Country", "Country.ID");
		createTable("Employee", h1, h2, "ID");
		//System.out.println(tableExists("Employe"));
		//System.out.println(colExists("Employee","I"));
		
		
		Hashtable h3 = new Hashtable();
		h3.put("id", "java.lang.Integer");
		h3.put("Vorname", "java.lang.String");
		//h3.put("Dept", "java.util.String");
		h3.put("Datum", "java.util.Date");
		//h3.put("Stadt", "java.util.String");
		//createTable("Kellner", h3, h2, "id");
		
		
		ArrayList cols = Table.ListofCols("Employee");
		for (int i = 0; i < cols.size(); i++) 
		{
		//	System.out.println(cols.get(i));
		}
		
		Hashtable h4 = new Hashtable();
		h4.put("ID", "1");
		h4.put("Name", "ahmed");
		h4.put("Dept", "cs");
		h4.put("Start_Date", "january");
		h4.put("Country", "egypt");
		
		insertIntoTable("Employee", h4);
		
	//	Page page = Table.retrieve();
	//	page.display();
		
	}
	
}
