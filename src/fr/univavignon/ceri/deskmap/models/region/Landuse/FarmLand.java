package fr.univavignon.ceri.deskmap.models.region.Landuse;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Mohamed BEN YAMNA
 */
public class FarmLand extends Region implements Landuse {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 7011586500744138759L;

	/**
	 * Constructor with name
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the forest
	 */
	public FarmLand(String id, String name) {
		super(id, name, Color.FARM_LAND);
	}

	/**
	 * Constructor with name
	 * @param id {@code Long} Identifier
	 * @param name {@code String} Name of the forest
	 */
	public FarmLand(Long id, String name) {
		super(id, name, Color.FARM_LAND);
	}

	/**
	 * Constructor with name
	 * @param id {@code Long} Identifier
	 */
	public FarmLand(Long id) {
		super(id, "", Color.FARM_LAND);
	}

	/**
	 * Constructor with name
	 * @param id {@code String} Identifier
	 */
	public FarmLand(String id) {
		super(id, "", Color.FARM_LAND);
	}

}
