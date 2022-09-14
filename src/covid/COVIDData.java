package covid;

import java.util.Date;

/**
 * @author Tony
 */
public class COVIDData
{
	private Date day;
	private long cases;
	private long deaths;
	private String country;
	private String continent;
	private long population;

	public COVIDData(Date day, long cases, long deaths, String country, long population, String continent)
	{
		super();
		this.day = day;
		this.cases = cases;
		this.deaths = deaths;
		this.country = country;
		this.population = population;
		this.continent = continent;
	}

	public Date getDay()
	{
		return day;
	}

	public long getCases()
	{
		return cases;
	}

	public long getDeaths()
	{
		return deaths;
	}

	public String getCountry()
	{
		return country;
	}

	public long getPopulation()
	{
		return population;
	}

	public String getContinent()
	{
		return continent;
	}

	@Override
	public String toString()
	{
		return "COVIDData [day=" + day + ", cases=" + cases + ", deaths=" + deaths + ", country=" + country
				+ ", continent=" + continent + ", population=" + population + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (cases ^ (cases >>> 32));
		result = prime * result + ((continent == null) ? 0 : continent.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result + (int) (deaths ^ (deaths >>> 32));
		result = prime * result + (int) (population ^ (population >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		COVIDData other = (COVIDData) obj;
		if (cases != other.cases)
			return false;
		if (continent == null)
		{
			if (other.continent != null)
				return false;
		} else if (!continent.equals(other.continent))
			return false;
		if (country == null)
		{
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (day == null)
		{
			if (other.day != null)
				return false;
		} else if (!this.day.toString().equals(other.day.toString()))
			return false;
		if (deaths != other.deaths)
			return false;
		if (population != other.population)
			return false;
		return true;
	}
}
