/**
 * 
 */
package fr.univavignon.ceri.deskmap.models.line;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.config.Settings;

/**
 * @author Yanis Labrak
 *
 */
@XmlRootElement(name = "path")
@XmlAccessorType(XmlAccessType.FIELD)
public class Path extends Road implements Serializable {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 2728510569281570599L;

	/**
	 * Constructor
	 */
	public Path() {
		super(0L);
		this.setColor(Color.PATH);
		this.setThickness(Settings.PATH);
	}
	
	/**
	 * Constructor color
	 * @param color {@code String} Color
	 */
	public Path(String color) {
		super(0L);
		this.setColor(color);
		this.setThickness(Settings.PATH);
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
