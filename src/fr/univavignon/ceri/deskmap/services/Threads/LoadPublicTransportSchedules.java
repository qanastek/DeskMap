package fr.univavignon.ceri.deskmap.services.Threads;

import fr.univavignon.ceri.deskmap.Map;
import fr.univavignon.ceri.deskmap.services.QueriesLoading;
import javafx.concurrent.Task;

/**
 * Thread which fetch the public transport positions
 * @author Zhiao Zheng
 */
public class LoadPublicTransportSchedules extends Task<Integer> {

	@Override
	protected Integer call() throws Exception {

		// Fetch the data
		QueriesLoading.loadPublicTransportsSchedules();
		
		// Exit
		return 0;
	}
	
	@Override
	protected void succeeded() {
		System.out.println("Bus stations path: " + Map.BUS_STATIONS_PATH.size());
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		System.out.println("STOP THE DOWNLOAD OF PUBLIC TRANSPORTS SCHEDULES !");
		return super.cancel(mayInterruptIfRunning);
	}
}
