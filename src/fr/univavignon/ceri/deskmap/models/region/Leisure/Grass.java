package fr.univavignon.ceri.deskmap.models.region.Leisure;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Grass extends Region implements Leisure {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -4062793179367349648L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 */
	public Grass(String id) {
		super(id, "", Color.GRASS);
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public Grass(Long id) {
		super(id, "", Color.GRASS);
	}

}
