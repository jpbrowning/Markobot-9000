public class HourlySent {
	public String symbol;
	public double sentiment;//-1 = NEG, 0 = NEUTRAL, 1 = POS
	public String date;
	public String time;
	
	public HourlySent(String s, double sent, String d, String t) {
		this.symbol = s;
		this.sentiment = sent;
		this.date = d;
		this.time = t;
	}
	
	public String toString() {
		return (symbol + " " + sentiment + " " + date + " " + time);
	}
	
	public String getSym(){
		return this.symbol;
	}
	
	public double getSentiment(){
		return this.sentiment;
	}
	
	public String getSentimentString(){
		if(this.sentiment == -1){
			return "NEG";
		}
		else if(this.sentiment == 0){
			return "NEUT";
		}
		else{
			return "POS";
		}
	}
	
	public String getDate(){
		return this.date;
	}
	
	public String getTime(){
		return this.time;
	}

}
