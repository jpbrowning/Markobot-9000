// Mark Atkins, James Browning, Eric Markovich
// Main class for MARKOBOT-9000

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Markobot {
	static ArrayList<PriceCollection> stocks;
	static boolean grabbing;

	public static void main(String args[]) {
		grabbing = false;
		
		Scanner in = new Scanner(System.in);
		String input = "";		
		
		System.out.println("Welcome to MarkoBot-9000\n");		
		menu();
		input = in.nextLine();
		while(!input.equals("2")) {
			
			if(input.equals("1")) {
				System.out.print("\nPrice grabbing has begun. Output files will be saved to \"prices\" directory.\n\n");
				grabbing = true;
				collect();
			}
			else {
				System.out.println("\nInvalid input.\n");
			}
			
			// Pause to create directory before printing menu.
			try {
			    Thread.sleep(1000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			
			menu();
			input = in.nextLine();
		}
		
		System.exit(0);
	}
	
	// Prints the menu for Markobot.
	public static void menu() {
		if(!grabbing){
			System.out.println("\t1. Begin collecting data.");
		}
		System.out.println("\t2. Quit program.\n");
	}
	
	// This sets up the thread to collect prices once an hour.
	public static void collect() {

		stocks = new ArrayList<PriceCollection>();
		stocks.add(new PriceCollection("GOOG"));
		stocks.add(new PriceCollection("AAPL"));
		stocks.add(new PriceCollection("MSFT"));
		stocks.add(new PriceCollection("SNE"));
		
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
				new Runnable() {
					@Override
					public void run() {
						for (PriceCollection s : stocks) {
							s.getPrice();
							s.writeOut();
						}
					}
				}, 0, 1, TimeUnit.HOURS);
	}
}
