package fr.univavignon.ceri.deskmap.config;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * All the textures
 */
public class Textures {

	/**
	 * Bus icon
	 */
	public static final String BUS = "../ressources/assets/bus_bis.png";
	
	/**
	 * Tramway icon
	 */
	public static final String TRAMWAY = "../ressources/assets/tramway.png";
	
	/**
	 * Pin icon
	 */
	public static final String PIN = "../ressources/assets/pin.png";
	
	/**
	 * Bus station icon
	 */
	public static final String BUS_STATION = "../ressources/assets/station.png";
	
	/**
	 * Arrow icon
	 */
	public static final String ARROW = "../ressources/assets/arrow.png";
	
	/**
	 * Sign icon
	 */
	public static final String SIGN = "../ressources/assets/sign.png";
	
	/**
	 * ----------------- ICONS PATH CONTEXT MENU (RIGHT CLICK) -----------------
	 */
	
	/**
	 * Identifier icon
	 */
	public static final String ID = "../ressources/assets/id.png";
	
	/**
	 * Latitude icon
	 */
	public static final String LAT = "../ressources/assets/lat.png";
	
	/**
	 * Longitude icon
	 */
	public static final String LON = "../ressources/assets/lon.png";

	/**
	 * Departure icon
	 */
	public static final String DEP = "../ressources/assets/dep.png";

	/**
	 * Arrival icon
	 */
	public static final String ARR = "../ressources/assets/arr.png";
	
	/**
	 * Public transport station icon
	 */
	public static final String STATION = "../ressources/assets/station.png";
	
	/**
	 * Address icon
	 */
	public static final String ADDR = "../ressources/assets/addr.png";
		
	/**
	 * Load an image as a {@code ImageView}
	 * @param img {@code String} The name and extension of the image
	 * @return {@code ImageView}
	 */
	public static ImageView getImageView(String img) {
		return new ImageView(new Image(Textures.class.getResourceAsStream(img), 16, 16, false, true));
	}
	
}
