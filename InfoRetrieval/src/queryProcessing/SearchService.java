package queryProcessing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.terrier.terms.PorterStemmer;

import com.google.common.collect.Sets;

public class SearchService {

	public SortedSet<String> stopWordList;
	public static String[] pageTitleArray = new String[10873965];//10873965   177592
	public static Integer[] pageIDArray = new Integer[10873965];//177592
	public static Integer[] pageOutlinkCountArray = new Integer[10873965];//10873965    177592
	public static Integer[] pageInlinkCountArray = new Integer[10873965];//10873965    177592
	public static TreeSet<String> tsDictionary = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

	
	public SearchService() {
		try {
			long start = System.nanoTime();
			FileInputStream fileIn = new FileInputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\Dictionary_45G_YOGESH.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn); 
			tsDictionary = (TreeSet<String>)in.readObject(); 
			in.close(); 
			fileIn.close();
			System.out.println("Dictionary In Memory:"+tsDictionary.size());
			
			fileIn = new FileInputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\PageIDArray_45G_1.ser");
			in = new ObjectInputStream(fileIn); 
			pageIDArray = (Integer[])in.readObject(); 
			in.close(); 
			fileIn.close();
			System.out.println("PageID in Memory");

			fileIn = new FileInputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\TitleArray_15G.ser");
			in = new ObjectInputStream(fileIn); 
			pageTitleArray = (String[])in.readObject(); 
			in.close(); 
			fileIn.close();
			System.out.println("Page Titel in Memory");

			fileIn = new FileInputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\PageOutCount_45G_1.ser");
			in = new ObjectInputStream(fileIn); 
			pageOutlinkCountArray = (Integer[])in.readObject(); 
			in.close(); 
			fileIn.close();
			System.out.println("Page Out Count in Memory");
			
			fileIn = new FileInputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\PageInCount_45G_1.ser");
			in = new ObjectInputStream(fileIn); 
			pageInlinkCountArray = (Integer[])in.readObject(); 
			in.close(); 
			fileIn.close();
			System.out.println("Page In Count in Memory");
			
			
			FileReader file;
			file = new FileReader("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\StopWordList.txt");
			BufferedReader br = new BufferedReader(file);
			stopWordList = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			String str;
			while ((str = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(str);
				while(st.hasMoreElements()){
					stopWordList.add(st.nextToken());
				}
			}
			stopWordList.add("");
			br.close();
			file.close();
			System.out.println("Stop List in Memory");
			
			long time = System.nanoTime() - start;
			System.out.printf(" Time Taken to deserializing everything %.3f seconds%n", time/1e9);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SearchService(int nothing){
		try{
			FileReader file;
			file = new FileReader("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\StopWordList.txt");
			BufferedReader br = new BufferedReader(file);
			stopWordList = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			String str;
			while ((str = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(str);
				while(st.hasMoreElements()){
					stopWordList.add(st.nextToken());
				}
			}
			stopWordList.add("");
			br.close();
			file.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void startSearch() {
		
		try {
			SearchService searchService=new SearchService();
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String answer;
			do
			{
				System.out.println("Enter the search string:");
				String input= br.readLine();
				long start_time = System.currentTimeMillis();
				searchService.searchResult(input);
				long end_time = System.currentTimeMillis();
				System.out.println("Total time taken for search is "+(end_time-start_time)+" milliseconds");
				System.out.println();
				System.out.println("Do you want to continue (Y/N)?");
				System.out.println("You must type a 'Y' or an 'N'");
				answer = br.readLine();
			}
			while((answer.equalsIgnoreCase("Y")));


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param input
	 * Method to display search results for input query
	 */
	public List<String> searchResult(String input){
		List<String[]> postList = new ArrayList<>();
		List<String> searchResult=new ArrayList<>();

		try{
			
		
		List<String> tokenlist=new ArrayList<>();
		PorterStemmer porterStemmer=new PorterStemmer();
		String str="";
		String line;
		Pattern specialCharPattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
		Pattern consecutiveCharPattern = Pattern.compile(".([a-z])\\1{2,}.", Pattern.CASE_INSENSITIVE);
		boolean isContainSpecialChar = specialCharPattern.matcher(input).find();
		BufferedReader br;

		if(isContainSpecialChar){
			input = input.replaceAll("[^a-zA-Z0-9]+", " ").trim();
		}
		
		StringTokenizer stringTokenizer=new StringTokenizer(input);
		System.out.println("Query String:"+input);
		
		while(stringTokenizer.hasMoreElements()){
			
			str=(String) stringTokenizer.nextToken().toLowerCase();
			if(!stopWordList.contains(str) && str.length()>0){
				
				if(!consecutiveCharPattern.matcher(str).find()){
					
					if(StringUtils.isNumeric(str) && str.length()<5){
						tokenlist.add(str);
					}
					else if(StringUtils.isAlpha(str)){
						str = porterStemmer.stem(str);
						tokenlist.add(str);
					}	
				}
			}
		}
		
		for(String token: tokenlist){
			System.out.println("\nToken TO search:"+token);
			long index=0;
			Iterator<String> itr=tsDictionary.iterator();
			while(itr.hasNext()){
				if(token.compareTo(itr.next().toLowerCase())<0){
					break;
				}
				index++;
			}
			
			index = index/50000;
			
			br = new BufferedReader(new FileReader("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\postings\\"+index+".txt"));
			
			System.out.println("Fetching "+index+".txt for token:"+token);
			String title = "";
			while ((line = br.readLine()) != null) {
				try{   
					title = line.substring(0,line.indexOf("=")).toLowerCase();
					if(title.equalsIgnoreCase(token)){			
						System.out.println("Postings:\t"+line);
						String[] documentIds = line.substring(line.indexOf("=")+1,line.length()).split(",");
						postList.add(documentIds);	
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			br.close();
		}

		if(postList!=null && postList.size()>0){
			
			
			Set<String> result = Sets.newHashSet(postList.get(0));
		    for (String[] numbers : postList) {
		        result = Sets.intersection(result, Sets.newHashSet(numbers));
		    }
		    System.out.println("\n"+result+" search results found");
		    
		    
		    
		    
		    
		    //----
		    /*String[] rankedOrderedDoc = new String[1000];
	        int docIDNumber = 0;
	        
		    if(result.size()>0){
		    	Iterator<String> zoneRank = result.iterator();
		        int rankIndex=0;
		        String rankDocID ="";
		        while(zoneRank.hasNext()){
		        	
		        	rankDocID = zoneRank.next();
		        	if(rankDocID.length()>0){
		        		rankIndex = Arrays.binarySearch(pageIDArray, Integer.parseInt(rankDocID));
		        		String documentTitle=pageTitleArray[rankIndex];
		        		
		        		for(String token: tokenlist){
		        			if(documentTitle.toLowerCase().contains(token)){
		        				zoneRank.remove();
		        				rankedOrderedDoc[]
		        			}
		        		}
		        	}
		        }
		    }*/
		    
		    
		    
		    
		    //----
		    
		    if(result.size()>0){
		    	Iterator<String> iterator = result.iterator();
		        int index=1;
		        while(iterator.hasNext()){
		        	String documentId=iterator.next();
		        	
			        	System.out.println("--"+documentId+"--");
			        if(documentId.length()>0){
			        	try{
				        	int tokenIndex=Arrays.binarySearch(pageIDArray, Integer.parseInt(documentId));
				        	String documentTitle=pageTitleArray[tokenIndex];
				        	
				        	if(documentTitle!=null){
				        		String documentURL="http://en.wikipedia.org/wiki/"+documentTitle.replaceAll(" ", "_");
				        		searchResult.add(documentURL);
					        	System.out.println(index+": "+documentTitle+" Link: "+documentURL);
					        	
					        	System.out.println("Inlink for the above url :"+pageInlinkCountArray[tokenIndex]);
					        	System.out.println("Outlink for the above url :"+pageOutlinkCountArray[tokenIndex]);
					        	
					        	boolean isInTitle = false;
				        		for(String token: tokenlist){
				        			if(documentTitle.contains(token)){
				        				isInTitle = true;
				        			}
				        		}
				        		
				        		if(isInTitle){
				        			System.out.println("ZoneScoring for above url: 0.6");
				        		}else{
				        			System.out.println("ZoneScoring for above url: 0.4");
				        		}
				        	}
				        	else{
					        	System.out.println(index+": Document information is missing");
				        	}
				        	index++;
				        	System.out.println();
			        	}catch(Exception e){
			        		e.printStackTrace();
			        	}
			        }
		        }
		    }
		}
		else{
			System.out.println("Sorry !!! No result found");
		}
	}
		catch(Exception e){
			e.printStackTrace();
		}
		return searchResult;
	}
}
