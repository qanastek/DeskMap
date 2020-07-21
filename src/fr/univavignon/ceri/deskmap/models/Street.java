package fr.univavignon.ceri.deskmap.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A class which represent the structure of an OSM Street entity
 * @author Yanis Labrak
 */
@XmlRootElement(name = "Street")
public class Street {
	
	/**
	 * Identifier of the street node {@code Long}
	 */
	@XmlAttribute(name = "id")
	public Long id;
	
	/**
	 * Name of the street {@code String}
	 */
	@XmlAttribute(name = "name")
	public String name;
	
	/**
	 * Latitude of the street {@code Double}
	 */
	@XmlAttribute(name = "lat")
	public Double lat;

	/**
	 * Longitude of the street {@code Double}
	 */
	@XmlAttribute(name = "lon")
	public Double lon;
	
	/**
	 * @param id {@code String} Identifier
	 * @param name {@code String} name
	 * @author Yanis Labrak
	 */
	public Street(String id, String name) {
		this.id = Long.parseLong(id);
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}