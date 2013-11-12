import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SwagMath {
	/*Notes: stock Symbols: GOOG, AAPL, SNE, MSFT
	*there are text files for Stock prices by day per hour
	*	24 Price.toStrings() in each file
	*there are text files for Tweet Sentiments by day per hour
	*/
	
	//maps each stock Symbol to a list of its hourly prices
	public Map<String, List<Price>> stockmap = new HashMap<String, List<Price>>();
	
	//maps each stock Symbol to a list of its hourly prices
	public Map<String, List<Tweet>> sentmap = new HashMap<String, List<Tweet>>();
	
	//constructor
	public SwagMath(){
		
	}
	
	////Methods
	//get stock price data from file and store in map
	public void readStock(String file){
		//open
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while((line = br.readLine()) != null){
				//get stuff from the line
				String[] hourprice = line.split(" ");
				Price p = new Price(hourprice[0], Double.parseDouble(hourprice[1]), hourprice[2], hourprice[3]);
				//add price to list of prices mapped at symbol hourprice[0]
				List<Price> l = stockmap.get(hourprice[0]);
				l.add(p);
				//add updated list back to map
				stockmap.put(hourprice[0], l);

			}
			br.close();
		}catch(Exception e){e.printStackTrace();}
	}
	
	//get stock sentiment data from file and store in map
		public void readSentiment(String file){
			//open
			try{
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				while((line = br.readLine()) != null){
					//get stuff from the line
					String[] hoursent = line.split(" ");
					/////ASSUMING Tweet Class outputs to file 24 lines of 
					/////	symbol + " " + sentiment + " " + date + " " + time/////
					//*** I think I actually need a Sentiment class similar to Price ***/
					Tweet t = new Tweet(hoursent[0], hoursent[1], hoursent[2], hoursent[3]);
					//add price to list of prices mapped at symbol hoursent[0]
					List<Tweet> l = sentmap.get(hoursent[0]);
					l.add(t);
					//add updated list back to map
					sentmap.put(hoursent[0], l);

				}
				br.close();
			}catch(Exception e){e.printStackTrace();}
		}
		
		////////////next task: public void read(file){if file == stock -->readStock(); if file == sentiment-->readSentiment()}
		public void read(String file){
			if(file.contains("S-"))
		}
}
