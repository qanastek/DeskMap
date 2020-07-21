/**
 * 
 */
package fr.univavignon.ceri.deskmap.services.Threads;

import fr.univavignon.ceri.deskmap.services.QueriesLoading;
import javafx.concurrent.Task;

/**
 * @author Yanis Labrak
 *
 */
public class AutocompleteCity extends Task<Void> {
	
	/**
	 * The content to fetch
	 */
	public static String nominatimUrl = null;
	
	/**
	 * Does the input are coordinates
	 */
	public static Boolean isCoordinate = false;

	@Override
	protected Void call() throws Exception {

		// Load the cities
		QueriesLoading.loadCities(AutocompleteCity.nominatimUrl, AutocompleteCity.isCoordinate);

		return null;
	}
	
	@Override
	protected void succeeded() {		
		System.out.println("finish autocomplete !");		
		super.succeeded();
	}	
	
	@Override
	protected void cancelled() {
		System.out.println("Cancelled !");
		super.cancelled();
	}
}
