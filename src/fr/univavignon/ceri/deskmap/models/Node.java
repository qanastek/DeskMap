package fr.univavignon.ceri.deskmap.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.Map;
import fr.univavignon.ceri.deskmap.controllers.MainViewController;
import fr.univavignon.ceri.deskmap.models.geopoint.BusStation;
import fr.univavignon.ceri.deskmap.models.line.PublicTransportRoute;
import fr.univavignon.ceri.deskmap.models.line.Road;
import fr.univavignon.ceri.deskmap.services.AStar;

/**
 * A class which represent the structure of an OSM Node
 * @author Mohamed BEN YAMNA
 */
@XmlRootElement(name = "node")
//@XmlAccessorType(XmlAccessType.FIELD)
public class Node implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 7108704932790557261L;

	/**
	 * {@code Long} Identifier for the node
	 */
    @XmlAttribute(name = "id")
	public Long id = 0L;
	
	/**
	 * {@code Double} Earth latitude coordinate
	 * <br>
	 * Aka y
	 */
	public Double lat = 0.0;
	
	/**
	 * {@code Double} Earth longitude coordinate
	 * <br>
	 * Aka x
	 */
	public Double lon = 0.0;
	
	/**
	 * {@code Boolean} Does it is a bus station
	 */
	public Boolean busStation = false;
	
	/**
	 * All the {@code Road}s linked to the {@code Node}
	 */
	public List<Road> roads = new ArrayList<Road>();
	
	/**
	 * All the public transport roads linked to the {@code Node}
	 */
	public List<PublicTransportRoute> publicTransportRoutes = new ArrayList<PublicTransportRoute>();
	
	/**
	 * Empty constructor
	 */
	public Node() {
		super();
	}
	
	/**
	 * Constructor from another node
	 * @param node {@code Node}
	 */
	public Node(Node node) {
		this.id = node.id;
		this.lat = node.lat;
		this.lon = node.lon;
		this.busStation = node.busStation;
		this.roads = node.roads;			
	}
	
	/**
	 * Constructor from a bus station
	 * @param station {@code BusStation}
	 */
	public Node(BusStation station) {
		this.id = station.id;
		this.lat = station.lat;
		this.lon = station.lon;
		this.busStation = station.busStation;
		this.roads = station.roads;			
	}
	
	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param lat {@code Double} Latitude
	 * @param lon {@code Double} Longitude
	 */
	public Node(String id, Double lat, Double lon) {
		
		// If the identifier isn't empty
		if (id != null ) {
			this.id = Long.parseLong(id);
		}
		
		this.lat = lat;
		this.lon = lon;
	}
	
	/**
	 * Constructor
	 * @param lat {@code Double} Latitude
	 * @param lon {@code Double} Longitude
	 */
	public Node(Double lat, Double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param lat {@code Double} Latitude
	 * @param lon {@code Double} Longitude
	 */
	public Node(Long id, Double lat, Double lon) {
		this.id = id;
		this.lat = lat;
		this.lon = lon;
	}
	
	/**
	 * Return the node after conversion
	 * @param node {@code Node} The {@code Node} which we want to convert
	 * @return {@code Node} The {@code Node} after conversion
	 */
	public static Node toPixel(Node node) {
		
		// If we have a empty node
		if (node == null) {
			node = new Node();
		}
		
		List<Double> coordinates = Node.toPixel(node.lat, node.lon);
		
		if (coordinates != null && coordinates.size() > 0) {
			node.lon = coordinates.get(0);
			node.lat = coordinates.get(1);			
		}
		
		return node;	
	}
	
	/**
	 * Transform the geographical coordinates into canvas coordinates
	 * @param lat {@code Double} x
	 * @param lon {@code Double} y
	 * @return Return the {@code x} and {@code y} of the {@code Node} converted into pixels 
	 * @author Philippe Beraud - Writer of the article on what I base my algorithm
	 * @author Mohamed BEN YAMNA - Adaptation of the formula
	 * @see <a href="https://blogs.msdn.microsoft.com/ogdifrance/2011/07/13/de-la-go-et-des-maths/?fbclid=IwAR3efsf9pp87SdKcxNy71T79GPfu7wcxwE-2JhpUWKYOhxW91f38fa_CynY">blogs.msdn.microsoft.com</a>
	 */
	public static List<Double> toPixel(Double lat, Double lon) {
		
		// Get the value of a degres of latitude into meters
		Double latMeters = lon / 111110;
		
		// Get the value of a degres of longitude into meters
		Double lonMeters = 111110 * Math.cos(latMeters);

		// Get the width in meters
		Double bboxWidth = Map.RIGHT * lonMeters - Map.LEFT * lonMeters;
		// Get the height in meters
		Double bboxHeight = Map.TOP * latMeters - Map.BOTTOM * latMeters;
		
		// Get the width in pixel
		Double ratioWidthPixel = Map.WIDTH / bboxWidth * Map.SCALE;
		// Get the height in pixel
		Double ratioHeightPixel = Map.HEIGHT / bboxHeight * Map.SCALE;
		
		// Set the map scale value on the screen
		Map.SCALE_METER.set((int) (50 * ratioWidthPixel));
		
		// Latitude
		Double posVertical = (lat - Map.BOTTOM) * latMeters;
		// Longitude
		Double posHorizontal = (lon - Map.LEFT) * lonMeters;
		
		// Move the node of X in latitude
		posVertical += Map.LATITUDE;
		// Move the node of X in longitude
		posHorizontal += Map.LONGITUDE;
		
		// Latitude in pixel for the canvas
		lat = posVertical * ratioHeightPixel;
		// Longitude in pixel for the canvas
		lon = posHorizontal * ratioWidthPixel;
		
		return Arrays.asList(lon, lat);
	}
		
	@Override
	public String toString() {
		return "[Id: " + this.id + ", Lat: " + this.lat + ", Lon: " + this.lon + "]";
	}

	/**
	 * Check If the {@code Node} is inside the {@code Bbox} area.
	 * @param bbox {@code Bbox} The area were the {@code Node} need to be
	 * @return
	 * {@code True} If inside the {@code Bbox}
	 * <br>
	 * {@code False} If not inside the {@code bbox}
	 */
	public boolean in(Bbox bbox) {
		
		// If empty bbox
		if (bbox == null) {
			return true;
		}
		
		if (bbox.topLeft < this.lon && this.lon < bbox.topRight) {
			
			if (bbox.bottomLeft < this.lat && this.lat < bbox.topLeft) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Return If the node is inside a {@code Road} or not.
	 * @return 
	 * {@code True} If is a {@code Node} inside a {@code Road} 
	 * <br>
	 * {@code False} If is not a {@code Node} inside a {@code Road} 
	 */
	public boolean isRoad() {
		
		// If empty
		if (Map.ROADS == null || Map.ROADS.size() <= 0) {
			return false;
		}
		
		// For each attached street of the node
		for (Road r : Map.ROADS) {
			
			// If empty
			if (r == null || r.getNodes() == null || r.getNodes().size() <= 0) {
				continue;
			}
				
			// If this Road contain the Node
			if (r.getNodes().contains(this.id)) {
				
				// Get this name
				return true;
			}
		}
		
		return false;
		
	}

	/**
	 * Add a {@code Road} to the roads list
	 * @param road {@code Road}
	 */
	public void addRoad(Road road) {
		
		// If not null
		if (road != null) {
			this.roads.add(road);			
		}
	}
	
	/**
	 * Return all the roads of this node
	 * @return {@code List<Road>} The roads
	 */
	public List<Road> getRoads() {		
		return this.roads;
		
	}
	
	/**
	 * Return all the roads of this node with names
	 * @return {@code List<Road>} The roads
	 * @author Yanis Labrak
	 */
	public List<Road> getRoadsWithNames() {
		
		List<Road> roadEntities = new ArrayList<Road>();
		
		for (Road r : this.roads) {
			
			// If the road exist
			if (r != null) {	
				
				// And have a name
				if (r.getName() != null && !r.getName().isEmpty()) {					
					roadEntities.add(r);
				}			
			}
		}
		
		return roadEntities;
	}
	
	/**
	 * Return the first road
	 * @return {@code Road}
	 */
	public Road getFirstRoad() {
		
		List<Road> roadsArround = this.getRoads();
		
		// If empty
		if (roadsArround == null || roadsArround.size() <= 0) {
			return new Road();
		}
		
		return this.getRoads().get(0);
	}
	
	/**
	 * Return the neighbors of the {@code Node}
	 * @return {@code ArrayList<Node>} Neighbors of the node
	 * @author Implement Zihao Zheng
	 * @author Modify Yanis Labrak
	 */
	public ArrayList<NodePath> getNeigthboors() {

		ArrayList<NodePath> around = new ArrayList<NodePath>();

		// If empty
		if (this.roads == null || this.roads.size() < 0) {
			return around;
		}
		
		// For each road were the node appear
		for (Road r : this.roads) {

			// If empty or not allowed to go on
			if (r == null || r.getNodes().size() <= 0 || r.isAllowed() == false) {
				continue;
			}

//			System.out.println("r name: " + r.name);
							
			// Current node index inside the road
			int currentIndexInNeigthboors = r.getNodes().indexOf(this.id);
			int index = currentIndexInNeigthboors >= 0 ? currentIndexInNeigthboors : 0;
			
			// Last index
			int max = r.getNodes().size() - 1;

			// Index of the node before
			int before = index - 1 >= 0 ? index - 1 : 0;
			// Index of the node after
			int after = index + 1 <= max ? index + 1 : max;		

			// Before & After nodes identifier
			Long beforeNodeId = r.getNodes().get(before);
			Long afterNodeId = r.getNodes().get(after);
			
			/**
			 * If this is a one way road and the sense is taken in consideration and we are in the wrong way
			 * @author Mohamed Ben Yamna
			 */
			if (MainViewController.SENS_ENABLED == true &&
				r.isOneWay() == true &&
				r.goodWay(this.id, afterNodeId) == false
			) {
//				System.out.println("Illegal !");
				continue;
			}

			// Both nodes
			NodePath b = new NodePath(Map.NODES.get(beforeNodeId));
			NodePath a = new NodePath(Map.NODES.get(afterNodeId));
			
			// Add names
			if (r.name != null) {
				b.street = r.name;
				a.street = r.name;				
			}
			
			// Add them to neighbors
			if (before != index) {
				around.add(b);					
			}
			
			around.add(a);	
		}
		
		return around;
	}
	
	/**
	 * Add a public transport route to the {@code Node}
	 * @param road {@code Road}
	 */
	public void addPublicTransportRoute(PublicTransportRoute road) {
		
		// Check if isn't null and if not already present in the list
		if (road != null && !this.publicTransportRoutes.stream().filter(line -> line.getNumber().equals(road.getNumber())).findFirst().isPresent()) {
			this.publicTransportRoutes.add(road);			
		}		
	}	

	/**
	 * Return the closest bus station of the Node
	 * @return  {@code BusStation}
	 */
	public BusStation getClosestBusStation() {
		
		BusStation closest = new BusStation();
		double closestDistance = Double.MAX_VALUE;
		double distance = 0;
		
		for (BusStation station : Map.BUS_STATIONS_PATH) {
			
			distance = AStar.distance(this, station);
			
			if (distance < closestDistance) {
				closestDistance = distance;
				closest = station;
			}
		}
		
		return closest;		
	}
	
	/**
	 * Return the next neighbor
	 * @param line {@code PublicTransportRoute} Lines of the bus station
	 * @param visited {@code List<Node>} Visited nodes
	 * @param arrival {@code BusStation} Arrival
	 * @return {@code Node} The closest neighbor
	 */
	public Node getNextNodePublicTransport(PublicTransportRoute line, List<Node> visited, BusStation arrival) {
		
		Node bestNode = null; // Best node
		double bestDistance = Double.MAX_VALUE; // Best distance
		
		int index = -1;
		
		// Get the first index 
		for (PublicTransportRoute publicTransportRoute : this.publicTransportRoutes) {
						
			// Current index
			index = publicTransportRoute.getNodeIndex(this.id);
			
			// If not find
			if (index == -1) { break; }
			
			// Number of nodes
			int maxIndex = publicTransportRoute.getNodesInstances().size() - 1;
			
			// Nodes before/after
			int before = index-1 >= 0 ? index-1 : 0;
			int after = index+1 <= maxIndex ? index+1 : maxIndex;
			
			// Nodes instances
			final Node beforeNode = publicTransportRoute.getNodesInstances().get(before);
			final Node afterNode = publicTransportRoute.getNodesInstances().get(after);
			
			// Distances
			double distanceBefore = arrival.distance(beforeNode);
			double distanceAfter = arrival.distance(afterNode);
						
			if (beforeNode != null && beforeNode.id != null && distanceBefore < bestDistance && !visited.stream().filter(n -> n.id.equals(beforeNode.id)).findFirst().isPresent()) {
//				System.out.println("Current bestDistance => " + bestDistance);
//				System.out.println("Better before: " + beforeNode + ", with distance => " + distanceBefore);
				if (!beforeNode.isPartOfAPublicTransportLine(line)) { continue; }
				bestNode = beforeNode;
				bestDistance = distanceBefore;
			}
			else if (afterNode != null && afterNode.id != null && distanceAfter < bestDistance && !visited.stream().filter(n -> n.id.equals(afterNode.id)).findFirst().isPresent()) {
//				System.out.println("Current bestDistance => " + bestDistance);
//				System.out.println("Better after: " + afterNode + ", with distance => " + distanceAfter);
				if (!afterNode.isPartOfAPublicTransportLine(line)) { continue; }
				bestNode = afterNode;
				bestDistance = distanceAfter;
			}
			
			// Visited nodes
			visited.add(beforeNode);
			visited.add(afterNode);
		}
		
		return bestNode;
	}
	
	/**
	 * Return if the node is inside a public transport line
	 * @param line {@code PublicTransportRoute} The line we want to be part of
	 * @return {@code boolean}
	 */
	private boolean isPartOfAPublicTransportLine(PublicTransportRoute line) {
		
		for (PublicTransportRoute route : Map.PUBLIC_TRANSPORT_ROUTES) {
			
			if (route.getNumber().equals(line.getNumber()) && route.getNodes().stream().filter(r -> r.equals(this.id)).findFirst().isPresent()) {
				return true;
			}			
		}
		
		return false;
	}

	/**
	 * Return the distance
	 * @param node {@code Node} The next node
	 * @return {@code Double} The distance in meters
	 */
	public double distance(Node node) {
		return NodePath.getDistanceInMeters(this, node);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the lat
	 */
	public Double getLat() {
		return this.lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lon
	 */
	public Double getLon() {
		return this.lon;
	}

	/**
	 * @param lon the lon to set
	 */
	public void setLon(Double lon) {
		this.lon = lon;
	}

	/**
	 * @return the busStation
	 */
	public Boolean getBusStation() {
		return this.busStation;
	}

	/**
	 * @param busStation the busStation to set
	 */
	public void setBusStation(Boolean busStation) {
		this.busStation = busStation;
	}

	/**
	 * @return the publicTransportRoutes
	 */
	public List<PublicTransportRoute> getPublicTransportRoutes() {
		return this.publicTransportRoutes;
	}

	/**
	 * @param publicTransportRoutes the publicTransportRoutes to set
	 */
	public void setPublicTransportRoutes(List<PublicTransportRoute> publicTransportRoutes) {
		this.publicTransportRoutes = publicTransportRoutes;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @param roads the roads to set
	 */
	public void setRoads(List<Road> roads) {
		this.roads = roads;
	}
}