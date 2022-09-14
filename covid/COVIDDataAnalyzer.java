package covid;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author CalebPapay
 */
public class COVIDDataAnalyzer
{
	// You will need to define attributes to manage the data here!
	private Map<String, List<COVIDData>> countryRonaData = new HashMap<>();
	//          Country,   COVIDData               gives us COVID data for country on each unique date
	private Map<String, Long> casesForCont = new HashMap<>();
	//          continent , cases                  gives us # of cases for a continent
	private Map<String, Long> popForCont = new HashMap<>();
	//          continent, incrementing continent pop     gives us population for a continent
	
	private Map<String, Collection<String>> aContinentsCountries= new HashMap<>();
	//          continent,      countries          gives us collection of countries in a continent
	private Map<String, String> countriesContinent = new HashMap<>();
	
	/**
	 * Read the data in the know WHO format from the specified file
	 * @param filename the name of the file to read
	 * @return a list of COVIDData objects read from the file
	 * @throws ParseException 
	 */
	public List<COVIDData> readFile(String filename) throws IOException, ParseException
	{
		List<COVIDData> data = new ArrayList<>();
		List<String> seenCountries = new ArrayList<>();
		List<String> addedToContinent = new ArrayList<>();
		
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String line = in.readLine(); 
		line = in.readLine(); //skip first line in file
		
		while (line != null) {
			
			String[] tokens = line.split(",");
			Date day = new SimpleDateFormat("dd/MM/yy").parse(tokens[0]); //lets see if this works (https://www.javatpoint.com/java-string-to-date)
			long cases = Long.parseLong(tokens[4]);
			long deaths = Long.parseLong(tokens[5]);
			String country = tokens[6];
			long countrypop = Long.parseLong(tokens[9]); //https://stackoverflow.com/questions/7693324/how-to-convert-string-to-long-in-java
			String continent = tokens[10];
			
			COVIDData countrydata = new COVIDData(day, cases, deaths, country, countrypop, continent);
			data.add(countrydata);
			if (countryRonaData.containsKey(country) == true) {
				countryRonaData.get(country).add(countrydata);
			}
			else if (countryRonaData.containsKey(country) ==false) {
				
				countryRonaData.put(country, new ArrayList<>());
				countryRonaData.get(country).add(countrydata);
				
			}
			
			if (casesForCont.containsKey(continent) == true) {
				long currCases = casesForCont.get(continent);
				currCases += cases;
				casesForCont.put(continent, currCases);
			}
			else if (casesForCont.containsKey(continent) == false) {
				casesForCont.put(continent, cases);
			}
			if (countriesContinent.containsKey(country) == false) {
				countriesContinent.put(country, continent);
			}
			if (popForCont.containsKey(continent) == false) {
				popForCont.put(continent, countrypop);
				seenCountries.add(country);
			}
			else if (popForCont.containsKey(continent) == true && seenCountries.contains(country) == false) {
				long currPop = popForCont.get(continent);
				currPop += countrypop;
				popForCont.put(continent, currPop);
				seenCountries.add(country);
				
			}
			if (aContinentsCountries.containsKey(continent) == false) {
				aContinentsCountries.put(continent, new ArrayList<>());
				aContinentsCountries.get(continent).add(country);
				addedToContinent.add(country);
			}
			else if(aContinentsCountries.containsKey(continent) == true && addedToContinent.contains(country) == false) {
				aContinentsCountries.get(continent).add(country);
				addedToContinent.add(country);
			}
			line = in.readLine();
			
		}
		in.close();
		return data;
	}
	
	/**
	 * Create a new report filtered to the specified country and stored in the specified
	 * file.  The new file should use the following format
	 * <country>, <continent>, <population>, <day>, <month>, <year>, <cases>, <deaths>
	 * @param country - The country to report upon
	 * @param toFilename - The destination filename to save the report
	 * @throws IOException 
	 */
	@SuppressWarnings("deprecation")
	public void generateReport(String country, String toFilename) throws IOException
	{
		BufferedWriter filewriter = new BufferedWriter(new FileWriter(toFilename)); //https://www.baeldung.com/java-write-to-file
		for (int i = 0; i<countryRonaData.get(country).size(); i++) {
				
			COVIDData thisCountriesDataOnDate = countryRonaData.get(country).get(i);
			Date thisDate = thisCountriesDataOnDate.getDay();
			
			
			Calendar calendar = Calendar.getInstance(); //https://www.baeldung.com/java-year-month-day
			calendar.setTime(thisDate);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int month =calendar.get(Calendar.MONTH)+1; //?
			int year = calendar.get(Calendar.YEAR);
			
			String continent = thisCountriesDataOnDate.getContinent();
			long pop = thisCountriesDataOnDate.getPopulation();
			long cases = thisCountriesDataOnDate.getCases();
			long deaths = thisCountriesDataOnDate.getDeaths();
			
			String addToFile = String.format("%s,%s,%d,%d,%d,%d,%d,%d\n", country, continent, pop, day, month, year, cases, deaths ); //adding 1 to month fixed the problem
			
			filewriter.append(addToFile);
			
		}
		filewriter.close();
		
	}
	
	/**
	 * Retrieve the number of cases on each continent
	 * @return a map with the key of the continent name and the 
	 *  value the total number of cases on that continent
	 */
	public long getCasesForContinent(String continent)
	{
		return casesForCont.get(continent);
	}

	/**
	 * Retrieve the population of a continent
	 * @param continent - the target
	 * @return a map with the key of the continent name and the 
	 *  value the population of that continent
	 */
	public long getPopulationForContinent(String continent)
	{
		return popForCont.get(continent);
	}	
	
	/**
	 * Retrieve all the countries on a continent
	 * @param continent - the target
	 * @return a list of countries
	 */
	public Collection<String> getCountriesOnContinent(String continent)
	{
		Collection<String> countries = new ArrayList<>();
		countries = aContinentsCountries.get(continent);
		return countries;
	}
}