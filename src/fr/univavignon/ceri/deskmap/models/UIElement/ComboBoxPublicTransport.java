/**
 * 
 */
package fr.univavignon.ceri.deskmap.models.UIElement;

import javafx.scene.control.ComboBox;

/**
 * @author Yanis Labrak
 *
 */
public class ComboBoxPublicTransport extends ComboBox<CheckBoxPublicTransport> {

	/**
	 * The old class
	 */
	public static String DEFAULT_STYLE_CLASS = "combo-box";
	
	/**
	 * The brand new class
	 */
	public static String NEW_STYLE_CLASS = "combo-box-public-transport";
	
	/**
	 * Constructor
	 */
	public ComboBoxPublicTransport() {
		super();
        this.getStyleClass().add(ComboBoxPublicTransport.NEW_STYLE_CLASS);
//        this.itemsProperty().get();
	}
}
