package fr.univavignon.ceri.deskmap.models.region.Leisure;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Bridge extends Region implements Leisure {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -7313279693694061197L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the bridge
	 */
	public Bridge(String id, String name) {
		super(id, name, Color.BRIDGE);
	}

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 */
	public Bridge(String id) {
		super(id, "", Color.BRIDGE);
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public Bridge(Long id) {
		super(id, "", Color.BRIDGE);
	}

}
