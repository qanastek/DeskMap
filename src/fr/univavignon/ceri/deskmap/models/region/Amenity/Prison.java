package fr.univavignon.ceri.deskmap.models.region.Amenity;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Prison extends Region implements Amenity {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 5027870331524661963L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name
	 */
	public Prison(String id, String name) {
		super(id, "", Color.PRISON);
	}

	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 * @param name {@code String} Name
	 */
	public Prison(Long id, String name) {
		super(id, "", Color.PRISON);
	}

	/**
	 * Constructor without name
	 * @param id {@code Long} Identifier
	 */
	public Prison(Long id) {
		super(id, "", Color.PRISON);
	}

	/**
	 * Constructor without name
	 * @param id {@code String} Identifier
	 */
	public Prison(String id) {
		super(id, "", Color.PRISON);
	}

}
