package fr.univavignon.ceri.deskmap;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.config.Settings;
import fr.univavignon.ceri.deskmap.config.Speeds;
import fr.univavignon.ceri.deskmap.config.TransportModeNames;
import fr.univavignon.ceri.deskmap.controllers.MainViewController;
import fr.univavignon.ceri.deskmap.models.GeoData;
import fr.univavignon.ceri.deskmap.models.Node;
import fr.univavignon.ceri.deskmap.models.Way;
import fr.univavignon.ceri.deskmap.models.Modes.PublicTransport;
import fr.univavignon.ceri.deskmap.models.UIElement.CheckBoxPublicTransport;
import fr.univavignon.ceri.deskmap.models.geopoint.BusStation;
import fr.univavignon.ceri.deskmap.models.line.PublicTransportRoute;
import fr.univavignon.ceri.deskmap.models.line.Road;
import fr.univavignon.ceri.deskmap.models.region.Region;
import fr.univavignon.ceri.deskmap.models.region.Wood;
import fr.univavignon.ceri.deskmap.models.region.Amenity.Parking;
import fr.univavignon.ceri.deskmap.models.region.Amenity.Prison;
import fr.univavignon.ceri.deskmap.models.region.Amenity.School;
import fr.univavignon.ceri.deskmap.models.region.Landuse.Commercial;
import fr.univavignon.ceri.deskmap.models.region.Landuse.FarmLand;
import fr.univavignon.ceri.deskmap.models.region.Landuse.Industrial;
import fr.univavignon.ceri.deskmap.models.region.Landuse.Military;
import fr.univavignon.ceri.deskmap.models.region.Landuse.Quarry;
import fr.univavignon.ceri.deskmap.models.region.Landuse.Railway;
import fr.univavignon.ceri.deskmap.models.region.Landuse.Residential;
import fr.univavignon.ceri.deskmap.models.region.Landuse.Retail;
import fr.univavignon.ceri.deskmap.models.region.Leisure.Bridge;
import fr.univavignon.ceri.deskmap.models.region.Leisure.Forest;
import fr.univavignon.ceri.deskmap.models.region.Leisure.GolfCourse;
import fr.univavignon.ceri.deskmap.models.region.Leisure.Grass;
import fr.univavignon.ceri.deskmap.models.region.Leisure.Healthcare;
import fr.univavignon.ceri.deskmap.models.region.Leisure.IceRink;
import fr.univavignon.ceri.deskmap.models.region.Leisure.Park;
import fr.univavignon.ceri.deskmap.models.region.Leisure.SportsCentre;
import fr.univavignon.ceri.deskmap.models.region.Natural.Water;
import fr.univavignon.ceri.deskmap.models.region.Natural.Wetland;
import fr.univavignon.ceri.deskmap.models.region.Road.Pedestrian;
import fr.univavignon.ceri.deskmap.models.region.Structure.Building;
import fr.univavignon.ceri.deskmap.models.region.Structure.Cemetery;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * This class represents the Map entity
 * @author Yanis Labrak
 * @author Mohamed BEN YAMNA
 */
public class Map {

	/**
	 * Scale of the map
	 */
	public static Double SCALE;
	
	/**
	 * Scale of the map in meters
	 */
	public static SimpleIntegerProperty SCALE_METER = new SimpleIntegerProperty(0);
	
	/**
	 * Latitude position bottom
	 */
	public static Double BOTTOM;

	/**
	 * Latitude position left
	 */
	public static Double LEFT;
	
	/**
	 * Longitude position top
	 */
	public static Double TOP;
	
	/**
	 * Longitude position right
	 */
	public static Double RIGHT;
	
	/**
	 * Height 
	 */
	public static Double HEIGHT;

	/**
	 * Width 
	 */
	public static Double WIDTH;
	
	/**
	 * Latitude of the screen
	 */
	public static Double LATITUDE = 0.0;
	
	/**
	 * Longitude of the screen
	 */
	public static Double LONGITUDE = 0.0;
	
	/**
	 * Drag and drop X axis delta
	 */
	public static Double X_DELTA = 0.0;
	
	/**
	 * Drag and drop Y axis delta
	 */
	public static Double Y_DELTA = 0.0;
	
	/**
	 * Status of the {@code Map} :
	 * <br>
	 * {@code False} Not ready
	 * <br>
	 * {@code True} Ready
	 */
	public static boolean STATE = false;
	
	/**
	 * All the GeoData loaded
	 */
	public static HashMap<Long,GeoData> CONTENT = new HashMap<Long,GeoData>();
	
	/**
	 * All the loaded roads
	 */
	public static List<Road> ROADS = new ArrayList<Road>();

	/**
	 * All the nodes to load
	 */
	public static HashMap<Long, Node> NODES = new HashMap<Long, Node>();
	
	/**
	 * All the bus stations
	 */
	public static ArrayList<BusStation> BUS_STATIONS = new ArrayList<BusStation>();
	
	/**
	 * All the bus stations for the path
	 */
	public static ArrayList<BusStation> BUS_STATIONS_PATH = new ArrayList<BusStation>();
	
	/**
	 * All the public transports of the city
	 * @author Zhiao Zheng
	 */
	public static ArrayList<PublicTransport> PUBLIC_TRANSPORTS_POSITIONS = new ArrayList<PublicTransport>();
	
	/**
	 * All the public transports routes of the city
	 */
	public static ArrayList<PublicTransportRoute> PUBLIC_TRANSPORT_ROUTES = new ArrayList<PublicTransportRoute>();
	
	/**
	 * Constructor
	 * @author Mohammed BEN YAMNA
	 */
	public Map() {
		Map.SCALE = 1.0;
		Map.BOTTOM = 0.0;
		Map.LEFT = 0.0;
		Map.TOP = 0.0;
		Map.RIGHT = 0.0;
		Map.HEIGHT = 0.0;
		Map.WIDTH = 0.0;
	}
	
	/**
	 * Parse the JSON file and make Object from It
	 * @param city {@code String} Name of the city
	 * @throws org.json.simple.parser.ParseException If the file wasn't find
	 * @author Yanis Labrak
	 */
	public static void loadCityAsObject(String city) throws org.json.simple.parser.ParseException {

		// Load all the nodes
		// 2.74400 seconds
		Map.loadNodes(city);
		
		// Draw Landuse, Natural, Leisure, Amenity, Highways and Building
		// 1.79200 seconds
		Map.loadWays(city);	

		// Load all the relations
		// 2.59000 seconds
		Map.loadRelations(city);
		
		Map.loadPublicTransportsRoutes(city);
	}
	
	/**
	 * Add a data to the {@code MapContent} {@code HashMap}
	 * @param data {@code GeoData} The data to add
	 * @param list {@code HashMap<Long,GeoData>}
	 * @author Mohamed BEN YAMNA
	 */
	public void addMapContent(GeoData data, HashMap<Long,GeoData> list) {
		list.put(data.id, data);
	}
	
	/**
	 * Add a {@code Node} inside the {@code HashMap}
	 * @param node The {@code Node} to add
	 * @author Mohamed BEN YAMNA
	 */
	public void addNode(Node node) {
		Map.NODES.put(node.id, node);
	}

	/**
	 * Add a {@code BusStation} inside the {@code List<BusStation>}
	 * @param station The {@code BusStation} to add
	 * @author Mohamed BEN YAMNA
	 */
	public void addBusStation(BusStation station) {
		Map.BUS_STATIONS.add(station);
	}
	
	/**
	 * Get the {@code Node} which have the same {@code Id}
	 * @param id {@code String} Identifier of the {@code Node}
	 * @return return the {@code Node} for which the identifier match
	 * @author Mohamed BEN YAMNA
	 */
	public Node getNode(String id) {
		return Map.NODES.get(Long.parseLong(id));
	}
	
	/**
	 * Load all the {@code Node}'s from the JSON file
	 * @param city {@code String} Name of the city
	 * @author Yanis Labrak
	 */
	public static void loadNodes(String city) {
		 
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        // Read the cache for the Map
//        try (FileReader reader = new FileReader(city + "Map.json"))
        try {
        	
            String path = city + "Map.json";
            FileReader fichier = new FileReader(path);

            //Read JSON file
        	// TODO: Too slow 2.25s
            Object obj = jsonParser.parse(fichier);
 
            JSONObject main = (JSONObject) obj;
            JSONArray elements = (JSONArray) main.get("elements");
            
            Iterator<?> iterator = elements.iterator();

            // While we have content
			while (iterator.hasNext()) {	
				
				// Get it
				JSONObject item = (JSONObject) iterator.next();

				String type = (String) item.get("type");
				
				// If its a Node
				if (type.toLowerCase().equals("node")) {
					
					Node node = new Node(
						(Long) item.get("id"),
						(Double) item.get("lat"),
						(Double) item.get("lon")
					);
					
					JSONObject tags = (JSONObject) item.get("tags");
					
					if (tags != null) {
						
						String highway = (String) tags.get("highway");						
						String name = (String) tags.get("name");						
						
						if (highway != null && name != null && highway.equals("bus_stop")) {
							BusStation station = new BusStation(node);
							station.nomarret = name;
							Map.BUS_STATIONS.add(station);
							continue;
						}
					}

					// Add it to the HashMap of Nodes
					MainViewController.map.addNode(node);
					
				}				
		    }
			
			System.out.println("Bus stations map: " + Map.BUS_STATIONS.size());
			
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * Load a {@code Region} from the JSON file
	 * @param entity {@code Region} The {@code Region} where we load the data
	 * @param item {@code JSONObject} The JSON which contain the data about the {@code Region}
	 * @param list {@code HashMap<Long,GeoData>}
	 * @author Yanis Labrak
	 */
	public static void loadRegion(Region entity, JSONObject item, HashMap<Long,GeoData> list) {
		JSONArray nodes;
		Iterator<?> it;
		
		// Read the 'nodes' array
		nodes = (JSONArray) item.get("nodes");
		
		it = nodes.iterator();
		
		// for each node identifier of this array
		while (it.hasNext()) {
			
			// Read it
			String nodeId = it.next().toString();
			
			// Add the id to the nodes list of the water
			entity.addNode(nodeId);
		}
		
		// Add the Building
		MainViewController.map.addMapContent(entity, list);
	}
	
	/**
	 * Load all the ways from the JSON file as {@code GeoData}
	 * @param city {@code String} Name of the city
	 * @author Yanis Labrak
	 */
	public static void loadWays(String city) {
		
		//JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();
		
//		try (FileReader reader = new FileReader(city.toLowerCase() + "Map.json"))
		try	{
        	
            String path = city + "Map.json";
            FileReader fichier = new FileReader(path);
            
			//Read JSON file
			Object obj = jsonParser.parse(fichier);
			
			JSONObject main = (JSONObject) obj;
			JSONArray elements = (JSONArray) main.get("elements");
			
			Iterator<?> iterator = elements.iterator();
			
			// For each JSON elements
			while (iterator.hasNext()) {	
				
				JSONObject item = (JSONObject) iterator.next();
				
				// Type: Node, Way or Relation
				String type = (String) item.get("type");
				
				// If its a way
				if (type.toLowerCase().equals("way")) {
					
					// Fetch tags
					JSONObject tags = (JSONObject) item.get("tags");
					
					Region entity = null;
					
					// If its not a isolated Way
					if (tags != null ) {
						
						if ((String) tags.get("landuse") != null) {
							
							switch ((String) tags.get("landuse")) {
							
								case "residential":
									entity = new Residential((Long) item.get("id"));
									break;
									
								case "industrial":									
									entity = new Industrial((Long) item.get("id"));
									break;
									
								case "commercial":									
									entity = new Commercial((Long) item.get("id"));
									break;
									
								case "retail":									
									entity = new Retail((Long) item.get("id"));
									break;
									
								case "railway":									
									entity = new Railway((Long) item.get("id"));					
									break;
									
								case "cemetery":
									entity = new Cemetery((Long) item.get("id"));
									break;
									
								case "forest":
									entity = new Forest((Long) item.get("id"));							
									break;
									
								case "farmland":
								case "farmyard":
								case "vineyard":
								case "orchard":
								case "greenhouse_horticulture":
									entity = new FarmLand((Long) item.get("id"));							
									break;
									
								case "grass":
								case "plant_nursery":
									entity = new Grass((Long) item.get("id"));
									break;									
									
								case "military":
								case "construction":
									entity = new Military((Long) item.get("id"));
									break;									
									
								case "quarry":
									entity = new Quarry((Long) item.get("id"));
									break;	
																
								case "healthcare":
									entity = new Healthcare(
										(Long) item.get("id")
									);
									break;
							}
							
							// If not empty add it
							if (entity != null && item != null) {
								Map.loadRegion(entity, item, Map.CONTENT);								
							}
							
						}
						// If it's a Leisure
						else if ((String) tags.get("leisure") != null) {
								
							switch ((String) tags.get("leisure")) {
							
								case "sports_centre":	
									entity = new SportsCentre((Long) item.get("id"));							
									break;
									
								case "ice_rink":	
									entity = new IceRink((Long) item.get("id"));							
									break;
									
								case "park":
								case "garden":
									entity = new Park((Long) item.get("id"));										
									break;
									
								case "golf_course":	
									entity = new GolfCourse((Long) item.get("id"));
									break;
									
								case "pitch":								
									entity = new Building((Long) item.get("id"));
									break;
							}
							
							// If not empty add it
							if (entity != null && item != null) {
								Map.loadRegion(entity, item, Map.CONTENT);						
							}
							
						}
						// If it's a Natural
						else if ((String) tags.get("natural") != null) {
							
							switch ((String) tags.get("natural")) {
							
								case "water":										
								case "bay":										
									entity = new Water((Long) item.get("id"));
									break;
									
								case "scrub":										
									entity = new Forest((Long) item.get("id"));
									break;
									
								case "wood":										
									entity = new Wood((Long) item.get("id"));
									break;
									
								case "wetland":										
									entity = new Wetland((Long) item.get("id"));
									break;
							}
							
							// If not empty add it
							if (entity != null && item != null) {
								Map.loadRegion(entity, item, Map.CONTENT);				
							}
						}
						// If it's a Waterway
						else if ((String) tags.get("waterway") != null) {
							
							switch ((String) tags.get("waterway")) {
							
								case "riverbank":								
									entity = new Water((Long) item.get("id"));
									break;
							}	
							
							// If not empty add it
							if (entity != null && item != null) {
								Map.loadRegion(entity, item, Map.CONTENT);		
							}						
						}
						// If it's a man_made
						else if ((String) tags.get("man_made") != null) {
							
							switch ((String) tags.get("man_made")) {
							
								case "bridge":						
									entity = new Bridge((Long) item.get("id"));
									break;
							}	
							
							// If not empty add it
							if (entity != null && item != null) {
								Map.loadRegion(entity, item, Map.CONTENT);	
							}						
						}
						// Buildings
						else if ((String) tags.get("building") != null) {
							
							switch ((String) tags.get("building")) {
							
								case "yes":								
								case "industrial":								
								case "greenhouse":	
								case "school":									
								case "hangar":									
								case "public":									
								case "residential":									
									entity = new Building((Long) item.get("id"));
									break;
							}
							
							// If not empty add it
							if (entity != null && item != null) {
								Map.loadRegion(entity, item, Map.CONTENT);
							}		
							
						}
						// If it's a Amenity
						else if ((String) tags.get("amenity") != null) {
							
							switch ((String) tags.get("amenity")) {

								case "prison":
									entity = new Prison(
										(Long) item.get("id"),
										(String) item.get("name")
									);
									break;
									
								case "parking":
									entity = new Parking(
										(Long) item.get("id"),
										(String) item.get("name")
									);
									break;
									
								case "hospital":
								case "school":
									entity = new fr.univavignon.ceri.deskmap.models.region.Leisure.School(
										(Long) item.get("id"),
										(String) item.get("name")
									);								
									break;
							}
							
							// If not empty add it
							if (entity != null && item != null) {	
								Map.loadRegion(entity, item, Map.CONTENT);
							}	
							
						}
						// If it's a railway
						else if ((String) tags.get("railway") != null) {
							
							Iterator<?> it;
							JSONArray nodes;
							Road entityRoad;
							
							// Set the identifier of the road
							entityRoad = new Road((Long) item.get("id"));		
							
							// Set the name of the road
							entityRoad.setName((String) tags.get("name"));
							
							// Set the max speed of the road
							String speed = (String) tags.get("maxspeed");
							
							if (speed != null) {
								
								int maxSpeed = 0;
								
								if (speed.equals("walk")) {
									maxSpeed = Speeds.PEDESTRIAN_SPEED;									
								}
								else {
									try {
									    maxSpeed = Integer.parseInt(speed);
									} catch(NumberFormatException ex){}									
								}
								
								if (maxSpeed > 0) {
									entityRoad.setMaxSpeed(maxSpeed);
								}
							}
							
							// Read the 'nodes' array
							nodes = (JSONArray) item.get("nodes");
							
							it = nodes.iterator();
							
							// For each node identifier of this array
							while (it.hasNext()) {
								
								// Read it
								String nodeId = it.next().toString();
								
								// Add the id to the nodes list of the GolfCourse
								entityRoad.addNode(nodeId);
								
								// The current node
								Node node = Map.NODES.get(Long.valueOf(nodeId));
								
								// Add the current road into the node
								node.addRoad(entityRoad);
							}
							
							switch ((String) tags.get("railway")) {
							
								case "abandoned":
									entityRoad.setType("ROAD");
									entityRoad.setColor(Color.ROAD);
									entityRoad.setThickness(Settings.LEVEL_4_ROAD_THICKNESS);
									
									// Allowed transport modes
									entityRoad.allowed.add(TransportModeNames.FOOT);
									entityRoad.allowed.add(TransportModeNames.BIKE);
									
									break;
							}

							// Add the road
							Map.ROADS.add(entityRoad);						
						}
						// If it's a Highway
						else if ((String) tags.get("highway") != null) {
							
							Iterator<?> it;
							JSONArray nodes;
							Road entityRoad;
							
							// Set the identifier of the road
							entityRoad = new Road((Long) item.get("id"));		
							
							// Set the name of the road
							entityRoad.setName((String) tags.get("name"));
							
							// Set the max speed of the road
							String speed = (String) tags.get("maxspeed");
							
							if (speed != null) {
								
								int maxSpeed = 0;
								
								if (speed.equals("walk")) {
									maxSpeed = Speeds.PEDESTRIAN_SPEED;									
								}
								else {
									try {
									    maxSpeed = Integer.parseInt(speed);
									} catch(NumberFormatException ex){}									
								}
								
								if (maxSpeed > 0) {
									entityRoad.setMaxSpeed(maxSpeed);
								}
							}
							
							// Does the road is one way ?
							String oneWay = (String) tags.get("oneway");
							if (oneWay != null) {
								entityRoad.setOneWay(oneWay.equals("yes") ? true : false );
							}
							
							// Read the 'nodes' array
							nodes = (JSONArray) item.get("nodes");
							
							it = nodes.iterator();
							
							// For each node identifier of this array
							while (it.hasNext()) {
								
								// Read it
								String nodeId = it.next().toString();
								
								// Add the id to the nodes list of the GolfCourse
								entityRoad.addNode(nodeId);
								
								// The current node
								Node node = Map.NODES.get(Long.valueOf(nodeId));
								
								// Add the current road into the node
								node.addRoad(entityRoad);
							}
							
							if ((String) tags.get("junction") != null) {
								entityRoad.setJunction((String) tags.get("junction"));
							} else {
								entityRoad.setJunction(null);
							}
							
							switch ((String) tags.get("highway")) {
							
								case "service":
									
									entityRoad.setType("SERVICE");
									entityRoad.setColor(Color.TERTIARY);
									entityRoad.setThickness(Settings.LEVEL_3_ROAD_THICKNESS);
									
									// Allowed transport modes
									entityRoad.allowed.add(TransportModeNames.CAR);						
									
									break;
							
								case "motorway":
									entityRoad.setType("MOTORWAY");
									entityRoad.setColor(Color.MOTORWAY);
									entityRoad.setThickness(Settings.LEVEL_1_ROAD_THICKNESS);
									
									// Allowed transport modes
									entityRoad.allowed.add(TransportModeNames.CAR);
									
									break;

								// Second biggest roads of the country after the motorways (Equivalent to the autobahn)
								case "trunk":
								case "trunk_link":
									entityRoad.setType("TRUNK");
									entityRoad.setColor(Color.TRUNK);
									entityRoad.setThickness(Settings.LEVEL_2_ROAD_THICKNESS);
									
									// Allowed transport modes
									entityRoad.allowed.add(TransportModeNames.CAR);							
									
									break;
									
								case "primary":
									entityRoad.setType("PRIMARY");
									entityRoad.setColor(Color.PRIMARY);
									entityRoad.setThickness(Settings.LEVEL_3_ROAD_THICKNESS);
																		
									// Allowed transport modes
									entityRoad.allowed.add(TransportModeNames.PUBLIC_TRANSPORT);
									entityRoad.allowed.add(TransportModeNames.CAR);
									entityRoad.allowed.add(TransportModeNames.FOOT);
									entityRoad.allowed.add(TransportModeNames.BIKE);
									
									break;
									
								case "secondary":
									entityRoad.setType("SECONDARY");
									entityRoad.setColor(Color.SECONDARY);
									entityRoad.setThickness(Settings.LEVEL_3_ROAD_THICKNESS);
									
									// Allowed transport modes
									entityRoad.allowed.add(TransportModeNames.PUBLIC_TRANSPORT);
									entityRoad.allowed.add(TransportModeNames.CAR);
									entityRoad.allowed.add(TransportModeNames.FOOT);
									entityRoad.allowed.add(TransportModeNames.BIKE);

									break;

								case "residential":
								case "tertiary":
								case "unclassified":
									entityRoad.setType("TERTIARY");
									entityRoad.setColor(Color.TERTIARY);
									entityRoad.setThickness(Settings.LEVEL_3_ROAD_THICKNESS);
									
									// Allowed transport modes
									entityRoad.allowed.add(TransportModeNames.PUBLIC_TRANSPORT);
									entityRoad.allowed.add(TransportModeNames.CAR);		
									entityRoad.allowed.add(TransportModeNames.FOOT);
									entityRoad.allowed.add(TransportModeNames.BIKE);							
									
									break;
									
								case "living_street":
								case "pedestrian":
								case "cycleway":
								case "footway":
								case "path":
								case "track":
								default:
									entityRoad.setType("ROAD");
									entityRoad.setColor(Color.ROAD);
									entityRoad.setThickness(Settings.LEVEL_4_ROAD_THICKNESS);
									
									// Allowed transport modes
									entityRoad.allowed.add(TransportModeNames.PUBLIC_TRANSPORT);
									entityRoad.allowed.add(TransportModeNames.FOOT);
									entityRoad.allowed.add(TransportModeNames.BIKE);
									
									break;
							}

							// Add the roads
							Map.ROADS.add(entityRoad);
							
						}	
					} else {
						// If no tags found, its a Way
						entity = new Way((Long) item.get("id"));
						Map.loadRegion(entity, item, Map.CONTENT);	
					}
				}				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * Load all the {@code Relation}'s from the JSON file
	 * @param city {@code String} Name of the city
	 * @author Yanis Labrak
	 */
	public static void loadRelations(String city) {
		
		//JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();
		
//		try (FileReader reader = new FileReader(city.toLowerCase() + "Map.json"))
		try	{
        	
            String path = city + "Map.json";
            FileReader fichier = new FileReader(path);
            
			//Read JSON file
			Object obj = jsonParser.parse(fichier);
			
			JSONObject main = (JSONObject) obj;
			JSONArray elements = (JSONArray) main.get("elements");
			
			Iterator<?> iterator = elements.iterator();
			
			// For each JSON object
			while (iterator.hasNext()) {
				
				JSONObject item = (JSONObject) iterator.next();
				
				// Get type
				String type = (String) item.get("type");
				
				// If its a relation
				if (type.toLowerCase().equals("relation")) {
					
					// Members iterator
					Iterator<?> it;
					
					// Read the array of ways
					JSONArray members = (JSONArray) item.get("members");
					
					// Get tags
					JSONObject tags = (JSONObject) item.get("tags");	
					
					Way baseWay;

					// If we have tags
					if (tags != null ) {	
						
						it = members.iterator();
						
						int i = 0;
						
						// for each ways of this array
						while (it.hasNext()) {
							
							// Get the way identifier
							String wayId = ((JSONObject) it.next()).get("ref").toString();
							
							// Set the name of the place on the way
//							Map.mapContent.get(Long.parseLong(wayId)).setName(name);							
							
							// If the corresponding way exist
							if (Map.CONTENT.get(Long.parseLong(wayId)) != null && Map.CONTENT.get(Long.parseLong(wayId)) instanceof Way) {
																
								baseWay = (Way) Map.CONTENT.get(Long.parseLong(wayId));
								
								// Create a new Way
								Region newWay = null;
								
								// Depending of the type
								if (tags.get("landuse") != null) {
									
									switch ((String) tags.get("landuse")) {
									
										case "residential":		
											newWay = new Residential(
												Long.parseLong(wayId) + i++
											);										
											break;
											
										case "industrial":		
											newWay = new Industrial(
												Long.parseLong(wayId) + i++
											);										
											break;
											
										case "commercial":	
											newWay = new Commercial(
												Long.parseLong(wayId) + i++
											);										
											break;
											
										case "retail":	
											newWay = new Retail(
												Long.parseLong(wayId) + i++
											);										
											break;
											
										case "railway":	
											newWay = new Railway(
												Long.parseLong(wayId) + i++
											);										
											break;
											
										case "cemetery":
											newWay = new Cemetery(
												Long.parseLong(wayId) + i++
											);
											break;
											
										case "forest":
											newWay = new Forest(
												Long.parseLong(wayId) + i++
											);										
											break;
											
										case "farmland":
										case "farmyard":
										case "greenhouse_horticulture":
											newWay = new FarmLand(
													Long.parseLong(wayId) + i++
													);										
											break;
											
										case "grass":
										case "allotments":
											newWay = new Grass(
												Long.parseLong(wayId) + i++
											);											
											break;
											
										case "quarry":
											newWay = new Quarry(
													Long.parseLong(wayId) + i++
													);											
											break;
									}									
								}
								else if (tags.get("building") != null) {
									switch ((String) tags.get("building")) {
										default:						
											newWay = new Building(
												Long.parseLong(wayId) + i++
											);											
											break;
									}
								}
								else if (tags.get("amenity") != null) {
									switch ((String) tags.get("amenity")) {
										case "school":		
											newWay = new School(
												Long.parseLong(wayId) + i++
											);										
											break;
									}
								}
								else if (tags.get("highway") != null) {
									
									
									if (tags.get("highway").equals("pedestrian")) {
										newWay = new Pedestrian(
											Long.parseLong(wayId) + i++
										);
										newWay.setColor(Color.PEDESTRIAN);
									}
									else {
										newWay = new fr.univavignon.ceri.deskmap.models.region.Road.Road(
											Long.parseLong(wayId) + i++
										);
										
										switch ((String) tags.get("highway")) {
										
											case "motorway":
												newWay.setColor(Color.MOTORWAY);
												break;
		
											case "trunk":
												newWay.setColor(Color.TRUNK);
												break;
												
											case "primary":
												newWay.setColor(Color.PRIMARY);
												break;
												
											case "secondary":
												newWay.setColor(Color.SECONDARY);
												break;
												
											case "tertiary":
												newWay.setColor(Color.TERTIARY);
												break;
												
											case "residential":
											case "living_street":
											case "unclassified":
											case "cycleway":
											default:
												newWay.setColor(Color.ROAD);
												break;
										}
									}
								}
								else if (tags.get("leisure") != null) {
									switch ((String) tags.get("leisure")) {
									
										case "sports_centre":
											newWay = new SportsCentre(
												Long.parseLong(wayId) + i++
											);	
											newWay.setColor(Color.SPORT_CENTRE);
											break;
	
										case "park":
											newWay = new Park(
												Long.parseLong(wayId) + i++
											);	
											newWay.setColor(Color.PARK);
											break;
											
										case "golf_course":
											newWay = new GolfCourse(
												Long.parseLong(wayId) + i++
											);	
											newWay.setColor(Color.GOLF_COURSE);
											break;
									}
								}
								else if (tags.get("natural") != null) {
									switch ((String) tags.get("natural")) {
									
										case "water":
										case "bay":
											newWay = new Water(
												Long.parseLong(wayId) + i++
											);	
											newWay.setColor(Color.WATER);
											break;
											
										case "scrub":
											newWay = new Forest(
													Long.parseLong(wayId) + i++
													);	
											newWay.setColor(Color.WATER);
											break;
	
										case "wood":
											newWay = new Wood(
												Long.parseLong(wayId) + i++
											);	
											newWay.setColor(Color.WOOD);
											break;
									}
								}
								else if (tags.get("waterway") != null) {
									switch ((String) tags.get("waterway")) {
									
									case "riverbank":
										newWay = new Water(
											Long.parseLong(wayId) + i++
										);	
										newWay.setColor(Color.WATER);
										break;
									}
								}
								
								// If one of the above class
								if (newWay != null) {
									
									// Load each nodes inside the new Way
									for (Long nodeId : baseWay.getNodes()) {										
										newWay.addNode(nodeId);										
									}

									// Add the way
									Map.CONTENT.put(newWay.id + i, newWay);
								}								
							}
						}							
					}					
				}				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
	}
	

	
	/**
	 * Load all the {@code Relation}'s from the JSON file
	 * @param city {@code String} Name of the city
	 * @author Yanis Labrak
	 */
	public static void loadPublicTransportsRoutes(String city) {
		
		//JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();
		
//		try (FileReader reader = new FileReader(city.toLowerCase() + "Map.json"))
		try
		{
        	
            String path = city + "Map.json";
            FileReader fichier = new FileReader(path);
			
			//Read JSON file
			Object obj = jsonParser.parse(fichier);
			
			JSONObject main = (JSONObject) obj;
			JSONArray elements = (JSONArray) main.get("elements");
			
			Iterator<?> iterator = elements.iterator();
			
			// For each JSON object
			while (iterator.hasNext()) {
				
				JSONObject item = (JSONObject) iterator.next();
				
				// Get type
				String type = (String) item.get("type");
				
				// If its a relation
				if (type.toLowerCase().equals("relation")) {
					
					// Members iterator
					Iterator<?> it;
					
					// Read the array of ways
					JSONArray members = (JSONArray) item.get("members");
					
					// Get tags
					JSONObject tags = (JSONObject) item.get("tags");
					
					Way baseWay;

					// If we have tags
					if (tags != null) {	
												
						if (tags.get("route") == null || tags.get("network") == null || !tags.get("network").equals("Irigo")) {
							continue;
						}
												
						String color = (String) tags.get("colour");
						String header = (String) tags.get("name");
						String from = (String) tags.get("from");
						String to = (String) tags.get("to");
						String number = (String) tags.get("ref");
						String route = (String) tags.get("route");
												
						it = members.iterator();
						
						int i = 0;
						
						// for each ways of this array
						while (it.hasNext()) {
							
							// Get the way identifier
							String wayId = ((JSONObject) it.next()).get("ref").toString();
							
							// Set the name of the place on the way
//							Map.mapContent.get(Long.parseLong(wayId)).setName(name);							
							
							// If the corresponding way exist
							if (Map.CONTENT.get(Long.parseLong(wayId)) != null && Map.CONTENT.get(Long.parseLong(wayId)) instanceof Way) {
																
								baseWay = (Way) Map.CONTENT.get(Long.parseLong(wayId));
								
								// Create a new Way
								PublicTransportRoute routeSegment = null;	
								
								routeSegment = new PublicTransportRoute(
									Long.parseLong(wayId) + i++
								);		
								
								// If one of the above class
								if (routeSegment != null) {
																		
									// Set the route color
									routeSegment.setColor(color);
									
									// Set from -> to header
									routeSegment.setHeader(header);
									
									// Set departure
									routeSegment.setFrom(from);
									
									// Set destination
									routeSegment.setTo(to);
									
									// Set tc line number
									routeSegment.setNumber(number);
																		
									// If the line isn't already present add it			
									if (!MainViewController.SELECTED_PUBLIC_TRANSPORTS.stream().filter(cb -> cb.line.equals(number.toUpperCase())).findFirst().isPresent()) {
										MainViewController.SELECTED_PUBLIC_TRANSPORTS.add(new CheckBoxPublicTransport(number));
									}
									
									// Set type of tc
									routeSegment.setRoute(route);
									
									// Load each nodes inside the new Way
									for (Long nodeId : baseWay.getNodes()) {
										
										// Add the node to the route segment
										routeSegment.addNode(nodeId);
										
										// Add the route segment to the node
										Map.NODES.get(nodeId).addPublicTransportRoute(routeSegment);									
									}

									// Add the Road
									Map.PUBLIC_TRANSPORT_ROUTES.add(routeSegment);
								}								
							}
						}							
					}					
				}				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Return all the roads nodes
	 * @return {@code List<Node>}
	 */
	public static List<Node> getRoadsNodes() {
		
		// List of all the roads nodes
		List<Node> nodes = new ArrayList<Node>();
		
		// For each roads of the map
		for (Road r : Map.ROADS) {
			
			// Add the nodes of the road
			nodes.addAll(r.getNodesInstances());
		}		
		
		return nodes;		
	}
	
}