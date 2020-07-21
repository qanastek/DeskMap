/**
 * 
 */
package fr.univavignon.ceri.deskmap.models;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.models.Modes.CalculMode;
import fr.univavignon.ceri.deskmap.models.Modes.TransportMode;
import fr.univavignon.ceri.deskmap.models.geopoint.City;
import fr.univavignon.ceri.deskmap.models.line.Road;

/**
 * The save
 * @author Mohammed Ben Yamna
 */
@XmlRootElement(name = "save")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Save implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2204334716035944365L;
	
	/**
	 * City
	 */
	private City city;

	/**
	 * Current arrival road
	 */
	private Road currentArrivalRoad;

	/**
	 * Current departure road
	 */
	private Road currentDepartureRoad;

	/**
	 * Current calcul mode
	 */
	private CalculMode currentCalculMode;

	/**
	 * Current transport mode
	 */
	private TransportMode currentTransportMode;

	/**
	 * Sens enabled
	 */
	private boolean sensEnabled;

	/**
	 * Correspondence enabled
	 */
	private boolean correspondenceEnabled;

	/**
	 * Paths
	 */
	private List<Path> paths;
	
	/**
	 * Anonymous constructor
	 */
	public Save() {
		super();
	}
	
	/**
	 * Constructor
	 * @param city {@code City}
	 * @param arrival  {@code Road}
	 * @param departure  {@code Road}
	 * @param calculMode  {@code CalculMode}
	 * @param transportMode  {@code TransportMode}
	 * @param sensEnabled  {@code boolean}
	 * @param correspondenceEnabled  {@code boolean}
	 * @param paths  {@code List<Path>}
	 */
	public Save(City city, Road arrival, Road departure, CalculMode calculMode, TransportMode transportMode, boolean sensEnabled, boolean correspondenceEnabled, List<Path> paths) {	
		this.city = city;
		this.currentArrivalRoad = arrival;
		this.currentDepartureRoad = departure;
		this.currentCalculMode = calculMode;
		this.currentTransportMode = transportMode;
		this.sensEnabled = sensEnabled;
		this.correspondenceEnabled = correspondenceEnabled;
		this.paths = paths;
	}

	/**
	 * @return the city
	 */
	public City getCity() {
		return this.city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(City city) {
		this.city = city;
	}

	/**
	 * @return the currentArrivalRoad
	 */
	public Road getCurrentArrivalRoad() {
		return this.currentArrivalRoad;
	}

	/**
	 * @param currentArrivalRoad the currentArrivalRoad to set
	 */
	public void setCurrentArrivalRoad(Road currentArrivalRoad) {
		this.currentArrivalRoad = currentArrivalRoad;
	}

	/**
	 * @return the currentDepartureRoad
	 */
	public Road getCurrentDepartureRoad() {
		return this.currentDepartureRoad;
	}

	/**
	 * @param currentDepartureRoad the currentDepartureRoad to set
	 */
	public void setCurrentDepartureRoad(Road currentDepartureRoad) {
		this.currentDepartureRoad = currentDepartureRoad;
	}

	/**
	 * @return the currentCalculMode
	 */
	public CalculMode getCurrentCalculMode() {
		return this.currentCalculMode;
	}

	/**
	 * @param currentCalculMode the currentCalculMode to set
	 */
	public void setCurrentCalculMode(CalculMode currentCalculMode) {
		this.currentCalculMode = currentCalculMode;
	}

	/**
	 * @return the currentTransportMode
	 */
	public TransportMode getCurrentTransportMode() {
		return this.currentTransportMode;
	}

	/**
	 * @param currentTransportMode the currentTransportMode to set
	 */
	public void setCurrentTransportMode(TransportMode currentTransportMode) {
		this.currentTransportMode = currentTransportMode;
	}

	/**
	 * @return the sensEnabled
	 */
	public boolean isSensEnabled() {
		return this.sensEnabled;
	}

	/**
	 * @param sensEnabled the sensEnabled to set
	 */
	public void setSensEnabled(boolean sensEnabled) {
		this.sensEnabled = sensEnabled;
	}

	/**
	 * @return the correspondenceEnabled
	 */
	public boolean isCorrespondenceEnabled() {
		return this.correspondenceEnabled;
	}

	/**
	 * @param correspondenceEnabled the correspondenceEnabled to set
	 */
	public void setCorrespondenceEnabled(boolean correspondenceEnabled) {
		this.correspondenceEnabled = correspondenceEnabled;
	}

	/**
	 * @return the paths
	 */
	public List<Path> getPaths() {
		return this.paths;
	}

	/**
	 * @param paths the paths to set
	 */
	public void setPaths(List<Path> paths) {
		this.paths = paths;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
