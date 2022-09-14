package covid.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.gradescope.jh61b.grader.GradedTest;

import covid.COVIDData;
import covid.COVIDDataAnalyzer;
import ledger.LoggedTest;

/**
 * @author Tony
 */
public class COVIDTest extends LoggedTest 
{
	private static final int TOTAL_COUNT        = 46580;
	private static final String FILENAME        = "download.csv";
	private static final String REPORT_FILENAME = "belizeReport.csv";
	private static final String TEST_COUNTRY    = "Belize";
	@SuppressWarnings("deprecation")
	private final COVIDData FIRST = new COVIDData(new Date(120, 9, 3), 5, 0, "Afghanistan", 38041757, "Asia");
	private final COVIDData LAST  = new COVIDData(new Date(120, 2, 21), 1, 0, "Zimbabwe", 14645473, "Africa");

    @Test		
    @GradedTest(name="Test readFile()", max_score=10)
    public void testLoad() 
    {
    	try
    	{
        	COVIDDataAnalyzer uut = new COVIDDataAnalyzer();
        	List<COVIDData> data = uut.readFile(FILENAME);
        	assertEquals("You did not read the same number of records as is in the file", TOTAL_COUNT, data.size());
        	assertEquals(FIRST, data.get(0));
        	assertEquals(LAST, data.get(TOTAL_COUNT - 1));
    	} catch (Exception e)
    	{
    		e.printStackTrace();
    		fail("Exception while testing readFile() " + e.getLocalizedMessage());
    	}
    }
    
    @Test		
    @GradedTest(name="Test generateReport()", max_score=10)
    public void testReport() 
    {
    	BufferedReader in = null;
    	try
    	{
    		new File(REPORT_FILENAME).delete(); // Cleanup any old runs
    		
        	COVIDDataAnalyzer uut = new COVIDDataAnalyzer();
        	List<COVIDData> data = uut.readFile(FILENAME);
        	uut.generateReport(TEST_COUNTRY, REPORT_FILENAME);
        	
        	assertTrue("File is not created as " + REPORT_FILENAME, new File(REPORT_FILENAME).exists());
    		in = new BufferedReader(new FileReader(REPORT_FILENAME));
    		String line = in.readLine();
    		for (COVIDData c : data)
    		{
    			if(c.getCountry().equals(TEST_COUNTRY))
    			{
	    			assertNotNull("Not enough lines in your report file", line);
	    			String[] tokens = line.split(",");
	    			
	    			COVIDData actual = new COVIDData(new Date(Integer.parseInt(tokens[5].trim())-1900, Integer.parseInt(tokens[4].trim())-1, Integer.parseInt(tokens[3].trim())), 
	    					                         Integer.parseInt(tokens[6].trim()), Integer.parseInt(tokens[7].trim()),
	    					                         tokens[0].trim(), Integer.parseInt(tokens[2].trim()), tokens[1].trim());
	    			assertEquals(c, actual);
	    			line = in.readLine();
    			}
    		}
    	} catch (Exception e)
    	{
    		e.printStackTrace();
    		fail("Exception while testing generateReport() " + e.getLocalizedMessage());
    	} finally
    	{
    		try
			{
    			if (in != null) in.close();
			} catch (IOException e){}
    	}
    }

    @Test		
    @GradedTest(name="Test getCasesForContinent()", max_score=10)
    public void testCases() 
    {
    	try
    	{
        	COVIDDataAnalyzer uut = new COVIDDataAnalyzer();
        	List<COVIDData> data = uut.readFile(FILENAME);

        	Map<String, Long> CONTINENT_CASES = Map.ofEntries(
            		Map.entry("Asia",    10814423L), 
            		Map.entry("Oceania",    33791L),
            		Map.entry("America", 17043337L),
            		Map.entry("Africa",   1498661L), 
    				Map.entry("Europe",   5289291L));
        	for(Map.Entry<String, Long> entry : CONTINENT_CASES.entrySet())
        	{
            	Long cases = uut.getCasesForContinent(entry.getKey());
        		assertEquals("Cases in " + entry.getKey() + " do not match",
        					 entry.getValue(), cases);
        	}

        	
    	} catch (Exception e)
    	{
    		e.printStackTrace();
    		fail("Exception while testing readFile() " + e.getLocalizedMessage());
    	}
    }
    
    @Test		
    @GradedTest(name="Test getPopulationForContinent()", max_score=10)
    public void testPopulation() 
    {
    	try
    	{
        	COVIDDataAnalyzer uut = new COVIDDataAnalyzer();
        	List<COVIDData> data = uut.readFile(FILENAME);
        	Map<String, Long> CONTINENT_POPULATION = Map.ofEntries(
        		Map.entry("Asia",    4542059903L), 
        		Map.entry("Oceania", 40438886L),
        		Map.entry("America", 1013601796L),
        		Map.entry("Africa",  1306903030L), 
				Map.entry("Europe",  766212338L));
        	for(Map.Entry<String, Long> entry : CONTINENT_POPULATION.entrySet())
        	{
            	Long population = uut.getPopulationForContinent(entry.getKey());
        		assertEquals("Population of " + entry.getKey() + " does not match",
        					 entry.getValue(), population);
        	}
    	} catch (Exception e)
    	{
    		e.printStackTrace();
    		fail("Exception while testing readFile() " + e.getLocalizedMessage());
    	}
    }
    
    @Test		
    @GradedTest(name="Test getCountriesOnContinent()", max_score=10)
    public void testCountries() 
    {
    	try
    	{
        	COVIDDataAnalyzer uut = new COVIDDataAnalyzer();
        	List<COVIDData> data = uut.readFile(FILENAME);
        	Map<String, Integer> CONTINENT_COUNTRIES = Map.ofEntries(
        		Map.entry("Asia",    43), 
        		Map.entry("Oceania", 8),
        		Map.entry("America", 49),
        		Map.entry("Africa",  55), 
				Map.entry("Europe",  54));
        	for(Map.Entry<String, Integer> entry : CONTINENT_COUNTRIES.entrySet())
        	{
        		Collection<String> countries = uut.getCountriesOnContinent(entry.getKey());
        		assertNotNull("Countries was null for " + entry.getKey(), countries);
        		assertEquals("Number of countries in " + entry.getKey() + " does not match",
        					 entry.getValue().intValue(), countries.size());
        	}
    	} catch (Exception e)
    	{
    		e.printStackTrace();
    		fail("Exception while testing readFile() " + e.getLocalizedMessage());
    	}
    }

	private static final String CODE_FILE= "src/covid/COVIDDataAnalyzer";
	@BeforeClass
	public static void grabCode()
	{
		LoggedTest.grabCode(CODE_FILE);
	}
}