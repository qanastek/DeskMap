package fr.univavignon.ceri.deskmap.models.region.Leisure;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class School extends Region implements Leisure {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -6254274724342876969L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the school
	 * @param color {@code String} Background color
	 */
	public School(String id, String name, String color) {
		super(id, name, Color.SCHOOL);
	}

	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 * @param name {@code String} Name of the school
	 * @param color {@code String} Background color
	 */
	public School(Long id, String name, String color) {
		super(id, name, Color.SCHOOL);
	}
	
	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the school
	 */
	public School(String id, String name) {
		super(id, name, Color.SCHOOL);
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 * @param name {@code String} Name of the school
	 */
	public School(Long id, String name) {
		super(id, name, Color.SCHOOL);
	}

	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public School(Long id) {
		super(id);
	}

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 */
	public School(String id) {
		super(id);
	}

}
