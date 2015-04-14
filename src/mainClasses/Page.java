package mainClasses;

import java.io.Serializable;

public class Page implements Serializable {
	
	static String [][] records;
	static String name;
	
	public Page(String name, int M)
	{
		records = new String[M+1][200];
		this.name = name;	
	}
	public static boolean isFull () {
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
	public static boolean isEmpty () {
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
	
	public static void display() {
		for (int i = 1; i < records.length ; i++)
		{
			System.out.println();
			for(int j = 0; j < records[i].length; j++) 
			{
				if (records [i][j] != null) 
					System.out.print(records[i][j]+" ");
			}
		}
	}
	
	
	
	
}
