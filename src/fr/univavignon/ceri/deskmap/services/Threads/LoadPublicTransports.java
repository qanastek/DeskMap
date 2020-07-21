package fr.univavignon.ceri.deskmap.services.Threads;

import fr.univavignon.ceri.deskmap.config.Settings;
import fr.univavignon.ceri.deskmap.controllers.MainViewController;
import fr.univavignon.ceri.deskmap.services.QueriesLoading;
import javafx.concurrent.Task;

/**
 * Thread which fetch the public transport positions
 * @author Made by Zhiao Zheng
 * @author Reworked by Mohamed Ben Yamna
 */
public class LoadPublicTransports extends Task<Integer> {

	@Override
	protected Integer call() throws Exception {
		
    	while(true) {
    		
    		// If the thread as been stopped, break the loop
    		if (MainViewController.UPDATE_PUBLIC_TRANSPORT_POSITIONS == false) {    	    	
    	    	return 0;
			}
    		
    		// Fetch the data
			QueriesLoading.loadPublicTransports();
			
			// Render the canvas
			Double val = Math.random();
			updateProgress(val, val+1);
			
            try {
				Thread.sleep(Settings.REFRESH_RATE * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		System.out.println("STOP THE DOWNLOAD OF PUBLIC TRANSPORTS POSITIONS !");
		return super.cancel(mayInterruptIfRunning);
	}
}
