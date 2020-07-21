package fr.univavignon.ceri.deskmap.models.region;

import java.util.ArrayList;
import java.util.List;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.GeoData;

/**
 * @author Mohamed BEN YAMNA
 */
public abstract class Region extends GeoData {
	
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -1858526944544461723L;

	/**
	 * Color of the region when drawn
	 */
	protected String color = Color.YELLOW;
	
	/**
	 * All the nodes which make the Region
	 */
	protected List<Long> nodes = new ArrayList<Long>();
	
	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 * @param name {@code String} Name of the region
	 * @param color {@code String} Color assigned to this {@code Region}
	 */
	public Region(String id, String name, String color) {
		super(id,name);
		this.color = color;
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 * @param name {@code String} Name of the region
	 * @param color {@code String} Color assigned to this {@code Region}
	 */
	public Region(Long id, String name, String color) {
		super(id,name);
		this.color = color;
	}
	
	/**
	 * Constructor
	 * @param id {@code Long} Identifier
	 */
	public Region(Long id) {
		super(id,"");
		this.color = "";
	}
	
	/**
	 * Constructor
	 * @param id {@code String} Identifier
	 */
	public Region(String id) {
		super(id,"");
		this.color = "";
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return this.color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Get all the nodes of this Region
	 * @return the nodes
	 */
	public List<Long> getNodes() {
		return this.nodes;
	}
	
	/**
	 * Add a node to the node list
	 * @param id {@code Long} Identifier of the node
	 */
	public void addNode(Long id) {
		this.nodes.add(id);
	}
	
	/**
	 * Add a node to the node list
	 * @param id {@code String} Identifier of the node
	 */
	public void addNode(String id) {
		this.nodes.add(Long.parseLong(id));
	}
	
	@Override
	public String toString() {
		return "[Name: " + this.name + ", Color: " + this.color + "]";
	}

}
