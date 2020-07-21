/**
 * 
 */
package fr.univavignon.ceri.deskmap.models.Modes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.config.Speeds;
import fr.univavignon.ceri.deskmap.config.TransportModeNames;
import fr.univavignon.ceri.deskmap.controllers.MainViewController;

/**
 * @author Zhiao Zheng
 */
@XmlRootElement(name = "transport_mode")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransportMode implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -4471725051573949309L;
	
	/**
	 * Name of the transportation way
	 */
	public String name;
	
	/**
	 * Empty constructor
	 */
	public TransportMode() {
		super();
	}
	
	/**
	 * Constructor
	 * @param name {@code String}
	 */
	public TransportMode(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	/**
	 * Get the roads of which the transport mode can go
	 * @return {@code List<String>}
	 * @author Yanis Labrak
	 */
	public static List<String> getAllowed() {
		
		// Allowed road types
		List<String> allowed = new ArrayList<String>();
		
		String transportMode = MainViewController.getTransportMode();
		
		// Check empty
		if (transportMode == null || transportMode.isEmpty() == true) {
			return new ArrayList<String>(Arrays.asList(
				"PRIMARY",
				"SECONDARY",
				"ROAD"
			));
		}
		
		switch (transportMode) {

			case TransportModeNames.PUBLIC_TRANSPORT:
			case TransportModeNames.CAR:
				
				System.out.println("CAR");
				
				allowed.add("MOTORWAY");
				allowed.add("TRUNK");
				allowed.add("PRIMARY");
				allowed.add("SECONDARY");
				allowed.add("TERTIARY");
				
				break;

			case TransportModeNames.FOOT:
			case TransportModeNames.BIKE:
			default:
				
				System.out.println("FOOT");
				
				allowed.add("PRIMARY");
				allowed.add("SECONDARY");
				allowed.add("ROAD");
				
				break;
		}
		
		return allowed;
	}
	
	/**
	 * Get the roads of which the transport mode cannot go
	 * @return {@code List<String>}
	 */
	public static List<String> getDisallowed() {
		
		// Allowed road types
		List<String> disallowed = new ArrayList<String>();
		
		switch (MainViewController.getTransportMode()) {

			case TransportModeNames.PUBLIC_TRANSPORT:
			case TransportModeNames.CAR:
				
				disallowed.add("ROAD");
				
				break;

			case TransportModeNames.FOOT:
			case TransportModeNames.BIKE:	
			default:			

				disallowed.add("MOTORWAY");
				disallowed.add("TRUNK");
				disallowed.add("TERTIARY");
				
				break;
		}
		
		return disallowed;
	}
	
	/**
	 * Return the time to walk the distance in minutes
	 * @param distance {@code Double} Distance
	 * @return {@code Double} The time in minutes
	 */
	public static double walkingTime(double distance) {
		return ((distance/1000) / Speeds.PEDESTRIAN_SPEED) * 60;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
