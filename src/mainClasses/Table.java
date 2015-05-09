package mainClasses;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.acl.LastOwnerException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import exceptions.DBAppException;

public class Table implements Serializable{
	String name;
	static boolean validTableName = true;
	static String metadata = "metadata";
	static boolean metadataIsCreated = false;
	

	static transient ArrayList<Page> pages= new ArrayList<Page>(); 
	static int pageCount = 0;
	ArrayList<String> refs;
	 static Page lastPage = null;
	static int recordsNumber = 0;
	static int noOfCols;
	
	public Table()
	{
		pages  = new ArrayList<Page>();
		refs = new ArrayList<String>(); 
	}
	public ArrayList ListofCols(String strTableName) {
		BufferedReader fileReader = null;
		String COMMA_DELIMITER = ",";
		ArrayList cols = new ArrayList();
		try
		{
			fileReader = new BufferedReader(new FileReader(metadata));
		}
		catch(Exception e)
		{
			
			return null;
			
		}
		try
		{
			String line = "";
			while ((line = fileReader.readLine()) != null)
			{
				
				String[] tokens = line.split(COMMA_DELIMITER);
				if (tokens.length > 0)
				{
					if(tokens[0].equals(strTableName))
					{
						cols.add(tokens[1]);
					}
				}
			}
			return cols;
		} 
		catch(Exception e)
		{
			System.out.println("Error in CsvFileReader !!!");
			e.printStackTrace();
		}
		return null;
		
		
	}
	public boolean tableExists( String strTableName) {
		BufferedReader fileReader = null;
		String COMMA_DELIMITER = ",";
		
		try
		{
			fileReader = new BufferedReader(new FileReader(metadata));
		}
		catch(Exception e)
		{
			
			return false;
			
		}
		
		try
		{
			String line = "";
			while ((line = fileReader.readLine()) != null)
			{
				
				String[] tokens = line.split(COMMA_DELIMITER);
				if (tokens.length > 0)
				{
					if(tokens[0].equals(strTableName))
					{
						return true;
					}
				}
			}
		} 
		catch(Exception e)
		{
			System.out.println("Error in CsvFileReader !!!");
			e.printStackTrace();
		}
		return false;
	}
	public Table(String strTableName, Hashtable<String, String> htblColNameType, 
			Hashtable<String, String> htblColNameRefs, String strKeyColName) throws DBAppException
	{    name=strTableName;
		final String COMMA_DELIMITER = ",";
		final String NEW_LINE_SEPARATOR = "\n";
		final String FILE_HEADER = "TableName,ColumnName,ColumnType,Key,Indexed,References";
		
		createTableHelper(strTableName);
		//System.out.print(metadataIsCreated);
		Enumeration e1 = htblColNameType.keys();
		ArrayList<String> l1 = new ArrayList();
		ArrayList<String> l3 = new ArrayList();
	    while (e1.hasMoreElements()) {
	      String key = (String) e1.nextElement();
	      //System.out.println(key + " : " + htblColNameType.get(key));
	      l1.add(key);
	      l3.add(htblColNameType.get(key));
	    }
	    
	    noOfCols = l1.size();
	    	    
	    Enumeration e2 = htblColNameRefs.keys();
	    ArrayList<String> l2 = new ArrayList();
	    ArrayList<String> l4 = new ArrayList();
	    while (e2.hasMoreElements()) {
	      String key = (String) e2.nextElement();
	      //System.out.println(key + " : " + htblColNameRefs.get(key));
	      l2.add(key);
	      l4.add(htblColNameRefs.get(key));
	    }
	    
	    FileWriter fileWriter = null;
	    
	    if(validTableName)
	    {
	    	try
		    {
		    	
		    	fileWriter = new FileWriter(metadata, true);
		    	metadataIsCreated = true;
		    	
		    	
		    	while(!htblColNameType.isEmpty())
		    	{
		    		fileWriter.append(strTableName);
			    	fileWriter.append(COMMA_DELIMITER);
			    	
			    	String col = l1.get(0);
			    	if(!l1.isEmpty())
			    		fileWriter.append(l1.remove(0));
			    	
			    	fileWriter.append(COMMA_DELIMITER);
			    	
			    	if(!l3.isEmpty())
			    		fileWriter.append(l3.remove(0));
			    	
			    	fileWriter.append(COMMA_DELIMITER);
			    	
			    	if(strKeyColName == col)
			    		fileWriter.append("TRUE");
			    	else
			    		fileWriter.append("FALSE");
			    	
			    	fileWriter.append(COMMA_DELIMITER);
			    	fileWriter.append("FALSE");
			    	fileWriter.append(COMMA_DELIMITER);
			    	boolean found = false;
			    	int listSize = l2.size();
			    	for(int i = 0; i < listSize; i++)
			    	{
			    		if(l2.get(0).equals(col))
			    		{
			    			l2.remove(0);
			    			found = true;
			    			fileWriter.append(l4.remove(0));
			    			break;
			    		}
			    		else
			    		{
			    			String tmp = l2.remove(0);
			    			l2.add(tmp);
			    		}
			    	}
			    	if(!found)
			    	{
			    		fileWriter.append("null");
			    	}
			    	
			    	htblColNameType.remove(col);
			    	fileWriter.append(NEW_LINE_SEPARATOR);
			    	
		    	}
		    	
		    	
		    	
		    }
		    catch(Exception e)
		    {
		    	System.out.print("There is an error");
		    }
		    finally
		    {
		    	try
		    	{
		    		fileWriter.flush();
		    		fileWriter.close();
		    	}
		    	catch(Exception e)
		    	{
		    		System.out.println("Error while flushing/closing fileWriter !!!");
		    		e.printStackTrace();
		    	}
		    }
	    }
	    else
	    {
	    	System.out.println("Table name already exists");
	    }
	    
	    
	}
	
	public void createTableHelper(String strTableName)
	{
		validTableName = true;
		BufferedReader fileReader = null;
		String COMMA_DELIMITER = ",";
		
		try
		{
			fileReader = new BufferedReader(new FileReader(metadata));
		}
		catch(Exception e)
		{
			
			return;
			
		}
		
		try
		{
			String line = "";
			
			
		
			
			while ((line = fileReader.readLine()) != null)
			{
				
				String[] tokens = line.split(COMMA_DELIMITER);
				if (tokens.length > 0)
				{
					if(tokens[0].equals(strTableName))
					{
						
						validTableName = false;
					}
				}
			}
		} 
		catch(Exception e)
		{
			System.out.println("Error in CsvFileReader !!!");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				fileReader.close();
			}
			catch(IOException e)
			{
				System.out.println("Error while closing fileReader !!!");
				e.printStackTrace();
			}
		}
	}
	
	public void insertIntoTable(String strTableName, Hashtable<String, String> htblColNameValue) 
	throws DBAppException, IOException
	{
		
		
		if (!tableExists(strTableName)) 
		{
			System.out.println("Table doesn't exist");
			return;
		}
		else 
		{
			File file = null;
			if (pages.isEmpty() || lastPage.isFull()) 
			{
				pageCount++;
				Page x = new Page(strTableName + pageCount , noOfCols+1);
				file = new File(strTableName + pageCount + ".ser");
				lastPage = x;
				pages.add(lastPage);
			}
			else 
			{
				//still need to check for insertion for the second time

				file = new File(lastPage.name+".ser");
				lastPage = IO.readPage(file);
			}
			
			Enumeration e1 = htblColNameValue.keys();
			List<String> Colname = new ArrayList();
			List<String> Value = new ArrayList();
			
			if (lastPage.isEmpty())
			  {
				ArrayList cols = ListofCols(strTableName);
				int j = 1;
				for (int i = 0; i < cols.size(); i++) {
					lastPage.records[0][j] = (String)cols.get(i);
					j++;
				}
				 
			  }
					
		
							while (e1.hasMoreElements()) 
							{
								int i=1;
								String key = (String) e1.nextElement();
								  if (!colExists(strTableName, key))
								  {
									  System.out.println("Invalid column name" + key);
									  return;  
								  }
								  
											
											
											lastPage.records[i][++recordsNumber] = htblColNameValue.get(key); 
								
											i++;
				
							}
							
							
							
				
				
					IO.savePage(lastPage, file);
						      
				}
			
						
			
			
			
			
		}
	
	public  Page retrieve() 
	{
		File file = new File("Employee1.ser");
		lastPage = IO.readPage(file);
		return lastPage;
	}
	
	public boolean colExists(String strTableName , String Colname) {
		BufferedReader fileReader = null;
		String COMMA_DELIMITER = ",";
		
		try
		{
			fileReader = new BufferedReader(new FileReader(metadata));
		}
		catch(Exception e)
		{
			
			return false;
			
		}
		
		try
		{
			String line = "";
			while ((line = fileReader.readLine()) != null)
			{
				
				String[] tokens = line.split(COMMA_DELIMITER);
				if (tokens.length > 0)
				{
					if(tokens[0].equals(strTableName))
					{
						if (tokens[1].equals(Colname))
						return true;
					}
				}
			}
		} 
		catch(Exception e)
		{
			System.out.println("Error in CsvFileReader !!!");
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	public boolean deleteFromTable(String strTableName, Hashtable<String, String> htblColNameValue, 
			String strOperator) {
		if (!tableExists(strTableName)) {
			return false;
		}
		else {
			boolean deleted = false;
			Enumeration cols;
			String col;
			cols = htblColNameValue.keys();
			while(cols.hasMoreElements()) {
				col = (String) cols.nextElement();
				if (!colExists(strTableName, col)) {
					return false;
				}
			}
			if (strOperator.equalsIgnoreCase("OR")) {
				Enumeration or;
				or = htblColNameValue.keys();
				for (Page currentPage: pages) {
					while (or.hasMoreElements()) {
						col = (String) or.nextElement();
						int i = 0;
						for (i = 1; i <= currentPage.records.length; i++) {
							if (col.equals(currentPage.records[i][0])) {
								break;
							}
						}
						for (int j = 0; j <= currentPage.records[i].length; j++) {
							if (htblColNameValue.get(col).equals(currentPage.records[i][j])) {
								currentPage.records[0][j] = "-1";
								deleted = true;
							}
						}
					}
				}
				return deleted;
			}
			else if (strOperator.equalsIgnoreCase("AND")) {
				int [] tmp = null;
				int fieldCount = htblColNameValue.size();
				Enumeration and;
				and = htblColNameValue.keys();
				for (Page currentPage: pages) {
					tmp = new int [currentPage.records[0].length];
					while (and.hasMoreElements()) {
						col = (String) and.nextElement();
						int i = 0;
						for (i = 1; i <= currentPage.records.length; i++) {
							if (col.equals(currentPage.records[i][0])) {
								break;
							}
						}
						for (int j = 0; j <= currentPage.records[i].length; j++) {
							if (htblColNameValue.get(col).equals(currentPage.records[i][j])) {
								tmp[j]++;
							}
						}
					}
					for (int i = 0; i < tmp.length; i++) {
						if (tmp[i] == fieldCount) {
							currentPage.records[0][i] = "-1";
							deleted = true;
						}
					}
				}
				return deleted;
			}
			else {
				Enumeration singleRecord;
				singleRecord = htblColNameValue.keys();
				for (Page currentPage: pages) {
					while (singleRecord.hasMoreElements()) {
						col = (String) singleRecord.nextElement();
						int i = 0;
						for (i = 1; i <= currentPage.records.length; i++) {
							if (col.equals(currentPage.records[i][0])) {
								break;
							}
						}
						for (int j = 0; j <= currentPage.records[i].length; j++) {
							if (htblColNameValue.get(col).equals(currentPage.records[i][j])) {
								currentPage.records[0][j] = "-1";
								deleted = true;
							}
						}
					}
				}
				return deleted;
			}
		}
	}
	
	public Iterator selectValueFromTable(String strTableName, Hashtable<String, String> htblColNameValue, 
			String strOperator) {
		if (!tableExists(strTableName)) {
			return null;
		}
		else {
			ArrayList<Record> records = new ArrayList<Record>();
			Record currentRecord = null;
			Enumeration cols;
			String col;
			cols = htblColNameValue.keys();
			while(cols.hasMoreElements()) {
				col = (String) cols.nextElement();
				if (!colExists(strTableName, col)) {
					return null;
				}
			}
			currentRecord.record = new String[pages.get(0).records.length];
			if (strOperator.equalsIgnoreCase("OR")) {
				Enumeration or;
				or = htblColNameValue.keys();
				for (Page currentPage: pages) {
					while (or.hasMoreElements()) {
						col = (String) or.nextElement();
						int i = 0;
						for (i = 1; i <= currentPage.records.length; i++) {
							if (col.equals(currentPage.records[i][0])) {
								break;
							}
						}
						for (int j = 0; j <= currentPage.records[i].length; j++) {
							if (htblColNameValue.get(col).equals(currentPage.records[i][j])) {
								for(int z = 0; z < currentPage.records.length; z++) {
									currentRecord.record[z] = currentPage.records[z][j];
								}
								records.add(currentRecord);
							}
						}
					}
				}
				return (Iterator) records;
			}
			else if (strOperator.equalsIgnoreCase("AND")) {
				int [] tmp = null;
				int fieldCount = htblColNameValue.size();
				Enumeration and;
				and = htblColNameValue.keys();
				for (Page currentPage: pages) {
					tmp = new int [currentPage.records[0].length];
					while (and.hasMoreElements()) {
						col = (String) and.nextElement();
						int i = 0;
						for (i = 1; i <= currentPage.records.length; i++) {
							if (col.equals(currentPage.records[i][0])) {
								break;
							}
						}
						for (int j = 0; j <= currentPage.records[i].length; j++) {
							if (htblColNameValue.get(col).equals(currentPage.records[i][j])) {
								tmp[j]++;
							}
						}
					}
					for (int i = 0; i < tmp.length; i++) {
						if (tmp[i] == fieldCount) {
							for(int z = 0; z < currentPage.records.length; z++) {
								currentRecord.record[z] = currentPage.records[z][i];
							}
							records.add(currentRecord);
						}
					}
				}
				return (Iterator) records;
			}
			else {
				Enumeration single;
				single = htblColNameValue.keys();
				for (Page currentPage: pages) {
					while (single.hasMoreElements()) {
						col = (String) single.nextElement();
						int i = 0;
						for (i = 1; i <= currentPage.records.length; i++) {
							if (col.equals(currentPage.records[i][0])) {
								break;
							}
						}
						for (int j = 0; j <= currentPage.records[i].length; j++) {
							if (htblColNameValue.get(col).equals(currentPage.records[i][j])) {
								for(int z = 0; z < currentPage.records.length; z++) {
									currentRecord.record[z] = currentPage.records[z][j];
								}
								records.add(currentRecord);
							}
						}
					}
				}
				return (Iterator) records;
			}
		}
	}
	
	public Iterator selectRangeFromTable(String strTableName, Hashtable<String, String> htblColNameValue, 
			String strOperator) {
		if (!tableExists(strTableName)) {
			return null;
		}
		else {
			ArrayList<Record> records = new ArrayList<Record>();
			Record currentRecord = null;
			Enumeration cols;
			String col;
			String col1;
			String col2;
			cols = htblColNameValue.keys();
			while(cols.hasMoreElements()) {
				col = (String) cols.nextElement();
				if (!colExists(strTableName, col)) {
					return null;
				}
			}
			currentRecord.record = new String[pages.get(0).records.length];
			if (strOperator.equalsIgnoreCase("OR")) {
				Enumeration or;
				or = htblColNameValue.keys();
				for (Page currentPage: pages) {
					while (or.hasMoreElements()) {
						col1 = (String) or.nextElement();
						col2 = (String) or.nextElement();
						int i = 0;
						for (i = 1; i <= currentPage.records.length; i++) {
							if (col1.equals(currentPage.records[i][0])) {
								break;
							}
						}
						for (int j = 0; j <= currentPage.records[i].length; j++) {
							int current = Integer.parseInt(currentPage.records[i][j]);
							int min = Integer.parseInt(htblColNameValue.get(col1));
							int max = Integer.parseInt(htblColNameValue.get(col2));
							if (current >= min && current <= max) {
								for(int z = 0; z < currentPage.records.length; z++) {
									currentRecord.record[z] = currentPage.records[z][j];
								}
								records.add(currentRecord);
							}
						}
					}
				}
				return (Iterator) records;
			}
			else if (strOperator.equalsIgnoreCase("AND")) {
				int [] tmp = null;
				int fieldCount = htblColNameValue.size()/2;
				Enumeration and;
				and = htblColNameValue.keys();
				for (Page currentPage: pages) {
					tmp = new int [currentPage.records[0].length];
					while (and.hasMoreElements()) {
						col1 = (String) and.nextElement();
						col2 = (String) and.nextElement();
						int i = 0;
						for (i = 1; i <= currentPage.records.length; i++) {
							if (col1.equals(currentPage.records[i][0])) {
								break;
							}
						}
						
						for (int j = 0; j <= currentPage.records[i].length; j++) {
							int current = Integer.parseInt(currentPage.records[i][j]);
							int min = Integer.parseInt(htblColNameValue.get(col1));
							int max = Integer.parseInt(htblColNameValue.get(col2));
							if (current >= min && current <= max) {
								tmp[j]++;
							}
						}
					}
					for (int i = 0; i < tmp.length; i++) {
						if (tmp[i] == fieldCount) {
							for(int z = 0; z < currentPage.records.length; z++) {
								currentRecord.record[z] = currentPage.records[z][i];
							}
							records.add(currentRecord);
						}
					}
				}
				return (Iterator) records;
			}
			else {
				Enumeration oneRecordOnly;
				oneRecordOnly = htblColNameValue.keys();
				for (Page currentPage: pages) {
					while (oneRecordOnly.hasMoreElements()) {
						col1 = (String) oneRecordOnly.nextElement();
						col2 = (String) oneRecordOnly.nextElement();
						int i = 0;
						for (i = 1; i <= currentPage.records.length; i++) {
							if (col1.equals(currentPage.records[i][0])) {
								break;
							}
						}
						for (int j = 0; j <= currentPage.records[i].length; j++) {
							int current = Integer.parseInt(currentPage.records[i][j]);
							int min = Integer.parseInt(htblColNameValue.get(col1));
							int max = Integer.parseInt(htblColNameValue.get(col2));
							if (current >= min && current <= max) {
								for(int z = 0; z < currentPage.records.length; z++) {
									currentRecord.record[z] = currentPage.records[z][j];
								}
								records.add(currentRecord);
							}
						}
					}
				}
				return (Iterator) records;
			}
		}
	}

}
