package mainClasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IO 
{
	
	public static void savePage(Page page,File file)
	{
			ObjectOutputStream out = null;
		
		try 
			{

			if (!file.exists()) {
			FileOutputStream fileOut = new FileOutputStream(file,true);
			
			out = new ObjectOutputStream(fileOut);
			out.writeObject(page);
			
			}
			}
			catch(Exception e) 
			{
				e.printStackTrace();
			}
	}
	
	
	public static Page readPage(File file)
	{
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream(file);
			ObjectInputStream in= new ObjectInputStream(fileIn);
			Page p= (Page)in.readObject();
			return p;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
	
	public static void main(String[]args)
	{
		Page p1 = new Page("omar" , 5);
		File f1 = new File("omar");
		
		p1.records[0][0] = "omar";
		p1.records[0][1] = "hadeel";
		p1.records[1][0] = "nader";
		p1.records[1][1] = "karim";
		
		savePage(p1, f1);
		System.out.println(p1.isFull());
	}
}
