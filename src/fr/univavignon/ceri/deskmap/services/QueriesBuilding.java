package fr.univavignon.ceri.deskmap.services;

import java.io.UnsupportedEncodingException;

/**
 * @author Yanis Labrak
 */
public abstract class QueriesBuilding {
	
	/**
	 * Build a Overpass Query in the way to fetch all the objects necessary to display the map
	 * @param bbox The Bounding box in which we want the data
	 * @return The OSM query
	 * @throws UnsupportedEncodingException Thrown when the encoding process failed
	 * @author Yanis Labrak
	 */
	public static String fullMapQuery(String bbox) throws UnsupportedEncodingException {

		OSM queryOverpass = new OSM();
		
		queryOverpass.output("json", "", false, "");
		queryOverpass.start();
		
		queryOverpass.nodeMulti("landuse", "residential|industrial|commercial|retail|railway|cemetery|forest|grass|farmland|greenhouse_horticulture|farmyard|military|quarry|vineyard|orchard|construction|plant_nursery", bbox);
		queryOverpass.way("landuse","allotments",bbox);
		queryOverpass.way("landuse","residential",bbox);
		queryOverpass.way("landuse","industrial",bbox);
		queryOverpass.way("landuse","commercial",bbox);
		queryOverpass.way("landuse","retail",bbox);
		queryOverpass.way("landuse","railway",bbox);
		queryOverpass.way("landuse","cemetery",bbox);
		queryOverpass.way("landuse","forest",bbox);
		queryOverpass.way("landuse","grass",bbox);
		queryOverpass.way("landuse","plant_nursery",bbox);
		queryOverpass.way("landuse","greenhouse_horticulture",bbox);
		queryOverpass.way("landuse","farmland",bbox);
		queryOverpass.way("landuse","farmyard",bbox);
		queryOverpass.way("landuse","military",bbox);
		queryOverpass.way("landuse","vineyard",bbox);
		queryOverpass.way("landuse","orchard",bbox);
		queryOverpass.way("landuse","quarry",bbox);
		queryOverpass.way("landuse","construction",bbox);
		queryOverpass.relation("landuse",bbox);

		queryOverpass.nodeMulti("amenity", "school|hospital|parking|prison", bbox);
		queryOverpass.way("amenity","school",bbox);
		queryOverpass.way("amenity","hospital",bbox);
		queryOverpass.way("amenity","parking",bbox);
		queryOverpass.way("amenity","prison",bbox);
		queryOverpass.relation("amenity",bbox);
		
		queryOverpass.nodeMulti("waterway", "riverbank|drain", bbox);
		queryOverpass.way("waterway","riverbank",bbox);
		queryOverpass.way("waterway","drain",bbox);
		queryOverpass.relation("waterway",bbox);
		
		queryOverpass.nodeMulti("man_made", "bridge", bbox);
		queryOverpass.way("man_made","bridge",bbox);
		queryOverpass.relation("man_made",bbox);
		
		queryOverpass.nodeMulti("railway", "contact_line|abandoned", bbox);
		queryOverpass.way("railway","contact_line",bbox);
		queryOverpass.way("railway","abandoned",bbox);
		queryOverpass.relation("railway",bbox);

		queryOverpass.nodeMulti("leisure", "sports_centre|park|golf_course|pitch|garden|track|ice_rink", bbox);
		queryOverpass.way("leisure","sports_centre",bbox);
		queryOverpass.way("leisure","park",bbox);
		queryOverpass.way("leisure","golf_course",bbox);
		queryOverpass.way("leisure","pitch",bbox);
		queryOverpass.way("leisure","garden",bbox);
		queryOverpass.way("leisure","track",bbox);
		queryOverpass.way("leisure","ice_rink",bbox);
		queryOverpass.relation("leisure",bbox);

		queryOverpass.nodeMulti("highway", "primary|secondary|tertiary|service|trunk|residential|living_street|pedestrian|motorway|unclassified|bus_stop|trunk_link|footway|cycleway|path|track", bbox);
		queryOverpass.way("highway","primary",bbox);
		queryOverpass.way("highway","secondary",bbox);
		queryOverpass.way("highway","tertiary",bbox);
		queryOverpass.way("highway","service",bbox);
		queryOverpass.way("highway","trunk",bbox);
		queryOverpass.way("highway","trunk_link",bbox);
		queryOverpass.way("highway","residential",bbox);
		queryOverpass.way("highway","living_street",bbox);
		queryOverpass.way("highway","pedestrian",bbox);
		queryOverpass.way("highway","motorway",bbox);
		queryOverpass.way("highway","unclassified",bbox);
		queryOverpass.way("highway","cycleway",bbox);
		queryOverpass.way("highway","footway",bbox);
		queryOverpass.way("highway","path",bbox);
		queryOverpass.way("highway","track",bbox);
		queryOverpass.relation("highway",bbox);

		queryOverpass.node("building","yes",bbox);
		queryOverpass.way("building","yes",bbox);
		queryOverpass.way("building","school",bbox);
		queryOverpass.way("building","industrial",bbox);
		queryOverpass.way("building","greenhouse",bbox);
		queryOverpass.way("building","hospital",bbox);
		queryOverpass.way("building","hangar",bbox);
		queryOverpass.way("building","public",bbox);
		queryOverpass.way("building","residential",bbox);
		queryOverpass.relation("building",bbox);
		
		queryOverpass.nodeMulti("natural","water|scrub|wood|bay|wetland",bbox);
		queryOverpass.way("natural","water",bbox);
		queryOverpass.way("natural","scrub",bbox);
		queryOverpass.way("natural","wood",bbox);
		queryOverpass.way("natural","bay",bbox);
		queryOverpass.way("natural","wetland",bbox);
		queryOverpass.relation("natural",bbox);
		
		queryOverpass.node("route", "bus", bbox);
		queryOverpass.way("route","bus",bbox);
		queryOverpass.relation("route",bbox);
		
		queryOverpass.outBodySkel();
		
		String query = queryOverpass.query;
		
		return query;
	}

}
