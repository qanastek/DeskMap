/**
 * 
 */
package fr.univavignon.ceri.deskmap.models.geopoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

import fr.univavignon.ceri.deskmap.Map;
import fr.univavignon.ceri.deskmap.config.Settings;
import fr.univavignon.ceri.deskmap.config.Speeds;
import fr.univavignon.ceri.deskmap.models.Node;
import fr.univavignon.ceri.deskmap.models.Schedule;
import fr.univavignon.ceri.deskmap.models.line.PublicTransportRoute;
import javafx.util.Pair;

/**
 * @author Yanis Labrak
 *
 */
public class BusStation extends Node {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2706941956117169950L;

	/**
	 * List of schedules
	 */
	public ArrayList<Schedule> schedules = new ArrayList<Schedule>();
	
	/**
	 * List of bus lines
	 */
	public ArrayList<PublicTransportRoute> busLines = new ArrayList<PublicTransportRoute>();

	/**
	 * Identifier of the bus station
	 */
	public String mnemoarret;
	
	/**
	 * Name of the bus station
	 */
	public String nomarret;
		
	/**
	 * Empty constructor
	 */
	public BusStation() {
		super();
		this.mnemoarret = null;
		this.nomarret = null;
	}
	
	/**
	 * Basic constructor
	 * @param node {@code Node}
	 */
	public BusStation(Node node) {
		super(node);
		this.mnemoarret = null;
		this.nomarret = null;
	}
	
	/**
	 * Constructor full
	 * @param node {@code Node} The Node
	 * @param nomarret {@code String} Name of the bus station
	 * @param mnemoarret {@code String} Identifier of the bus station
	 */
	public BusStation(Node node, String mnemoarret, String nomarret) {
		super(node);
		this.mnemoarret = mnemoarret;
		this.nomarret = nomarret;
	}
	
	/**
	 * Return the waiting time before the next schedule
	 * @param route {@code PublicTransportRoute} The line
	 * @return {@code Integer} The time to wait in minutes
	 */
	public int waitingTime(PublicTransportRoute route) {
		return this.waitingTime(route.getNumber());
	}
	
	/**
	 * Return the waiting time before the next schedule
	 * @param mnemoligne {@code String} The line
	 * @return {@code Integer} The time to wait in minutes
	 */
	public int waitingTime(String mnemoligne) {
				
		// Get the next schedule
		Date next = this.getNextSchedule(mnemoligne);
		
		// Check if empty
		if (next == null) { return Integer.MAX_VALUE; }
		
		// Current date
		Date now = new Date();
		
		// Delta between both dates
		int delta = Math.abs(next.getMinutes() - now.getMinutes());
		
		return delta;
	}
	
	/**
	 * Return the next schedule for the bus line of this bus station
	 * @param mnemoligne {@code String}
	 * @return {@code Date}
	 */
	public Date getNextSchedule(String mnemoligne) {
		
		if (mnemoligne == null) { return null; }

		Date now = new Date();
		
		return this.getNextSchedule(mnemoligne, now);
	}
	

	/**
	 * Return the next schedule for the bus line of this bus station after another date
	 * @param mnemoligne {@code String} The bus line
	 * @param moment {@code Date} The minimum date
	 * @return {@code Date} The schedule
	 */
	public Date getNextSchedule(String mnemoligne, Date moment) {
		
		if (mnemoligne == null || moment == null || this.schedules == null || this.schedules.size() <= 0) { return null; }

		// Lowercase
		mnemoligne = mnemoligne.toUpperCase();
		
		// Best founded next date
		Date nextDate = null;
		
		// Closest difference
		long bestDiff = Long.MAX_VALUE;
		
		// Current difference
		long diffInMillies;
		
		// For each schedules
		for (Schedule schedule : this.schedules) {
			
			// If it's the right bus line and pass after now
			if (schedule.mnemoligne.equals(mnemoligne) == true && schedule.arriveetheorique.after(moment)) {
									
				// Get difference
				diffInMillies = Math.abs(moment.getTime() - schedule.arriveetheorique.getTime());
				
				// Check if better
				if (diffInMillies < bestDiff) {
					
					bestDiff = diffInMillies;
					nextDate = schedule.arriveetheorique;					
				}
			}		
		}
		
		return nextDate;
	}
	
	/**
	 * Add a bus line
	 * @param number {@code String}
	 * @param name {@code String}
	 */
	public void addBusLine(String number, String name) {
		
		// If not inside
		if (!this.busLines.stream().filter(line -> line.getHeader().equals(name)).findFirst().isPresent()) {
			
			PublicTransportRoute busLine = new PublicTransportRoute();
			
			busLine.setNumber(number);
			busLine.setHeader(name);
			
			// Add it
			this.busLines.add(busLine);
		}
	}
	
	/**
	 * Return the closest road node of the bus station
	 * @return {@code Node} The closest node
	 */
	public Node closestPlublicTransportRouteNode() {
		
		Node closestNode = null;
		double closestDistance = Double.MAX_VALUE;
		
		double currentDistance = 0.0;
		
		// For each public transport line segments
		for (PublicTransportRoute route : Map.PUBLIC_TRANSPORT_ROUTES) {
			
			// For each nodes
			for (Node node : route.getNodesInstances()) {
				
				currentDistance = this.distance(node);
				
				// If is better and accessible by the transport mode
				if (currentDistance < closestDistance && node.getFirstRoad().isAllowed() == true) {
					closestDistance = currentDistance;
					closestNode = node;
				}
			}
				
		}
		
		return closestNode;
	}

	/**
	 * Check if the bus station contains the line
	 * @param line {@code String} The line to check if inside
	 * @return {@code Boolean} Contains one or not
	 */
	public boolean containsLine(String line) {
		return this.containsLines(Arrays.asList(line));
	}
	
	/**
	 * Check if the bus station contains one of the lines
	 * @param lines {@code List<String>} The lines to check if inside
	 * @return {@code Boolean} Contains one or not
	 */
	public boolean containsLines(List<String> lines) {
				
		for (String line : lines) {
			
			if (this.busLines.stream().filter(b -> b.getNumber().toUpperCase().equals(line.toUpperCase())).findFirst().isPresent()) {
				return true;
			}			
		}
		
		return false;
	}

	/**
	 * Return the 4th closest bus stations of the bus station.
	 * @param visited {@code List<BusStation>}
	 * @return {@code List<BusStation>}
	 */
	public List<BusStation> getStationsAroundUs(List<BusStation> visited) {

		/**
		 * Return the 4th closest bus stations of the bus station.
		 * Don't take the already visited stations
		 */
		
		// Comparator
		Comparator<Pair<Double,BusStation>> comparator = new Comparator<Pair<Double,BusStation>>() {
            @Override
            public int compare(Pair<Double,BusStation> o1, Pair<Double,BusStation> o2) {            	
                return Double.compare(o1.getKey(), o2.getKey());
            }
        };
        
        // The ordered distances
		PriorityQueue<Pair<Double,BusStation>> ordered = new PriorityQueue<Pair<Double,BusStation>>(comparator);
		
		// Insert all stations
		for (BusStation busStation : Map.BUS_STATIONS_PATH) {        	
			ordered.add(new Pair<Double,BusStation>(this.distance(busStation),busStation));
		}
				
		// The output list
		List<BusStation> closest = new ArrayList<BusStation>();
		
		// Get the N closest bus stations from the departure
		for (int i = 0; i < Settings.STATIONS_AROUND_US; i++) {
			
			BusStation station = ordered.poll().getValue();			
			
			// Check if is not already visited
			if (!visited.stream().filter(s -> s.isSame(station)).findFirst().isPresent()) {

				// Pop the best
				closest.add(station);
			}
		}
		
		return closest;
	}
	
	/**
	 * Check if the stations are the same
	 * @param station {@code BusStation} The bus station to compare with
	 * @return {@code Boolean} Same or not
	 */
	public boolean isSame(BusStation station) {
		
		// If they are the same stations
		if (station.lat.equals(this.lat) && station.lon.equals(this.lon)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Return the unvisited lines
	 * @param visited {@code List<PublicTransportRoute>} List of already visited lines
	 * @return {@code List<PublicTransportRoute>} List of unvisited lines
	 */
	public List<PublicTransportRoute> getNonVisitedLines(List<PublicTransportRoute> visited) {
		
		// Result
		List<PublicTransportRoute> res = new ArrayList<PublicTransportRoute>();
		
		// For each lines
		for (PublicTransportRoute line : this.busLines) {
			
			// Check if already visited
			boolean alreadyVisited = visited.stream().filter(v -> v.getNumber().toUpperCase().equals(line.getNumber().toUpperCase())).findFirst().isPresent();
			
			// Check if already selected
			boolean alreadySelected = res.stream().filter(v -> v.getNumber().toUpperCase().equals(line.getNumber().toUpperCase())).findFirst().isPresent();
			
			// If the line was't visited or already selected
			if (alreadyVisited == false && alreadySelected == false) {
				
				// Add the line
				res.add(line);
			}
		}
		
		return res;
	}

	/**
	 * Return the distance between two bus stations
	 * @param station {@code BusStation} The other bus station
	 * @return {@code double} The distance
	 */
	protected double distance(BusStation station) {
		return this.distance((Node) station);
	}

	/**
	 * Calcul the time between two bus stations with a specific transport mode
	 * @param next {@code BusStation} The next station
	 * @return {@code Integer} The time in seconds
	 */
	public int time(BusStation next) {
		
		// Speed in km/h
		int speed = Speeds.getSpeed(this);
		
		// Meters
		double meters = this.distance(next);
		
		// Kilometers
		double kilometers = meters / 1000;
		
		// Hours
		double hours = (kilometers / speed);
		
		// Time in minutes
		int seconds = (int) (hours * 60 * 60);
		
		// Time in minutes
		return seconds;
	}
}
