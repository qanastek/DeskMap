/**
 * 
 */
package fr.univavignon.ceri.deskmap.services.Threads;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import fr.univavignon.ceri.deskmap.controllers.MainViewController;
import fr.univavignon.ceri.deskmap.models.Save;
import fr.univavignon.ceri.deskmap.models.Modes.TransportMode;
import javafx.concurrent.Task;

/**
 * Thread for the export
 * @author Mohamed Ben Yamna
 */
public class Export extends Task<File> {
	
	/**
	 * The destination file
	 */
	public static File OUTPUT_PATH;
	
	@Override
	protected File call() throws Exception {
		
		XMLEncoder encoder = null;

		try {
			
			// Display the progress
			MainViewController.addStateBar("Exportation start...");

			Save save = new Save(
				MainViewController.CITY,
				MainViewController.CURRENT_ARRIVAL_ROAD,
				MainViewController.CURRENT_DEPARTURE_ROAD,
				MainViewController.currentCalculMode,
				new TransportMode(MainViewController.CURRENT_TRANSPORT_MODE),
				MainViewController.SENS_ENABLED,
				MainViewController.CORRESPONDENCE_ENABLED,
				RunningThreads.SEARCHING.paths
			);
            
			// The output file
			encoder = new XMLEncoder(new FileOutputStream(Export.OUTPUT_PATH));
			
			/**
			 * Insert fields
			 */
			
		    encoder.writeObject(save);
          
		    /**
		     * Write in the file
		     */
		    encoder.flush();
		    
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			// Close the file
		    encoder.close();
		}
		
		return null;
	}
	
	@Override
	protected void succeeded() {
		MainViewController.addStateBar("File saved!");
		super.succeeded();
	}
	
	@Override
	protected void cancelled() {
		MainViewController.addStateBar("Exportation canceled !");
		super.cancelled();
	}
	
	@Override
	protected void failed() {
		MainViewController.addStateBar("Exportation canceled !");
		super.failed();
	}

	@Override
	protected void finalize() throws Throwable {
		MainViewController.addStateBar("Exportation canceled !");
		super.finalize();
	}
}
