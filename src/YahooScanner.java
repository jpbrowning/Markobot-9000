import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

// Mark Atkins, James Browning, Eric Markovich
// Loads a Yahoo! Finance URL and grabs the price from CSV format

public class YahooScanner
{
   public YahooScanner()
   {
      
   }
   
   public Price getPrice(String symbol)
   {
	   String Line[] = null;
	   
	   try{
		   URL yahoofinance =  new URL("http://finance.yahoo.com/d/quotes.csv?s=" + symbol + "&f=sl1d1t1c1ohgv&e=.csv");
		   URLConnection yc = yahoofinance.openConnection();
		   BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		   
		   String inputLine;
	       while ((inputLine = in.readLine()) != null)
	       {
	           Line = inputLine.split(",");
	       }
	       in.close();
	   }
	   catch (IOException ex){
		   
		   System.out.println("Bad symbol.");
	   }
         
	   return new Price(Line[0], Double.parseDouble(Line[1]), Line[2], Line[3]);
   }
}
