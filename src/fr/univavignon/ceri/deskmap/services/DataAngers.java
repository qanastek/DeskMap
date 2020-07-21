/**
 * 
 */
package fr.univavignon.ceri.deskmap.services;

import fr.univavignon.ceri.deskmap.config.Settings;

/**
 * @author Yanis Labrak
 *
 */
public class DataAngers {
	
	/**
	 * The Data Angers API server URL
	 */
	public static final String URL = "https://data.angers.fr/api/records/1.0/search/?dataset=";

	/**
	 * The query URL
	 */
	public String query;
	
	/**
	 * Constructor
	 */
	public DataAngers() {
	}

	/**
	 * Create the query to fetch the public transports of Angers positions
	 * @return {@code String} The query
	 * @author Zhiao Zheng
	 */
	public static String getPublicTransportPositionQuery() {
		return DataAngers.URL + "bus-tram-position-tr&lang=fr&rows=9999&facet=mnemoligne&facet=nomligne&facet=dest";
	}
	
	/**
	 * Create the query to fetch the public transports lines of the Irigo network
	 * @return {@code String} The query
	 * @author Zhiao Zheng
	 */
	public static String getPublicTransportLinesIrigoQuery() {
		return DataAngers.URL + "lignes-irigo&rows=9999&facet=ligne";
	}
	
	/**
	 * Create the query to fetch the public transports schedules of the Irigo network
	 * @return {@code String} The query
	 * @author Yanis Labrak
	 */
	public static String getPublicTransportSchedulesIrigoQuery() {
		return DataAngers.URL + "bus-tram-circulation-passages&rows=" + Settings.QTS_SCHEDULES + "&facet=mnemoligne&facet=nomligne&facet=dest&facet=mnemoarret&facet=nomarret&facet=numarret";
	}
}
