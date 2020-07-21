package fr.univavignon.ceri.deskmap.config;

import fr.univavignon.ceri.deskmap.models.Modes.TransportMode;
import fr.univavignon.ceri.deskmap.models.geopoint.City;

/**
 * This class represent all the settings of the software
 * @author Yanis Labrak
 */
public final class Settings {	
	
	/**
	 * Bbox offset
	 */
	public static final double BBOX_SIZE = 0.05;
	
	/**
	 * Enable highlight can't go zone
	 */
	public static final boolean ENABLE_CANT_GO = true;
	
	/**
	 * The default city draw when the software start
	 */
	public static final City DEFAULT_CITY = new City("178351",47.4739884,-0.5515588,"Angers","Pays de la Loire","France");

	/**
	 * Max scale of the canvas
	 */
	public static final int MIN_SCALE = 1;
	
	/**
	 * Max scale of the canvas
	 */
	public static final int MAX_SCALE = 40;
	
	/**
	 * Zooming scale
	 */
	public static final Double ZOOMING_SCALE = 1.25;
	
	/**
	 * Ratio of the canvas
	 */
	public static final int CANVAS_RATIO = 1;
	
	/**
	 * Thickness of the Motorway roads
	 */
	public static final int LEVEL_1_ROAD_THICKNESS = 4;
	
	/**
	 * Thickness of the PATH
	 */
	public static final int PATH = 4;
	
	/**
	 * Thickness of the Trunk roads
	 */
	public static final int LEVEL_2_ROAD_THICKNESS = 3;
	
	/**
	 * Thickness of the Primary, Secondary and Tertiary roads
	 */
	public static final int LEVEL_3_ROAD_THICKNESS = 2;
	
	/**
	 * Thickness of the road for the Residential, Living Street, Pedestrian and all others roads
	 */
	public static final int LEVEL_4_ROAD_THICKNESS = 1;
	
	/**
	 * Horizontal moving distance
	 */
	public static final Double HORI_MOVE_DIST = 350.0;
	
	/**
	 * Vertical moving distance
	 */
	public static final Double VERT_MOVE_DIST = 0.19 / 4000000;

	/**
	 * The default transport mode
	 */
	public static final TransportMode DEFAULT_TRANSPORT_MODE = new TransportMode("Vélo");

	/**
	 * The time in seconds before each refresh of the public transport position
	 * @author Mohamed Ben Yamna
	 */
	public static final int REFRESH_RATE = 10;

	/**
	 * Update message
	 */
	public static final String UPDATE_MSG = "Updated";

	/**
	 * Number of schedule fetch to the API
	 */
	public static final String QTS_SCHEDULES = "9999";

	/**
	 * Minimal zoom before drawing the strokes of the buildings on the canvas
	 */
	public static final Double ROADS_STROKE_MIN_SCALE = 2.54;
	
	/**
	 * Minimum gain if under 1 hour
	 */
	public static final Double MINIMUM_GAIN_LESS_ONE_HOUR = 1 - 0.4;
	
	/**
	 * Minimum gain if above 1 hour
	 */
	public static final Double MINIMUM_GAIN_ABOVE_ONE_HOUR = 1 - 0.3;
	
	/**
	 * How many bus stations around us we take
	 */
	public static final int STATIONS_AROUND_US = 4;

}
