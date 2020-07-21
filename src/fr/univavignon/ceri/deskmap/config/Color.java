package fr.univavignon.ceri.deskmap.config;

import java.util.Arrays;
import java.util.List;

/**
 * Class which contain all the color used for displaying the map.
 * @author Yanis Labrak
 */
public final class Color {
	
	/**
	 * Color YELLOW
	 */
	public static final String YELLOW = "#ffff00";
	
	/**
	 * Color of the WATER area
	 */
	public static final String WATER = "#aadaff";
	
	/**
	 * Color of the RAILWAY area
	 */
	public static final String RAILWAY = "#eaebec";
	
	/**
	 * Color of the BUILDING
	 */
	public static final String BUILDING = "#d9d0c9";
	
	/**
	 * Color of the BUILDING STROKE
	 */
	public static final String BUILDING_STROKE = "#cdc3ba";
	
	/**
	 * Color of the RESIDENTIAL area
	 */
	public static final String RESIDENTIAL = "#e0dfdf";
	
	/**
	 * Color of the INDUSTRIAL area
	 */
	public static final String INDUSTRIAL = "#ebdbe8";
		
	/**
	 * Color of the COMMERCIAL area
	 */
	public static final String COMMERCIAL = "#f2d9d8";
	
	/**
	 * Color of the RETAIL area
	 */
	public static final String RETAIL = "#ffd5d0";
	
	/**
	 * Color of the PARK
	 */
	public static final String PARK = "#c9eec9";
	
	/**
	 * Color of the FOREST
	 */
	public static final String FOREST = "#c6e8c6";
	
	/**
	 * Color of the WOOD
	 */
	public static final String WOOD = "#add19e";
	
	/**
	 * Color of the GRASS
	 */
	public static final String GRASS = "#c6e8c6";
	
	/**
	 * Color of the MILITARY
	 */
	public static final String MILITARY = "#f3ded9";
	
	/**
	 * Color of the QUARRY
	 */
	public static final String QUARRY = "#c5c3c3";
	
	/**
	 * Color of the FarmLand
	 */
	public static final String FARM_LAND = "#e4e6cc";
	
	/**
	 * Color of the WETLAND area
	 */
	public static final String WETLAND = "#cdebb0";
	
	/**
	 * Color of the SPORT_CENTRE
	 */
	public static final String SPORT_CENTRE = "#dffce2";
	
	/**
	 * Color of the ICE_RINK
	 */
	public static final String ICE_RINK = "#ddecec";
	
	/**
	 * Color of the SCHOOL
	 */
	public static final String SCHOOL = "#ffffe5";
	
	/**
	 * Color of the HEALTHCARE
	 */
	public static final String HEALTHCARE = "#ffffe5";
	
	/**
	 * Color of the PARKING
	 */
	public static final String PARKING = "#eeeeee";
	
	/**
	 * Color of the PRISON
	 */
	public static final String PRISON = "#e2e2e2";
	
	/**
	 * Color of the CEMETERY
	 */
	public static final String CEMETERY = "#aacbaf";
	
	/**
	 * Color of the GOLF_COURSE
	 */
	public static final String GOLF_COURSE = "#b5e3b5";
	
	/**
	 * Color of the Motorway roads
	 */
	public static final String MOTORWAY = "#e990a0";
	
	/**
	 * Color of the Trunk roads
	 */
	public static final String TRUNK = "#fbb29a";
	
	/**
	 * Color of the Primary roads
	 */
	public static final String PRIMARY = "#fcd6a4";
	
	/**
	 * Color of the Secondary roads
	 */
	public static final String SECONDARY = "#f7fabf";
	
	/**
	 * Color of the Tertiary roads
	 */
	public static final String TERTIARY = "#fefefe";
	
	/**
	 * Color of the road for the Residential, Living Street, Pedestrian and all others roads
	 */
	public static final String ROAD = "#fefefe";
	
	/**
	 * Color of the Pedestrian roads
	 */
	public static final String PEDESTRIAN = "#dddde8";
	
	/**
	 * Color of the path
	 */
	public static final String PATH = "#669df6";
	
	/**
	 * Color of the bus stations
	 */
	public static final String BUS_STOP = "#aadaff";
	
	/**
	 * Color of the bridges
	 */
	public static final String BRIDGE = "#b8b8b8";
	
	/**
	 * Color of the public transport lines
	 */
	public static final List<String> PUBLIC_TRANSPORT = Arrays.asList(
		"#2d393f",
		"#ffe601",
		"#71bf44",
		"#00aeef",
		"#f166a7",
		"#df8619",
		"#f36e21",
		"#ee1d23",
		"#ae636a",
		"#00a54f",
		"#f9a64a",
		"#782a8b",
		"#fcb94b",
		"#b4dbae",
		"#00baf1",
		"#2e3092"
	);
	
	/**
	 * Make the color darker
	 * @param color {@code Color}
	 * @return {@code Color}
	 */
	public static javafx.scene.paint.Color darker(javafx.scene.paint.Color color) {
		// Change the darkness ratio
		return color.darker();
	}
}
