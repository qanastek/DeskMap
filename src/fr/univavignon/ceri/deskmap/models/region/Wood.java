package fr.univavignon.ceri.deskmap.models.region;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Natural.Natural;

/**
 * @author Mohamed BEN YAMNA
 */
public class Wood extends Region implements Natural {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1343031783348433389L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the Wood
	 * @param color {@code String} Color of the Wood
	 */
	public Wood(String id, String name, String color) {
		super(id, name, Color.WOOD);
	}

	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 * @param name {@code String} Name of the Wood
	 * @param color {@code String} Color of the Wood
	 */
	public Wood(Long id, String name, String color) {
		super(id, name, Color.WOOD);
	}

	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public Wood(Long id) {
		super(id, "", Color.WOOD);
	}

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 */
	public Wood(String id) {
		super(id);
	}

}
