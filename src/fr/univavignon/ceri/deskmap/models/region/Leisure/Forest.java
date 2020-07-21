package fr.univavignon.ceri.deskmap.models.region.Leisure;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Forest extends Region implements Leisure {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 6140909852432207200L;

	/**
	 * Constructor with name
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the forest
	 */
	public Forest(String id, String name) {
		super(id, name, Color.FOREST);
	}
	
	/**
	 * Constructor without name
	 * @param id {@code String} Identifier
	 */
	public Forest(String id) {
		super(id, "", Color.FOREST);
	}
	
	/**
	 * Constructor without name
	 * @param id {@code Long} Identifier
	 */
	public Forest(Long id) {
		super(id, "", Color.FOREST);
	}

}
