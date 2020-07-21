package fr.univavignon.ceri.deskmap.models.region.Leisure;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class SportsCentre extends Region implements Leisure {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -5008333114972866904L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the sport centre
	 */
	public SportsCentre(String id, String name) {
		super(id, name, Color.SPORT_CENTRE);
	}

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 */
	public SportsCentre(String id) {
		super(id, "", Color.SPORT_CENTRE);
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public SportsCentre(Long id) {
		super(id, "", Color.SPORT_CENTRE);
	}

}
