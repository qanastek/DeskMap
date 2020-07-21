package fr.univavignon.ceri.deskmap.models.Modes;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.models.Node;

/**
 * @author Yanis Labrak
 */
@XmlRootElement(name = "public_transport")
public class PublicTransport {
	
	/**
	 * Name of the line
	 */
	@XmlAttribute(name = "name_line")
	private String nameLine;
	
	/**
	 * Number of line
	 * <br>
	 * For example: 02
	 */
	@XmlAttribute(name = "nbr_line")
	private String nbrLine;
	
	/**
	 * Type of transport
	 */
	@XmlAttribute(name = "type")
	private String type;
	
	/**
	 * Destination
	 */
	@XmlAttribute(name = "destination")
	private String destination;
	
	/**
	 * Coordinates of the vehicle
	 */
	@XmlAttribute(name = "coordinates")
	private Node coordinates;

	/**
	 * Constructor
	 * @param nameLine {@code String}
	 * @param nbrLine {@code String}
	 * @param type {@code String}
	 * @param destination {@code String}
	 * @param x {@code Double}
	 * @param y {@code Double}
	 */
	public PublicTransport(String nameLine, String nbrLine, String type, String destination, Double x, Double y) {
		this.nameLine = nameLine;
		this.nbrLine = nbrLine;
		this.type = type;
		this.destination = destination;
		this.coordinates = new Node(y, x);
	}

	/**
	 * @return the nameLine
	 */
	public String getNameLine() {
		return this.nameLine;
	}

	/**
	 * @param nameLine the nameLine to set
	 */
	public void setNameLine(String nameLine) {
		this.nameLine = nameLine;
	}

	/**
	 * @return the nbrLine
	 */
	public String getNumber() {
		return this.nbrLine;
	}

	/**
	 * @param nbrLine the nbrLine to set
	 */
	public void setNbrLine(String nbrLine) {
		this.nbrLine = nbrLine;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {
		return this.destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * @return the x
	 */
	public Double getX() {
		return this.coordinates.lon;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(Double x) {
		this.coordinates.lon = x;
	}

	/**
	 * @return the y
	 */
	public Double getY() {
		return this.coordinates.lat;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(Double y) {
		this.coordinates.lat = y;
	}

	/**
	 * @return the coordinates
	 */
	public Node getCoordinates() {
		return this.coordinates;
	}

	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(Node coordinates) {
		this.coordinates = coordinates;
	}
	
	@Override
	public String toString() {
		return this.nbrLine;
	}

}
