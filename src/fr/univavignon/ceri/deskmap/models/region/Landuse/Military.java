/**
 * 
 */
package fr.univavignon.ceri.deskmap.models.region.Landuse;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Yanis Labrak
 */
public class Military extends Region implements Landuse {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 2453152226497137397L;

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 */
	public Military(String id) {
		super(id, "", Color.MILITARY);
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public Military(Long id) {
		super(id, "", Color.MILITARY);
	}
}
