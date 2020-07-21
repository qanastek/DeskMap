package fr.univavignon.ceri.deskmap.models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A GeoData is something which will be displayed like a {@code Region}, a point of interest or {@code Line} (road, building, etc ...)
 * @author Mohamed BEN YAMNA
 */
@XmlRootElement(name = "geodata")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class GeoData implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 889993234953764801L;

	/**
	 * The identifier of the data
	 */
	public Long id;

	/**
	 * The name of the data
	 */
	public String name;
	
	/**
	 * Empty constructor
	 */
	public GeoData() {
		super();
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 * @param name Name of the data
	 */
	public GeoData(Long id,String name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name Name of the data
	 */
	public GeoData(String id,String name) {
		this.id = Long.parseLong(id);
		this.name = name;
	}
	
	/**
	 * Getter for the identifier
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Setter for the identifier
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Getter for the name
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter for the name
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}