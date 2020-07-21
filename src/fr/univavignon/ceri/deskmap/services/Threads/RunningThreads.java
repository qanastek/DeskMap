/**
 * 
 */
package fr.univavignon.ceri.deskmap.services.Threads;

/**
 * In this class you will find all the running threads
 * @author Yanis Labrak
 */
public class RunningThreads {
	
	/**
	 * The thread for the path finding
	 */
	public static Searching SEARCHING = new Searching();
	
	/**
	 * The thread for the auto-completion
	 */
	public static AutocompleteCity AUTOCOMPLETE;

}
