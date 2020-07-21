package fr.univavignon.ceri.deskmap.models.region.Leisure;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class IceRink extends Region implements Leisure {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -2337256931102642971L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name
	 */
	public IceRink(String id, String name) {
		super(id, name, Color.ICE_RINK);
	}

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 */
	public IceRink(String id) {
		super(id, "", Color.ICE_RINK);
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public IceRink(Long id) {
		super(id, "", Color.ICE_RINK);
	}

}
