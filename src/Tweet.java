import java.util.ArrayList;
import java.util.List;


public class Tweet {
	public String tweet;
	public int startOffset;
	public int endOffset;
	public String label;
	public String id1, id2;
	//not sure if this should be stored as string or Date()
	public String date;
	
	public ArrayList<String> features;
	public List<String> tokensSplitWhitespace;

	public Tweet(String id1, String id2, String t, int s, int e, String label, String date){
		this.id1 = id1;
		this.id2 = id2;
		this.tweet = t;
		this.startOffset = s;
		this.endOffset = e;
		this.label = label;
		this.date = date;
		this.features = new ArrayList<String>();
		//this.tokenize()
	}
	
}
