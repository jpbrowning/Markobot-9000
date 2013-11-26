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
	
	Map<String, StockTrend> trendmap = new HashMap<String, StockTrend>();//stays null until findTrends is called in YOLO()
	
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
		
		//Output the correlation for a given date, time in an
		//Array list of the four stocks with values in the format:
			//SYM-[UP|DOWN]-[POS|NEG|NEUT]
		public List<String> hourlyCorrelation(String dateone, String timeone, String datetwo, String timetwo){
			List<String> results = new ArrayList<String>();
			for(String sym : stockmap.keySet()){
				//initializations
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
				else if (pone.getPrice() > ptwo.getPrice()){
					results.add(sym+"-"+"DOWN"+"-"+stwo.getSentimentString());
				}
				//Price stayed the same
				else{
					results.add(sym+"-"+"NEUT"+"-"+stwo.getSentimentString());
				}
			}
			return results;
		}
		
		//Finds the trend of the stocks based off of many hourly inputs
		//Outputs a map of stock SYMs to their StockTrends where each trend can call getSentTrend("POS|NEG|NEUT") to get "UP|DOWN|NEUT" average correlation for the sentiment
		public Map<String, StockTrend> findTrends(){
			
			//creates a list of StockTrends of Size equal to the number of stocks in stockmap
			Map<String, StockTrend> strends = new HashMap<String, StockTrend>();
			for(String s : stockmap.keySet()){
				strends.put(s, new StockTrend(s));
			}
			
			//for the number of prices currently stored:
			List<Price> pricelist = stockmap.get("GOOG");//all stocks should have the same number of prices
			for(int i = 1; i < pricelist.size(); i++){
				//hourlyCorrelation for this price/sent given the previous one
				List<String> onehour = hourlyCorrelation(pricelist.get(i-1).getDate(), pricelist.get(i-1).getTime(), pricelist.get(i).getDate(), pricelist.get(i).getTime());
				//
				for(String s: onehour){
					String[] sray = s.split("-");
					//sray should have 3 elements: SYM, UP|DOWN|NEUT, POS|NEG|NEUT
					//price goes up, add to counter for sent
					if(sray[1]=="UP"){
						if(sray[2]=="POS"){
							strends.get(sray[0]).add("POS");
						}
						else if (sray[2] == "NEG"){
							strends.get(sray[0]).add("NEG");
						}
						else{
							strends.get(sray[0]).add("NEUT");
						}
					}
					//price goes down, subtract from counter for sent
					else if(sray[1]=="DOWN"){
						if(sray[2]=="POS"){
							strends.get(sray[0]).sub("POS");
						}
						else if (sray[2] == "NEG"){
							strends.get(sray[0]).sub("NEG");
						}
						else{
							strends.get(sray[0]).sub("NEUT");
						}
					}
					//price stays neutral, don't change counters for sent; i.e. do nothing
					else{}
				}
			}
			//do the final magic
			for(String s : strends.keySet()){
				strends.get(s).makeTrends();//outputs an ArrayList if I want to use it. Also changes the sentiment reaction values in the StockTrend
			}
			
			//output map of StockTrends
			return strends;
		}
		
		//calls all other necessary functions to do the work of the program
/*		public void YOLO(){
			///read all sentiment and stock txt files
			for(//File f : Folder of text files){
					read(f);
			}
			
			trendmap = findTrends();
					
			//Print line outputs for now
			for(String s : trendmap.keySet()){
				
				System.out.println(s+" : "+"with POS sent prices go : "+trendmap.get(s).getSentTrend("POS"));
				System.out.println(s+" : "+"with NEG sent prices go : "+trendmap.get(s).getSentTrend("NEG"));
				System.out.println(s+" : "+"with NEUT sent prices go : "+trendmap.get(s).getSentTrend("NEUT"));
				
			}
		}
*/
}
