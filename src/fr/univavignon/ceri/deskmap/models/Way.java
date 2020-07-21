package fr.univavignon.ceri.deskmap.models;

import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Yanis Labrak
 */
public class Way extends Region {

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the Way
	 * @param color {@code String} Color of the Way
	 */
	public Way(String id, String name, String color) {
		super(id, name, color);
	}

	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 * @param name {@code String} Name of the Way
	 * @param color {@code String} Color of the Way
	 */
	public Way(Long id, String name, String color) {
		super(id, name, color);
	}

	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public Way(Long id) {
		super(id, "", "#000000");
	}

}