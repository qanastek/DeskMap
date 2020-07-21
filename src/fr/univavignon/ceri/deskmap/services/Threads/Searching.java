package fr.univavignon.ceri.deskmap.services.Threads;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAttribute;

import fr.univavignon.ceri.deskmap.Map;
import fr.univavignon.ceri.deskmap.config.Settings;
import fr.univavignon.ceri.deskmap.config.TransportModeNames;
import fr.univavignon.ceri.deskmap.controllers.MainViewController;
import fr.univavignon.ceri.deskmap.models.Node;
import fr.univavignon.ceri.deskmap.models.NodePath;
import fr.univavignon.ceri.deskmap.models.Path;
import fr.univavignon.ceri.deskmap.models.Modes.TransportMode;
import fr.univavignon.ceri.deskmap.models.geopoint.BusStation;
import fr.univavignon.ceri.deskmap.models.line.PublicTransportRoute;
import fr.univavignon.ceri.deskmap.services.AStar;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.util.Pair;

/**
 * @author Yanis Labrak
 *
 */
public class Searching extends Task<Void> {

	/**
	 * The AStar algorithm
	 */
	public AStar aStar = new AStar();

	/**
	 * Departure node
	 */
	public Node currentDeparture = null;

	/**
	 * Arrival node
	 */
	public Node currentArrival = null;

	/**
	 * Current transport mode
	 */
	public String currentTransportMode;

	/**
	 * Current calcul mode
	 */
	public String currentCalculMode;

	/**
	 * Best line name
	 */
	public PublicTransportRoute bestLine = null;

	/**
	 * Closest node of the arrival
	 */
	public BusStation bestDepartureBusStation = null;

	/**
	 * Closest node of the departure
	 */
	public BusStation bestArrivalBusStation = null;

	/**
	 * Best distance
	 */
	public double bestDistance = Double.MAX_VALUE;

	/**
	 * Best time
	 */
	public double bestTime = Double.MAX_VALUE;
	
	/**
	 * Distance between the bus stations and the departure
	 */
	public double distanceDeparture;
	
	/**
	 * Distance between the bus stations and the arrival
	 */
	public double distanceArrival;

	/**
	 * Table of lines ID_LINE | DEP_BEST_NODE | DEP_BEST_DIST | ARR_BEST_NODE |
	 * ARR_BEST_DIST
	 */
	public ArrayList<ArrayList<Object>> table;

	/**
	 * All the path and their colors
	 */
	@XmlAttribute(name = "paths")
	public List<Path> paths = new ArrayList<Path>();

	/**
	 * The departure road node
	 */
	public Node roadNodeDeparture = null;

	/**
	 * The arrival road node
	 */
	public Node roadNodeArrival = null;

	/**
	 * AStar for departure
	 */
	public AStarSearching pathDeparture = null;

	/**
	 * Departure thread
	 */
	public Thread departureThread = null;

	/**
	 * Arrival thread
	 */
	public Thread arrivalThread = null;

	/**
	 * AStar for arrival
	 */
	public AStarSearching pathArrival = null;

	/**
	 * If both, departure & arrival busStations are the same go by foots
	 */
	private boolean same = false;

	/**
	 * Empty constructor
	 */
	public Searching() {
		super();
	}

	/**
	 * Constructor
	 * @param currentDeparture     {@code Node}
	 * @param currentArrival       {@code Node}
	 * @param currentTransportMode {@code String}
	 * @param currentCalculMode    {@code String}
	 */
	public Searching(Node currentDeparture, Node currentArrival, String currentTransportMode,
			String currentCalculMode) {
		this.currentDeparture = currentDeparture;
		this.currentArrival = currentArrival;
		this.currentTransportMode = currentTransportMode;
		this.currentCalculMode = currentCalculMode;
	}

	/**
	 * Searching
	 * 
	 * @author Yanis Labrak
	 */
	@Override
	protected Void call() throws Exception {

		// If the departure or the arrival cannot be accessed with the current transport
		// mode
		if (this.currentDeparture.getFirstRoad().isAllowed() == true
				&& this.currentArrival.getFirstRoad().isAllowed() == true) {

			// Clear paths
			this.paths = new ArrayList<Path>();

			// Clear path
			if (this.aStar != null && this.aStar.path != null) {
				this.aStar = null;
			}

			/**
			 * If the user select the public transport as transport mode
			 */
			if (this.currentTransportMode.equals(TransportModeNames.PUBLIC_TRANSPORT)) {

				// Path with public transports
				this.publicTransports();
				
				// If the correspondence is enabled
				if (MainViewController.CORRESPONDENCE_ENABLED == true) {
					
					// Path with correspondence
					this.correspondence();
				}

			} else {

				// Direct path between departure and arrival
				this.directPath(MainViewController.CURRENT_TRANSPORT_MODE);
			}

		} else {

			MainViewController.addMapPath("Cannot go here !");
			System.out.println("Cannot go here !");
		}

		return null;
	}

	/**
	 * Search the path in public transports without correspondence
	 * 
	 * @author Yanis Labrak
	 */
	private void publicTransports() {

		// Table of lines
		this.table = new ArrayList<ArrayList<Object>>();

		// Fetch best bus station of each lines
		this.bestBestBusStationOfEachLines();

		/**
		 * Search for the closest public transport line
		 */
		this.closestPublicTransportLine();

		/**
		 * If no departure and arrival stations was found go straight to the destination
		 */
		if (this.bestDepartureBusStation == null || this.bestArrivalBusStation == null) {

			// Direct path between departure and arrival
			this.directPath();
			return;
		}

		// Are they the same ?
		this.same = this.bestDepartureBusStation.mnemoarret.equals(this.bestArrivalBusStation.mnemoarret);

		// Get direct path
		this.directPath();

		// If both busStation are the same or this is faster to go by walking
		if (this.same == true || (this.aStar != null && this.isBetterToWalk())) { return; }

		/**
		 * AStar for start -> b1 and b2 -> end If both, departure & arrival busStations
		 * are the same go by foots
		 */
		this.astarBoth();

		// Check empty
		if (this.pathDeparture == null || this.pathArrival == null) { return; }

		// Distance with departure and arrival
		this.distanceDeparture = this.pathDeparture.result;
		this.distanceArrival = this.pathArrival.result;

		this.paths.add(this.pathDeparture.astar.path);
		this.paths.add(this.pathArrival.astar.path);

//		this.debugInfos(distanceDeparture, distanceArrival);
	}

	/**
	 * Debug all the necessary informations
	 * 
	 * @param distanceDeparture {@code Double}
	 * @param distanceArrival   {@code Double}
	 */
	private void debugInfos(Double distanceDeparture, Double distanceArrival) {
		System.out.println("---------------------------------------------------");
		System.out.println("The closest line is the " + this.bestLine + " whith only " + this.bestDistance + "m.");
		System.out.println("Distance departure: " + distanceDeparture);
		System.out.println("Distance arrival: " + distanceArrival);
		System.out.println("Sum distance real: " + (distanceDeparture + 0 + distanceArrival));
		System.out.println("Best departure : " + this.bestDepartureBusStation);
		System.out.println("Best arrival : " + this.bestArrivalBusStation);
		System.out.println("Straight bistance : " + this.currentDeparture.distance(this.currentArrival));
		System.out.println("--------------");
		System.out.println("Walking time : " + TransportMode.walkingTime(this.aStar.getTotalDistance()));
		System.out.println("Best time : " + this.bestTime);
		System.out.println("---------------------------------------------------");
	}

	/**
	 * Return if it's better to walk
	 * 
	 * @return {@code Boolean} True => Better <br>
	 *         False => Worst
	 */
	private boolean isBetterToWalk() {

		Double travelTimeFoot = TransportMode.walkingTime(this.aStar.getTotalDistance());

		// If less than one hour
		if (travelTimeFoot < 60.0) {
			return travelTimeFoot < (this.bestTime * Settings.MINIMUM_GAIN_LESS_ONE_HOUR);
		}

		return travelTimeFoot < (this.bestTime * Settings.MINIMUM_GAIN_ABOVE_ONE_HOUR);
	}

	@Override
	protected void succeeded() {

		/**
		 * If it's a direct path
		 */
		if (this.paths == null || this.paths.size() <= 0) {

			// Add the straight path to the printable paths
			this.paths.add(this.aStar.path);
			
			// Display the informations about the path
			this.aStar.getPathInformations();
			
			// Disable the lines printing
			MainViewController.STATUS_PUBLIC_TRANSPORT_ROUTE.set(false);
			
			// Clear the selected lines
			if (MainViewController.SELECTED_ROUTES.size() > 0) { MainViewController.SELECTED_ROUTES.clear(); }
			if (MainViewController.SELECTED_PUBLIC_TRANSPORTS.size() > 0) { MainViewController.SELECTED_PUBLIC_TRANSPORTS.clear(); }

		} 
		/**
		 * If it's a public transports path
		 */
		else {

			// Clear the old paths
			if (this.paths != null && this.paths.size() > 0) {
				this.paths.clear();
			}

			/**
			 * Display informations inside the side text area
			 */
			this.getInformations();

			Path p1 = new Path();
			Path p3 = new Path();

			/**
			 * First part
			 */
			p1.addAll(this.pathDeparture.astar.path);
			
			/**
			 * Display the corresponding line
			 */
			MainViewController.SELECTED_ROUTES = FXCollections.observableArrayList(this.bestLine.getNumber());
			MainViewController.STATUS_PUBLIC_TRANSPORT_ROUTE.set(true);
			
			/**
			 * Second part
			 */
			p3.addAll(this.pathArrival.astar.path);
			
			// Add them
			this.paths.add(p1);
			this.paths.add(p3);
		}

		// Render the result
		MainViewController.render();
	}

	/**
	 * Fetch the best bus station for every single lines
	 */
	private void bestBestBusStationOfEachLines() {

		// For each bus stations
		for (BusStation busStation : Map.BUS_STATIONS_PATH) {

			// For each public transport lines (not the segment)
			for (PublicTransportRoute route : busStation.busLines) {

				ArrayList<Object> line = null;

				/**
				 * Check if the bus line name already exist in the list If we find it, keep it
				 */
				for (ArrayList<Object> LINE : this.table) {

					// Check if it's the same public transport
					if (((PublicTransportRoute) LINE.get(0)).getNumber().equals(route.getNumber())) {
						line = LINE;
						break;
					}
				}

				/**
				 * If the line wasn't already in the list
				 */
				if (line == null) {

					// Create it
					line = new ArrayList<Object>();

					// Add informations
					line.add(0, route); // Add the line number
					line.add(1, new Node()); // departure node
					line.add(2, Double.MAX_VALUE); // departure node distance
					line.add(3, new Node()); // arrival node
					line.add(4, Double.MAX_VALUE); // arrival node distance

					// Add it
					this.table.add(line);
				}

				/**
				 * Check distance with the bus station
				 */

				// Distance between START and the first bus station
				double distanceDeparture = NodePath.getDistanceInMeters(this.currentDeparture, busStation);

				// Distance between the first bus station and the arrival
				double distanceArrival = NodePath.getDistanceInMeters(busStation, this.currentArrival);

				// If it's a better distance for the line
				if (distanceDeparture < (double) line.get(2)) {
					line.set(1, busStation);
					line.set(2, distanceDeparture);
				}

				// If it's a better distance for the line
				if (distanceArrival < (double) line.get(4)) {
					line.set(3, busStation);
					line.set(4, distanceArrival);
				}
			}
		}

	}

	/**
	 * Direct path between departure and arrival
	 * 
	 * @param transportMode {@code String} The transport mode to take
	 */
	private void directPath(String transportMode) {

		// Setup the AStar algorithm with the departure and arrival
		this.aStar = new AStar(this.currentDeparture, this.currentArrival, transportMode, this.currentCalculMode);

		// Process the path
		this.aStar.findPath(MainViewController.currentCalculMode);
	}

	/**
	 * Direct path between departure and arrival
	 */
	private void directPath() {
		this.directPath(TransportModeNames.FOOT);
	}

	/**
	 * Search for the closest public transport line
	 */
	private void closestPublicTransportLine() {

		// Check if empty
		if (this.table == null) {
			return;
		}

		this.bestLine = null; // Closest line
		this.bestDepartureBusStation = null; // Closest node of the arrival
		this.bestArrivalBusStation = null; // Closest node of the departure
		this.bestDistance = Double.MAX_VALUE;
		this.bestTime = Double.MAX_VALUE;

		// For each best bus station of each lines
		for (ArrayList<Object> ld : this.table) {

			PublicTransportRoute line = (PublicTransportRoute) ld.get(0); // Current line
			BusStation b1 = (BusStation) ld.get(1); // Best station departure
			BusStation b2 = (BusStation) ld.get(3); // Best station arrival

			double sumDistanceWalking = (double) ld.get(2) + (double) ld.get(4);

			double sumTime = TransportMode.walkingTime(sumDistanceWalking);

			Date d1 = b1.getNextSchedule(line.getNumber());

			if (d1 == null) {
				continue;
			}

			Date d2 = b2.getNextSchedule(line.getNumber(), d1);

			if (d2 == null) {
				continue;
			}

			int travelTime = (d1.getMinutes() - d2.getMinutes());
			travelTime = travelTime > 0 ? travelTime : -travelTime;
			sumTime += travelTime;

			// Add the time before the next bus
			int wait = b1.waitingTime(line);

			sumTime += wait;

			// If better
			if (sumTime < this.bestTime) {

				this.bestTime = sumTime;

				this.bestLine = line;
				this.bestDepartureBusStation = b1;
				this.bestArrivalBusStation = b2;
			}
		}
	}

	/**
	 * Search the path in public transports with correspondence
	 * @author Mohamed Ben Yamna
	 */
	private void correspondence() {
		
		/**
		 * Initialization
		 * currentLineTotalTime | station | closestStation | line
		 */
		Vector<Vector<Object>> bestNeighbors = new Vector<Vector<Object>>();
		
		/**
		 * History of selection
		 */
		Vector<Vector<Object>> bestNeighborsPath = new Vector<Vector<Object>>();
		
		// The current path
	    Path path = new Path();
		
		// The already visited stations
		List<BusStation> visitedStations = new ArrayList<BusStation>();
				
		// The already visited lines
		List<PublicTransportRoute> visitedLines = new ArrayList<PublicTransportRoute>();
		
		// Selected lines
		List<PublicTransportRoute> selectedLines = new ArrayList<PublicTransportRoute>();
		
		// Current best arrival station
		BusStation currentArrivalStation = this.bestArrivalBusStation;
		
		// The current best line
		PublicTransportRoute currentLine = this.bestLine;
		
		// Check empty
		if (currentLine == null || currentArrivalStation == null) { return; }
		
		selectedLines.add(currentLine);
		
		// The starting point of the algorithm is the first bus station after the one were the user come in the public transport.
		BusStation currentStation = currentLine.next(this.bestDepartureBusStation, currentArrivalStation, visitedStations);
		
		// Check if empty
		if (currentStation == null) { return; }
		
		// Add the current station to the path
		path.add(currentStation);
		
		// Total time from the start
		double currentTotalTime = this.bestDepartureBusStation.time(currentStation);

		// Add the current line on which we are
		visitedLines.add(this.bestLine);
		
		// Until we aren't arrive to the final bus station
	    while(currentStation != null && currentStation != this.bestArrivalBusStation) {

	        // For the N th closest stations around the current one
	        for(BusStation station : currentStation.getStationsAroundUs(visitedStations)) {
	        	
	        	// Set the current station as visited
	        	visitedStations.add(station);

	        	// Check if the bus station contains lines
	            if(station.busLines.size() > 0) {

	            	// For each lines found
	                for(PublicTransportRoute line : station.getNonVisitedLines(visitedLines)) {
	                	
	                	// Set the current line as visited
	                	visitedLines.add(line);
	                	
	                	// Get the closest station and it's distance
	                	Pair<BusStation,Double> closest = line.getClosestFromArrival(this.currentArrival);
	                	
	                	// Check if empty
	                	if (closest == null) { continue; }
	                	
	                    BusStation closestStation = closest.getKey();
	                    double closestStationDistance = closest.getValue();
	                    
	                    // The current distance from the bestArrivalStation to the arrival
	                    double currentDistance = this.bestArrivalBusStation.distance(this.currentArrival);

	                    // If the station is closer from the arrival than me
	                    if(closestStationDistance < currentDistance)  {
	                    	
	        				// Waiting time
	        				double waitingTime = station.waitingTime(line);
	        				
	        				// Travel duration from current station to the arrival station
	        				double travelTime = line.time(station, closestStation, visitedStations);
	        				
	        				// Check empty
	        				if (travelTime == 0) { continue; }
	        				
	        				// Duration to walk from arrivalStation -> arrivalNode
	        				double walkToArrival = TransportMode.walkingTime(closestStation.distance(this.currentArrival));
	        				
	                		// Get the total time of the travel
	                        double currentLineTotalTime = currentTotalTime + waitingTime + travelTime + walkToArrival;

	                        // Take this line if it's minimize the total time of the current path
	                        if(currentLineTotalTime < this.bestTime) {
	                        	
	                        	// Create a record
	                        	Vector<Object> record = new Vector<Object>();
		                    	
	                        	// Fill up the record
	                        	record.add(currentLineTotalTime);
	                        	record.add(station);
	                        	record.add(closestStation);
	                        	record.add(line);
		                    	
	                        	// Insert it
	                            bestNeighbors.add(record);
	                        }
	                    }
	                }
	            }
	        }
			
	        // If the best neighbors table wasn't empty
	        if(bestNeighbors.size() > 0)  {
	            
	        	// Current best time
	        	double bestTime = Double.MAX_VALUE;
	        	
	        	// Take the fastest line
	        	// currentLineTotalTime | station | closestStation
	        	Vector<Object> bestNeighbor = null;

	        	// For each potential neighbors
	        	for (Vector<Object> currentNeighbor : bestNeighbors) {

	        		// If it's minimize the time
		        	if ((Double) currentNeighbor.get(0) < bestTime) {
		    			
		    			// Change the best neighbor
		        		bestNeighbor = currentNeighbor;
					}
				}
	        	
	        	// Add the neighbor to the history
	        	bestNeighborsPath.add(bestNeighbor);
	        	
	        	// Add the time to the total duration
	        	currentTotalTime += (double) bestNeighbor.get(0);
	        	
	        	// Set the current bus station
	        	currentStation = (BusStation) bestNeighbor.get(1);
	        	
	        	// Set the closest bus station from the arrival
	        	currentArrivalStation = (BusStation) bestNeighbor.get(2);
	        	
	        	// Set the current line
	        	currentLine = (PublicTransportRoute) bestNeighbor.get(2);
	        	
	        	// Add the station
	            path.add(currentStation);
	            
	            // Add the current line
	            selectedLines.add(currentLine);
	            
	            // Clear the table
	            bestNeighbors.clear();
	        }
	        else {

	        	// Find the next station on the current line
	            BusStation next = currentLine.next(currentStation, currentArrivalStation, visitedStations);
	            
	            // Check empty
	    		if (next == null) { break; }
	            
	        	// Add the time to the total duration
	        	currentTotalTime += currentStation.time(next);

	        	// Add the station
	            path.add(currentStation);
	            
	        	// Go to the next station
	            currentStation = next;  
	            
	            // Add the current station to the path
	            path.add(currentStation);
	        }
	    }
	    
	    // If no path as been found
	    if (path.size() > 2) {

        	
	    	// Add the arrival station
	        path.add(currentArrivalStation);
	        	    
		    // Clear the old paths and add the new
		    this.paths.clear();
	        		
		    this.paths.add(path);	    	
		}
	}

	/**
	 * AStar for start -> b1 and b2 -> end
	 */
	private void astarBoth() {

		// Get roads
		this.roadNodeDeparture = this.bestDepartureBusStation.closestPlublicTransportRouteNode();
		this.roadNodeArrival = this.bestArrivalBusStation.closestPlublicTransportRouteNode();
		
		/**
		 * If we cannot access to by foots
		 */
		if (this.roadNodeDeparture.getFirstRoad().isAllowed() == false
			|| this.roadNodeArrival.getFirstRoad().isAllowed() == false
			|| this.currentDeparture.getFirstRoad().isAllowed() == false
			|| this.currentArrival.getFirstRoad().isAllowed() == false) {
			System.out.println("One cannot be accessed");
			return;
		}

		// Departure path
		this.pathDeparture = new AStarSearching(this.currentDeparture, this.roadNodeDeparture, this.currentTransportMode, this.currentCalculMode);
		this.departureThread = new Thread(this.pathDeparture);
		this.departureThread.start();

		// Arrival path
		this.pathArrival = new AStarSearching(this.roadNodeArrival, this.currentArrival, this.currentTransportMode, this.currentCalculMode);
		this.arrivalThread = new Thread(this.pathArrival);
		this.arrivalThread.start();

		// Wait for the thread
		while (this.departureThread.isAlive() && this.arrivalThread.isAlive()) {

			try {
				this.departureThread.join();
				this.arrivalThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Display informations inside the side text area
	 * 
	 * @author Mohamed Ben Yamna
	 */
	private void getInformations() {

		MainViewController.clearMapPathTextArea();

		MainViewController.addMapPath("Walk " + (int) this.pathDeparture.result + " "
				+ this.pathDeparture.mode.getUnit() + " to the bus station " + this.bestDepartureBusStation.nomarret);
		MainViewController.addMapPath("Wait " + this.bestDepartureBusStation.waitingTime(this.bestLine)
				+ " min until the " + this.bestLine.getNumber() + " - " + this.bestLine.getHeader() + " comming");
		MainViewController.addMapPath("Get of at the bus stop called " + this.bestArrivalBusStation.nomarret);
		MainViewController.addMapPath("Walk from the bus stop to your destination during "
				+ (int) this.pathArrival.result + " " + this.pathDeparture.mode.getUnit());
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		System.out.println("Searching stopped !");
		return super.cancel(mayInterruptIfRunning);
	}

	@Override
	protected void updateProgress(double workDone, double max) {
		System.out.println(workDone + "% of " + max + "% done !");
		super.updateProgress(workDone, max);
	}
}