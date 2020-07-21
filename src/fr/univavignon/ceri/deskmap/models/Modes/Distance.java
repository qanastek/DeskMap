/**
 * 
 */
package fr.univavignon.ceri.deskmap.models.Modes;

import fr.univavignon.ceri.deskmap.models.NodePath;
import fr.univavignon.ceri.deskmap.services.AStar;

/**
 * @author Yanis Labrak
 */
public class Distance extends CalculMode {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -3023592852979583462L;

	@Override
	public NodePath getCloser(AStar pathProcess) {

		Double bestDistance = Double.MAX_VALUE;

		NodePath best = null;

		// Distance
		Double d1;
		Double d2;
		Double distance;
		
		// Time
		Double t1;
		Double t2;
		Double time;
		
		// Empty check
		if (pathProcess.close == null || pathProcess.close.size() <= 0) {
			return null;
		}

		// For each close nodes
		for (NodePath node : pathProcess.close) {
			
			// Empty check
			if (node == null) {
				continue;
			}

			// Distance
			d1 = AStar.distance(node, pathProcess.now);
			d2 = AStar.distance(node, pathProcess.arrival);
			distance = d1 + d2;

			// Time
			t1 = pathProcess.time(node, pathProcess.now);
			t2 = pathProcess.time(node, pathProcess.arrival);
			time = t1 + t2;

			// If the distance is better
			if (distance < bestDistance) {
				
				best = new NodePath(node);
				best.distance = d1;
				best.duration = t1;
				bestDistance = distance;
				best.setStreet();
			}
		}
		
		return best;
	}
	
	@Override
	public String getUnit() {
		return "m";
	}

	@Override
	public double getMesurement(AStar pathProcess) {
		return pathProcess.getTotalDistance();
	}

}
