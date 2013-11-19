import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PriceCollection {
	String symbol;
	YahooScanner scan = new YahooScanner();
	ArrayList<Price> prices;

	// Basic constructor, pass in the all-caps stock ticker. (AAPL for Apple)
	public PriceCollection(String s) {
		this.symbol = s;
		prices = new ArrayList<Price>();
	}

	// Uses the YahooScanner class to get the price 20 minutes ago.
	public void getPrice() {
		prices.add(scan.getPrice(symbol));
	}

	// Prints the price.
	public void printOut() {
		for (Price p : prices) {
			System.out.println(p.toString());
		}
	}

	// Writes the price to a file in the price directory. If that directory 
	// does not exist, it will create it.
	public void writeOut() {
		
		File theDir = new File("../prices");
		
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("creating directory: " + "prices");
			boolean result = theDir.mkdir();
			
			if(result) {    
				System.out.println("Directory created.\n");  
			}
		}

		Calendar calendar = Calendar.getInstance();
		Date dt = new Date();
		calendar.setTime(dt);
		String filename = "../prices/" + calendar.get(Calendar.DATE) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.YEAR) + "-" + "S" + "-" + symbol;
		
		if(Calendar.HOUR_OF_DAY > 9 && Calendar.HOUR_OF_DAY < 17) {
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
		}
	}
}
