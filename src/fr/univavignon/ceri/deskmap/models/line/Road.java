package fr.univavignon.ceri.deskmap.models.line;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.Map;
import fr.univavignon.ceri.deskmap.config.Speeds;
import fr.univavignon.ceri.deskmap.controllers.MainViewController;
import fr.univavignon.ceri.deskmap.models.Node;

/**
 * @author Mohamed Ben Yamna
 */
@XmlRootElement(name = "road")
public class Road extends Line implements Highway, Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -3726009461112019849L;

	/**
	 * Type of the road (Primary, Secondary, Trunk ...)
	 */
	private String type = "";
	
	/**
	 * Does it's a junction
	 */
	public String junction = "";
	
	/**
	 * Does it's a one way road
	 */
	private boolean oneWay = false;
	
	/**
	 * Max speed
	 */
	private int maxSpeed = 0;
	
	/**
	 * Allowed transport mode
	 */
	public List<String> allowed = new ArrayList<String>();
	
	/**
	 * Empty constructor
	 */
	public Road() {
		super();
	}

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the road
	 * @param thickness {@code Integer} Thickness of the road line
	 */
	public Road(String id, String name, Integer thickness) {
		super(id, name, thickness, "#ffffff");
	}
	
	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param thickness {@code Integer} Thickness of the road line
	 */
	public Road(String id, Integer thickness) {
		super(id, "", thickness, "#ffffff");
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 * @param thickness {@code Integer} Thickness of the road line
	 */
	public Road(Long id, Integer thickness) {
		super(id, "", thickness, "#ffffff");
	}
	
	/**
	 * Constructor identifier only
	 * @param id {@code Long} Identifier
	 */
	public Road(Long id) {
		super(id, "", 1, "#ffffff");
	}
	
	/**
	 * Constructor identifier only
	 * @param id {@code String} Identifier
	 */
	public Road(String id) {
		super(id, "", 1, "#ffffff");
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	/**
	 * Return the center of the {@code Road}
	 * @return {@code Long} The {@code Node} identifier
	 */
	public Node getMiddle() {
		int index = this.nodes.size() <= 2 ? 0 : this.nodes.size() / 2; 
		return Map.NODES.get(this.nodes.get(index));
	}

	/**
	 * Return the angle of the {@code Road}
	 * @return {@code Integer} The angle
	 */
	public Double getAngle() {
		
		Long startId = this.nodes.get(0);
		Long endId = this.nodes.get(this.nodes.size() - 1);
		
		Node start = Map.NODES.get(startId);
		Node end = Map.NODES.get(endId);
		
		Double ex = start.lat;
		Double ey = start.lon;
		
		Double cx = end.lat;
		Double cy = end.lon;

		Double dy = ey - cy;
		Double dx = ex - cx;
		
		Double theta = Math.atan2(dy, dx);
		theta *= 180 / Math.PI;
		
		if (theta < 0) theta = 360 + theta;
		
		return theta;
	}

	/**
	 * Setter for junction
	 * @param junction {@code String}
	 */
	public void setJunction(String junction) {
		this.junction = junction;
	}

	/**
	 * Getter junction
	 * @return {@code String} The junction
	 */
	public String getJunction() {
		return this.getJunction();
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
	 * @return the oneWay
	 */
	public boolean isOneWay() {
		return this.oneWay;
	}

	/**
	 * @param oneWay the oneWay to set
	 */
	public void setOneWay(boolean oneWay) {
		this.oneWay = oneWay;
	}

	/**
	 * Get the max speed of the {@code Road}
	 * @return the maxSpeed
	 */
	public int getMaxSpeed() {
		return this.maxSpeed;
	}

	/**
	 * @param maxSpeed the maxSpeed to set
	 */
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	/**
	 * Return the speed of the {@code Road}
	 * @return {@code Integer}
	 * @author Zihao Zheng
	 */
	public int getSpeed() {
		
		// If we have the real speed
		if (this.getMaxSpeed() > 0) {
			return this.getMaxSpeed();
		}
		
		// If to maxSpeed
		switch (this.type) {
		
			case "SERVICE":
				return Speeds.MOTORWAY_MAX_SPEED;
				
			case "MOTORWAY":
				return Speeds.MOTORWAY_MAX_SPEED;
				
			case "TRUNK":
				return Speeds.TRUNK_MAX_SPEED;
				
			case "PRIMARY":
				return Speeds.PRIMARY_MAX_SPEED;
				
			case "SECONDARY":
				return Speeds.SECONDARY_MAX_SPEED;
				
			case "TERTIARY":
				return Speeds.TERTIARY_MAX_SPEED;
				
			case "ROAD":
			default:
				return Speeds.ROAD_MAX_SPEED;
		}		
	}
	
	/**
	 * Return if we go in the right way
	 * @param from {@code Node}
	 * @param to {@code Node}
	 * @return {@code boolean}
	 * @author Mohamed Ben Yamna
	 */
	public boolean goodWay(Long from, Long to) {
		
		// If itself
		if (from == to) {
			return true;
		}
		
		int indexFrom = this.nodes.indexOf(from);
		int indexTo= this.nodes.indexOf(to);
				
		// Nice way
		if (indexFrom < indexTo) { return true; }
				
		return false;
	}
	
	/**
	 * Return all the nodes instances
	 * @return the nodes
	 */
	public List<Node> getNodesInstances() {
		
		// List of all the road nodes
		List<Node> nodesInstances = new ArrayList<Node>();
		
		// Current node
		Node current = null;
		
		// For each nodes of this road
		for (Long id : this.nodes) {
			
			// Get the real node
			current = Map.NODES.get(id);
			
			// Add It to the list
			nodesInstances.add(current);
		}
		
		// Return all the nodes instances of this road
		return nodesInstances;
	}
	
	/**
	 * Return if the current transport mode allow us to go on this {@code Road}
	 * @return {@code Boolean}
	 * <br>
	 * true => Allowed
	 * <br>
	 * false => Not allowed
	 */
	public boolean isAllowed() {
		
		String transportMode = MainViewController.getTransportMode();
		
		// If the type is contained in the allowed list
		if (this.allowed != null  &&
			this.allowed.size() > 0 &&
			transportMode != null
		) {
			return this.allowed.contains(transportMode);
		}
		
		return false;
	}

	/**
	 * @return the allowed
	 */
	public List<String> getAllowed() {
		return this.allowed;
	}

	/**
	 * @param allowed the allowed to set
	 */
	public void setAllowed(List<String> allowed) {
		this.allowed = allowed;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
