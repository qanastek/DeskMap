package fr.univavignon.ceri.deskmap.models.region.Landuse;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Quarry extends Region implements Landuse {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -5500258399456723045L;

	/**
	 * Constructor with name
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the Quarry
	 */
	public Quarry(String id, String name) {
		super(id, "", Color.QUARRY);
	}

	/**
	 * Constructor with name
	 * @param id {@code Long} Identifier
	 * @param name {@code String} Name of the Quarry
	 */
	public Quarry(Long id, String name) {
		super(id, "", Color.QUARRY);
	}

	/**
	 * Constructor with name
	 * @param id {@code Long} Identifier
	 */
	public Quarry(Long id) {
		super(id, "", Color.QUARRY);
	}

	/**
	 * Constructor with name
	 * @param id {@code String} Identifier
	 */
	public Quarry(String id) {
		super(id, "", Color.QUARRY);
	}

}
