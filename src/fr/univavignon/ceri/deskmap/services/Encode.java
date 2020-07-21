/**
 * 
 */
package fr.univavignon.ceri.deskmap.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fr.univavignon.ceri.deskmap.models.geopoint.City;

/**
 * @author Yanis Labrak
 *
 */
public class Encode {
	
	/**
	 * Encode the URL
	 * @param city {@code City}
	 * @return {@code String}
	 */
	public static String encodeUrl(City city) {
		
		try {
			
			return URLEncoder.encode(city.name
					.replaceAll("\\.", "\\_")
					.replaceAll("\\/", "\\_"), "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			System.err.println("##### Error in the Encode class. Null exception. #####");
			e.printStackTrace();
		}
		
		return null;
	}

}
