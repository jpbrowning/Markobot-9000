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

		stocks = new ArrayList<PriceCollection>();
		stocks.add(new PriceCollection("GOOG"));
		stocks.add(new PriceCollection("AAPL"));
		stocks.add(new PriceCollection("MSFT"));
		stocks.add(new PriceCollection("SNE"));
		
		System.out.println("Welcome to MarkoBot-9000\n");
		
		menu();
		input = in.nextLine();
		while(! input.equals("2")){
			if(input.equals("1")){
				System.out.print("Price grabbing has begun. Output will be saved to parent directory.\n");
				grabbing = true;
				collect();
			}
			menu();
			input = in.nextLine();
		}		
	}
	
	public static void menu(){
		if(!grabbing){
			System.out.println("\t1. Begin collecting data.");
		}
		System.out.println("\t2. Quit program.\n");
	}
	
	public static void collect(){
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
				new Runnable() {
					@Override
					public void run() {
						for (PriceCollection s : stocks) {
							s.getPrice();
							s.printOut();
							s.writeOut();
						}
					}
				}, 0, 1, TimeUnit.HOURS);
	}
}
