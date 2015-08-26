package searchEngine.postingList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;

public class GeneratePostingList {
	//HashMap<String, String> mapPosting= new HashMap<String, String>();
	public static TreeSet<String> tsDictionary = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	public String dictionaryToken[] = new String[100000];
	public String postings[] = new String[100000];
	
	public void constructPostingList(){
		try {
		
		
		
		long start = System.nanoTime();
//		Pattern pattern = Pattern.compile(".*([a-z])\\1{2,}.*", Pattern.CASE_INSENSITIVE);
		//   (?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])
		//Pattern pattern = Pattern.compile("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])", Pattern.DOTALL);
		
		FileInputStream fileIn = new FileInputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\Dictionary_45G_YOGESH.ser"); 
		ObjectInputStream in = new ObjectInputStream(fileIn); 
		tsDictionary = (TreeSet<String>)in.readObject(); 
		in.close(); fileIn.close(); 
		long time = System.nanoTime() - start; 
		System.out.printf(" Time Taken to desearialized tsDictionary %.3f seconds%n", time/1e9);
		int count = 0;
		System.out.println("Dictionary Size before:"+tsDictionary.size());
		Iterator<String> itr=tsDictionary.iterator();
		int temp_count = 0;
		String temp="";
		int arrayIndex = 0;
		while(itr.hasNext()){
			temp = itr.next();
			
			if(temp_count>=0 && temp_count<100000){
				//mapPosting.put(temp, "");
				dictionaryToken[arrayIndex] = temp.toLowerCase();
				arrayIndex++;
			}
			temp_count++;
			
			if(temp_count>=100000){
				break;
			}
			
		}
		//System.out.println("Posting List of "+mapPosting.size());
		System.out.println("Array Size :"+dictionaryToken.length);
		System.out.println("Last Word of Posting List :"+temp);
		Arrays.sort(dictionaryToken);
		Arrays.fill(postings, "");
		System.out.println("Sorting Done");
		
		/*try
	      {	
			start = System.nanoTime();
	         FileOutputStream fileOut =
	         new FileOutputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\Dictionary_45G_CamelCaseCheck.ser");
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
	      }*/
		start = System.nanoTime();
		BufferedReader br;
		br = new BufferedReader(new FileReader("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\Assig2_Stage1.txt"));
		String line;
		int index;
		int lineNumber =0;
		while ((line = br.readLine()) != null) {
		   try{
				String pageId=line.substring(line.indexOf("Title-")+6, line.indexOf("-Page-ID"));
				String[] arr= line.substring(line.indexOf("[")+1, line.indexOf("]")).split(", ");
				for(int i=0;i<arr.length;i++){
	
					index = Arrays.binarySearch(dictionaryToken,arr[i].toLowerCase());
					if(index>=0){
						postings[index] = postings[index].concat(","+pageId);
//						System.out.println(arr[i]+" "+postings[index]);
					}
					/*if(mapPosting.containsKey(arr[i])){
						mapPosting.put(arr[i], mapPosting.get(arr[i])+","+pageId);
					}*/
				}
		   }catch(Exception e){
			   e.printStackTrace();
		   }
		
		   lineNumber ++;
		   if(lineNumber%100000 ==0){
			   System.out.println("Line Number :"+lineNumber);
		   }
		}
		br.close();
		writePostingList();
		time = System.nanoTime() - start; 
		System.out.printf(" Time Taken to construct posting list tsDictionary %.3f seconds%n", time/1e9);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

	public void writePostingList(){
	
	try {
		    FileWriter writer;
			writer = new FileWriter("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\Posting_0_100K_WITHARRAY.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(writer, (int) (Math.pow(1024, 2)));
	        long start = System.nanoTime();
	      /*  Iterator<Entry<String, String>> iterator=mapPosting.entrySet().iterator(); 
	        while(iterator.hasNext()) {
	        	Map.Entry<String, String> entry= (Entry<String, String>) iterator.next();
	        	bufferedWriter.write(entry.getKey().trim()+"="+entry.getValue());
	        	bufferedWriter.newLine();
	        }*/
	        
	        for(int i=0; i<100000;i++){
	        	bufferedWriter.write(dictionaryToken[i]+"="+postings[i]);
	        	bufferedWriter.newLine();
	        }
	        
	        
	        bufferedWriter.flush();
	        bufferedWriter.close();
	        long time = System.nanoTime() - start; 
			System.out.printf(" Time Taken to desearialized tsDictionary %.3f seconds%n", time/1e9);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
