package fr.univavignon.ceri.deskmap.models.region.Leisure;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class Healthcare extends Region implements Leisure {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 3312815460924534131L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the hospital
	 */
	public Healthcare(String id, String name) {
		super(id, "", Color.HEALTHCARE);
	}

	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 * @param name {@code String} Name of the hospital
	 */
	public Healthcare(Long id, String name) {
		super(id, "", Color.HEALTHCARE);
	}

	/**
	 * Constructor without name
	 * @param id {@code Long} Identifier
	 */
	public Healthcare(Long id) {
		super(id, "", Color.HEALTHCARE);
	}

	/**
	 * Constructor without name
	 * @param id {@code String} Identifier
	 */
	public Healthcare(String id) {
		super(id, "", Color.HEALTHCARE);
	}

}
