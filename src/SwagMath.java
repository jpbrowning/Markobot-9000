import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
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
	
	//maps each stock Symbol to a list of its hourly sentiments
	public Map<String, List<HourlySent>> sentmap = new HashMap<String, List<HourlySent>>();
	
	//constructor with 4 standard stocks
	public SwagMath(){
		stockmap.put("GOOG", new ArrayList<Price>());
		stockmap.put("AAPL", new ArrayList<Price>());
		stockmap.put("SNE", new ArrayList<Price>());
		stockmap.put("MSFT", new ArrayList<Price>());
		
		sentmap.put("GOOG", new ArrayList<HourlySent>());
		sentmap.put("AAPL", new ArrayList<HourlySent>());
		sentmap.put("SNE", new ArrayList<HourlySent>());
		sentmap.put("MSFT", new ArrayList<HourlySent>());
	}
	
	//constructor for list of given stocks
	public SwagMath(String[] symbols){
		for(String sym : symbols){
			stockmap.put(sym, new ArrayList<Price>());
			sentmap.put(sym, new ArrayList<HourlySent>());
		}
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
					/////ASSUMING Tweet/HourlySent Class outputs to file 24 lines of 
					/////	symbol + " " + sentiment + " " + date + " " + time/////
					HourlySent hs = new HourlySent(hoursent[0], Double.parseDouble(hoursent[1]), hoursent[2], hoursent[3]);
					//add price to list of prices mapped at symbol hoursent[0]
					List<HourlySent> l = sentmap.get(hoursent[0]);
					l.add(hs);
					//add updated list back to map
					sentmap.put(hoursent[0], l);

				}
				br.close();
			}catch(Exception e){e.printStackTrace();}
		}
		
		//Helper 'read' function
		public void read(String file){
			//Stock market file
			if(file.contains("-S-")){
				readStock(file);
			}
			//Sentiment tweet file
			else{
				readSentiment(file);
			}
		}
		
		//Output the correlation for a given date, time
		//Array list of the four stocks with values in the format:
			//SYM-[UP|DOWN]-[POS|NEG|NEUT]
		public List<String> hourlyCorrelation(String dateone, String timeone, String datetwo, String timetwo){
			List<String> results = new ArrayList<String>();
			for(String sym : stockmap.keySet()){
				//////////////Next thing to do************///////////////
				//if price at datetwo, timetwo is greater than dateone, timeone and 
				//sent at datetwo, timetwo is POS, output GOOG-UP-POS
				//repeat for all for cases for each stock
				List<Price> stockprices = stockmap.get(sym);
				List<HourlySent> stocksent = sentmap.get(sym);
				Price pone = null;
				Price ptwo = null;
				HourlySent stwo = null;
				//loop finds the two prices to be compared
				for(Price p : stockprices){
					if(dateone == p.getDate() && timeone == p.getTime()){
						pone = p;
					}
					else if(datetwo == p.getDate() && timetwo == p.getTime()){
						ptwo = p;
					}
					else{
						System.out.println("No matched price for "+sym+" at "+dateone+" : "+timeone+"  or "+datetwo+" : "+timetwo);
					}
				}
				//find the HourlySent corresponding to the second date, time
				for (HourlySent hs : stocksent){
					if(datetwo == hs.getDate() && timetwo == hs.getTime()){
						stwo = hs;
					}
					else{
						System.out.println("No matched hourly sentiment for "+sym+" at "+datetwo+" : "+timetwo);
					}
				}
				////////Results////////
				//Price went up
				if(pone.getPrice() < ptwo.getPrice()){
					results.add(sym+"-"+"UP"+"-"+stwo.getSentimentString());
				}
				//Price went down
				else{
					results.add(sym+"-"+"DOWN"+"-"+stwo.getSentimentString());
				}
			}
			return results;
		}
}
