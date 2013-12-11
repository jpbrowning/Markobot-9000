import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import edu.stanford.nlp.classify.Classifier;
import edu.stanford.nlp.io.IOUtils;

public class TweetMachine {
	public static final String MENTION = "<@m>";
	public static final String URL = "<@url>";
	public static final String GOOGLE = "<@goog>";
	public static final String SONY = "<@sne>";
	public static final String APPLE = "<@aapl>";
	public static final String MICROSOFT = "<@msft>";
	public static final String[] googTags = {"google", "goog" };
	public static final String[] sonyTags = {"sne", "sony", "playstation", "ps4", "ps3"};
	public static final String[] appTags = {"aapl", "apple", "iphone", "ipod", "osx" };
	public static final String[] microTags = {"msft", "microsoft", "xbox" };
	private static String infile;
	static Classifier<String, String> classifier = null;
	static BufferedReader tweetReader = null;
	static SentAnal SA = null;
	
	public static void main(String[] args){
		//usage
		if(args.length != 1){
			System.err.println("USAGE: java TweetMachine filename");
		}
		else{
			SentAnal SA = new SentAnal();
			//file locations for classifier and file we are going to read
			File classfile = new File("classifier.ser");
			infile = args[0];
			
			//load the classifier
			try{
				classifier = ((Classifier<String, String>)IOUtils.readObjectFromFile(classfile));
				processTweets();				
			} catch (IOException e) { e.printStackTrace();
			} catch (ClassNotFoundException e) { e.printStackTrace(); }
			
			
		}
		
	}

	private static void processTweets() throws FileNotFoundException, IOException {
		//initialize buffered reader
		
		File file = new File(infile);
		InputStream in = new GZIPInputStream(new FileInputStream(file));
		tweetReader = new BufferedReader(new InputStreamReader(in));
		while(true) {
			String s = getNextRawTweet();
			if(s == null)
				break;
			processTweets(s);
		}
		
	}

	private static void processTweets(String rawtweet) {
		//split tweet
		String[] parts = rawtweet.split("\t");
		if(parts.length > 4){
			String text = parts[0];
			String user = parts[1];
			String date = parts[2];
			String stock = null;
			boolean useable = true;
			
			if(!isEnglish(text))
				useable = false;
				

				Tweet newtweet = null;	

				//identify stock label
				if(useable){
					String count  = null;
					
					text = text.toLowerCase();
		
					text = containsStock(text);
					//System.out.println(text);
				
					if(text != null ){
						newtweet = new Tweet(text, 0, 0, null, date, stock);
						int tempIn = -1;
						
						
						for(int i = 0; i < newtweet.tokensMarkup.size(); i++){
							if(newtweet.tokensMarkup.get(i).token().equals(GOOGLE)){
								newtweet.stock = GOOGLE;
								tempIn = i;
								break;
							}
							if(newtweet.tokensMarkup.get(i).token().equals(APPLE)){
								newtweet.stock = APPLE;
								tempIn = i;
								break;
							}
							if(newtweet.tokensMarkup.get(i).token().equals(SONY)){
								newtweet.stock = SONY;
								tempIn = i;
								break;
							}
							if(newtweet.tokensMarkup.get(i).token().equals(MICROSOFT)){
								newtweet.stock = MICROSOFT;
								tempIn = i;
								break;
							}						
						}
						if(tempIn == -1)
							useable = false;
						newtweet.setStartOffset(tempIn);
						newtweet.setEndOffset(tempIn);


					}
				}
				
				//now classify the tweet
				if(useable && newtweet != null){
					newtweet.label = SentAnal.whatItIs(newtweet, classifier);
					System.out.println(cleanResult(newtweet));
				}
				
			
		}
		
	}

			  
	private static String cleanResult(Tweet newtweet) {
		String result = "";
		if(newtweet.stock.equals(GOOGLE))
			result += "\"GOOG\" ";
		else if(newtweet.stock.equals(APPLE))
			result += "\"AAPL\" ";
		else if(newtweet.stock.equals(SONY))
			result += "\"SNE\" ";
		else
			result += "\"MSFT\" ";
		
		if(newtweet.label.equals("positive"))
			result += "1 ";
		else if(newtweet.label.equals("negative"))			
			result += "-1 ";	
		else result += "0 ";
		
		String[] split = newtweet.date.split(" ");
		String[] date = split[0].replace("\"" ,"" ).split("-");
		result += "\"" + date[1] + "/" + date[2] + "/" + date[0] + "\" \"" + split[1].substring(0, 5) + "\"";
		return result;
		 
		
	}

	private static String containsStock(String text) {
		//google
		for(String s : googTags){
			if(text.contains(s)){
				text = text.replaceAll("\\b(" + s + ")\\b" , GOOGLE);
				
				return text;
			}
		}


		//apple
		for(String s : appTags){
			if(text.contains(s)){
				text = text.replaceAll("\\b(" + s + ")\\b", APPLE);
				return text;
			}
		}		

		//sony
		for(String s : sonyTags){
			if(text.contains(s)){
				text = text.replaceAll("\\b(" + s + ")\\b", SONY);
				return text;
			}
		}		

		//microsoft
		for(String s : microTags){
			if(text.contains(s)){
				text = text.replaceAll("\\b(" + s + ")\\b", MICROSOFT);
				return text;
			}
		}		

		return null;
	}

			public static boolean isEnglish(String words){
		  		int count = 0;
		  		if(words.toLowerCase().contains("the"))
		  			count++;
		  		if(words.toLowerCase().contains("to"))
		  			count++;
		  		if(words.toLowerCase().contains(" i "))
		  			count++;
		  		if(words.toLowerCase().contains(" a "))
		  			count++;
		  		if(words.toLowerCase().contains("and"))
		  			count++;
		  		if(words.toLowerCase().contains("you"))
		  			count++;
		  		if(words.toLowerCase().contains("of"))
		  			count++;
		  		if(words.toLowerCase().contains("for"))
		  			count++;
		  		if(words.toLowerCase().contains("on"))
		  			count++;
		  		if(words.toLowerCase().contains("my"))
		  			count++;
		  		if(words.toLowerCase().contains("with"))
		  			count++;
		  		if(words.toLowerCase().contains("have"))
		  			count++;
		  		if(words.toLowerCase().contains("are"))
		  			count++;
		  		if(words.toLowerCase().contains("now"))
		  			count++;
		  		if(words.toLowerCase().contains("from"))
		  			count++;
		  		if(count >= 5)
		  			return true;
		  		return false;
		  		
		  	}
		  
		  

	

	private static String getNextRawTweet() {
		if(tweetReader != null){
			try{
				String line = tweetReader.readLine();
				
				if(line != null)
					return line;
			} catch (IOException e){
				e.printStackTrace();}
			
			
		}
		return null;
	
	}
	
	
	
	

}
