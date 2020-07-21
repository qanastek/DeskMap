package fr.univavignon.ceri.deskmap.models.Modes;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.models.NodePath;
import fr.univavignon.ceri.deskmap.services.AStar;

/**
 * (Strategy)
 * <br>
 * Process modes
 * @author Yanis Labrak
 */
@XmlRootElement(name = "calcul_mode")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class CalculMode implements Serializable {
	
	/**
	 * Empty constructor
	 */
	public CalculMode() {
		super();
	}

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -8050806981223458590L;

	/**
	 * Get the closest {@code NodePath} around the current position
	 * @return {@code NodePath} The closest node around the current position
	 * @param pathProcess {@code AStar} The path processing instance on which the method apply to
	 * @author Implemented by Mohamed Ben Yamna
	 * @author Modified by Zhiao Zheng
	 */
	public abstract NodePath getCloser(AStar pathProcess);
	
	/**
	 * Return the time or distance
	 * @param pathProcess {@code AStar}
	 * @return {@code Double}
	 */
	public abstract double getMesurement(AStar pathProcess);
	
	
	/**
	 * Return the unit of the mode
	 * @return {@code String} The unit
	 */
	public abstract String getUnit();
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
