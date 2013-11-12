import java.util.ArrayList;
import java.util.Arrays;

import edu.stanford.nlp.classify.*;

public class SentAnal {
	public static final String MENTION = "<@m>";
	public static final String URL = "@url>";
	
	
	//classifier and RVF dataset to be used to analyze sentiment
	Classifier<String, String> classifier;
	RVFDataset<String, String> classifiedData;
	
	//popular positive and negative emoticons
	static final ArrayList<String> POS_EMOTICONS = new ArrayList<String>(
			Arrays.asList(":)", "=)", ":-)", ":]", "=]", ":-]", " :} ",
					" :o) ", " :-D ", " :D ", " =D ", " C: ", " =P ", " :-P ",
					" :P "));
	static final ArrayList<String> NEG_EMOTICONS = new ArrayList<String>(
			Arrays.asList(":(", "=(", ":-(", ":[", "=[", ":-[", " :{ ",
					" :-c ", " :c ", " D: ", " :S ", " :'( ", " :_( ", " :/ ",
					" =/ ", " D= ", " :-/ "));
	
	
	//use lexicons from lab4 to build mood dictionaries
	//Mood posWords = new Mood("/courses/nchamber/nlp/lab4/data/lexicon/positive-words.txt");
	//Mood negWords = new Mood("/courses/nchamber/nlp/lab4/data/lexicon/negative-words.txt");
	
	//main method
	public static void main(String[] args){
		
		
	}
	
	
	

}
