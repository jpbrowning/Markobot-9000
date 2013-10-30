// Mark Atkins, James Browning, Eric Markovich
// Main class for MARKOBOT-9000

import java.util.Scanner;
 
class Markobot
{
   public static void main(String args[])
   {
      String s;
      Scanner in = new Scanner(System.in);
      
      YahooScanner prices = new YahooScanner();
      
      System.out.println("Enter the symbol you would like to look up: ");
      s = in.next();
      Price symbol = prices.getPrice(s);
      
      System.out.println("\nMarket price for " + s + ", with timestamp: ");      
      System.out.println(symbol.toString());
      
      in.close();
   }
}
