/**
 * 
 */
package fr.univavignon.ceri.deskmap.models.line;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.Map;
import fr.univavignon.ceri.deskmap.models.Node;
import fr.univavignon.ceri.deskmap.models.geopoint.BusStation;
import javafx.util.Pair;

/**
 * @author Yanis Labrak
 *
 */
@XmlRootElement(name = "public_transport_route")
@XmlAccessorType(XmlAccessType.FIELD)
public class PublicTransportRoute extends Road implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 6306177393155102158L;

	/**
	 * Route name
	 */
	private String header;
	
	/**
	 * Departure
	 */
	private String from;
	
	/**
	 * Arrival
	 */
	private String to;
	
	/**
	 * Route type
	 */
	private String route;

	/**
	 * The route number
	 */
	private String number;
		
	/**
	 * Constructor empty
	 */
	public PublicTransportRoute() {
		super();
	}
	
	/**
	 * Constructor id
	 * @param id {@code Long} identifier
	 */
	public PublicTransportRoute(Long id) {
		super(id);
	}
	
	/**
	 * Constructor number
	 * @param number {@code String} The number of the line
	 */
	public PublicTransportRoute(String number) {
		super();
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
		
		// For each nodes of the route
		for (Node n : this.getNodesInstances()) {
			
			// If we find it
			if (n.id == id) {
				
				// Return it
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
		
		List<Node> nodes = this.getNodesInstances();
		
		// For each nodes of the route		
		for (int i = 0; i < nodes.size(); i++) {
			
			// If we find it
			if (nodes.get(i).id == id) {
				
				// Return it
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Return the closest bus station from the arrival
	 * @param currentArrival {@code Node} The arrival node
	 * @return {@code List<Object>} The closest bus station and it's distance
	 */
	public Pair<BusStation,Double> getClosestFromArrival(Node currentArrival) {
				
		// Distance and best distance
		double distance = Double.MAX_VALUE;
		
		// Best distance
		double bestDistance = Double.MAX_VALUE;

		// Closest bus station from the arrival and it's distance
		Pair<BusStation,Double> closest = null;
		
		/**
		 * For each bus station
		 */
		for (BusStation station : Map.BUS_STATIONS_PATH) {
			
			// If is crossed by the line
			if (station.containsLine(this.getNumber()) == true) {
				
				// Get the distance with the arrival
				distance = station.distance(currentArrival);
				
				// If is closer
				if (distance < bestDistance) {
					
					// Save the station
					closest = new Pair<BusStation,Double>(station, distance);
					
					// Change the best distance
					bestDistance = distance;
				}				
			}
		}
		
		return closest;
	}

	/**
	 * Distance in meters between two bus stations
	 * @param b1 {@code BusStation} First bus station
	 * @param b2 {@code BusStation} Second bus station
	 * @return {@code Double} The distance in meters
	 */
	public double getDistance(BusStation b1, BusStation b2) {
		return b1.distance(b2);
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/**
	 * Return the bus station ordered in the good order
	 * @param departure {@code BusStation} The departure bus station
	 * @param arrival {@code BusStation} The arrival bus station
	 * @param visited {@code List<BusStation>} List of already visited stations
	 * @return {@code PriorityQueue<BusStation>} The ordered bus stations of the line
	 */
	public List<BusStation> getBusStationsInFront(BusStation departure, BusStation arrival, List<BusStation> visited) {

		// List of the ordered stations in front of us
		List<BusStation> ordered = new ArrayList<BusStation>();
		
		// the current bus station
		BusStation current = departure;
		
		// Add it to the list
		ordered.add(current);
		
		while(current != arrival) {
			
			// Both closest station of the same line
			Pair<BusStation,BusStation> closest = this.getClosest(current, visited);
			
			// Check if empty
			if (closest == null) { return null; }
			
			// Set the stations as visited
			visited.add(closest.getKey());
			visited.add(closest.getValue());
			
			// Distance of both stations with the arrival
			double d1 = closest.getKey().distance(arrival);
			
			double d2 = closest.getValue().distance(arrival);
			
			// Which is the one is in front of us ?
			if (d1 < d2) {
				current = closest.getKey();
			} else {
				current = closest.getValue();
			}
			
			// Add it to the list
			ordered.add(current);
		}
				
		return ordered;		
	}

	/**
	 * Return the two closest bus stations of the same line
	 * @param current {@code BusStation} The current bus station
	 * @param visited {@code List<BusStation>} List of already visited stations
	 * @return {@code }
	 */
	private Pair<BusStation,BusStation> getClosest(BusStation current, List<BusStation> visited) {

		// Comparator
		Comparator<Pair<Double,BusStation>> comparator = new Comparator<Pair<Double,BusStation>>() {
            @Override
            public int compare(Pair<Double,BusStation> o1, Pair<Double,BusStation> o2) {
                return Double.compare(o1.getKey(), o2.getKey());
            }
        };
        
        // Ordered list of bus station of the same line
		PriorityQueue<Pair<Double,BusStation>> closest = new PriorityQueue<Pair<Double,BusStation>>(comparator);
        
        // For each stations
        for (BusStation station : Map.BUS_STATIONS_PATH) {
        	
        	boolean correctLine = station.containsLine(this.getNumber().toUpperCase());
        	boolean visitedBefore = visited.stream().filter(v -> v.isSame(station)).findFirst().isPresent();
        	
        	// If is from the same line
        	if (correctLine == true && visitedBefore == false) {
        		
        		// Add it
            	closest.add(new Pair<Double,BusStation>(current.distance(station),station));
			}
        }
        
        // Check if not empty
        if (closest.size() >= 2) {
            
        	// Take the two best elements
            BusStation b1 = closest.poll().getValue();
            BusStation b2 = closest.poll().getValue();	
            
            // Add them to an pair
            Pair<BusStation, BusStation> res = new Pair<BusStation, BusStation>(b1,b2);
            
            // Return them
            return res;
		}
      		
		return null;
	}

	/**
	 * Return the next bus station
	 * @param departure {@code BusStation} The departure bus station
	 * @param arrival {@code BusStation} The arrival bus station
	 * @param visited {@code List<BusStation>} List of already visited stations
	 * @return {@code BusStation} The next bus station
	 */
	public BusStation next(BusStation departure, BusStation arrival, List<BusStation> visited) {
		
		// The next station
		BusStation next = null;
				
		// Both closest station of the same line
		Pair<BusStation,BusStation> closest = this.getClosest(departure, visited);
		
		// Check if empty
		if (closest == null) { return null; }
				
		// Distance of both stations with the arrival
		double d1 = closest.getKey().distance(arrival);
		double d2 = closest.getValue().distance(arrival);
		
		// Which is the one is in front of us ?
		if (d1 < d2) {
			next = closest.getKey();
		} else {
			next = closest.getValue();
		}
		
		return next;
	}

	/**
	 * Return time up to the destination in minutes
	 * @param departure {@code BusStation} The departure bus station
	 * @param arrival {@code BusStation} The arrival bus station
	 * @param visited {@code List<BusStation>} List of already visited stations
	 * @return {@code Integer} The time in minute
	 */
	public int time(BusStation departure, BusStation arrival, List<BusStation> visited) {
				
		// All the bus stations in front of us
		List<BusStation> front = this.getBusStationsInFront(departure, arrival, visited);
		
		// Check if empty
		if (front == null) { return 0; }
		
		// Total time of the travel
		int totalTime = 0;

		// For each bus station in front of us
		// Stop before the last one
		for (int i = 0; i < front.size()-1; i++) {
			
			BusStation current = front.get(i);
			BusStation next = front.get(i+1);
			
			totalTime += current.time(next);
		}
		
		return totalTime;
	}
}
