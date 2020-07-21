package fr.univavignon.ceri.deskmap.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.univavignon.ceri.deskmap.Map;
import fr.univavignon.ceri.deskmap.config.Settings;
import fr.univavignon.ceri.deskmap.controllers.MainViewController;
import fr.univavignon.ceri.deskmap.models.Bbox;
import fr.univavignon.ceri.deskmap.models.GeoData;
import fr.univavignon.ceri.deskmap.models.Node;
import fr.univavignon.ceri.deskmap.models.Modes.PublicTransport;
import fr.univavignon.ceri.deskmap.models.geopoint.BusStation;
import fr.univavignon.ceri.deskmap.models.line.Line;
import fr.univavignon.ceri.deskmap.models.line.Path;
import fr.univavignon.ceri.deskmap.models.line.PublicTransportRoute;
import fr.univavignon.ceri.deskmap.models.line.River;
import fr.univavignon.ceri.deskmap.models.line.Road;
import fr.univavignon.ceri.deskmap.models.region.Region;
import fr.univavignon.ceri.deskmap.models.region.Amenity.Amenity;
import fr.univavignon.ceri.deskmap.models.region.Landuse.Landuse;
import fr.univavignon.ceri.deskmap.models.region.Leisure.Leisure;
import fr.univavignon.ceri.deskmap.models.region.Natural.Natural;
import fr.univavignon.ceri.deskmap.models.region.Road.Pedestrian;
import fr.univavignon.ceri.deskmap.models.region.Structure.Structure;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * @author Yanis Labrak
 */
public class Draw {
	
	/**
	 * Draw all the ways on the {@code Canvas}
	 * @author Yanis Labrak
	 */
	public static void drawWays() {
		
		// Draw Landuse
		Draw.drawRegion(Landuse.class);
				
		// Draw rivers
		Draw.drawRegion(River.class);

		// Draw Leisure
		Draw.drawRegion(Leisure.class);
		
		// Draw Natural
		Draw.drawRegion(Natural.class);
		
		// Draw Amenity
		Draw.drawRegion(Amenity.class);

		// Draw roads
		Draw.drawRoads();
		
		// Pedestrian
		Draw.drawRegion(Pedestrian.class);
		
		// Draw buildings		
		Draw.drawRegion(Structure.class, true);		
	}
	
	/**
	 * Draw the streets names
	 */
	public static void drawStreetsName() {

		int fontSize = (int) (1 * Map.SCALE / 1.5);
		
		MainViewController.gc.setLineWidth(1.0);
		MainViewController.gc.setFill(Color.BLACK);
		MainViewController.gc.setFont(new Font("Verdana", fontSize));
        
        // Handle empty case
        if (Map.CONTENT == null || Map.CONTENT.isEmpty()) {
        	return;			
		}
        
		for (GeoData g: Map.CONTENT.values()) {
			
			if (Map.SCALE > 5 && g instanceof Road && ((Road) g).getNodes() != null && ((Road) g).getNodes().size() > 3) {
				
				Node center = ((Road) g).getMiddle();
				
				if (center == null) {
					continue;
				}
				
	    		// Coordinate after processing
	    		List<Double> coordinates = Node.toPixel(center.lat, center.lon);	    		
	    		
	    		Double x = coordinates.get(0);
	    		Double y = Map.HEIGHT - coordinates.get(1);
	    		
	    		if (x == null || y == null) {
					continue;
				}
			    
			    if (Bbox.isContained(x, y, Bbox.bboxScreen)) {
			    	MainViewController.gc.fillText(((Road) g).name, x, y);		    		
			    }
				
			}
			
		}
		
	}
	
	/**
	 * Display on the map a texture according to a {@code Node}
	 * @param node {@code Node} The location to display
	 * @param texture {@code String} The texture path
	 * @author Yanis Labrak
	 */
	public static void drawNodeTexture(Node node, String texture) {
		Draw.drawNodeTexture(node, texture, Draw.iconSize());
	}
	
	/**
	 * Display on the map a texture according to a {@code Node}
	 * @param node {@code Node} The location to display
	 * @param texture {@code String} The texture path
	 * @param size {@code Double} Size
	 * @author Yanis Labrak
	 */
	public static void drawNodeTexture(Node node, String texture, Double size) {

        // Handle empty case
        if (node == null || texture == null || texture.isEmpty()) {
        	return;
		}
	    
		// Coordinate after processing
		List<Double> coordinates = Node.toPixel(node.lat, node.lon);
		
		Double x = coordinates.get(0) - (size/2);
		Double y = Map.HEIGHT - coordinates.get(1) - size;
		
		if (x == null || y == null) {
			return;
		}
		
	    if (Bbox.isContained(x, y, Bbox.bboxScreen)) {

	    	// Draw the icon
			MainViewController.gc.drawImage(
				new Image(Draw.class.getResourceAsStream(texture)),
				x,
				y,
				size,
				size
			);
    		
		}
	}	

	/**
	 * Display on the map a point of interest
	 * @param node {@code Node} The node to display
	 * @author Yanis Labrak
	 */
	public static void displayPOI(Node node) {
	       
	// Handle empty case
	if (node == null) {
		return;
	}
 
		Draw.drawNodeTexture(node, fr.univavignon.ceri.deskmap.config.Textures.PIN);
	}
	
	/**
	 * Return the size of the busStation icon according to the level of zoom
	 * @return {@code Double} The output size
	 */
	public static Double iconSize() {
		
		// Zoom lvl 1
		if (Map.SCALE > 14) {
			return Map.SCALE * 2;
		}
		// Zoom lvl 2
		else if (Map.SCALE < 14 && Map.SCALE > 4.4) {
			return Map.SCALE * 4;
		}
		// Zoom lvl 3
		else if (Map.SCALE < 4.5 && Map.SCALE > 1.2) {
			return Map.SCALE * 11;
		}
		else {
			return Map.SCALE * 13;
		}
		
	}

	/**
	 * Draw all the {@code Nodes} on the {@code Canvas}
	 * @param gc {@code GraphicsContext} The canvas
	 * @param bbox {@code Bbox} The {@code Bbox} where the {@code Node}'s need to be for being displayed
	 * @author Yanis Labrak
	 */
	public static void drawNodes(GraphicsContext gc, Bbox bbox) {
		
		// Handle empty case
		if (bbox == null || gc == null) {
			return;
		}

	    gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);
				
		// Handle empty case
		if (Map.NODES == null || Map.NODES.size() <= 0) {
			return;
		}
		
		for (Long key : Map.NODES.keySet()) {
			
			if (key == null) {
				continue;
			}
		    
		    Node node = Map.NODES.get(key);
		    
		    if (node == null) {
				continue;
			}
		    
    		// Coordinate after processing
    		List<Double> coordinates = Node.toPixel(node.lat, node.lon);
    		
    		if (coordinates == null || coordinates.size() < 2) {
				continue;
			}
    		
    		Double x = coordinates.get(0);
    		Double y = Map.HEIGHT - coordinates.get(1);
	    		
		    if (x != null && y != null && Bbox.isContained(x, y, bbox)) {

	    		gc.fillOval(x, y, Map.SCALE/5, Map.SCALE/5);
	    		
			}
			
		}
	}
	
	/**
	 * Convert a {@code List<Double>} to a {@code double[]}
	 * @param doubles {@code List<Double>} List of double which will be converted
	 * @return {@code double[]} Return a {@code Array} of {@code Double}
	 * @author Yanis Labrak
	 */
	public static double[] convertDoubles(List<Double> doubles)
	{
		// Handle empty case
		if (doubles == null || doubles.size() <= 0) {
			System.err.println("ConvertDoubles empty");
			return null;
		}
		
	    double[] ret = new double[doubles.size()];
	    
	    Iterator<Double> iterator = doubles.iterator();
	    
	    int i = 0;
	    
	    while(iterator.hasNext())
	    {
	        ret[i] = iterator.next();
	        i++;
	    }
	    
	    return ret;
	}
	
	/**
	 * Draw the Region in argument on the {@code Canvas}
	 * @param gc {@code GraphicsContext} The canvas
	 * @param prop {@code Region} The {@code Region} to draw
	 * @author Yanis Labrak
	 */
	public static void drawPropRegion(GraphicsContext gc, Region prop) {
		Draw.drawPropRegionStroke(gc, prop, false);
	}
	
	/**
	 * Draw the Region in argument on the {@code Canvas}
	 * @param gc {@code GraphicsContext} The canvas
	 * @param prop {@code Region} The {@code Region} to draw
	 * @param stroke {@code Boolean} Draw with stroke or not
	 * @author Yanis Labrak
	 */
	public static void drawPropRegionStroke(GraphicsContext gc, Region prop, boolean stroke) {
		
		// Handle empty case
		if (prop == null || gc == null) {
			return;
		}
		
		List<Long> nodes = prop.getNodes();
		
		// Handle empty case
		if (nodes == null || nodes.size() <= 0) {
			return;
		}
		
		gc.setFill(Color.web(prop.getColor()));
		gc.setStroke(Color.web(prop.getColor()));
		
		List<Double> x = new ArrayList<Double>();
		List<Double> y = new ArrayList<Double>();
		
		if (x == null || y == null) {
			return;
		}
		
		// For each node id
		for (Long nodeId : nodes) {
			
			if (nodeId == null) {
				continue;
			}
			
			// Get the correspondent node
			Node node = Map.NODES.get(nodeId);
			
			// Coordinate after processing
			List<Double> coordinates = Node.toPixel(node.lat, node.lon);
			
			if (coordinates == null || coordinates.size() < 2) {
				continue;
			}
			
			x.add(coordinates.get(0));
			y.add(Map.HEIGHT - coordinates.get(1));   		
		}
		
		// Draw the building
		gc.fillPolygon(Draw.convertDoubles(x), Draw.convertDoubles(y), x.size());
		
		// If the stroke is enabled
		if (stroke == true && Map.SCALE >= Settings.ROADS_STROKE_MIN_SCALE) {
			gc.setStroke(Color.web(fr.univavignon.ceri.deskmap.config.Color.BUILDING_STROKE));
			gc.strokePolygon(Draw.convertDoubles(x), Draw.convertDoubles(y), x.size());
		}
	}
	

	/**
	 * Draw the Line in argument on the {@code Canvas}
	 * @param gc {@code GraphicsContext} The canvas
	 * @param prop {@code Line} The {@code Line} to draw
	 * @author Yanis Labrak
	 */
	public static void drawPropLine(GraphicsContext gc, Line prop) {
		Draw.drawPropLine(gc,prop,true);
	}	
	
	/**
	 * Draw the Line in argument on the {@code Canvas}
	 * @param gc {@code GraphicsContext} The canvas
	 * @param prop {@code Line} The {@code Line} to draw
	 * @param allowed {@code String} The highlight color
	 * @author Yanis Labrak
	 */
	public static void drawPropLine(GraphicsContext gc, Line prop, boolean allowed) {
		
		// Handle empty case
		if (prop == null || gc == null) {
			return;
		}
				
		List<Long> nodes = prop.getNodes();
		
		// Handle empty case
		if (nodes == null || nodes.size() <= 0) {
			return;
		}
		
		// Set up the main color according to if the road is allowed or not
		if (Settings.ENABLE_CANT_GO && allowed == false) {
			
			// Darker
			gc.setFill(fr.univavignon.ceri.deskmap.config.Color.darker(Color.web(prop.getColor())));
			gc.setStroke(fr.univavignon.ceri.deskmap.config.Color.darker(Color.web(prop.getColor())));
		} else {
			
			// Normal
			gc.setFill(Color.web(prop.getColor()));
			gc.setStroke(Color.web(prop.getColor()));
		}
				
		gc.setLineWidth(prop.getThickness());
		
		List<List<Double>> allNodes = new ArrayList<List<Double>>();
		
		if (nodes == null || nodes.size() <= 0) {
			return;
		}
		
		// Load each node in a List
		for (Long nodeId : nodes) {
			
			// Check null
			if (nodeId == null) {
				continue;
			}
			
			// Get the correspondent node
			Node node = Map.NODES.get(nodeId);
			
			// Check null
			if (node == null || node.lat == null || node.lon == null) {
				continue;
			}
			
			// Coordinate after processing
			List<Double> coordinates = Node.toPixel(node.lat, node.lon);
			
			// Check null
			if (coordinates == null || coordinates.size() <= 0) {
				continue;
			}
			
			List<Double> nodeAdd = new ArrayList<Double>();
			nodeAdd.add(coordinates.get(0));			    		
			nodeAdd.add(Map.HEIGHT- coordinates.get(1));
			
			// Check empty
			if (nodeAdd == null || nodeAdd.size() <= 0) {
				continue;
			}
			
			allNodes.add(nodeAdd);
		}
		
		// Check null
		if (allNodes == null || allNodes.size() < 2) {
			return;
		}
		
		// Draw the segment of the Road
		for (int i = 0; i < allNodes.size() - 1; i++) {
			
			List<Double> now = allNodes.get(i);
			List<Double> next = allNodes.get(i + 1);
			
			// If null
			if (now == null || next == null) {
				continue;
			}
			
			gc.strokeLine(
				now.get(0),
				now.get(1),
				next.get(0),
				next.get(1)
			);
		}
	}
	
	/**
	 * Draw region on the {@code Canvas}
	 * @param type {@code Class} The type
	 * @author Yanis Labrak
	 */
	public static void drawRegion(Class<?> type) {
		Draw.drawRegion(type, false);
	}

	/**
	 * Draw region on the {@code Canvas}
	 * @param type {@code Class} The type
	 * @param stroke {@code Boolean} Display the stroke or not
	 * @author Yanis Labrak
	 */
	public static void drawRegion(Class<?> type, boolean stroke) {
		
		// Handle empty case
		if (MainViewController.gc == null || Map.CONTENT == null || Map.CONTENT.size() <= 0) {
			return;
		}
		
		for (Long key : Map.CONTENT.keySet()) {
			
			// Empty check
			if (key == null) {
				continue;
			}
		    
		    Object prop = Map.CONTENT.get(key);
		    
		    if (prop != null && prop instanceof Region) {	
		    	
		    	// Check interface
		    	if (type.isInstance(prop)) {
		    		
			    	Draw.drawPropRegionStroke(MainViewController.gc, (Region) prop, stroke);
	    		}
			}			
		}
	}
		
	/**
	 * Draw the roads layout on the {@code Canvas} and also the one way arrows
	 * @author Yanis Labrak
	 */
	public static void drawRoads() {
		
		// Handle empty case
		if (MainViewController.gc == null || Map.ROADS == null || Map.ROADS.size() <= 0) {
			return;
		}
		
		double angle;
		
		// Get the size of the icon
		Double size = Draw.iconSize()/4;
		Double halfSize = (size/2);
		
		// For each road
		for (Road r : Map.ROADS) {
			
			// If isn't null
			if(r != null && r.isAllowed() == true) {
				Draw.drawPropLine(MainViewController.gc, r);
			} else {
				Draw.drawPropLine(MainViewController.gc, r, false);				
			}
			
			/**
			 * Add the arrow if it's a oneway
			 * @author Quentin Capedepon
			 */
			if (r.isOneWay() == true) {
			
				// Make a backup of the the current canvas
				MainViewController.gc.save();
				
				// Get the middle of the road
				Node center = r.getMiddle();
				
				// Coordinate after processing
				List<Double> coordinates = Node.toPixel(center.lat, center.lon);
				
				// Get converted coordiates
				Double x = coordinates.get(0);
				Double y = Map.HEIGHT - coordinates.get(1);
				
				// Check empty
				if (x == null || y == null) { return; }
				
				// Check if the arrow will be displayed in the current screen tile
			    if (Bbox.isContained(x, y, Bbox.bboxScreen)) {
			    	
			    	// The current angle
			    	angle = r.getAngle();

			    	// Place it at the nice coordinates according to the angle
			    	if (angle > 0 && angle <=180) {
			    		MainViewController.gc.translate(x,y-halfSize+Math.sin(angle));
					} else if (angle > 180 && angle <= 360) {
						MainViewController.gc.translate(x,y+halfSize-Math.sin(angle));
					}
			    				    	
			    	MainViewController.gc.rotate(angle);
			    	
			    	// Draw the icon
					MainViewController.gc.drawImage(
						new Image(Draw.class.getResourceAsStream(fr.univavignon.ceri.deskmap.config.Textures.ARROW)),
						0,
						0,
						size,
						size
					);
		    		
				}

			    // Restore the rest of the canvas
				MainViewController.gc.restore();
			}
		}
	}
	
	/**
	 * Display the path on the {@code Map} {@code Canvas}
	 * @param path {@code Path}
	 * @author Capdepon Quentin
	 */
	public static void drawPath(fr.univavignon.ceri.deskmap.models.Path path) {
		
		// Handle empty case
		if (MainViewController.gc == null || path == null) {
			return;
		}
		
		// If the public transport printing is enabled
//		if (MainViewController.statusPublicTransportRoute == true) {
//			return;
//		}
				
		// If the path isn't empty
		if (path != null && !path.isEmpty() && path.size() >= 2) {
			
			// Draw each segment of the path
			for (int i = 0; i < path.size() - 1; i++) {
				
				Line segment = new Path(path.getColor());
				
				Node now = path.get(i);
				Node next = path.get(i+1);
				
				// Check empty
				if (now == null || next == null || now.id == null || next.id == null) {
					continue;
				}
				
				Long n0 = now.id;
				Long n1 = next.id;
				
				// If one of the identifier is empty
				if (n0 == null || n1 == null) {
					continue;
				}
				
				// Check null
				if (n0 == null || n1 == null) {
					continue;
				}
				
				segment.addNode(n0);
				segment.addNode(n1);
				
				Draw.drawPropLine(MainViewController.gc, segment);
			}			
		}
	}

	/**
	 * Draw all the bus stations on the {@code Canvas}
	 * @author Yanis Labrak
	 */
	public static void drawBusStationsMain() {

		// If not empty
		if (MainViewController.SELECTED_ROUTES != null && MainViewController.SELECTED_ROUTES.size() > 0) {
			Draw.drawBusStations(MainViewController.SELECTED_ROUTES);
		} else {
			Draw.drawBusStations(null);
		}
	}

	/**
	 * Draw all the bus stations on the {@code Canvas}
	 * @param lines {@code List<String>} The lines of display
	 * @author Yanis Labrak
	 */
	public static void drawBusStations(List<String> lines) {
		
		// If the public transport printing is disabled
		if (MainViewController.STATUS_PUBLIC_TRANSPORT_ROUTE.get() == false) {
			return;
		}
		
		// Check if null
		if (Map.BUS_STATIONS_PATH == null || Map.BUS_STATIONS_PATH.size() <= 0) {
			return;
		}
		
		// For each bus station
		for (BusStation station : Map.BUS_STATIONS_PATH) {	
			
			// Handle empty case
			if (station == null || (lines != null && !station.containsLines(lines))) { continue; }
			
			Draw.drawNodeTexture(station, fr.univavignon.ceri.deskmap.config.Textures.BUS_STATION);			
		}
	}
	
	/**
	 * Display on the {@code Canvas} all the public transport of the city of Angers
	 * @author Zhiao Zheng
	 */
	public static void drawPublicTransportMain() {
		
		// If not empty
		if (MainViewController.SELECTED_ROUTES != null && MainViewController.SELECTED_ROUTES.size() > 0) {
			Draw.drawPublicTransport(MainViewController.SELECTED_ROUTES);
		} else {
			Draw.drawPublicTransport(null);
		}
	}
	
	/**
	 * Display on the {@code Canvas} all the position of the public transport for the city of Angers
	 * @param lines {@code List<String>} The lines of display
	 * @author Zhiao Zheng
	 */
	public static void drawPublicTransport(List<String> lines) {
		
		// If the public transport printing is disabled
		if (MainViewController.STATUS_PUBLIC_TRANSPORT_ROUTE.get() == false || Map.PUBLIC_TRANSPORTS_POSITIONS == null || Map.PUBLIC_TRANSPORTS_POSITIONS.size() <= 0) {
			return;
		}
    	
    	// Size of the icon
    	Double scale = Draw.iconSize();
		
		// For each public transport positions
		for (PublicTransport transport: Map.PUBLIC_TRANSPORTS_POSITIONS) {
			
			if (transport == null) { continue; }
			
    		// Coordinate after processing
    		List<Double> coordinates = Node.toPixel(transport.getY(), transport.getX());
    		
    		// Handle empty case
    		if (coordinates == null || coordinates.size() <= 0 || (lines != null && !lines.contains(transport.getNumber().toUpperCase()))) { continue; }
    		
    		Double x = coordinates.get(0) - (scale/2);
    		Double y = Map.HEIGHT - coordinates.get(1) - (scale/2);
    		
    		// If is in the bbox
		    if (Bbox.isContained(x, y, Bbox.bboxScreen)) {
		    			    	
		    	String image;
		    	
		    	if (transport.getNumber() != null && transport.getNumber().equals("A") == true) {
					image = fr.univavignon.ceri.deskmap.config.Textures.TRAMWAY;					
				} else {
					image = fr.univavignon.ceri.deskmap.config.Textures.BUS;
				}
				
		    	// Draw the icon
		    	MainViewController.gc.drawImage(
					new Image(Draw.class.getResourceAsStream(image)),
					x,
					y,
					scale,
					scale
				);
	    		
			}
		}
		
	}
	
	/**
	 * Draw the routes of the public transports in parameter
	 * @param lines {@code List<String>} The lines to display
	 */
	public static void drawPublicTransportsRoutes(List<String> lines) {
		
		// If we are allowed to draw the public transport
		if (MainViewController.STATUS_PUBLIC_TRANSPORT_ROUTE.get() == true) {
			
			// For each public transport line
			for (PublicTransportRoute road : Map.PUBLIC_TRANSPORT_ROUTES) {
				
				// Handle empty case
				if (road == null || (lines != null && !lines.contains(road.getNumber().toUpperCase()))) { continue; }
				
				// Draw It on the canvas
				Draw.drawPropLine(MainViewController.gc, road);			
			}
		}
		
	}
	
	/**
	 * Draw the routes of the public transports
	 */
	public static void drawPublicTransportsRoutesMain() {
		
		// If not empty
		if (MainViewController.SELECTED_ROUTES != null && MainViewController.SELECTED_ROUTES.size() > 0) {
			Draw.drawPublicTransportsRoutes(MainViewController.SELECTED_ROUTES);
		} else {
			Draw.drawPublicTransportsRoutes(null);
		}
	}

}
