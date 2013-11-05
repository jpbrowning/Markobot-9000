import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PriceCollection {
	YahooScanner scan = new YahooScanner();
	ArrayList<Price> prices;
	
	public PriceCollection() {
		prices = new ArrayList<Price>();
	}	
	
	public void add(String s) {
		prices.add(scan.getPrice(s));
	}
	
	public void printOut() {
		for(Price p : prices){
			System.out.println(p.toString());
		}
	}
	
	public void writeOut() {
		String filename= "../data.txt";
	    FileWriter fw;
		try {
			fw = new FileWriter(filename,true);
			for(Price p : prices){
				fw.write(p.toString() + "\n");
				
			}
		    fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
}
