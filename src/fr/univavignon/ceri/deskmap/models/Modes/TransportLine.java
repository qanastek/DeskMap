package fr.univavignon.ceri.deskmap.models.Modes;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Transport line
 * @author Labrak Yanis
 */
public class TransportLine {
	
	/**
	 * Name of the transport
	 */
	@XmlAttribute(name = "name")
	String name;
	
	/**
	 * Constructor
	 * @param name {@code String}
	 */
	public TransportLine(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
