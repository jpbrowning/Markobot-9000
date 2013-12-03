import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SwagMath {
	/*Notes: stock Symbols: GOOG, AAPL, SNE, MSFT
	*there are text files for Stock prices by day per hour
	*	24 Price.toStrings() in each file
	*there are text files for Tweet Sentiments by day per hour
	*/
	//Most recent HourlySent
	public HourlySent gresent = new HourlySent("\"GOOG\"", 0, "\"11/18/1970\"" , "\"10:30am\"");//old year will always make this start as oldest
	public HourlySent aresent = new HourlySent("\"AAPL\"", 0, "\"11/18/1970\"" , "\"10:30am\"");//old year will always make this start as oldest
	public HourlySent sresent = new HourlySent("\"SNE\"", 0, "\"11/18/1970\"" , "\"10:30am\"");//old year will always make this start as oldest
	public HourlySent mresent = new HourlySent("\"MSFT\"", 0, "\"11/18/1970\"" , "\"10:30am\"");//old year will always make this start as oldest
	//maps each stock Symbol to a list of its hourly prices
	public Map<String, List<Price>> stockmap = new HashMap<String, List<Price>>();
	
	//maps each stock Symbol to a list of its hourly sentiments
	public Map<String, List<HourlySent>> sentmap = new HashMap<String, List<HourlySent>>();
	
	Map<String, StockTrend> trendmap = new HashMap<String, StockTrend>();//stays null until findTrends is called in YOLO()
	
	//constructor with 4 standard stocks
	public SwagMath(){
		stockmap.put("\"GOOG\"", new ArrayList<Price>());
		stockmap.put("\"AAPL\"", new ArrayList<Price>());
		stockmap.put("\"SNE\"", new ArrayList<Price>());
		stockmap.put("\"MSFT\"", new ArrayList<Price>());
		
		sentmap.put("\"GOOG\"", new ArrayList<HourlySent>());
		sentmap.put("\"AAPL\"", new ArrayList<HourlySent>());
		sentmap.put("\"SNE\"", new ArrayList<HourlySent>());
		sentmap.put("\"MSFT\"", new ArrayList<HourlySent>());
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
			BufferedReader br = new BufferedReader(new FileReader("./prices/"+file));
			String line;
			while((line = br.readLine()) != null){
				//get stuff from the line
				String[] hourprice = line.split(" ");
				//Browning's gay format needs changed
				String[] temp = hourprice[3].replace("\"", "").split(":");
				String temp2 ="";
				if(temp[1].contains("p")){
					if(temp[0].equals("12")){
						temp2 = "12";
					}
					else{
						int t = Integer.parseInt(temp[0])+12;
						temp2 = temp2+t;//cast to a string
					}
				}
				else{
					if(temp[0].equals("12")){
						temp2 = "00";
					}
					else if(temp[0].length()==1){
						temp2 = "0"+temp[0];
					}
					else{
						temp2 = temp[0];
					}
				}
				//now the minutes...
				temp2 = "\""+temp2+":"+temp[1].substring(0,2)+"\"";
//				System.out.println("the time is now: "+temp2);
				//temp2 now has the time we need after changing Browning's format
				
				
				
				Price p = new Price(hourprice[0], Double.parseDouble(hourprice[1]), hourprice[2], temp2);
//				System.out.println("Price Object in readStock: \n"+"Symbol: "+p.getSym()+" Price: "+p.getPrice()+" Date: "+p.getDate()+" Time: "+p.getTime());
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
				BufferedReader br = new BufferedReader(new FileReader("./prices/"+file));
				String line;
				List<String[]> compiledlines = new ArrayList<String[]>();
				int count = 0;
				while((line = br.readLine()) != null){
					//get stuff from the line
					String[] hoursent = line.split(" ");
					
					//compiled lines so there is only one line per hour
					hoursent[3] = hoursent[3].substring(0,4)+"30\"";
					if(count == 0){
						compiledlines.add(hoursent);
					}
					if(count > 0){
	//					System.out.println("entering with count = "+count);
						boolean check = true;
						for(String[] ssray : compiledlines ){
	//						System.out.println("HERE compiled lines size: "+compiledlines.size());
							if(ssray[3].equals(hoursent[3])){
								System.out.println("changing a compiled line");
								ssray[1] += hoursent[1];
								check = false;
							}
						}
						//if there was no line with same time
						if(check){
								System.out.println("adding new compiled line");
								compiledlines.add(hoursent);
						}
					}
					count++;
//					System.out.println("count : "+count);
				}
				
				
				
				
				/////ASSUMING Tweet/HourlySent Class outputs to file 24 lines of 
				/////	symbol + " " + sentiment + " " + date + " " + time/////
				for(String[] hoursent2 : compiledlines){
					
					HourlySent hs = new HourlySent(hoursent2[0], Double.parseDouble(hoursent2[1]), hoursent2[2], hoursent2[3]);
					
					
					//check to see if most recent sentiment and mark if it is
					if(hoursent2[0].equals("\"GOOG\"")){
						gresent = moreRecent(gresent, hs);
						System.out.println("gresent updated");
					}
					else if(hoursent2[0].equals("\"AAPL\"")){
						aresent = moreRecent(aresent, hs);
						System.out.println("aresent updated with: "+aresent.getDate()+"  "+aresent.getTime());
					}
					else if(hoursent2[0].equals("\"SNE\"")){
						sresent = moreRecent(sresent, hs);
						System.out.println("sresent updated");
					}
					else{
						mresent = moreRecent(mresent, hs);
						System.out.println("mresent updated");
					}
					
					
					System.out.println("HourlySent Object in readStock: \n"+"Symbol: "+hs.getSym()+" Sentiment: "+hs.getSentiment()+" Date: "+hs.getDate()+" Time: "+hs.getTime());
					//add price to list of prices mapped at symbol hoursent2[0]
					List<HourlySent> l = sentmap.get(hoursent2[0]);
					l.add(hs);
					//add updated list back to map
					sentmap.put(hoursent2[0], l);
				}
				
				br.close();
			}catch(Exception e){e.printStackTrace();}
			
		
			
			
		}
		
		//Helper 'read' function
		//File naming convention: 27-11-2013-S-GOOG
		public void read(String file){
			//Stock market file
			if(file.contains("-S-")){
//				System.out.println("Directing file "+file+" to readStock()");
				readStock(file);
			}
			//Sentiment tweet file
			else{
//				System.out.println("Directing file "+file+" to readSentiment()");
				readSentiment(file);
			}
		}
		
		//returns the HourlySent with the most recent timestamp
		public HourlySent moreRecent(HourlySent champ, HourlySent contestor){
			String[] champdate = champ.getDate().replace("\"","").split("/");//replace quotes with nothing to trim them off and splits by '/'
			String[] contestordate = contestor.getDate().replace("\"","").split("/");
			//turn into years
			double champdoub = Double.parseDouble(champdate[2]);
			champdoub += Double.parseDouble(champdate[0])/12.0;
			champdoub += Double.parseDouble(champdate[1])/365.0;
			
			double contestordoub = Double.parseDouble(contestordate[2]);
			contestordoub += Double.parseDouble(contestordate[0])/12.0;
			contestordoub += Double.parseDouble(contestordate[1])/365.0;
			
			//champ has more recent date
			if(champdoub > contestordoub){
				return champ;
			}
			//contestor has more recent date
			else if(champdoub < contestordoub){
				return contestor;
			}
			//same date, check times
			else{
				String[] champtime = champ.getTime().replace("\"","").split(":");
				String[] contestortime = contestor.getTime().replace("\"","").split(":");
				
				//champ in the pm and contestor in am
				if(champtime[1].contains("p") && contestortime[1].contains("a")){
					return champ;
				}
				//contestor in the pm and champ in am
				else if(champtime[1].contains("a") && contestortime[1].contains("p")){
					return contestor;
				}
				//both in same am or pm
				else{
					//change to minutes and compare
					double champmin = Double.parseDouble(champtime[0]) * 60.0;
					champmin += Double.parseDouble(champtime[1].substring(0,2));//grabs the minutes after the ':' and cuts off the 'am|pm'
					
					double contestormin = Double.parseDouble(contestortime[0]) * 60.0;
					contestormin += Double.parseDouble(contestortime[1].substring(0,2));//grabs the minutes after the ':' and cuts off the 'am|pm'
					
					if(champmin > contestormin){
						return champ;
					}
					else{
						return contestor;
					}
				}
				
			}
		}
		
		
		
		//Output the correlation for a given date, time in an
		//Array list of the four stocks with values in the format:
			//SYM-[UP|DOWN]-[POS|NEG|NEUT]
		public List<String> hourlyCorrelation(String dateone, String timeone, String datetwo, String timetwo){
			List<String> results = new ArrayList<String>();
			for(String sym : stockmap.keySet()){
				//initializations
//				System.out.println("sym in upper for loop is : "+sym+" and stockmap.get(sym) size is : "+stockmap.get(sym).size());
				List<Price> stockprices = stockmap.get(sym);
				List<HourlySent> stocksent = sentmap.get(sym);
				Price pone = null;
				Price ptwo = null;
				HourlySent sone = null;
				//loop finds the two prices to be compared
				for(Price p : stockprices){
					if(dateone.equals(p.getDate()) && timeone.equals(p.getTime())){
						pone = p;
//						System.out.println("In here and assigned pone to p");
					}
					else if(datetwo.equals(p.getDate()) && timetwo.equals(p.getTime())){
						ptwo = p;
//						System.out.println("In here and assigned ptwo to p");
					}
//					else{
//						System.out.println("No matched price for "+sym+" at "+dateone+" : "+timeone+"  or "+datetwo+" : "+timetwo);
//					}
				}
				//find the HourlySent corresponding to the first date, time
				for (HourlySent hs : stocksent){
					if((dateone.equals(hs.getDate()) ) && (timeone.equals(hs.getTime()) ) ){
						sone = hs;
//						System.out.println("Found the matching hourlySent");
					}
//					else{
//						System.out.println("No matched hourly sentiment for "+sym+" at "+datetwo+" : "+timetwo+" cuz hs has date and time: "+hs.getDate()+"  "+hs.getTime());
//					}
				}
				////////Results////////
				//Price or Sentiment doesn't exist for this stock at this date/time
				if(pone == null || ptwo == null || sone == null){
//					System.out.println("something is null.....");
					//do nothing.  Do not add to results because the symbol does not have data
				}
				//Price went up
				else if(pone.getPrice() < ptwo.getPrice()){
					results.add(sym+"-"+"UP"+"-"+sone.getSentimentString());
					System.out.println("In hourlyCorrelation:\n pone price: "+pone.getPrice()+" and ptwo price: "+ptwo.getPrice());
					System.out.println("Added to results: "+sym+"-"+"UP"+"-"+sone.getSentimentString());
				}
				//Price went down
				else if (pone.getPrice() > ptwo.getPrice()){
					results.add(sym+"-"+"DOWN"+"-"+sone.getSentimentString());
					System.out.println("In hourlyCorrelation:\n pone price: "+pone.getPrice()+" and ptwo price: "+ptwo.getPrice());
					System.out.println("Added to results: "+sym+"-"+"DOWN"+"-"+sone.getSentimentString());
				}
				//Price stayed the same
				else{
					results.add(sym+"-"+"NEUT"+"-"+sone.getSentimentString());
					System.out.println("In hourlyCorrelation:\n pone price: "+pone.getPrice()+" and ptwo price: "+ptwo.getPrice());
					System.out.println("Added to results: "+sym+"-"+"NEUT"+"-"+sone.getSentimentString());
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
			List<Price> pricelist = stockmap.get("\"GOOG\"");//all stocks should have the same number of prices
			for(int i = 1; i < pricelist.size(); i++){
				//hourlyCorrelation for this price/sent given the previous one
				List<String> onehour = hourlyCorrelation(pricelist.get(i-1).getDate(), pricelist.get(i-1).getTime(), pricelist.get(i).getDate(), pricelist.get(i).getTime());
				//
				for(String s: onehour){
					String[] sray = s.split("-");
					//sray should have 3 elements: SYM, UP|DOWN|NEUT, POS|NEG|NEUT
					//price goes up, add to counter for sent
//					System.out.println("s in this for loop is "+ s+" and sray[1] is "+sray[1]+" and sray[2] is "+sray[2]);
					if(sray[1].equals("UP")){
						if(sray[2].equals("POS")){
							System.out.println("adding to UP, POS in strends for "+sray[0]);
							strends.get(sray[0]).add("POS");
						}
						else if (sray[2].equals("NEG")){
							strends.get(sray[0]).add("NEG");
						}
						else{
							strends.get(sray[0]).add("NEUT");
						}
					}
					//price goes down, subtract from counter for sent
					else if(sray[1].equals("DOWN")){
						if(sray[2].equals("POS")){
							strends.get(sray[0]).sub("POS");
						}
						else if (sray[2].equals("NEG")){
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
		
		//calls all other necessary functions to do the work of the program, training the data
		public void YOLO(){
			///read all sentiment and stock txt files
			File f = new File("./prices");
			ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
			for(String s : names){
					System.out.println("filename: "+s);
					read(s);
			}
			
			trendmap = findTrends();
					
			//Print line outputs for now
			for(String s : trendmap.keySet()){
				
				System.out.println(s+" : with POS sent prices go : "+trendmap.get(s).getSentTrend("POS"));
				System.out.println(s+" : with NEG sent prices go : "+trendmap.get(s).getSentTrend("NEG"));
				System.out.println(s+" : with NEUT sent prices go : "+trendmap.get(s).getSentTrend("NEUT"));
				
			}
		}

}
