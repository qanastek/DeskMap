package fr.univavignon.ceri.deskmap.models.region.Leisure;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Park extends Region implements Leisure {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -5906689452034418501L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 */
	public Park(String id) {
		super(id, "", Color.PARK);
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public Park(Long id) {
		super(id, "", Color.PARK);
	}
	
	/**
	 * Constructor with name
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the {@code Park}
	 */
	public Park(String id, String name) {
		super(id, name, Color.PARK);
	}

}
