public class Price {
  public String symbol;
	public double value;
	public String date;
	public String time;
	
	public Price(String s, double v, String d, String t) {
		this.symbol = s;
		this.value = v;
		this.date = d;
		this.time = t;
	}
	
	public String toString() {
		return (symbol + " " + value + " " + date + " " + time);
	}

}
