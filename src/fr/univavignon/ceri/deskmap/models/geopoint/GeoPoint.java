package fr.univavignon.ceri.deskmap.models.geopoint;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.models.GeoData;
import fr.univavignon.ceri.deskmap.models.Node;

/**
 * Point of interest like a {@code City} or a {@code Mountain}
 * @author Mohamed BEN YAMNA
 */
@XmlRootElement(name = "geopoint")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class GeoPoint extends GeoData implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2521897444081069253L;
	
	/**
	 * Node which represent the location
	 */
	public Node point;
	
	/**
	 * Empty constructor
	 */
	public GeoPoint() {
		super();
	}

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name Name of the place
	 * @param point The only {@code Node} of the place
	 */
	public GeoPoint(String id, String name, Node point) {
		super(Long.parseLong(id),name);
		this.point = point;
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 * @param name Name of the place
	 * @param point The only {@code Node} of the place
	 */
	public GeoPoint(Long id, String name, Node point) {
		super(id,name);
		this.point = point;
	}
	
	@Override
	public String toString() {
		return "[Id: " + this.point.id + ",Lat: " + this.point.lat + ",Lon: " + this.point.lon + ",Name: " + this.name + "]";
	}

	/**
	 * @return the point
	 */
	public Node getPoint() {
		return this.point;
	}

	/**
	 * @param point the point to set
	 */
	public void setPoint(Node point) {
		this.point = point;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
