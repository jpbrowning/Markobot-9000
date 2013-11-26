import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

// Mark Atkins, James Browning, Eric Markovich
// Loads a Yahoo! Finance URL and grabs the price from CSV format

public class YahooScanner {
	public YahooScanner() {

	}

	public Price getPrice(String symbol) {
		String Line[] = null;

		try {
			URL yahoofinance = new URL(
					"http://finance.yahoo.com/d/quotes.csv?s=" + symbol
							+ "&f=sl1d1t1c1ohgv&e=.csv");
			URLConnection yc = yahoofinance.openConnection();
			BufferedReader brin = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));

			String inputLine;
			while ((inputLine = brin.readLine()) != null) {
				Line = inputLine.split(",");
			}
			brin.close();
		} catch (IOException ex) {

			System.out.println("Bad symbol.");
		}

		return new Price(Line[0], Double.parseDouble(Line[1]), Line[2], Line[3]);
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

}
*/
