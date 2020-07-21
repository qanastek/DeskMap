package fr.univavignon.ceri.deskmap.models.region.Structure;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Cemetery extends Region implements Structure {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -6464773722678144309L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the {@code Cemetery}
	 */
	public Cemetery(String id, String name) {
		super(id, name, Color.CEMETERY);
	}

	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public Cemetery(Long id) {
		super(id, "", Color.CEMETERY);
	}

}
