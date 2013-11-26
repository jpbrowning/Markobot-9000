import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//This is a tweet object, pulled from my research.
//This was not written for this course, I am using it as a utility
public class Tweet {
	public String tweet;
	public int startOffset;
	public int endOffset;
	public String label;//sentiment
	public String id1, id2;
	//not sure if this should be stored as string or Date()
	public String date;
	public String stock;
	
	public ArrayList<String> features;
	public List<String> tokensSplitWhitespace;
	public List<AnnotatedToken> tokensMarkup;

	public Tweet(String id1, String id2, String t, int s, int e, String label, String date, String stock){
		this.id1 = id1;
		this.id2 = id2;
		this.tweet = t;
		this.startOffset = s;
		this.endOffset = e;
		this.label = label;
		this.date = date;
		this.stock = stock;
		this.features = new ArrayList<String>();
		this.tokenize();
	}
	

public void tokenize()
{
	tweet = tweet.toLowerCase(Locale.ENGLISH);
	String[] array = tweet.split(" ");

	// Bad offsets
	if( startOffset >= array.length || endOffset >= array.length ) {
	  features = null;
	  System.out.println("ERROR: offsets out of bounds: " + startOffset + ":" + endOffset + "\t" + tweet);
	  return;
	}
	
// Save the original tokens, unaltered.
tokensSplitWhitespace = new ArrayList<String>(array.length);
for( String s : array ) tokensSplitWhitespace.add(s);

// Create annotated token objects, so we can save the punctuation.
tokensMarkup = annotatePunctuation(tokensSplitWhitespace);

	if(this.endOffset - this.startOffset >= 1) 
	{
		for(int i = this.startOffset; i<=this.endOffset;i++)
		{
		  if( array[i].equals(SentAnal.MENTION.toString()) || array[i].equals(SentAnal.URL.toString()) )
		    features.add(array[i]);
		  else {
		    String [] results = array[i].split("\\p{Punct}+");
		    for(String ss: results)
		      features.add(ss);
		  }
		}
	}
	else
	{
		for(String s: array)
		{
				if(s.equals(array[this.startOffset]))
				{
					features.add("XXXX");
				}
				else
					features.add(s);
		}
	}
	
	ArrayList<String> deleteWords = new ArrayList<String>();
	for(int i = 0;i<this.features.size();i++) //not concatnating
	{
		if(this.features.get(i).equals("not") && i < this.features.size() -1)
		{
			String word = this.features.get(i);
			features.add("not" + features.get(i+1));
			deleteWords.add(word);
		}
	}
	for(String s: deleteWords)
	{
		features.remove(s);
	}

}

/**
 * Given a list of token strings, create a list of token objects that carry
 * annotations for each of the original tokens.
 * @param tokens List of string tokens
 * @return A List of AnnotatedToken objects.
 */
private List<AnnotatedToken> annotatePunctuation(List<String> tokens) {
List<AnnotatedToken> anns = new ArrayList<AnnotatedToken>();
for( String token : tokens ) {
//  System.out.println("annotate " + token);
  AnnotatedToken ann = new AnnotatedToken(token);
  anns.add(ann);
  
  // If there is at least one letter or number...then split the punctuation off.
  if( token.matches(".*[a-zA-Z0-9].*") && !token.equals(SentAnal.MENTION.toString()) && !token.equals(SentAnal.URL.toString()) ) {
    String cleanpre = stripPrePunc(token);
    int start = token.length() - cleanpre.length();
    String prepunc = (start > -1 ? token.substring(0, start) : "");

    String cleanpost = stripPostPunc(token);
    int end = token.length() - (token.length() - cleanpost.length());
//    System.out.println("end=" + end + " cleanpost=" + cleanpost);
    String postpunc = (end > -1 ? token.substring(end) : "");

//    System.out.println("Keeping start=" + start + " end=" + end);
    ann.add("clean", token.substring(start, end));
//    ann.add("clean", token);
    
    if( prepunc.length() > 0 )
      ann.add("prepunc", prepunc);
    if( postpunc.length() > 0 )
      ann.add("postpunc", postpunc);
  }
  // The token is all punc, or a special reserved token like "<url>"
  else
    ann.add("clean", token);
  
}
return anns;
}

private String stripPrePunc(String token) {
if( token == null ) return null;
String cleanpre = token.replaceFirst("^\\p{Punct}+", "");
return cleanpre;
}

private String stripPostPunc(String token) {
if( token == null ) return null;
String cleanpost = token.replaceFirst("\\p{Punct}+$", "");
return cleanpost;
}

public String getTweet() {
	return tweet;
}

public void setTweet(String tweet) {
	this.tweet = tweet;
}

public int getStartOffset() {
	return startOffset;
}

public void setStartOffset(int startOffset) {
	this.startOffset = startOffset;
}

public int getEndOffset() {
	return endOffset;
}

public void setEndOffset(int endOffset) {
	this.endOffset = endOffset;
}



}

/*public void writeOut() {

	Calendar calendar = Calendar.getInstance();
	Date dt = new Date();
	calendar.setTime(dt);
	String filename = "../" + calendar.get(Calendar.DATE) + "-"
			+ (calendar.get(Calendar.MONTH) + 1) + "-"
			+ calendar.get(Calendar.YEAR) + "-" + "S" + "-" + symbol;

	FileWriter fw;
	try {
		fw = new FileWriter(filename, true);
		for (Price p : prices) {
			fw.write(p.toString() + "\n");

		}
		fw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}*/