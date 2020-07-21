/**
 * 
 */
package fr.univavignon.ceri.deskmap.models.Modes;

import fr.univavignon.ceri.deskmap.controllers.MainViewController;
import fr.univavignon.ceri.deskmap.models.geopoint.City;
import javafx.concurrent.Task;

/**
 * @author Yanis Labrak
 *
 */
public class RenderCity extends Task<Void> {
	
	/**
	 * The city
	 */
	public City city = null;
	
	/**
	 * True when load a city via import
	 */
	public boolean importing;
	
	/**
	 * Constructor
	 * @param city {@code City}
	 */
	public RenderCity(City city) {
		super();		
		this.city = city;
		
	}
	
	/**
	 * Constructor
	 * @param city {@code City}
	 * @param importing {@code Boolean}
	 */
	public RenderCity(City city, boolean importing) {
		super();		
		this.city = city;
		this.importing = importing;
		
	}

	@Override
	protected Void call() throws Exception {
		
		// Only allow to fetch public transport position in the city of Angers
		if (!this.city.name.equals("Angers")) {
			MainViewController.UPDATE_PUBLIC_TRANSPORT_POSITIONS = false;
		}
		
		// If we use the function during the import
		if (this.importing == true) {			
	        // Load/render the map
			MainViewController.renderCityMap(this.city,true);			
		} else {			
	        // Load/render the map
			MainViewController.renderCityMap(this.city);
		}
		
		return null;
	}

	@Override
	protected void succeeded() {
		
		// Render all the objects of the canvas
		MainViewController.renderMap();
		
		super.succeeded();
	}
}
