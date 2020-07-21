package fr.univavignon.ceri.deskmap.models.region.Landuse;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Residential extends Region implements Landuse {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -6207315312910083192L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 */
	public Residential(String id) {
		super(id, "", Color.RESIDENTIAL);
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public Residential(Long id) {
		super(id, "", Color.RESIDENTIAL);
	}

}
