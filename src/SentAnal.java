

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


import edu.stanford.nlp.classify.ClassifiedDatum;
import edu.stanford.nlp.classify.Classifier;
import edu.stanford.nlp.classify.ClassifierTester;
import edu.stanford.nlp.classify.LinearClassifierFactory;
import edu.stanford.nlp.classify.RVFDataset;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.RVFDatum;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.stats.Counters;
import edu.stanford.nlp.util.ErasureUtils;

public class SentAnal {
	public static final String MENTION = "<@m>";
	public static final String URL = "@url>";
	public static boolean saveClassifier = true;
	public boolean debug = false;
	public static double cutoff = 0.3;//probability cutoff for testing
	
	//classifier and RVF dataset to be used to analyze sentiment
	static Classifier<String, String> classifier; 
	static RVFDataset<String, String> classifiedData;
	
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
	Mood posWords = new Mood("/courses/nchamber/nlp/lab4/data/lexicon/positive-words.txt");
	Mood negWords = new Mood("/courses/nchamber/nlp/lab4/data/lexicon/negative-words.txt");
	
	//main method, will be used to train and save a classifier
	public static void main(String[] args){
		if(args.length < 2){
			System.out.println("USAGE: java SentAnal trainFile testFile");
			System.exit(1);
		}
		String trainFile = args[0];
		String testFile = args[1];
		
		//load training data, train it
		ArrayList<Tweet> trainingData = loadTweets(trainFile, true, true);
		train(trainingData);
		
		//test all results, to test accuracy of training data
		ArrayList<Tweet> testData = loadTweets(testFile, true, true);
		ArrayList<ClassifiedDatum<String, String>> results = test(testData);
		
		//accuracy check, used to debug
		ClassifiedDatum<String, String>[] arr = ErasureUtils.mkTArray(ClassifiedDatum.class, results.size());
		arr = (ClassifiedDatum<String, String>[])results.toArray(arr);
		double accuracy = ClassifierTester.accuracy(arr);
		System.out.println("Accuracy: " + accuracy);
		

		
		//saveclassifier
		if(saveClassifier){
			try {
				IOUtils.writeObjectToFile(classifier, "classifier.ser");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		writeToFile("testset.labels", testData, results);


		
	}

	private static void writeToFile(String string, ArrayList<Tweet> testData,
			ArrayList<ClassifiedDatum<String, String>> results) {
		
	}
	
	//main testing function to test sentiment
	private static ArrayList<ClassifiedDatum<String, String>> test(
			ArrayList<Tweet> testData) {
		ArrayList<ClassifiedDatum<String, String>> datums = new ArrayList<ClassifiedDatum<String, String>>();
		//loop through all input tweets
		for(Tweet t : testData)
			datums.add(classifyTweet(t, classifier));
		
		return datums;
	}

	
	//Method to classify individual tweet
	//structure taken from my research project
	private static ClassifiedDatum<String, String> classifyTweet(Tweet t,
			Classifier<String, String> classifier) {
		//counter to count  features
		Counter<String> words = new ClassicCounter<String>();
		createFeatures(t, words);
		Classifier<String, String> thisClassifier = classifier;
		RVFDatum<String, String> rvf = new RVFDatum<String, String>(words, t.label);
		ClassifiedDatum<String, String> classified = testWithCutoff(thisClassifier, rvf);
		return classified;
	}

	private static ClassifiedDatum<String, String> testWithCutoff(
			Classifier<String, String> thisClassifier,
			RVFDatum<String, String> rvf) {
		Counter<String> vals = thisClassifier.scoresOf(rvf);
		Counters.logNormalizeInPlace(vals);
		for(String l : vals.keySet())
			vals.setCount(l, Math.exp(vals.getCount(l)));
		String label = Counters.argmax(vals);
		double prob = vals.getCount(label);
		if(prob > cutoff)
			return new ClassifiedDatum<String, String>(rvf, label);
		else
			return new ClassifiedDatum<String, String>(rvf, "positive");
			
	}

	private static void train(ArrayList<Tweet> tweets) {
		//stores classified data, altered by insertAndCreateFeatures
		classifiedData = new RVFDataset<String, String>();
		for(Tweet t : tweets)
			insertAndCreateFeatures(t);
		
		//use linearclassifierfactory to create classifier from RVFDataset
		LinearClassifierFactory<String, String> linearFactory = new LinearClassifierFactory<String, String>();
		classifier = linearFactory.trainClassifier(classifiedData);
	}

	//for each tweet, creates features and adds them to RVFDataset
	private static void insertAndCreateFeatures(Tweet t) {
		Counter<String> words = new ClassicCounter<String>();
		//create features and add to "words"
		createFeatures(t, words);
		//add data to RVF datum
		classifiedData.add(new RVFDatum<String, String>(words, t.label));
	}

	private static void createFeatures(Tweet t, Counter<String> words) {
		int start = t.startOffset;
		int end = t.endOffset;
		
		//unigrams
		for(int x = start; x < end; x++){
			String clean = t.tokensMarkup.get(x).get("clean");
			words.incrementCount(clean);
		}
		
		//bigrams
		for(int xx = start; xx < end; xx++){
			String pre = "<s>";
			if( xx > 0) pre = t.tokensMarkup.get(xx - 1).get("clean");
			String clean = t.tokensMarkup.get(xx).get("clean");
			words.incrementCount(pre + clean);
		}
		
		//trigrams
		for(int xx = start; xx < end; xx++){
			String pre1 = "<s>";
			String pre2 = "<s>";
			if( xx > 0) pre1 = t.tokensMarkup.get(xx - 1).get("clean");
			if( xx > 1) pre2 = t.tokensMarkup.get(xx - 2).get("clean");
			String clean = t.tokensMarkup.get(xx).get("clean");
			words.incrementCount(pre2 + " " + pre1 + " " + clean);
		}
		
		//XX patterns
		String pre = "<s>";
		String post = "</s>";
		if( t.startOffset > 0 ) pre = t.tokensMarkup.get(t.startOffset-1).get("clean");
		if( t.endOffset < t.tokensMarkup.size()-1 ) post = t.tokensMarkup.get(t.endOffset+1).get("clean");
		words.incrementCount(pre + " XX " + post);
		words.incrementCount(pre + " XX");
		words.incrementCount("XX " + post);
		
		if(containsSmileyFace(t.tokensMarkup, start, end))
			words.incrementCount("SMILEYFACE");
		if(containsSadFace(t.tokensMarkup, start, end))
			words.incrementCount("SADFACE");
		if(containsExclamationMark(t.tokensMarkup, start, end))
			words.incrementCount("EXCLAMATION");


		
		
	}

	private static boolean containsExclamationMark(
			java.util.List<AnnotatedToken> tokens, int i, int j) {
		for( int xx = i; xx < j; xx++ ) {
		    String prepunc = tokens.get(xx).get("prepunc");
		    String postpunc = tokens.get(xx).get("postpunc");
		    if( (prepunc != null && prepunc.matches("^\\!+$")) ||
		        (postpunc != null && postpunc.matches("^\\!+$")) )
		      return true;
		  }
		  return false;

	}

	private static boolean containsSadFace(
			java.util.List<AnnotatedToken> tokens, int i, int j) {
		for( int xx = i; xx < j; xx++ ) {
		       String prepunc = tokens.get(xx).get("prepunc");
		       String postpunc = tokens.get(xx).get("postpunc");
		       if( (prepunc != null && prepunc.matches(":-?[\\(\\[]$")) ||
		           (postpunc != null && postpunc.matches(":-?[\\(\\[]$")) ||
		           tokens.get(xx).token().matches(":-?[\\(\\(]$"))
		         return true;
		     }
		     return false;

	}

	private static boolean containsSmileyFace(
			java.util.List<AnnotatedToken> tokens, int i, int j) {
		for( int xx = i; xx < j; xx++ ) {
		      String prepunc = tokens.get(xx).get("prepunc");
		      String postpunc = tokens.get(xx).get("postpunc");
		      if( (prepunc != null && prepunc.matches(":-?[\\)\\]p]$")) ||
		          (postpunc != null && postpunc.matches(":-?[\\)\\]p]$")) ||
		          tokens.get(xx).token().matches(":-?[\\)\\]p]$"))
		        return true;
		    }
		    return false;

	}


	private static ArrayList<Tweet> loadTweets(String filename, boolean forceLoadAll,
			boolean haslabel) {
		//List of tweets to be returned
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		
		File file = new File(filename);
		try{
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()){
				String in = scan.nextLine();
				String cleanIn = cleanLine(in);
				String[] parts = cleanIn.split("\t");
				String id1 = parts[0];
				String id2 = parts[1];		
				String start = parts[2];
				String end = parts[3];
				String text = parts[5];
				String label = parts[2];
				Tweet newTweet = null;
				if(!text.equals("Not Available") || forceLoadAll){
					if(forceLoadAll || label.equals("positive") ||label.equals("negative") || label.equals("neutral") || label.equals("objective")){
						newTweet = new Tweet(id1, id2, text, Integer.parseInt(start), Integer.parseInt(end), label, null, null);
					}
					//window size for tweets
					if(newTweet.features != null)//if(newTweet.endOffset - newTweet.startOffset < 4)
						tweets.add(newTweet);
				}
			}
		} 
		catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		return tweets;

		
	}
	
	//taken from research. Used to clean a line's tags, and mentions
	private static String cleanLine(String line) {
		line = line.replaceAll("@[^\\s]+", MENTION);
		// Normalize URLs.
		line = line.replaceAll("http:[^\\s]+", URL);
		line = line.replaceAll("[^\\s]+\\.(com|org|edu)[^\\s]+", URL);
		return line;
	}
	
	
	

}
