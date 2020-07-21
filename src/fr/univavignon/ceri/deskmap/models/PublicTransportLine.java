/**
 * 
 */
package fr.univavignon.ceri.deskmap.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.models.line.PublicTransportRoute;

/**
 * @author Yanis Labrak
 *
 */
@XmlRootElement(name = "public_transport_line")
public class PublicTransportLine {
	
	/**
	 * Route name
	 */
	@XmlAttribute(name = "header")
	private String header = "";
	
	/**
	 * Departure
	 */
	@XmlAttribute(name = "from")
	private String from = "";
	
	/**
	 * Arrival
	 */
	@XmlAttribute(name = "to")
	private String to = "";
	
	/**
	 * Route type
	 */
	@XmlAttribute(name = "route")
	private String route = "";

	/**
	 * The route number
	 */
	@XmlAttribute(name = "number")
	private String number = "";
	
	/**
	 * Segments of the line
	 */
	@XmlAttribute(name = "routes")
	public List<PublicTransportRoute> routes = new ArrayList<PublicTransportRoute>();
	
	/**
	 * Constructor empty
	 */
	public PublicTransportLine() {
		super();
	}
	
	/**
	 * Constructor number
	 * @param number {@code String} The number of the line
	 */
	public PublicTransportLine(String number) {
		this.setNumber(number);
	}
	
	/**
	 * @return the name
	 */
	public String getHeader() {
		return this.header;
	}

	/**
	 * @param name the name to set
	 */
	public void setHeader(String name) {
		this.header = name;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return this.from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return this.to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the route
	 */
	public String getRoute() {
		return this.route;
	}

	/**
	 * @param route the route to set
	 */
	public void setRoute(String route) {
		this.route = route;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return this.number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	
	@Override
	public String toString() {
		return "Number: " + getNumber();
	}
	
	/**
	 * Return the node thanks to this identifier
	 * @param id {@code Long}
	 * @return {@code Node}
	 */
	public Node getNodeById(Long id) {
		
		// For each segments
		for (PublicTransportRoute route : this.routes) {
			
			Node n = route.getNodeById(id);
			
			// If found
			if (n != null) {
				return n;
			}
		}
		
		return null;
	}
	
	/**
	 * Return the node thanks to this identifier
	 * @param id {@code Long}
	 * @return {@code Integer}
	 */
	public int getNodeIndex(Long id) {
		

		// For each segments
		for (PublicTransportRoute route : this.routes) {
			
			int n = route.getNodeIndex(id);

			// If found
			if (n != -1) {
				return n;
			}
		}
		
		return -1;
	}

}
