import java.util.ArrayList;
import java.util.List;

//Stores what happens for a stock for each of the three sentiments
public class StockTrend {
	//stock symbol: GOOG, MSFT, etc
	private String SYM;
	//holds each sentiment's price reaction as values of "UP|DOWN|NEUT"
	private String POS;
	private String NEG;
	private String NEUT;
	//counters for this stock
	//when price goes up with sentiment, count goes up
	//when price goes down with sentiment, count goes down
	//when price stays neutral, counts are not changed
	private int cpos;
	private int cneg;
	private int cneut;
	
	//xchange should be of value "UP|DOWN|NEUT"
	public StockTrend(String sym, String poschange, String negchange, String neutchange){
		this.SYM = sym;
		this.POS = poschange;
		this.NEG = negchange;
		this.NEUT = neutchange;
		this.cpos = 0;
		this.cneg = 0;
		this.cneut = 0;
	}
	
	//empty constructor
	public StockTrend(String sym){
		this.SYM = sym;
		this.cpos = 0;
		this.cneg = 0;
		this.cneut = 0;
	}
	
	//add count to sentiment counter
	public void add(String sent){
		if(sent == "POS"){
			cpos++;
		}
		if(sent == "NEG"){
			cneg++;
		}
		if(sent == "NEUT"){
			cneut++;
		}
	}
	
	//subtract count from sentiment counter
	public void sub(String sent){
		if(sent == "POS"){
			cpos--;
		}
		if(sent == "NEG"){
			cneg--;
		}
		if(sent == "NEUT"){
			cneut--;
		}
	}
	
	//makes the trend for each sentiment for this stock, setting POS,NEG,& NEUT to UP|DOWN|NEUT
	//outputs an arraylist in order POS,NEG,NEUT
	public List<String> makeTrends(){
		//pos sentiment reaction
		if(cpos > 0){
			this.POS = "UP";
		}
		else if(cpos < 0){
			this.POS = "DOWN";
		}
		else{
			this.POS = "NEUT";
		}
		//neg sentiment reaction
		if(cneg > 0){
			this.NEG = "UP";
		}
		else if(cneg < 0){
			this.NEG = "DOWN";
		}
		else{
			this.NEG = "NEUT";
		}
		
		//neut sentiment reaction
		if(cneut > 0){
			this.NEUT = "UP";
		}
		else if(cneut < 0){
			this.NEUT = "DOWN";
		}
		else{
			this.NEUT = "NEUT";
		}
		
		//output
		List<String> output = new ArrayList<String>();
		output.add(POS);
		output.add(NEG);
		output.add(NEUT);
		return output;
	}
	
	public String getSentTrend(String sent){
		if(sent == "POS"){
			return this.POS;
		}
		else if(sent == "NEG"){
			return this.NEG;
		}
		else{
			return this.NEUT;
		}
		
	}
	
	public String getSYM(){
		return this.SYM;
	}
}
