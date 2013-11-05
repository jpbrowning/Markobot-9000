import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PriceCollection {
	String symbol;
	YahooScanner scan = new YahooScanner();
	ArrayList<Price> prices;

	public PriceCollection(String s) {
		this.symbol = s;
		prices = new ArrayList<Price>();
	}

	public void getPrice() {
		prices.add(scan.getPrice(symbol));
	}

	public void printOut() {
		for (Price p : prices) {
			System.out.println(p.toString());
		}
	}

	public void writeOut() {

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
}
