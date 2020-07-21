package fr.univavignon.ceri.deskmap.models.region.Amenity;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Parking extends Region implements Amenity {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -176587655725520119L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name
	 */
	public Parking(String id, String name) {
		super(id, "", Color.PARKING);
	}

	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 * @param name {@code String} Name
	 */
	public Parking(Long id, String name) {
		super(id, "", Color.PARKING);
	}

	/**
	 * Constructor without name
	 * @param id {@code Long} Identifier
	 */
	public Parking(Long id) {
		super(id, "", Color.PARKING);
	}

	/**
	 * Constructor without name
	 * @param id {@code String} Identifier
	 */
	public Parking(String id) {
		super(id, "", Color.PARKING);
	}

}
