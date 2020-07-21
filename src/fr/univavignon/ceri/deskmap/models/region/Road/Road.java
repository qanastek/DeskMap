package fr.univavignon.ceri.deskmap.models.region.Road;

import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Road extends Region {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -26684614061348348L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the road
	 * @param color {@code String} Color of the road
	 */
	public Road(String id, String name, String color) {
		super(id, name, color);
	}

	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 * @param name {@code String} Name of the road
	 * @param color {@code String} Color of the road
	 */
	public Road(Long id, String name, String color) {
		super(id, name, color);
	}

	/**
	 * @param id {@code Long} Identifier
	 */
	public Road(Long id) {
		super(id);
	}
	
	/**
	 * @param id {@code String} Identifier
	 */
	public Road(String id) {
		super(id);
	}

}
