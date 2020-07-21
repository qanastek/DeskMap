/**
 * 
 */
package fr.univavignon.ceri.deskmap.config;

import fr.univavignon.ceri.deskmap.controllers.MainViewController;
import fr.univavignon.ceri.deskmap.models.Node;

/**
 * @author Yanis Labrak
 */
public class Speeds {
	
	/**
	 * Pedestrian speed
	 */
	public static final int PEDESTRIAN_SPEED = 5;
	
	/**
	 * Bike speed
	 */
	public static final int BIKE_SPEED = 20;
	
	/**
	 * Average speed of a public transport in the city
	 */
	public static final int PUBLIC_TRANSPORT = 23;
	
	/**
	 * Average speed of a car inside a city
	 */
	public static final int CAR = 30;
	
	// -------------------------------------------------
		
	/**
	 * Motorway road max speed
	 */
	public static final int MOTORWAY_MAX_SPEED = 130;

	/**
	 * Trunk road max speed
	 */
	public static final int TRUNK_MAX_SPEED = 50;

	/**
	 * Primary road max speed
	 */
	public static final int PRIMARY_MAX_SPEED = 80;

	/**
	 * Secondary road max speed
	 */
	public static final int SECONDARY_MAX_SPEED = 50;

	/**
	 * Tertiary road max speed
	 */
	public static final int TERTIARY_MAX_SPEED = 50;

	/**
	 * Normal road max speed
	 */
	public static final int ROAD_MAX_SPEED = 30;
	
	/**
	 * Services road max speed (parking)
	 */
	public static final int SERVICE_MAX_SPEED = 15;
	
	/**
	 * Get the current speed
	 * @param n {@code Node}
	 * @return {@code Integer}
	 * @author Zhiao Zheng
	 */
	public static int getSpeed(Node n) {
		
		switch (MainViewController.getTransportMode()) {
		
			case TransportModeNames.BIKE:
				return Speeds.BIKE_SPEED;
				
			case TransportModeNames.CAR:	
			case TransportModeNames.PUBLIC_TRANSPORT:
				return n.getFirstRoad().getSpeed();
				
			case TransportModeNames.FOOT:
			default:
				return Speeds.PEDESTRIAN_SPEED;
				
		}
		
	}
	
	/**
	 * Get the current speed
	 * @param transportMode {@code String} The transport mode
	 * @return {@code Integer} The speed in km/h
	 * @author Zhiao Zheng
	 */
	public static int getSpeed(String transportMode) {
		
		switch (transportMode) {
		
			case TransportModeNames.BIKE:
				return Speeds.BIKE_SPEED;
				
			case TransportModeNames.CAR:	
				return Speeds.CAR;
				
			case TransportModeNames.PUBLIC_TRANSPORT:
				return Speeds.PUBLIC_TRANSPORT;
				
			case TransportModeNames.FOOT:
			default:
				return Speeds.PEDESTRIAN_SPEED;			
		}
		
	}

}
