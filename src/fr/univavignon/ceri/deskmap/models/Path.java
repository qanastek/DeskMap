/**
 * 
 */
package fr.univavignon.ceri.deskmap.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import fr.univavignon.ceri.deskmap.config.Color;
import fr.univavignon.ceri.deskmap.models.geopoint.BusStation;

/**
 * @author Yanis Labrak
 *
 */
@XmlRootElement(name = "path")
@XmlAccessorType(XmlAccessType.FIELD)
public class Path implements Serializable {
		
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -7121679041102443453L;

	/**
	 * Color of the path
	 */
	public String color = Color.PATH;

	/**
	 * The path
	 */
	public List<NodePath> path = new ArrayList<NodePath>();
	
	/**
	 * Empty constructor
	 */
	public Path() {
		super();
	}
	
	/**
	 * Return the nodePath at index
	 * @param index {@code Integer}
	 * @return {@code NodePath}
	 */
	public NodePath get(int index) {
		return this.path.get(index);
	}

	/**
	 * Return if the pat his empty
	 * @return {@code Boolean}
	 */
	public boolean isEmpty() {
		return this.path.isEmpty();
	}
	
	/**
	 * Return the size of the path
	 * @return {@code Integer}
	 */
	public int size() {
		return this.path.size();
	}

	/**
	 * Add the node to the path
	 * @param node {@code Node}
	 */
	public void add(NodePath node) {
		this.path.add(node);
	}
	
	/**
	 * Add the station to the path
	 * @param station {@code BusStation}
	 */
	public void add(BusStation station) {
		this.path.add(new NodePath(station));
	}
	
	/**
	 * Add the nodes to the path
	 * @param nodes {@code List<NodePath>}
	 */
	public void addAll(List<NodePath> nodes) {
		this.path.addAll(nodes);
	}
	
	/**
	 * Add the nodes to the path
	 * @param nodes {@code Path}
	 */
	public void addAll(Path nodes) {
		this.path.addAll(nodes.path);
	}
	
	/**
	 * Remove the nodes to the path
	 * @param nodes {@code List<NodePath>}
	 */
	public void removeAll(List<NodePath> nodes) {
		this.path.removeAll(nodes);
	}
	
	/**
	 * Remove the nodes to the path
	 * @param path {@code Path}
	 */
	public void removeAll(Path path) {
		this.path.removeAll(path.path);
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
	 * Clear the current path
	 */
	public void clear() {
		this.path.clear();
	}

	/**
	 * @return the path
	 */
	public List<NodePath> getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(List<NodePath> path) {
		this.path = path;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
