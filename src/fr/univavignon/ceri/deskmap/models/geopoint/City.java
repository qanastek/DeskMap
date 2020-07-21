package fr.univavignon.ceri.deskmap.models.geopoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.controllers.MainViewController;
import fr.univavignon.ceri.deskmap.models.Node;

/**
 * A class which represent the structure of an OSM City entity
 * @author Mohamed BEN YAMNA
 */
@XmlRootElement(name = "city")
@XmlAccessorType(XmlAccessType.FIELD)
public class City extends GeoPoint implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 558954182673294802L;

	/**
	 * Country of the city
	 */
	public String country;
	
	/**
	 * State of the city
	 */
	public String state;
	
	/**
	 * Empty constructor
	 */
	public City() {
		super();
	}
	
	/**
	 * Main constructor
	 * @param id {@code String} Identifier
	 * @param lat {@code Double} Latitude
	 * @param lon {@code Double} Longitude
	 * @param name {@code String} Name of the city
	 * @author Mohamed BEN YAMNA
	 */
	public City(String id, Double lat, Double lon, String name) {
		super(id, name,new Node(Long.parseLong(id),lat,lon));
	}
	
	/**
	 * Main constructor
	 * @param id {@code String} Identifier
	 * @param lat {@code Double} Latitude
	 * @param lon {@code Double} Longitude
	 * @param name {@code String} Name of the city
	 * @param country {@code String} name of the country
	 * @param state {@code String} name of the state
	 * @author Yanis Labrak
	 */
	public City(String id, Double lat, Double lon, String name, String country, String state) {
		super(id, name,new Node(Long.parseLong(id),lat,lon));
		this.country = country;
		this.state = state;
	}
	
	/**
	 * Copy constructor
	 * @param city {@code City} Another City object
	 * @author Mohamed BEN YAMNA
	 */
	public City(City city) {
		super(city.id, city.name,city.point);
	}
	
	/**
	 * Check if the city is a real one.
	 * @param cityFrom {@code City} The city
	 * @return {@code City} if found else {@code null}
	 * @author Yanis Labrak
	 */
	public static City isInListCity(City cityFrom) {
		
		// For each cities
		for (City city : MainViewController.LIST_CITY) {
					
			if (!city.name.isEmpty() && city.name.toLowerCase().equals(cityFrom.getName().toLowerCase())) {
				System.out.println("Inside: " + city.name.toLowerCase());
				return city;
			}
		}
		return null;
		
	}
	
	/**
	 * Return the coordinates of a city
	 * @param city {@code String} Name of the city
	 * @return {@code String} Coordinates
	 * @author Yanis Labrak
	 */
	public static String getCityCoordinates(String city) {
		try {
			
			// Open a stream for the file which contain all the streets
			BufferedReader bfr = new BufferedReader(new FileReader("cities.csv"));
			
			String line;
			
			// While the file have lines
			while ((line = bfr.readLine()) != null) {
				
				String[] values = line.split("\\|");
		        
		        // The City need to be fully complete to be processed
		        if (values.length == 4 &&
		        	!values[0].isEmpty() &&
		        	!values[1].isEmpty() &&
		        	!values[2].isEmpty() &&
		        	!values[3].isEmpty()) {
					
		        	// When we found the city
					if (values[3].toLowerCase().equals(city.toLowerCase())) {
						bfr.close();
						return values[1] + "|" + values[2];
						
					}
				}		        
			}
			
			bfr.close();
			
			return null;
			
		} catch(Exception e) {
			return null;
		}		
	}

	@Override
	public String toString() {
		return this.name + ", " + this.state + ", " + this.country;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return this.state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
