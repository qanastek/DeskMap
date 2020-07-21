/**
 * 
 */
package fr.univavignon.ceri.deskmap.services.Threads;

import fr.univavignon.ceri.deskmap.controllers.MainViewController;
import fr.univavignon.ceri.deskmap.models.Node;
import fr.univavignon.ceri.deskmap.models.Modes.CalculMode;
import fr.univavignon.ceri.deskmap.models.Modes.Distance;
import fr.univavignon.ceri.deskmap.services.AStar;
import javafx.concurrent.Task;

/**
 * @author Yanis Labrak
 * @author Zhiao Zheng
 */
public class AStarSearching extends Task<Double> {

	/**
	 * AStar
	 */
	public AStar astar;
	
	/**
	 * Departure node
	 */
	public Node currentDeparture;
	
	/**
	 * Arrival node
	 */
	public Node currentArrival;
	
	/**
	 * Current transport mode
	 */
	public String currentTransportMode;
	
	/**
	 * Current calcul mode
	 */
	public String currentCalculMode;
	
	/**
	 * Calcul mode
	 */
	public CalculMode mode;
	
	/**
	 * The distance/time
	 */
	public double result;
	
	/**
	 * Constructor
	 * @param currentDeparture {@code Node}
	 * @param currentArrival {@code Node}
	 * @param currentTransportMode {@code String}
	 * @param currentCalculMode {@code String}
	 */
	public AStarSearching(Node currentDeparture, Node currentArrival, String currentTransportMode, String currentCalculMode) {
		this.currentDeparture = currentDeparture;
		this.currentArrival = currentArrival;
		this.currentTransportMode = currentTransportMode;
		this.currentCalculMode = currentCalculMode;
	}

	@Override
	protected Double call() throws Exception {
    	
    	this.astar = new AStar(this.currentDeparture, this.currentArrival, this.currentTransportMode, this.currentCalculMode);
    	
    	// The current calculMode
    	this.mode = MainViewController.getCalculMode(this.currentCalculMode);
    	
    	// If empty
    	if (this.mode == null) {
			this.mode = new Distance();
		}
    	
    	// Process
    	this.astar.findPath(this.mode);
		
    	this.result = this.mode.getMesurement(this.astar);
    	    	
    	// Result
		return this.result;
	}
	
}
