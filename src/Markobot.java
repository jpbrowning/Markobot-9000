// Mark Atkins, James Browning, Eric Markovich
// Main class for MARKOBOT-9000

import java.util.Scanner;
 
class Markobot {
   public static void main(String args[]) {
	   
      String s;
      Scanner in = new Scanner(System.in);
      
      PriceCollection data = new PriceCollection();
      
      System.out.println("Enter the symbol you would like to look up: ");
      s = in.next();
      
      data.add(s);
      
      data.writeOut();
      data.printOut();
      
      in.close();
   }//monkeys
}
