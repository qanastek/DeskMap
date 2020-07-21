package fr.univavignon.ceri.deskmap.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import fr.univavignon.ceri.deskmap.Map;
import fr.univavignon.ceri.deskmap.config.Settings;
import fr.univavignon.ceri.deskmap.models.Bbox;

/**
 * A class which contain every single command necessary for the built of an Overpass API query.
 * @author Yanis Labrak
 */
public class OSM {

	/**
	 * The Overpass API server URL
	 */
	public final String URL_OSM = "https://lz4.overpass-api.de/api/interpreter?data=";
	
	/**
	 * The query URL
	 */
	public String query;
	
	/**
	 * <b>Constructor</b>
	 * <br>
	 * Initialize the query to an empty {@code String}
	 * @author Yanis Labrak
	 */
	public OSM() {
		this.query = this.URL_OSM;
	}
	
	/**
	 * Define the output fields, format and column separator
	 * @param format {@code String} Output format ({@code CSV}, {@code JSON} or {@code XML})
	 * @param parameters {@code String} Fields we want to get from the API
	 * @param header {@code Boolean} Does the API return the header fields name
	 * @param delimiter {@code String} Change the default TAB delimiter
	 * @author Yanis Labrak
	 */
	public void output(String format, String parameters, Boolean header, String delimiter) {
		
		this.query += "[out:" + format;
		
		switch (format) {
			case "csv":			
				this.query += "(" + parameters.toString() +  ";" + header.toString() + ";\"" + delimiter + "\")";			
				break;
			case "json":
				break;
		}
		
		this.query += "];";
	}
	
	/**
	 * Set the area where we search
	 * @param place {@code String} Zone dans laquelle les noeuds devront ce trouvé
	 * @author Yanis Labrak
	 */
	public void area(String place) {
		this.query += "(area[name=\"" + place + "\"];)->.SA;";
	}
	
	/**
	 * Before queries
	 */
	public void start() {
		this.query += "(";
	}
	
	/**
	 * Add a kind of node to fetch
	 * @param key {@code String} Key
	 * @param value {@code String} Value
	 * @author Yanis Labrak
	 */
	public void node(String key, String value) {
		this.query += "node[\"" + key + "\"=\"" + value + "\"](area.SA);";
	}
	
	/**
	 * Add a kind of node to fetch
	 * @param key {@code String} Key
	 * @author Yanis Labrak
	 */
	public void node(String key) {
		this.query += "node[\"" + key + "\"](area.SA);";
	}
	
	/**
	 * Add a kind of node to fetch with bbox
	 * @param key {@code String} Key
	 * @param bbox {@code String} BBOX
	 * @author Yanis Labrak
	 */
	public void nodeBbox(String key, String bbox) {
		this.query += "node[\"" + key + "\"](" + bbox + ");";
	}
	
	/**
	 * Add a kind of node to fetch with bbox and multiple values
	 * @param key {@code String} Key
	 * @param bbox {@code String} BBOX
	 * @param values {@code String} The multiple values like "landuse"~"forest|grass"
	 * @author Yanis Labrak
	 */
	public void nodeMulti(String key, String values, String bbox) {
		this.query += "node[\"" + key + "\"~\"" + values + "\"](" + bbox + ");";
	}
	
	/**
	 * Add a kind of node to fetch with bbox
	 * @param key {@code String} Key
	 * @param bbox {@code String} BBOX
	 * @param value {@code String} Value
	 * @author Yanis Labrak
	 */
	public void node(String key, String value, String bbox) {
		this.query += "node[\"" + key + "\"=\"" + value + "\"](" + bbox + ");";
	}
	
	/**
	 * <u>Example:</u> {@code way("amenity", "post_box");}
	 * @param key {@code String} Key
	 * @param value {@code String} Value
	 * @author Yanis Labrak
	 */
	public void way(String key, String value) {
		this.query += "way[\"" + key + "\"=\"" + value + "\"](area.SA);";		
	}
	
	/**
	 * <u>Example:</u> {@code way("amenity", "post_box");}
	 * @param key {@code String} Key
	 * @param value {@code String} Value
	 * @param bbox {@code String} BBox coordinates
	 * @author Yanis Labrak
	 */
	public void way(String key, String value, String bbox) {
		this.query += "way[\"" + key + "\"=\"" + value + "\"](" + bbox + ");";		
	}
	
	/**
	 * <u>Example:</u> {@code way("highway");}
	 * @param key {@code String} Key
	 * @author Yanis Labrak
	 */
	public void way(String key) {
		this.query += "way[\"" + key + "\"](area.SA);";
	}
	
	/**
	 * <u>Example:</u> {@code relation("highway");}
	 * @param key {@code String} Key
	 * @author Yanis Labrak
	 */
	public void relation(String key) {
		this.query += "relation[\"" + key + "\"](area.SA);";
	}
	
	/**
	 * <u>Example:</u> {@code relation("highway");}
	 * @param key {@code String} Key
	 * @param bbox {@code String} BBox coordinates
	 * @author Yanis Labrak
	 */
	public void relation(String key, String bbox) {
		this.query += "relation[\"" + key + "\"](" + bbox + ");";
	}
	
	/**
	 * End of the query
	 * @author Yanis Labrak
	 */
	public void out() {
		this.query += ");out;";
	}
	
	/**
	 * End of the query body and skeleton
	 * @throws UnsupportedEncodingException Thrown when the encoding process failed
	 * @author Yanis Labrak
	 */
	public void outBodySkel() throws UnsupportedEncodingException {
		this.query += URLEncoder.encode(");out body;>;out skel qt;", StandardCharsets.UTF_8.toString());
	}
	
	/*
	 * Convert the object into a {@code String}
	 * @author Yanis Labrak
	 */
	@Override
	public String toString() {
		return this.query;
	}
	
	/**
	 * BBox calculation from the {@code City} coordinates
	 * @param lat {@code Double} Latitude
	 * @param lon {@code Double} Longitude
	 * @return {@code Bbox} The BBox
	 */
	public static Bbox bboxCalc(Double lat, Double lon) {
		
		Double topLeft = lat - Settings.BBOX_SIZE;
		Double bottomLeft = lon - Settings.BBOX_SIZE;
		Double topRight = lat + Settings.BBOX_SIZE*(1-0.60);
		Double bottomRight = lon + Settings.BBOX_SIZE;
		
		topLeft =  Math.floor(topLeft * 100) / 100;
		bottomLeft =  Math.floor(bottomLeft * 100) / 100;
		topRight =  Math.floor(topRight * 100) / 100;
		bottomRight =  Math.floor(bottomRight * 100) / 100;
		
		Map.BOTTOM = topLeft;
		Map.LEFT = bottomLeft;
		Map.TOP = topRight;
		Map.RIGHT = bottomRight;
		
		return new Bbox(topLeft, topRight, bottomLeft, bottomRight);
	}
}
