import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;


public class Boot {
	static ArrayList<String> googleWords = new ArrayList<String>();
	static ArrayList<String> mostCommon = new ArrayList<String>();
	public static void main(String[] args) throws FileNotFoundException, IOException {
		TreeMap<String, Integer> keyWords = new TreeMap<String,Integer>();
		int n = 10000;
		
		Scanner in = new Scanner(System.in);
		File file = new File("20131118.txt.gz");
        InputStream inz = new GZIPInputStream(new FileInputStream(file));
        BufferedReader tweetReader = new BufferedReader(new InputStreamReader(inz));
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("../google-10000-english.txt"));
			
			String line = br.readLine();

	        for(int i = 0; i < n; i++) {
	        	if(i < 50){
	        		mostCommon.add(line);
	        	}
	        	googleWords.add(line);
	            line = br.readLine();
	        }
			
			br.close();
	            
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String input = "";
		
		int count = 0;

		
		
		while(tweetReader != null && count < 500000){
			input = tweetReader.readLine();
			String[] inWords = input.split("((?<=;)|(?=;)|(?<=[.!?])|(?=[.!?])|(?<=,)|(?=,)|(?<=:)|(?=:)|(?<=--)|(?=--)|(?<=\")|(?=\")|(?<=\")|(?=\")| )");
			
			if(isEnglish(inWords) && AAPL(inWords)){
				for(String s : inWords){
					if(googleWords.contains(s) || isNumeric(s) || GMT(s) || punc(s)){
						//Do nothing
					}
					else{
						if(keyWords.keySet().contains(s)){
							keyWords.put(s, keyWords.get(s) + 1);
						}
						else{
							keyWords.put(s, 1);
						}
					}
				}
			}
			count++;
			
		}
		
		SortedSet<Map.Entry<String,Integer>> kw = entriesSortedByValues(keyWords);
		
		ArrayList<String> swords = new ArrayList<String>();
		for(Map.Entry<String,Integer> e : kw){
			swords.add(0, e.getKey());
		}
		
		for(String s : swords){
			System.out.println(s);
		}
		
		System.out.println("");
		
		
	}
	
	public static boolean isEnglish(String[] tweet){
		
		for(String s : tweet){
			if(mostCommon.contains(s)){
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean AAPL(String[] tweet){
		for(String s : tweet){
			if(s.contains("iphone")){
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean GMT(String s){
		if(s.contains("GMT")){
			return true;
		}
		
		return false;
	}
	
	public static boolean punc(String s){
		if(s.contains(":") || s.contains(".") || s.contains(",") || s.contains("!") || s.contains("?")
				|| s.contains("\"") || s.contains(";") || s.contains("-") || s.contains("RT")){
			return true;
		}
		
		return false;
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	static <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                return e1.getValue().compareTo(e2.getValue());
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}
}
