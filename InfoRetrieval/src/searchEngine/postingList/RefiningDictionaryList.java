package searchEngine.postingList;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class RefiningDictionaryList {

	HashMap<String, String> mapPosting= new HashMap<String, String>();
	public static TreeSet<String> tsDictionary = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

	
	public void constructPostingList(){
		try {
			
			
			long start = System.nanoTime();
			
			Pattern pattern = Pattern.compile("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])", Pattern.DOTALL);
			
			FileInputStream fileIn = new FileInputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\Dictionary_45G_CamelCaseCheck.ser"); 
			ObjectInputStream in = new ObjectInputStream(fileIn); 
			tsDictionary = (TreeSet<String>)in.readObject(); 
			in.close(); fileIn.close(); 
			long time = System.nanoTime() - start; 
			System.out.printf(" Time Taken to desearialized tsDictionary %.3f seconds%n", time/1e9);
			int count = 0;
			System.out.println("Dictionary Size before:"+tsDictionary.size());
			Iterator<String> itr=tsDictionary.iterator();
			int temp_count = 0;
			while(itr.hasNext()){
				String temp = itr.next();
				
				if(temp.endsWith("nbsp") || temp.endsWith("ref") || temp.endsWith("titl") || temp.endsWith("styl") || temp.endsWith("typ")
						|| temp.endsWith("url") || temp.endsWith("refcit") || temp.endsWith("br") || temp.endsWith("refpt") || temp.endsWith("author") ){
					itr.remove();temp_count++;
				}
			}
			System.out.println("Removed word count:"+temp_count);
			System.out.println("Dictionary Size After:"+tsDictionary.size());
			
			
			try
		      {	
				start = System.nanoTime();
		         FileOutputStream fileOut =
		         new FileOutputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\Dictionary_45G_YOGESH.ser");
		         ObjectOutputStream out = new ObjectOutputStream(fileOut);
		         out.writeObject(tsDictionary);
		         out.close();
		         fileOut.close();
		         System.out.printf("Dictionary Serialized");
		         time = System.nanoTime() - start;
				 System.out.printf(" Time Taken to searized Dictionary %.3f seconds%n", time/1e9);
		      }catch(IOException i)
		      {
		          i.printStackTrace();
		      }
			
			
			
			time = System.nanoTime() - start; 
			System.out.printf(" Time Taken to Create Posting  %.3f seconds%n", time/1e9);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
