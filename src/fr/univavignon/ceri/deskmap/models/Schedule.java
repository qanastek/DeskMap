/**
 * 
 */
package fr.univavignon.ceri.deskmap.models;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Yanis Labrak
 *
 */
@XmlRootElement(name = "schedule")
public class Schedule {
	
	/**
	 * Identifier of the line
	 */
	@XmlAttribute(name = "mnemoligne")
	public String mnemoligne;
	
	/**
	 * Name of the line
	 */
	@XmlAttribute(name = "nomligne")
	public String nomligne;
	
	/**
	 * Arrival date
	 */
	@XmlAttribute(name = "arriveetheorique")
	public Date arriveetheorique;
	
	/**
	 * Empty constructor
	 */
	public Schedule() {
		this.mnemoligne = null;
		this.nomligne = null;
		this.arriveetheorique = null;
	}
	
	/**
	 * Full constructor
	 * @param mnemoligne {@code String}
	 * @param nomligne {@code String}
	 * @param arriveetheorique {@code Date}
	 */
	public Schedule(String mnemoligne, String nomligne, Date arriveetheorique) {
		this.mnemoligne = mnemoligne;
		this.nomligne = nomligne;
		this.arriveetheorique = arriveetheorique;
	}
}
