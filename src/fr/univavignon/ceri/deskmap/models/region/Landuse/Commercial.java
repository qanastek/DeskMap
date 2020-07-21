package fr.univavignon.ceri.deskmap.models.region.Landuse;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Commercial extends Region implements Landuse {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 3080132924511778575L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 */
	public Commercial(String id) {
		super(id, "", Color.COMMERCIAL);
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public Commercial(Long id) {
		super(id, "", Color.COMMERCIAL);
	}

}
