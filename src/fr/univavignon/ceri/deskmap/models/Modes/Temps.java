/**
 * 
 */
package fr.univavignon.ceri.deskmap.models.Modes;

import fr.univavignon.ceri.deskmap.models.NodePath;
import fr.univavignon.ceri.deskmap.services.AStar;

/**
 * Find the closest one according to the time
 * @author Yanis Labrak
 */
public class Temps extends CalculMode {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 5729517209811910414L;

	@Override
	public NodePath getCloser(AStar pathProcess) {

		Double bestTime = Double.MAX_VALUE;

		NodePath best = null;

		// Time
		Double t1;
		Double t2;
		Double time;

		// Distance
		Double d1;
		Double d2;
		Double distance;

		for (NodePath node : pathProcess.close) {

			// Time
			t1 = pathProcess.time(node, pathProcess.now);
			t2 = pathProcess.time(node, pathProcess.arrival);
			time = t1 + t2;
			
			// Distance
			d1 = AStar.distance(node, pathProcess.now);
			d2 = AStar.distance(node, pathProcess.arrival);
			distance = d1 + d2;

			// If the distance is better
			if (time < bestTime) {
				
				best = new NodePath(node);
				best.duration = t1;
				best.distance = d1;
				bestTime = time;

				best.setStreet();
			}
		}
		
		return best;
	}
	
	/**
	 * Convert seconds into a date in the format hh:mm:ss
	 * @param secondsTotal {@code Integer}
	 * @return {@code String}
	 * @author Yanis Labrak
	 */
	public static String secondToTime(Double secondsTotal) {
		
		int minutes =  (int) (Math.floor((secondsTotal / 60 )* 100) / 100);
		int seconds = (int) (Math.floor((secondsTotal % 60)* 10000) / 100);
		
		return minutes + "." + seconds;		
	}
	
	@Override
	public String getUnit() {
		return "min";
	}

	@Override
	public double getMesurement(AStar pathProcess) {
		return pathProcess.getTotalTime();
	}
	
}
