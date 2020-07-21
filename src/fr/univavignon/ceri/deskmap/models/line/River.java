package fr.univavignon.ceri.deskmap.models.line;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.config.Color;

/**
 * @author Mohamed BEN YAMNA
 */
@XmlRootElement(name = "river")
public class River extends Line implements Highway {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 7386985798287563533L;

	/**
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the river
	 * @param thickness {@code Integer} Thickness of the river line
	 */
	public River(String id, String name, Integer thickness) {
		super(id, name, thickness, Color.WATER);
	}

}
