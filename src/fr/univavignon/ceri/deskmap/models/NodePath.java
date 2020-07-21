package fr.univavignon.ceri.deskmap.models;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.Map;
import fr.univavignon.ceri.deskmap.config.Speeds;
import fr.univavignon.ceri.deskmap.models.line.Road;
import fr.univavignon.ceri.deskmap.services.AStar;

/**
 * A class which represent the structure of an OSM Node for the path
 * @author Zihao Zheng
 * @author Mohamed Ben Yamna
 * @author Reworked by Zihao Zheng
 */
@XmlRootElement(name = "node_path")
public class NodePath extends Node implements Serializable {
		
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2314810467256930721L;

	/**
	 * Distance from the last {@code Node} to this one
	 */
	public Double distance = 0.0;
	
	/**
	 * The total distance
	 */
	public Double totalDistance = 0.0;
	
	/**
	 * Time from the last {@code Node} to this one in seconds
	 */
	public Double duration = 0.0;
	
	/**
	 * The total duration
	 */
	public Double totalDuration = 0.0;
	
	/**
	 * Street name
	 */
	public String street = "";
	
	/**
	 * The path to the node
	 */
	public Path path = new Path();
	
	/**
	 * Empty constructor
	 */
	public NodePath() {
		super();
	}

	/**
	 * Constructor
	 * @param n {@code Node} Base {@code Node}
	 * @author Zihao Zheng
	 * @author Mohamed Ben Yamna
	 */
	public NodePath(Node n) {
		super(n);
	}
	
	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param lat {@code Double} Latitude
	 * @param lon {@code Double} Longitude
	 * @param distance {@code Double} Distance from the last {@code Node} to this one
	 * @param parent {@code NodePath} Parent
	 * @author Zihao Zheng
	 * @author Mohamed Ben Yamna
	 */
	public NodePath(Long id, Double lat, Double lon, Double distance, NodePath parent) {
		super(id, lat, lon);
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.distance = distance;
	}

	/**
	 * Set the street name of the {@code NodePath}
	 */
	public void setStreet() {
		
		// Get the street name of the node
		for (Road r : Map.ROADS) {
			
			// If this Road contain the Node
			if (r.getNodes().contains(this.id)) {
				
				// Get this name
				this.street = r.name;
				break;
			}
		}
		
	}

	/**
	 * Get the distance in meters
	 * @return {@code Double} The distance
	 * @author Zihao Zheng
	 * @author Mohamed Ben Yamna
	 * @author Yanis Labrak
	 */
	public Double getDistanceInMeters() {
		return Math.floor((this.distance * 84600) * 100) / 100;
	}
	
	/**
	 * Get the distance in meters
	 * @return {@code Double} The distance
	 * @param n {@code Node}
	 * @param n1 {@code Node}
	 * @author Zihao Zheng
	 * @author Mohamed Ben Yamna
	 * @author Yanis Labrak
	 */
	public static Double getDistanceInMeters(Node n, Node n1) {
		double distance = AStar.distance(n,n1);
		return Math.floor((distance * 84600) * 100) / 100;
	}
	
	@Override
	public String toString() {
		return "[Id: " + this.id + ", Lat: " + this.lat + ", Lon: " + this.lon + ", Distance: " + this.distance + "]";
	}
	
	/**
	 * Time to cross the road up to the next road
	 * @return {@code Double}
	 */
	public double getTime() {
		
		// Speed in km/h
		int speed = Speeds.getSpeed(this);
		
		// Time in seconds
		double secondsTotal = ((this.distance / speed) * 60 * 60);
		
		return secondsTotal;
	}

	/**
	 * @return the distance
	 */
	public Double getDistance() {
		return this.distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(Double distance) {
		this.distance = distance;
	}

	/**
	 * @return the totalDistance
	 */
	public Double getTotalDistance() {
		return this.totalDistance;
	}

	/**
	 * @param totalDistance the totalDistance to set
	 */
	public void setTotalDistance(Double totalDistance) {
		this.totalDistance = totalDistance;
	}

	/**
	 * @return the duration
	 */
	public Double getDuration() {
		return this.duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Double duration) {
		this.duration = duration;
	}

	/**
	 * @return the totalDuration
	 */
	public Double getTotalDuration() {
		return this.totalDuration;
	}

	/**
	 * @param totalDuration the totalDuration to set
	 */
	public void setTotalDuration(Double totalDuration) {
		this.totalDuration = totalDuration;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return this.street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the path
	 */
	public Path getPath() {
		return this.path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(Path path) {
		this.path = path;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}