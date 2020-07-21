package fr.univavignon.ceri.deskmap.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import fr.univavignon.ceri.deskmap.config.Speeds;
import fr.univavignon.ceri.deskmap.config.TransportModeNames;
import fr.univavignon.ceri.deskmap.controllers.MainViewController;
import fr.univavignon.ceri.deskmap.models.Node;
import fr.univavignon.ceri.deskmap.models.NodePath;
import fr.univavignon.ceri.deskmap.models.Path;
import fr.univavignon.ceri.deskmap.models.Modes.CalculMode;

/**
 * The A star algorithm
 * @author Implementation Ben Yamna Mohamed
 * @author Implementation & Optimization Zheng Zihao
 * @author Modification & Optimization Labrak Yanis (R2)
 * @author Full rework Zheng Zhiao (R3)
 */
public class AStar {
	
	/**
	 * The departure of the path
	 */
	public Node departure;
	
	/**
	 * The arrival of the path
	 */
	public Node arrival;

	/**
	 * The current transport mode
	 */
	public String transportMode;
	
	/**
	 * The current calcul mode
	 */
	public String calculMode;
	
	/**
	 * The current position
	 */
	public NodePath now;

	/**
	 * The path
	 */
	public Path path;
	
	/**
	 * The list of all the closest {@code Node}'s
	 */
	public List<NodePath> close;

	/**
	 * All of nodes already visited
	 */
	public List<Node> visited = new ArrayList<Node>();

	/**
	 * All the banned {@code NodePath} were we cannot go anymore
	 */
	public Path banned;

	/**
	 * Total distance of the path
	 */
	public Double totalDistance = 0.0;
	
	/**
	 * Total time of the path
	 */
	public Double totalTime = 0.0;
	
	/**
	 * Non visited nodes around the current node
	 */
	public PriorityQueue<NodePath> closer;

	
	/**
	 * Constructor
	 * @author Zihao Zheng
	 * @author Mohamed Ben Yamna
	 */
	public AStar() {
		
		this.departure = new Node();
		this.arrival = new Node();

		this.now = new NodePath(this.departure);

		this.path = new Path();
		this.close = new ArrayList<NodePath>();
		this.banned = new Path();
	}
	
	/**
	 * Constructor
	 * @param from {@code Node} The departure {@code Node}
	 * @param to {@code Node} The arrival {@code Node}
	 * @param transportMode {@code String}
	 * @param calculMode {@code String}
	 * @author Zihao Zheng
	 * @author Mohamed Ben Yamna
	 */
	public AStar(Node from, Node to, String transportMode, String calculMode) {
		
		// Set up the departure and arrival
		this.departure = from;
		this.arrival = to;
		
		// Set up the transport and calcul mode
		this.transportMode = transportMode;
		this.calculMode = calculMode;

		// Current node
		this.now = new NodePath(this.departure);
		this.now.street = this.now.getFirstRoad().name;
		
		// And all the arrayList
		this.path = new Path();
		this.close = new ArrayList<NodePath>();
		this.banned = new Path();
	}
	
	/**
	 * Display the departure and the arrival of the path
	 * @author Yanis Labrak
	 */
	public void displayFromTo() {
		Draw.displayPOI(this.departure); 
		Draw.displayPOI(this.arrival); 
	}

	/**
	 * 
	 * Process and display in real time on the {@code Canvas} the path.
	 * @param calcul {@code CalculMode}
	 * @return path of node
	 * @author Zihao Zheng
	 */
	public Path findPath(CalculMode calcul) {
		
		Comparator<NodePath> comparator = new Comparator<NodePath>() {
            @Override
            public int compare(NodePath o1, NodePath o2) {
                return Double.compare(o1.totalDistance, o2.totalDistance);
            }
        };
        
        // Instantiate a PriorityQueue for the best nodes
        this.closer = new PriorityQueue<NodePath>(comparator);
        
        // Add the departure node to the path
		this.path.add(this.now);
		
		// Departure node
		NodePath departure = this.now;
		
		// While the thread is able to run
		while (MainViewController.STATUS == true) {
						
			// Check empty
			if (this.visited == null || this.now == null || this.closer == null) { break; }
			
			this.visited.add(this.now);
			
			// Neighbors
			this.close = this.now.getNeigthboors();

			// if we have neighbors
			if (this.close != null && this.close.size() > 0) {
				
				// For each nodes around us
				for (NodePath node : this.close) {
										
					boolean isVisited = false;
					
					/**
					 * Check if the neighbor is already visited
					 */
					if (this.visited.stream().filter(e -> e.id.equals(node.id)).findFirst().isPresent() == true) { isVisited = true; }

					// If wasn't visited
					if (!isVisited) {
												
						// Add the current path to the node
						node.path.addAll(this.path);
						
						// Add the current total distance of the path
						node.totalDistance = distance(this.now, node) + distance(node, this.arrival);
						node.distance = distance(this.now, node);
						
						// Add durations
						node.duration = node.getTime();
						
						// Add the node to the closest
						this.closer.add(node);
					}
				}
				
			}

			// Get the node with the tiniest distance
			this.now = this.closer.poll();
			
			// Update the path
			this.path.clear();			
			this.path.addAll(this.now.path);
			this.path.add(this.now); 
			
			// If we reach the end 
			if (this.now.id == this.arrival.id) {								  
				return this.path; 
			}	
		}
		
		// If the status was changed, stop the path finding
		if (MainViewController.STATUS == false) {
			System.out.println("Process stoped !");
			return null;
		}
		
		// Return the current path
		return this.path;		
	}

	/**
	 * Calculate the distance between two {@code Node}'s
	 * @param n {@code Node} Departure
	 * @param n1 {@code Node} Arrival
	 * @return {@code Double} The distance
	 * @author Mohamed Ben Yamna
	 */
	public static double distance(Node n, Node n1) {
		return Math.sqrt(Math.pow((n1.lat - n.lat), 2) + Math.pow((n1.lon - n.lon), 2));
	}
	
	/**
	 * Calculate the time between two {@code Node}'s in seconds
	 * @param n {@code Node} Departure
	 * @param n1 {@code Node} Arrival
	 * @return {@code Integer} The distance
	 * @author Yanis Labrak
	 */
	public double time(Node n, Node n1) {
		
		// Distance in kilometers
		double distance = NodePath.getDistanceInMeters(n,n1) / 1000;
		
		// Speed in km/h
		int speed = Speeds.getSpeed(n);
		
		// Time in seconds
		double secondsTotal = ((distance / speed) * 60 * 60);
		
		return secondsTotal;
	}

	/**
	 * Display the informations about the length/time of the path and all the segments of It too.
	 * @author Initially made by Capdepon Quentin
	 * @author Reworked by Zhiao Zheng (R2)
	 * @author Reworked by Mohamed Ben Yamna (R3)
	 */
	public void getPathInformations() {
		
		// Check empty
		if (this.path == null || this.path.size() <= 0) { return; }
		
		if (MainViewController.CURRENT_CALCUL_MODE == null) {
			this.getPathInformationsDistance();			
		} else {

			switch (MainViewController.CURRENT_CALCUL_MODE) {
					
				case "Temps":
					this.getPathInformationsTime();				
					break;

				case "Distance":	
				default:
					this.getPathInformationsDistance();
					break;
				
			}			
		}		
	}
	
	/**
	 * Return the total distance
	 * @return {@code Double} Total distance
	 */
	public double getTotalDistance() {
		
		this.totalDistance = 0.0;
		
		// Add each segments
		for (int i = 1; i < this.path.size() - 1; i++) {
			
			// Add to the total
			this.totalDistance += this.path.get(i).getDistanceInMeters();		
		}
		
		return Math.floor(this.totalDistance * 100) / 100;
	}
	
	/**
	 * Display the informations about the length of the path and all the segments of It too.
	 * @author Initially made by Capdepon Quentin
	 * @author Reworked by Zhiao Zheng (R2)
	 * @author Reworked by Mohamed Ben Yamna (R3)
	 */
	public void getPathInformationsDistance() {
		
		MainViewController.clearMapPathTextArea();
		
		HashMap<String, Double> cleanPath = new HashMap<String, Double>();
		
		// Distance of a street
		Double distanceStreet = 0.0;
		this.totalDistance = 0.0;

		String nextStreetName;
		
		String lastEntry = null;
		
		/**
		 * For each segment, add the length to the road
		 */
		for (int i = 1; i < this.path.size() - 1; i++) {
						
			// Streets names
			nextStreetName = this.path.get(i+1).street;
			
			// Distance of the section
			distanceStreet += this.path.get(i+1).getDistanceInMeters();
			
			// Trunk
			distanceStreet = Math.floor(distanceStreet * 100 ) / 100;
			
			// Check if it's empty
			if (nextStreetName != null && !nextStreetName.isEmpty() && distanceStreet > 0.0) {
				
				double currentDistanceStreet;
				
				// Check if the road already exist
				if (cleanPath.containsKey(nextStreetName) == true) {
					currentDistanceStreet = cleanPath.get(nextStreetName) + distanceStreet;
				} else {
					currentDistanceStreet = distanceStreet;
				}
				
				cleanPath.put(nextStreetName, currentDistanceStreet);	
				lastEntry = nextStreetName;
				
			} else if (lastEntry != null && nextStreetName == null && distanceStreet > 0.0) {
				cleanPath.put(lastEntry, distanceStreet);
			} else if (lastEntry == null && distanceStreet > 0.0) {
				cleanPath.put(this.now.street, distanceStreet);				
			}
			
			// Clear the distance
			distanceStreet = 0.0;
		}
		
		/**
		 * Display to the screen
		 */
		for (java.util.Map.Entry<String, Double> road : cleanPath.entrySet()) {
			
			// Add to the total
			this.totalDistance += road.getValue();
			int distance = road.getValue().intValue();

			// Display the segment informations
			if (distance > 0) {
				MainViewController.addMapPath(this.getTerm() + " " +  Integer.toString(distance) + " m to " + road.getKey());					
			}
		}
		
		// Trunk the decimal part of the double
		this.totalDistance = Math.floor(this.totalDistance * 100) / 100;
		
		// Print the total length of the path
		MainViewController.addMapPath("Total length of the path: " + this.totalDistance.toString() + "m");		
	}
	
	/**
	 * Get the term according to the transport mode
	 * @return {@code String} The term to use
	 */
	private String getTerm() {
		
		switch (this.transportMode) {
		
			case TransportModeNames.BIKE:				
			case TransportModeNames.CAR:				
			case TransportModeNames.PUBLIC_TRANSPORT:
				return "ride";
				
			case TransportModeNames.FOOT:	
			default:
				return "walk";
		}
	}
	
	/**
	 * Return the total time
	 * @return {@code Double} Total time
	 */
	public double getTotalTime() {
		
		this.totalTime = 0.0;
		
		// Add each segments
		for (int i = 1; i < this.path.size() - 1; i++) {
			
			// Add to the total
			this.totalTime += this.path.get(i).duration;			
		}
		
		return this.totalTime;
	}

	/**
	 * Display the informations about the time of the path and all the segments of It too.
	 * @author Initially made by Capdepon Quentin
	 * @author Reworked by Zhiao Zheng (R2)
	 * @author Reworked by Mohamed Ben Yamna (R3)
	 */
	public void getPathInformationsTime() {
		
		MainViewController.clearMapPathTextArea();
		
		HashMap<String, Double> cleanPath = new HashMap<String, Double>();
		
		// Distance of a street
		Double timeStreet = 0.0;
		this.totalTime = 0.0;
		
		String nextStreetName;
		
		String lastEntry = null;
		
		/**
		 * For each segment, add the length to the road
		 */
		for (int i = 1; i < this.path.size() - 1; i++) {
									
			// Streets names
			nextStreetName = this.path.get(i+1).street;
			
			// Distance of the section
			timeStreet += this.path.get(i+1).duration;
			
			// Trunk
			timeStreet = Math.floor(timeStreet * 100 ) / 100;
			
			// Check if it's empty
			if (nextStreetName != null && timeStreet > 0.0) {
				
				double currentDistanceStreet;
				
				// Check if the road already exist
				if (cleanPath.containsKey(nextStreetName) == true) {
					currentDistanceStreet = cleanPath.get(nextStreetName) + timeStreet;
				} else {
					currentDistanceStreet = timeStreet;
				}
				
				cleanPath.put(nextStreetName, currentDistanceStreet);	
				lastEntry = nextStreetName;
				
			} else if (lastEntry != null && nextStreetName == null && timeStreet > 0.0) {
				cleanPath.put(lastEntry, timeStreet);
			} else if (lastEntry == null && timeStreet > 0.0) {
				cleanPath.put(this.now.street, timeStreet);		
			}
			
			// Clear the distance
			timeStreet = 0.0;
		}
		
		/**
		 * Display to the screen
		 */
		for (java.util.Map.Entry<String, Double> road : cleanPath.entrySet()) {
			
			// Minutes
			double time = road.getValue();
			int mins = road.getValue().intValue();
			int secs = (int) ((time - mins) * 100);
			
			this.totalTime += Double.parseDouble(mins + "." + secs);

			// Display the segment informations
			if (mins > 1) {
				MainViewController.addMapPath(this.getTerm() + " " +  Integer.toString(mins) + " min to " + road.getKey());
			} else if (secs < 60 && secs >= 20) {
				MainViewController.addMapPath(this.getTerm() + " " +  Integer.toString(secs) + " seconds to " + road.getKey());				
			}
		}
		
		// Trunk the decimal part of the double
		this.totalTime = Math.floor(this.totalTime * 100) / 100;
		
		// Print the total length of the path
		MainViewController.addMapPath("Total length of the path: " + this.totalTime.toString() + "min");	
	}
}
