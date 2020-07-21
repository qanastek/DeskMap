/**
 * 
 */
package fr.univavignon.ceri.deskmap.models.UIElement;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckBox;

/**
 * @author Yanis Labrak
 *
 */
public class CheckBoxPublicTransport extends CheckBox {

	/**
	 * The old class
	 */
	public static String DEFAULT_STYLE_CLASS = "check-box";
	
	/**
	 * The brand new class
	 */
	public static String NEW_STYLE_CLASS = "check-box-public-transport";
	
	/**
	 * The public transport line
	 */
	public String line = "";
	
	/**
	 * Empty constructor
	 */
	public CheckBoxPublicTransport() {}
	
	/**
	 * Constructor
	 * @param line {@code String}
	 */
	public CheckBoxPublicTransport(String line) {
		super(line.toUpperCase());
		this.line = line.toUpperCase();
        this.getStyleClass().add(CheckBoxPublicTransport.NEW_STYLE_CLASS);
	}
	
	/**
	 * Return the check status
	 * @return {@code BooleanProperty}
	 */
	public BooleanProperty checkProperty() {
        return this.selectedProperty();
    }
	
	@Override
	public String toString() {
		return this.line;
	}

}
