import java.io.File;
import java.util.HashSet;
import java.util.Scanner;


public class Mood {
	private HashSet<String> dict = new HashSet<String>();

	public Mood(String filename) {
		readInWords(filename);
	}

	
	//used in constructor, reads in OpinionLexicon from file
	private void readInWords(String filename) {
		try{
			File f = new File(filename);
			Scanner scan = new Scanner(f);
			String line = "";
			while(scan.hasNextLine()){
				line = scan.nextLine();
				dict.add(line);
			}

		} catch(Exception e){
			e.printStackTrace();
		}

	}
	
	//boolean whether this mood dictionary contains given String
	public boolean has(String t){
		if(dict.contains(t)) return true;
		return false;
	}
	
	//getter, returns hashset of this dictionary
	public HashSet<String> getDictionary(){
		return dict;
	}

}


