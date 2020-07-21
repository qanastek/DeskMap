/**
 * 
 */
package fr.univavignon.ceri.deskmap.models;

import fr.univavignon.ceri.deskmap.controllers.MainViewController;
import fr.univavignon.ceri.deskmap.models.line.Line;
import fr.univavignon.ceri.deskmap.models.region.Region;

/**
 * @author Yanis Labrak
 *
 */
public class Bbox {
	
	/**
	 * {@code Double} The lowest latitude
	 */
	public Double topLeft;
	
	/**
	 * {@code Double} The highest latitude
	 */
	public Double topRight;
	
	/**
	 * {@code Double} The lowest longitude
	 */
	public Double bottomLeft;
	
	/**
	 * {@code Double} The highest longitude
	 */
	public Double bottomRight;
	
	/**
	 * Bounding box of the canvas according to the screen size
	 */
	public static Bbox bboxScreen;

	/**
	 * Constructor
	 * @param topLeft {@code Double}
	 * @param topRight {@code Double}
	 * @param bottomLeft {@code Double}
	 * @param bottomRight {@code Double}
	 */
	public Bbox(Double topLeft, Double topRight, Double bottomLeft, Double bottomRight) {
		this.topLeft = topLeft;
		this.topRight = topRight;
		this.bottomLeft = bottomLeft;
		this.bottomRight = bottomRight;
	}
	
	/**
	 * Get the {@code Bbox} of the chunck
	 * @return {@code Bbox} The {@code Bbox} for the {@code Canvas}
	 */
	public static Bbox getBboxScreen() {
		return bboxScreen;
	}
	
	/**
	 * Calculate the {@code Bbox} of the chunck
	 * @return {@code Bbox} The {@code Bbox} for the {@code Canvas}
	 */
	public static Bbox calculateBboxScreen() {
		return new Bbox(
			0.0,
			MainViewController.canvasMapSize.getX(),
			0.0,
			MainViewController.canvasMapSize.getY()
		);
	}
	
	/**
	 * Return if the {@code Node} is inside the {@code Bbox} or not
	 * @param node {@code Node}
	 * @param bbox {@code Bbox}
	 * @return {@code Boolean}
	 */
	public static Boolean isContained(Node node, Bbox bbox) {
		if (node.lon < bbox.topRight && node.lon > bbox.topLeft && node.lat > bbox.bottomLeft && node.lat < bbox.bottomRight) {
			return true;
		}
		return false;
	}

	/**
	 * Return if the coordinates are inside the {@code Bbox} or not
	 * @param x {@code Double}
	 * @param y {@code Double}
	 * @param bbox {@code Bbox}
	 * @return {@code Boolean}
	 */
	public static Boolean isContained(Double x, Double y, Bbox bbox) {
		if (x < bbox.topRight && x > bbox.topLeft && y > bbox.bottomLeft && y < bbox.bottomRight) {
			return true;
		}
		return false;
	}

	/**
	 * Return if the {@code Line} appear inside the canvas area
	 * @param line {@code Line}
	 * @param bbox {@code Bbox}
	 * @return {@code Boolean}
	 */
	public static Boolean lineIsContained(Line line, Bbox bbox) {
		return null;
	}
	
	/**
	 * Return if the {@code Region} appear inside the canvas area
	 * @param region {@code Region}
	 * @param bbox {@code Bbox}
	 * @return {@code Boolean}
	 */
	public static Boolean regionIsContained(Region region, Bbox bbox) {
		return null;
	}
	
	@Override
	public String toString() {
		return this.topLeft + "," + this.bottomLeft + "," + this.topRight + "," + this.bottomRight;
	}

}
