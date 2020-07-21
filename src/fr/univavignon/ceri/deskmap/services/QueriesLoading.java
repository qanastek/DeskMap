package fr.univavignon.ceri.deskmap.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import DeskMapExceptions.CannotReachServerException;
import fr.univavignon.ceri.deskmap.Map;
import fr.univavignon.ceri.deskmap.controllers.MainViewController;
import fr.univavignon.ceri.deskmap.models.Node;
import fr.univavignon.ceri.deskmap.models.Schedule;
import fr.univavignon.ceri.deskmap.models.Modes.PublicTransport;
import fr.univavignon.ceri.deskmap.models.geopoint.BusStation;
import fr.univavignon.ceri.deskmap.models.geopoint.City;
import fr.univavignon.ceri.deskmap.models.line.Road;

/**
 * All the functions which is used for loading and downloading the queries results
 * @author Yanis Labrak
 */
public abstract class QueriesLoading {
	
	/**
	 * Load cities from cache or API
	 * @param query {@code String} Query to send to the OSM API
	 * @param outputName {@code String} name of the output file
	 * @throws CannotReachServerException Exception thrown when the server cannot be reached
	 * @author Yanis Labrak
	 */
	public static void loadQueryInFile(String query, String outputName) throws CannotReachServerException {
		
		System.out.println("Cache creation");
		
		try {
			System.out.println("Query: " + query);
			System.out.println("Output: " + outputName);
			
			// Make the URL
			URL queryUrl = new URL(query);
			
			// Open the stream
			ReadableByteChannel stream = Channels.newChannel(queryUrl.openStream());
			
			// Create the output file
			FileOutputStream outputFile = new FileOutputStream(outputName);
			
			// Copy the line into the output file
			outputFile.getChannel().transferFrom(stream, 0, Long.MAX_VALUE);
			
			outputFile.close();
			
			System.out.println("Caching done !");
		    
		} catch (Exception e) {
			throw new CannotReachServerException();
		}
	}	
	
	/**
	 * Load the cities into their list
	 * @param nominatimUrl {@code String} Query URL
	 * @param isCoordinate {@code Boolean}
	 */
	public static void loadCities(String nominatimUrl, Boolean isCoordinate) {
		
		JSONObject loadedQuery = null;
		
		try {
			loadedQuery = QueriesLoading.getQueryResult(nominatimUrl);
		} catch (IOException | ParseException | CannotReachServerException e) {
			e.printStackTrace();
		}	
		
		JSONArray items = (JSONArray) loadedQuery.get("features");
		Iterator<JSONObject> i = items.iterator();
				
        while (i.hasNext()) {
        	
        	JSONObject item = (JSONObject) i.next();
        	
        	JSONObject properties = (JSONObject) item.get("properties");
        	
        	JSONObject geometry = (JSONObject) item.get("geometry");
			JSONArray coordinates = (JSONArray) geometry.get("coordinates");
        	
			Long osm_id = (Long) properties.get("osm_id");
            Double lat = (Double) coordinates.get(1);
            Double lon = (Double) coordinates.get(0);
            String country = (String) properties.get("country");
            String name = (String) properties.get("name");
            String state = (String) properties.get("state");
            String osm_value = (String) properties.get("osm_value");
            
            // If this is coordinates
            if (isCoordinate || osm_value.equals("city") || osm_value.equals("village")) {
				MainViewController.LIST_CITIES_SORTED.add(
					new City(osm_id.toString(), lat, lon, name, state, country)
				);
			}
        }
	}
	
	/**
	 * Get the query result directly
	 * @param query {@code String} Query to send to the OSM API
	 * @throws CannotReachServerException Exception thrown when the server cannot be reached
	 * @return {@code JSONObject} The full object which contain all the results
	 * @throws IOException Thrown when the file is missing
	 * @throws MalformedURLException Thrown when the {@code URL} isn't complete
	 * @throws ParseException Thrown when the {@code JSONObject} cannot be parse
	 * @author Yanis Labrak
	 */
	public static JSONObject getQueryResult(String query) throws CannotReachServerException, MalformedURLException, IOException, ParseException {
		
	    InputStream is = new URL(query).openStream();
	     
	    try {
	    	
	    	InputStreamReader rd = new InputStreamReader(is);
	    	
			StringBuilder sb = new StringBuilder();
			
			int cp;			
			while ((cp = rd.read()) != -1) {
				sb.append((char) cp);
			}
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(sb.toString());
			
			return json;
	      
	    } finally {
	      is.close();
	    }
	    
	}

	/**
	 * Load the streets of the city into the variables of the {@code comboBox}'s
	 * @author Yanis Labrak
	 */
	public static void loadStreets() {
		
		// Clear the current list of streets
		MainViewController.LIST_STREETS_NAMES.clear();
		
		// Load all the street inside listStreetName
		for (Road r : Map.ROADS) {

//	    	System.out.println("Before name");
	    	if (r.name != null && !r.name.isEmpty() && !r.name.equals("")) {
	    		
	    		boolean present = false;
	    		
	    		for (Road rd : MainViewController.LIST_STREETS_NAMES) {
	    			if (rd.name.equals(r.name)) {
						present = true;
					}
				}
	    		
	    		if (present == false) {
		    		MainViewController.LIST_STREETS_NAMES.add(r);
				}	    	
			}	
		}
		
		// Reassign the comboBox auto-completion of the departure field
		MainViewController.LIST_STREETS_NAMES_SORTED_FROM.clear();
		MainViewController.LIST_STREETS_NAMES_SORTED_FROM.addAll(MainViewController.LIST_STREETS_NAMES);

		// Reassign the comboBox auto-completion of the arrival field
		MainViewController.LIST_STREETS_NAMES_SORTED_TO.clear();
		MainViewController.LIST_STREETS_NAMES_SORTED_TO.addAll(MainViewController.LIST_STREETS_NAMES);
	}
	
	/**
	 * Load the public transports of Angers into {@code Map.publicTransports}
	 * @author Zhiao Zheng
	 */
	public static void loadPublicTransports() {
		
		// Delete the old one
		Map.PUBLIC_TRANSPORTS_POSITIONS.clear();
		
		// Get the API request URL
		String url = DataAngers.getPublicTransportPositionQuery();
		
		JSONObject loadedQuery = null;
		
		try {
			loadedQuery = QueriesLoading.getQueryResult(url);
		} catch (Exception |CannotReachServerException e) {
			MainViewController.addStateBar("The public transport position cannot be actualize due to servers issues !");
			return;
		}
		
//		System.out.println("Fetch the public transports positions...");
		
		JSONArray items = (JSONArray) loadedQuery.get("records");
		Iterator<JSONObject> i = items.iterator();
		
        while (i.hasNext()) {
        	
        	JSONObject item = (JSONObject) i.next();
        	
        	JSONObject fields = (JSONObject) item.get("fields");
        	JSONObject geometry = (JSONObject) item.get("geometry");
        	
    		JSONArray coordinates = (JSONArray) geometry.get("coordinates");
        	        	
            String nameLine = (String) fields.get("nomligne");
            String nbrLine = (String) fields.get("mnemoligne");
            String type = (String) fields.get("type");
            String destination = (String) fields.get("dest");

            Double y = (Double) coordinates.get(1);
            Double x = (Double) coordinates.get(0);
            
        	PublicTransport transport = new PublicTransport(nameLine, nbrLine, type, destination, x, y);

        	Map.PUBLIC_TRANSPORTS_POSITIONS.add(transport);
        }
		
	}
	
	/**
	 * Load the public transports schedules of Angers into {@code Map.SCHEDULES}
	 * @author Zhiao Zheng
	 */
	public static void loadPublicTransportsSchedules() {
		
		// Get the API request URL
		String url = DataAngers.getPublicTransportSchedulesIrigoQuery();
		
		String fileName = "angersPublicTransportsSchedules.json";
		
		File file = new File(fileName);
        
		// Check if the file need to be updated
		QueriesLoading.updateTheFileIfNecessary(file,url,fileName);

        JSONParser jsonParser = new JSONParser();
        FileReader fichier = null;
        JSONObject loadedQuery = null;
        Object obj = null;
        
        /**
         * Load the file
         */
		try {
			fichier = new FileReader(fileName);
	        obj = jsonParser.parse(fichier);
			loadedQuery = (JSONObject) obj;
			System.out.println(fileName + " loaded !");
	        
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}	
		
		// Check if empty
		if (loadedQuery == null) {
			System.err.println("Cannot find the schedules for the city of angers");
			return;
		}
				
		// All the objects
		JSONArray items = (JSONArray) loadedQuery.get("records");
		// The iterator for the array
		Iterator<JSONObject> i = items.iterator();
		
		while (i.hasNext()) {
			
			// Current object
			JSONObject item = (JSONObject) i.next();
			
			// Fields
			JSONObject fields = (JSONObject) item.get("fields");
			
			// nomarret
			String nomarret = (String) fields.get("nomarret");
			
			// mnemoarret
			String mnemoarret = (String) fields.get("mnemoarret");
			
			// nomligne
			String nomligne = (String) fields.get("nomligne");
			
			// mnemoligne
			String mnemoligne = (String) fields.get("mnemoligne");
			
			// coordonnees
			JSONArray coordonnees = (JSONArray) fields.get("coordonnees");
            Double x = (Double) coordonnees.get(0);			
            Double y = (Double) coordonnees.get(1);
			
			// arriveetheorique
			String arriveetheorique = (String) fields.get("arriveetheorique");
			
			Date date = null;
			try {			
								
				// If empty pass
				if (arriveetheorique == null) { break; }
				
				// Formated as a date
			    date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(arriveetheorique);
			    			    
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			
			// The coordinates
			Node node = new Node(x,y);
			
			// Current bus station
			BusStation station = new BusStation(node,mnemoarret,nomarret);
			
			// The schedule for this bus station
			Schedule schedule = new Schedule(mnemoligne,nomligne,date);

			// Does the bus station is already present in the list ?
			boolean present = false;
			
			// If the line isn't already present add it			
			if (!MainViewController.PUBLIC_TRANSPORTS_ALLOWED.stream().filter(l -> l.equals(mnemoligne)).findFirst().isPresent()) {
				MainViewController.PUBLIC_TRANSPORTS_ALLOWED.add(mnemoligne);
			}
			
			// Check if already exist
			for (BusStation busStation : Map.BUS_STATIONS_PATH) {
				
				// If found
				if (busStation.mnemoarret.equals(mnemoarret)) {
					
					present = true; // Present
					station = busStation; // The bus station
					break; // Stop
				}
			}
			
			// Add the schedule to the bus station
			station.schedules.add(schedule);
			
			// Add bus line to the station
			station.addBusLine(mnemoligne, nomligne);
			
			// If not already stored
			if (present == false) {
				Map.BUS_STATIONS_PATH.add(station);
			}
		}
	}

	/**
	 * Update the file if it's necessary
	 * @param file {@code File} The file
	 * @param url {@code String} The URL if the file is out dated
	 * @param fileName {@code String} The output file name
	 */
	private static void updateTheFileIfNecessary(File file, String url, String fileName) {

		// If the file exist
        if (file.exists()) {

            long diff = new Date().getTime() - file.lastModified();
            
            int days = 1;
            int hours = 8;
            
            // Check if the file is out dated
            if (diff > days * hours * 60 * 60 * 1000) {
            	
                file.delete();
                
				try {
					
					String msg = "Download angersPublicTransportsSchedules!";
					System.out.println(msg);
					MainViewController.addStateBar(msg);
					
					QueriesLoading.loadQueryInFile(url,fileName);
					
				} catch (CannotReachServerException e1) {
					e1.printStackTrace();
				} 
            }
            
		} else {			

			try {
				
				System.out.println("Download angersPublicTransportsSchedules bis");
				QueriesLoading.loadQueryInFile(url,fileName);
				
			} catch (CannotReachServerException e1) {
				e1.printStackTrace();
			}  
		}
	}
}
