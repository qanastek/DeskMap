package fr.univavignon.ceri.deskmap.models.region.Landuse;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Railway extends Region implements Landuse {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 2669465372313260238L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 */
	public Railway(String id) {
		super(id, "", Color.RAILWAY);
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public Railway(Long id) {
		super(id, "", Color.RAILWAY);
	}
}
