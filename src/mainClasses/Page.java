package mainClasses;

import java.io.Serializable;

public class Page implements Serializable {
	
	 String [][] records;
	 String name;
	
	public Page(String name, int M)
	{
		records = new String[M+1][200];
		this.name = name;	
	}
	public  boolean isFull () {
		for (int i = 1; i < records.length ; i++)
		{
			for(int j = 0; j < records[i].length; j++) 
			{
				if (records [i][j] == null) 
					return false;
			}
		}
		return true;
	}
	public  boolean isEmpty () {
		for (int i = 1; i < records.length ; i++)
		{
			for(int j = 0; j < records[i].length; j++) 
			{
				if (records [i][j] != null) 
					return false;
			}
		}
		return true;
	}
	
	public  void display() {
		 
		
		for (int i = 0; i < records.length ; i++)
		{
			System.out.print("4");
			for(int j = 1; j < records[i].length; j++) 
			{
				
				
				if (records [i][j] != null) 
				{
					System.out.print(records[i][j]+" ");
					
				}
				
			}
			System.out.println();
		}
	}
	
	
	
	
	
	
}
