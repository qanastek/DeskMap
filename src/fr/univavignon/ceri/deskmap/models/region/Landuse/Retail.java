package fr.univavignon.ceri.deskmap.models.region.Landuse;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Retail extends Region implements Landuse {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 6263205269582558633L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 */
	public Retail(String id) {
		super(id, "", Color.RETAIL);
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public Retail(Long id) {
		super(id, "", Color.RETAIL);
	}

}
