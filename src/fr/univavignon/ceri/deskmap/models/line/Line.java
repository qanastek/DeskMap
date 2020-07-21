package fr.univavignon.ceri.deskmap.models.line;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.models.GeoData;

/**
 * @author Mohamed BEN YAMNA
 */
@XmlRootElement(name = "line")
@XmlAccessorType(XmlAccessType.FIELD)
public class Line extends GeoData implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -3555086711794002318L;

	/**
	 * Thickness of the line
	 */
	protected Integer thickness;
	
	/**
	 * Color of it
	 */
	protected String color;
	
	/**
	 * Stroke color of it
	 */
	protected String strokeColor;

	/**
	 * List of the identifiers for each nodes which make the line
	 */
	protected List<Long> nodes = new ArrayList<Long>();
	
	/**
	 * Empty constructor
	 */
	public Line() {
		super();
	}
	
	/**
	 * Add a {@code Node} reference in this {@code Line}
	 * @param id {@code String} Identifier of the {@code Node}
	 */
	public void addNode(String id) {
		this.nodes.add(Long.parseLong(id));
	}

	/**
	 * Add a {@code Node} reference in this {@code Line}
	 * @param id {@code Long} Identifier of the {@code Node}
	 */
	public void addNode(Long id) {
		this.nodes.add(id);		
	}
	
	/**
	 * Get the thickness of the line
	 * @return the thickness
	 */
	public Integer getThickness() {
		return this.thickness;
	}

	/**
	 * Set the thickness of the line
	 * @param thickness the thickness to set
	 */
	public void setThickness(Integer thickness) {
		this.thickness = thickness;
	}

	/**
	 * Get the color of the line
	 * @return the color
	 */
	public String getColor() {
		return this.color;
	}

	/**
	 * Set the color of the line
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the nodes
	 */
	public List<Long> getNodes() {
		return this.nodes;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(List<Long> nodes) {
		this.nodes = nodes;
	}
	
	/**
	 * @return the strokeColor
	 */
	public String getStrokeColor() {
		
		if (this.strokeColor == null || this.strokeColor.isEmpty() == true) {
			return this.color;
		}
		
		return this.strokeColor;
	}

	/**
	 * @param strokeColor the strokeColor to set
	 */
	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}

	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the data
	 * @param thickness {@code Integer} Thickness of the line on the canvas
	 * @param color {@code String} Color of the line on the canvas
	 */
	public Line(String id, String name, Integer thickness, String color) {
		super(id,name);
		this.thickness = thickness;
		this.color = color;
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 * @param name {@code String} Name of the data
	 * @param thickness {@code Integer} Thickness of the line on the canvas
	 * @param color {@code String} Color of the line on the canvas
	 */
	public Line(Long id, String name, Integer thickness, String color) {
		super(id,name);
		this.thickness = thickness;
		this.color = color;
	}
	
	@Override
	public String toString() {
		return "[Name" + this.name + ", Thickness: " + this.thickness + ",Color: " + this.color + "]";
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
