# Mark Atkins, James Browning, Eric Markovich
# Main class for MARKOBOT-9000

import java.util.Scanner;
 
class Markobot
{
   public static void main(String args[])
   {
      String s;
      YahooScanner prices = new YahooScanner();
      
      System.out.println("Enter the symbol you would like to look up: ");
      s = in.next();
      
      System.out.println("\nMarket price for " + s ", 20 minutes ago: );
      System.out.println(prices.getPrice(s));
   }
}
