public class Price {
	public String symbol;
	public double value;
	public String date;
	public String time;

	// Basic constructor.
	public Price(String s, double v, String d, String t) {
		this.symbol = s;
		this.value = v;
		this.date = d;
		this.time = t;
	}

	// Basic toString() method.
	public String toString() {
		return (symbol + " " + value + " " + date + " " + time);
	}
	
	public double getPrice(){
		return this.value;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public String getTime(){
		return this.time;
	}
	
	public String getSym(){
		return this.symbol;
	}

}
