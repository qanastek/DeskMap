package fr.univavignon.ceri.deskmap.models.region.Natural;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Wetland extends Region implements Natural, Waterway {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 2125393473095242615L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the water spot
	 */
	public Wetland(String id, String name) {
		super(id, name, Color.WETLAND);
	}
	
	/**
	 * Constructor without name
	 * @param id {@code String} Identifier
	 */
	public Wetland(String id) {
		super(id, "", Color.WETLAND);
	}

	/**
	 * Constructor without name
	 * @param id {@code Long} Identifier
	 */
	public Wetland(Long id) {
		super(id, "", Color.WETLAND);
	}

}
