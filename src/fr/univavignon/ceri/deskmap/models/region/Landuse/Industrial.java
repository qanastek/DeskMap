package fr.univavignon.ceri.deskmap.models.region.Landuse;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Industrial extends Region implements Landuse {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 4246030765338367813L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 */
	public Industrial(String id) {
		super(id, "", Color.INDUSTRIAL);
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public Industrial(Long id) {
		super(id, "", Color.INDUSTRIAL);
	}

}
