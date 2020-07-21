package fr.univavignon.ceri.deskmap.controllers;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import javax.xml.bind.annotation.XmlAttribute;

import DeskMapExceptions.CannotReachServerException;
import fr.univavignon.ceri.deskmap.Launcher;
import fr.univavignon.ceri.deskmap.Map;
import fr.univavignon.ceri.deskmap.config.Settings;
import fr.univavignon.ceri.deskmap.config.Textures;
import fr.univavignon.ceri.deskmap.config.TransportModeNames;
import fr.univavignon.ceri.deskmap.models.Bbox;
import fr.univavignon.ceri.deskmap.models.Node;
import fr.univavignon.ceri.deskmap.models.Path;
import fr.univavignon.ceri.deskmap.models.Save;
import fr.univavignon.ceri.deskmap.models.Modes.CalculMode;
import fr.univavignon.ceri.deskmap.models.Modes.Distance;
import fr.univavignon.ceri.deskmap.models.Modes.RenderCity;
import fr.univavignon.ceri.deskmap.models.Modes.Temps;
import fr.univavignon.ceri.deskmap.models.Modes.TransportMode;
import fr.univavignon.ceri.deskmap.models.UIElement.CheckBoxPublicTransport;
import fr.univavignon.ceri.deskmap.models.UIElement.ComboBoxPublicTransport;
import fr.univavignon.ceri.deskmap.models.geopoint.BusStation;
import fr.univavignon.ceri.deskmap.models.geopoint.City;
import fr.univavignon.ceri.deskmap.models.line.PublicTransportRoute;
import fr.univavignon.ceri.deskmap.models.line.Road;
import fr.univavignon.ceri.deskmap.services.Draw;
import fr.univavignon.ceri.deskmap.services.Encode;
import fr.univavignon.ceri.deskmap.services.OSM;
import fr.univavignon.ceri.deskmap.services.QueriesBuilding;
import fr.univavignon.ceri.deskmap.services.QueriesLoading;
import fr.univavignon.ceri.deskmap.services.Threads.AutocompleteCity;
import fr.univavignon.ceri.deskmap.services.Threads.Export;
import fr.univavignon.ceri.deskmap.services.Threads.LoadPublicTransportSchedules;
import fr.univavignon.ceri.deskmap.services.Threads.LoadPublicTransports;
import fr.univavignon.ceri.deskmap.services.Threads.Searching;
import fr.univavignon.ceri.deskmap.services.Threads.RunningThreads;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The controller of the FXML file
 * @author Quentin Capdepon
 */
public class MainViewController implements Initializable {
	
	/**
	 * Map canvas
	 */
	@FXML
	private Canvas canvasMap;
	
	/**
	 * The size of the canvas
	 */
	public static Point2D canvasMapSize;
	
	/**
	 * Node canvas
	 */
	@FXML
	private Canvas canvasNodes;
	
	/**
	 * Name of the city
	 */
	@FXML
	private ComboBox<City> cityName;
	
	/**
	 * Start house number
	 */
	@FXML
	private TextField fromNumber;
	
	/**
	 * Start street name
	 */
	@FXML
	private ComboBox<Road> fromName;
	
	/**
	 * Destination house number
	 */
	@FXML
	private TextField toNumber;
	
	/**
	 * Destination street name
	 */
	@FXML
	private ComboBox<Road> toName;
	
	/**
	 * Path
	 */
	@FXML
	private TextArea mapPath;
	
	/**
	 * Bottom status bar
	 */
	@FXML
	private TextArea statusBar;
	
	/**
	 * Zoom slider
	 */
	@FXML
	private Slider slider;
	
	/**
	 * Search street for the city
	 */
	@FXML
	private Button cityButton;
	
	/**
	 * Reset all fields
	 */
	@FXML
	private Button resetBtn;
	
	/**
	 * Search the path
	 */
	@FXML
	private Button searchBtn;
	
	/**
	 * Hide the left panel
	 */
	@FXML
	private Button hideLeft;
	
	/**
	 * Show the left panel
	 */
	@FXML
	private Button showLeft;
	
	/**
	 * The horizontal {@code splitPane}
	 */
	@FXML
	private SplitPane splitPane;
	
	/**
	 * The vertical {@code splitPane}
	 */
	@FXML
	private SplitPane verticalSplitPane;
	
	/**
	 * The {@code Pane} which contain the {@code Canvas}
	 */
	@FXML
	private Pane canvasPane;
	
	/**
	 * The value of the scale
	 */
	@FXML
    private Text scaleValue;

    /**
     * Calculation modes selection
     * @author Quentin Capedepon
     */
    @FXML
    private ComboBox<CalculMode> calculMode = new ComboBox<CalculMode>();
    
    /**
     * This is the current calculation mode selected
     * @author Yanis Labrak
     */
    @XmlAttribute(name = "current_calcul_mode")
    public static CalculMode currentCalculMode = new Distance();

    /**
     * The current calculation mode
     */
	public static String CURRENT_CALCUL_MODE;

    /**
     * Transport modes selection
	 * @author Quentin Capedepon
     */
    @FXML
    private ComboBox<TransportMode> transportMode = new ComboBox<TransportMode>();
    
    /**
     * The current transport mode
	 * @author Quentin Capedepon
     */
    @XmlAttribute(name = "current_transport_mode")
    public static String CURRENT_TRANSPORT_MODE = Settings.DEFAULT_TRANSPORT_MODE.name;
    
    /**
     * Reference all the bus and tramway lines
     */
    @FXML
    private ComboBox<CheckBoxPublicTransport> lines = new ComboBoxPublicTransport();
    
    /**
     * Display the transports lines
     */
    @FXML
    private Button displayTC;
    
    /**
     * Hide the transports lines
     */
    @FXML
    private Button hideTC;

    /**
     * Correspondence or not
     */
    @FXML
    private CheckBox correspondance;

    /**
     * Both sens or not
     */
    @FXML
    private CheckBox sens;
    
    /**
     * Does the one way is enabled ?
     */
    @XmlAttribute(name = "sens_enabled")
    public static boolean SENS_ENABLED;
    
    /**
     * Correspondence enabled
     */
    @XmlAttribute(name = "correspondence_enabled")
    public static boolean CORRESPONDENCE_ENABLED;

    /**
     * Export to XML
     */
    @FXML
    private Button exportXML;

    /**
     * Import to XML
     */
    @FXML
    private Button importXML;
    
    /**
     * Progress indicator for the file parsing
     */
    @FXML
    private ProgressIndicator loading;
    
    /**
     * Progress indicator status
     */
	public static ObservableBooleanValue LOADING_STATUS;
	
	/**
	 * This stack contain all the {@code ContextMenu} present on the screen
	 */
	private Stack<ContextMenu> stackContextMenu = new Stack<ContextMenu>();
	
	/**
	 * List of {@code City}
	 */
	public static ObservableList<City> LIST_CITY;
	
	/**
	 * The default list of streets for a specific city
	 */
	public static ObservableList<Road> LIST_STREETS_NAMES = FXCollections.observableArrayList();
	
	/**
	 * The observable variable for the {@code FROM} comboBox
	 */
	public static ObservableList<Road> LIST_STREETS_NAMES_SORTED_FROM = FXCollections.observableArrayList();

	/**
	 * The observable variable for the {@code TO} comboBox
	 */
	public static ObservableList<Road> LIST_STREETS_NAMES_SORTED_TO = FXCollections.observableArrayList();
	
	/**
	 * The observable variable for the {@code City} comboBox
	 */
	public static ObservableList<City> LIST_CITIES_SORTED = FXCollections.observableArrayList();
	
	/**
	 * The observable variable for the bottom {@code TextArea}
	 */
	public static StringProperty TEXT_AREA_BOTTOM = new SimpleStringProperty("No logs...");
	
	/**
	 * The observable variable for the left hand side {@code TextArea}
	 */
	public static StringProperty TEXT_AREA_LEFT = new SimpleStringProperty("Informations about the path...");
	
	/**
	 * The current city selected by the user
	 */
	public static City CITY = new City();
	
	/**
	 * The current departure {@code Road}
	 */
    @XmlAttribute(name = "departure_road")
	public static Road CURRENT_DEPARTURE_ROAD = null;
	
	/**
	 * The current departure node
	 */
	public static Node CURRENT_DEPARTURE_NODE = null;
	
	/**
	 * The current arrival {@code Road}
	 */
    @XmlAttribute(name = "arrival_road")
	public static Road CURRENT_ARRIVAL_ROAD = null;
	
	/**
	 * The current arrival node
	 */
	public static Node CURRENT_ARRIVAL_NODE = null;

	/**
	 * Status of the map rendering
	 */
	public static boolean FIRST_RENDER = true;
	
	/**
	 * Way of transportation options
	 * @author Zhiao Zheng
	 */
	public static ObservableList<TransportMode> OPTIONS_TRANSPORTATION_MODES = FXCollections.observableArrayList(
        new TransportMode(TransportModeNames.BIKE),
        new TransportMode(TransportModeNames.CAR),
        new TransportMode(TransportModeNames.PUBLIC_TRANSPORT),
        new TransportMode(TransportModeNames.FOOT)
    );
	
	/**
	 * Bus and tramway lines options
	 */
	public static ObservableList<CheckBoxPublicTransport> SELECTED_PUBLIC_TRANSPORTS = FXCollections.observableArrayList();
		
	/**
	 * The allowed public transports lines
	 */
	public static List<String> PUBLIC_TRANSPORTS_ALLOWED = new ArrayList<String>();
			
	/**
	 * Way of process
     * @author Yanis Labrak
	 */
	public static ObservableList<CalculMode> OPTIONS_CALCUL = FXCollections.observableArrayList(
        new fr.univavignon.ceri.deskmap.models.Modes.Distance(),
        new Temps()
    );
	
	/**
	 * The map instance which will contain all the objects to display
	 */
	public static Map map = new Map();	

	/**
	 * GraphicsContext for the canvas
	 */
	public static GraphicsContext gc;
	
	/**
	 * GraphicsContext for the nodes canvas
	 */
	public static GraphicsContext gcNodes;
	
	/**
	 * Status
	 * <br>
	 * {@code False} : Stopped
	 * <br>
	 * {@code True} : Running
	 */
	public static boolean STATUS = true;
	
	/**
	 * The thread which is dedicated to the public transports positions update every minutes
	 * @author Zihao Zheng
	 */
	public static boolean UPDATE_PUBLIC_TRANSPORT_POSITIONS = true;

	/**
	 * Status of the printability of the public transport routes
	 */
	public static SimpleBooleanProperty STATUS_PUBLIC_TRANSPORT_ROUTE = new SimpleBooleanProperty(false);

	/**
	 * List of the select public transport routes
	 */
	public static ObservableList<String> SELECTED_ROUTES = FXCollections.observableArrayList();
	
	/**
	 * Automatically started when the program start
	 * @author First implementation Yanis Labrak
	 * @author Quentin Capdepon
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		System.out.println("Initialize");
		
		// Get the graphics context of the canvas
		MainViewController.gc = this.canvasMap.getGraphicsContext2D();
		MainViewController.gcNodes = this.canvasNodes.getGraphicsContext2D();
		
		// Link the value of the scale to the Text object
		this.scaleValue.textProperty().bind(Map.SCALE_METER.asString());
		
		// Set the min and max value of the slider
		this.slider.setMax(Settings.MAX_SCALE / 6);
		this.slider.setMin(Settings.MIN_SCALE);
		this.slider.setMajorTickUnit(Settings.MAX_SCALE / 6);
		this.slider.setMinorTickCount(Settings.MIN_SCALE);

		// Assign both lists to their comboBox's
		this.fromName.setItems(MainViewController.LIST_STREETS_NAMES_SORTED_FROM);
		this.toName.setItems(MainViewController.LIST_STREETS_NAMES_SORTED_TO);

		// Assign the auto-completion list to this comboBox
		this.cityName.setItems(MainViewController.LIST_CITIES_SORTED);
		
		/**
		 * ComboBox calcul modes
		 */
		this.calculMode.setItems(OPTIONS_CALCUL);
		
		/**
		 * ComboBox transportation modes
		 */
		this.transportMode.setItems(OPTIONS_TRANSPORTATION_MODES);
		
		/**
		 * Listen the status changements
		 */
		MainViewController.STATUS_PUBLIC_TRANSPORT_ROUTE.addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				
				// Click on hide
				if (newValue == false) {
					
					// Disable the display button
					MainViewController.this.displayTC.setDisable(false);
					
					// Enable the hide button
					MainViewController.this.hideTC.setDisable(true);
					
				}
				// Click on display
				else if(newValue == true) {
					
					// Disable the display button
					MainViewController.this.displayTC.setDisable(true);
					
					// Enable the hide button
					MainViewController.this.hideTC.setDisable(false);
				}
			}
		});
		
		/**
		 * ComboBox public transport routes
		 */
		this.lines.setItems(MainViewController.SELECTED_PUBLIC_TRANSPORTS);
					
		/**
		 * Listen checkBox click
		 */
		this.lines.setCellFactory( c -> {
			
            ListCell<CheckBoxPublicTransport> cell = new ListCell<CheckBoxPublicTransport>(){
                @Override
                protected void updateItem(CheckBoxPublicTransport item, boolean empty) {
                	
                    super.updateItem(item, empty);
                    
                    if (!empty) {
                        final CheckBoxPublicTransport cb = new CheckBoxPublicTransport(item.line);
                        cb.selectedProperty().bind(item.checkProperty());
                        setGraphic(cb);
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            	
                cell.getItem().checkProperty().set(!cell.getItem().checkProperty().get());
                
                // Clear the lines selection
				MainViewController.SELECTED_ROUTES.clear();
                
				// For each checked lines
                this.lines.getItems().filtered( f-> f != null).filtered( f-> f.checkProperty().get()).forEach( p -> {
                    
                	// Select it
    				MainViewController.SELECTED_ROUTES.add(p.line.toUpperCase());	
                });

				System.out.println(MainViewController.SELECTED_ROUTES.toString());                
                this.lines.setPromptText(MainViewController.SELECTED_ROUTES.toString());
                
        		// Re-render the canvas if it's allowed
        		if (MainViewController.STATUS_PUBLIC_TRANSPORT_ROUTE.get() == true) {
        			
        			// If no lines as been selected
        			if (MainViewController.SELECTED_ROUTES.size() <= 0 && RunningThreads.SEARCHING.paths.size() > 0) {
						
        				// Clear the path
        				RunningThreads.SEARCHING.paths.clear();
					}
        			
            		MainViewController.render();				
    			}
        		
        		// Consume
        		event.consume();
            });

            return cell;
        });
		
		// Assign the observable to their text area
		this.statusBar.textProperty().bind(TEXT_AREA_BOTTOM);
		MainViewController.TEXT_AREA_BOTTOM.addListener((observable, oldValue, newValue) -> {			
			// Auto scroll down
			this.statusBar.selectPositionCaret(this.statusBar.getLength());
			this.statusBar.deselect();
        });
		
		this.mapPath.textProperty().bind(TEXT_AREA_LEFT);
		MainViewController.TEXT_AREA_LEFT.addListener((observable, oldValue, newValue) -> {
			// Auto scroll down
			this.mapPath.selectPositionCaret(this.mapPath.getLength());
			this.mapPath.deselect();
        });
		
		try {
			
			Platform.runLater(()->{
	    		try {
	    			
	    			// Set visible
	    			this.loading.setVisible(true);
	    			
	    			RenderCity firstRender = new RenderCity(Settings.DEFAULT_CITY);
	    			new Thread(firstRender).start();
	    			
	    			this.loading.visibleProperty().bind(firstRender.runningProperty());
	    			
	    			// Load all the streets
	    			QueriesLoading.loadStreets();
	    			
	    			// Fetch the public transport and refresh the canvas
	    			LoadPublicTransports threadLoadPublicTransport = new LoadPublicTransports();
	    			new Thread(threadLoadPublicTransport).start();
	    			
	    			// Listening to position update
	    			threadLoadPublicTransport.progressProperty().addListener((obs, oldValue, newValue) -> {
	    				MainViewController.render();
	    			});
	    			
	    			// Fetch the public transport schedules
	    			LoadPublicTransportSchedules threadLoadPublicTransportSchedules = new LoadPublicTransportSchedules();
	    			new Thread(threadLoadPublicTransportSchedules).start();
	    			
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
			});
			
			// When the canvas width change
			this.canvasPane.widthProperty().addListener((obs, oldVal, newVal) -> {
				
				Double size;
				
				if (newVal.doubleValue() > this.canvasPane.heightProperty().get()) {
					size = newVal.doubleValue();
				} else {
					size = this.canvasPane.heightProperty().get();
				}
				
				this.canvasMap.setHeight(size);
				this.canvasMap.setWidth(size);
				MainViewController.canvasMapSize = new Point2D(size, size);
				MainViewController.renderMap();
			});

			// When the canvas height change
			this.canvasPane.heightProperty().addListener((obs, oldVal, newVal) -> {
				
				Double size;
				
				if (this.canvasPane.widthProperty().get() > newVal.doubleValue()) {
					size = this.canvasPane.widthProperty().get();
				} else {
					size = newVal.doubleValue();
				}
				
				this.canvasMap.setHeight(size);
				this.canvasMap.setWidth(size);
				MainViewController.canvasMapSize = new Point2D(size, size);
				MainViewController.renderMap();
			});
			
		} catch (Exception e) {
			System.err.println(e);
		} 
		
	}
	
	/**
	 * Add informations into the status bar {@code textArea}
	 * @param newLine {@code String} The line to add
	 * @author Quentin Capdepon
	 */
	public static void addStateBar(String newLine) {
		
		// If its empty
		if (MainViewController.TEXT_AREA_BOTTOM.get().equals("No logs...")) {
			MainViewController.TEXT_AREA_BOTTOM.set(newLine);
		} else {
			// Add to the next line
			MainViewController.TEXT_AREA_BOTTOM.set(MainViewController.TEXT_AREA_BOTTOM.get() + '\n' + newLine);
		}
	}
	
	/**
	 * Add informations into the path area {@code textArea}
	 * @param newLine {@code String} The line to add
	 * @author Quentin Capdepon
	 */
	public static void addMapPath(String newLine) {		
		
		// If its empty
		if (MainViewController.TEXT_AREA_LEFT.get().equals("Informations about the path...")) {
			MainViewController.TEXT_AREA_LEFT.set(newLine);
		} else {
			// Add to the next line
			MainViewController.TEXT_AREA_LEFT.set(MainViewController.TEXT_AREA_LEFT.get() + '\n' + newLine);
		}
	}
	
	/**
	 * Clear informations of the path {@code textArea}
	 * @author Quentin Capdepon
	 */
	public static void clearMapPathTextArea() {	
		MainViewController.TEXT_AREA_LEFT.set("Informations about the path...");
	}
	
	/**
	 * Method trigged when the user click on the search button
	 * @param event {@code no informations}
	 * @throws Exception {@code no informations}
	 * @author Quentin Capdepon
	 */
	@FXML
	public void searching(ActionEvent event) throws Exception {
		
		// Change current running status
		MainViewController.STATUS = true;

		// Enable the reset button
		this.resetBtn.setDisable(false);
		
		// Get fields string values
		String fromNumber = this.fromNumber.getText();
		Boolean fromNameEmpty = this.fromName.getSelectionModel().isEmpty();
		
		String toNumber = this.toNumber.getText();
		Boolean toNameEmpty = this.toName.getSelectionModel().isEmpty();
		
		if (fromNameEmpty) {
			MainViewController.addStateBar("Invalid departure adress");	
		}
		else if (toNameEmpty) {
			MainViewController.addStateBar("Invalid arrival address");	
		}
		else {
			
			MainViewController.addStateBar("Searching for the best path");
			
			// Get the current departure and arrival nodes
			Node currentDeparture = MainViewController.CURRENT_DEPARTURE_NODE;
			Node currentArrival = MainViewController.CURRENT_ARRIVAL_NODE;

			// The current departure road
			Road from = MainViewController.LIST_STREETS_NAMES_SORTED_FROM.get(this.fromName.getSelectionModel().getSelectedIndex());
			
			// If the current departure node is null get the one from the current selected from street
			if (currentDeparture == null) {
				currentDeparture = from.getMiddle();
				MainViewController.CURRENT_DEPARTURE_ROAD = from;
			}

			// The current arrival road
			Road to = MainViewController.LIST_STREETS_NAMES_SORTED_TO.get(this.toName.getSelectionModel().getSelectedIndex());
			
			// If the current arrival node is null get the one from the current selected to street
			if (currentArrival == null) {
				currentArrival = to.getMiddle();
				MainViewController.CURRENT_ARRIVAL_ROAD = to;
			}
			
			MainViewController.addMapPath(fromNumber + " " + from + " -> " + toNumber + " " + to);
			
			// Fetch the current process method
			this.getProcessMethod();
			
			/**
			 * Set current from & to
			 */
			MainViewController.CURRENT_ARRIVAL_ROAD = currentDeparture.getFirstRoad();
			MainViewController.CURRENT_DEPARTURE_ROAD = currentArrival.getFirstRoad();

			// Start the thread for the path finding
			RunningThreads.SEARCHING = new Searching(
				currentDeparture,
				currentArrival,
				MainViewController.CURRENT_TRANSPORT_MODE,
				MainViewController.CURRENT_CALCUL_MODE
			);
			new Thread(RunningThreads.SEARCHING).start();
			
			// Enable the export button
			this.exportXML.setDisable(false);
		}
	}
	
	/**
	 * Return the process method to use
     * @author Quentin Capedepon
	 * @return {@code CalculMode}
	 */
	private CalculMode getProcessMethod() {
		
		// The current process method
		String method = this.calculMode.getEditor().getText();
		return MainViewController.currentCalculMode = MainViewController.getCalculMode(method);
	}
	
	/**
	 * Return the calcul mode as a instance
	 * @param currentCalculMode {@code String}
	 * @return {@code CalculMode}
	 */
	public static CalculMode getCalculMode(String currentCalculMode) {
    	
    	CalculMode mode = null;
		
		// If the field is empty ,return a default mode
		if (currentCalculMode == null || currentCalculMode.isEmpty() == true) {
			mode = new Distance();
		} else {

			try {
				Class<?> classe = Class.forName("fr.univavignon.ceri.deskmap.models.Modes." + currentCalculMode);
				mode = (CalculMode) classe.newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}			
		}
		
		return mode;
	}

	/**
	 * Make the objects for the map
	 * @param city {@code City} The city
	 * @throws Exception If the coordinates wasn't found
	 * @throws CannotReachServerException Exception thrown when the server cannot be reached
	 * @author Yanis Labrak
	 */
	public static void fetchAllContentCity(City city) throws Exception, CannotReachServerException {
		
		System.out.println("City loaded: " + city.name + "," + city.point);
		
		try {
			// Fetch the coordinates of the city
			String cityCoordinate = city.point.lat + "|" + city.point.lon;
			
			String[] coordinates = cityCoordinate.split("\\|");
			
			if (coordinates.length <= 0) {
				System.err.println("cityCoordinate wasn't found !");
			} else {
				MainViewController.addStateBar("City coordinates find");
			}
			
			// Make the bbox
			String bbox = OSM.bboxCalc(
				Double.parseDouble(coordinates[0]),
				Double.parseDouble(coordinates[1])
			).toString();
			
			MainViewController.addStateBar("BBox created");
			
			// Make the query for getting the full map
			String query = QueriesBuilding.fullMapQuery(bbox);
			
			String CITIES_FILE = URLEncoder.encode(city.name
			.replaceAll("\\.", "\\_")
			.replaceAll("\\/", "\\_")
			.toLowerCase(), "UTF-8") + "Map.json";
			
//			System.out.println("File name: " + CITIES_FILE);
			
			File f = new File(CITIES_FILE);
			
			// If the file doesn't exist
			if (!f.exists()) {
				
				MainViewController.addStateBar("File not found !");
				
				// Download the file
				QueriesLoading.loadQueryInFile(query, CITIES_FILE);
				
				// Show the progress
			    MainViewController.addStateBar("Streets of " + city.name + " downloaded !");
			    System.out.println("Streets of " + city.name + " downloaded !");
			}
			
		} 
		catch (CannotReachServerException e) {
			MainViewController.addStateBar("Server cannot be reached !");
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}
	
	/**
	 * Method trigged when the user click on the reset button
	 * @param event {@code ActionEvent}
	 * @author Quentin Capdepon
	 */
	@FXML
	public void Reset(ActionEvent event) {
		
		MainViewController.STATUS = false;
		
		// Stop the thread
		if (RunningThreads.SEARCHING != null && RunningThreads.SEARCHING.isRunning() == true) {
			
			RunningThreads.SEARCHING.cancel();			
			
			this.searchBtn.setDisable(false);
			this.resetBtn.setDisable(true);
			
			// Disable the export button
			this.exportXML.setDisable(true);
			
			// Render the map
			MainViewController.renderMap();
			
			MainViewController.addStateBar("Process stopped !");
			
		} else {			
			MainViewController.addStateBar("Cannot stop !");			
		}
	}
	
	/**
	 * Disable search until all field wasn't fill up
	 * @author Quentin Capdepon
	 */
	private void checkAllFields() {
		
		if (this.cityName.getSelectionModel().isEmpty() ||
			this.fromName.getSelectionModel().getSelectedIndex() < 0 ||
			this.toName.getSelectionModel().getSelectedIndex() < 0
 		) {
			this.searchBtn.setDisable(true);
		}
		else {			
			this.searchBtn.setDisable(false);
		}
	}

	/**
	 * Check if the input is a integer
	 * @param event {@code KeyEvent} The input character
	 * @author Quentin Capdepon
	 */
	private void checkInputIsInteger(KeyEvent event)
    {		
		char value = event.getCharacter().charAt(0);
		
		// Destroy the input if it's not a Integer
		if (!Character.isDigit(value)) {
			event.consume();
		}
    }
	
	/**
	 * Check if the input is a letter
	 * @author Quentin Capdepon
	 */
	public void checkInputCity()
    {
		
		String currentCityName = this.cityName.getEditor().getText();
		
		// If is empty
		if (currentCityName.length() == 0) {
			return;
		}
		
		int idLast = currentCityName.length() > 1 ? currentCityName.length() - 1 : 0;
		char value = currentCityName.charAt(idLast);
		
		if (currentCityName.length() >= 1) {
			
			// If it currently looks like GPS coordinates
			if (currentCityName.matches(".*\\d.*")) {
				
				long countComma = currentCityName.chars().filter(ch -> ch == ',').count();
				
				if ((value != ',' && !Character.isDigit(value)) || countComma > 3) {

					System.out.println(this.cityName.getEditor().getText());
					
					// Delete the new char
					this.cityName.getEditor().setText(
						currentCityName.substring(0, this.cityName.getEditor().getCaretPosition() - 1)
						+
						currentCityName.substring(this.cityName.getEditor().getCaretPosition())
					);
					this.cityName.getEditor().positionCaret(currentCityName.length() - 1);
					
					System.out.println(this.cityName.getEditor().getText());
					
				}
				
			} else {
				
				if (value != ' ' && value != '-' && !Character.isLetter(value)) {
					
					// Delete the new char
					this.cityName.getEditor().setText(
						currentCityName.substring(0, this.cityName.getEditor().getCaretPosition() - 1)
						+
						currentCityName.substring(this.cityName.getEditor().getCaretPosition())
					);
					this.cityName.getEditor().positionCaret(currentCityName.length() - 1);
					
				}
				
			}
			
		}
		
    }
	
	/**
	 * Check if the input key is a integer
	 * @param event {@code keyEvent}
	 * @author Quentin Capdepon
	 */
	@FXML
	public void checkInputFrom(KeyEvent event) {
		
		// Clear the path text area
		MainViewController.clearMapPathTextArea();
		
		// Check if the input key is a integer
		this.checkInputIsInteger(event);
		
		// Update the status of the reset / search button
		this.checkAllFields();
	}

	/**
	 * Check if the input key is a integer
	 * @param event {@code KeyEvent}
	 * @author Quentin Capdepon
	 */
	@FXML
	public void checkInputTo(KeyEvent event) {
		
		// Check if the input key is a integer
		this.checkInputIsInteger(event);
		
		// Update the status of the reset / search button
		this.checkAllFields();
	}
	
	/**
	 * Check all the input when a comboBox value change
	 * @param event {@code ActionEvent}
	 * @author Quentin Capdepon
	 */
	@FXML
	public void checkAllComboBox(ActionEvent event) {
		
		// Clear the path text area
		MainViewController.clearMapPathTextArea();
		
		// Checl all the fields
		this.checkAllFields();
	}
	
	/**
	 * Fetch the street for this city
	 * @param event {@code ActionEvent}
	 * @throws Exception Throw a exception when the city doesn't exists
	 * @throws CannotReachServerException Exception thrown when the server cannot be reached
	 * @author Yanis Labrak
	 */
	@FXML
	public void setCity(ActionEvent event) throws Exception, CannotReachServerException
	{
		
		// If the city name isn't empty
		if (!this.cityName.getSelectionModel().isEmpty()) {
			
			// Align center
			Map.LATITUDE = 0.0;
			Map.LONGITUDE = 0.0;

			// Initialize
			Map.SCALE = 1.0;
			
			MainViewController.addStateBar("Search for " + this.cityName.getSelectionModel().getSelectedItem());
			
			// Load streets etc ... In the way to all the user to make a path research
			try {

				Boolean theCityExist = this.fromName.getSelectionModel().isEmpty();
				
				// If the city exist
				if (theCityExist != false) {
				
					City city = MainViewController.LIST_CITIES_SORTED.get(this.cityName.getSelectionModel().getSelectedIndex());
										
					try {
						
						// Can continuous to run
						MainViewController.STATUS = true;
						
						// If this is not Angers
						if (!city.name.equals(Settings.DEFAULT_CITY.name)) {
							
							// Disable public transport related fields
							this.lines.setDisable(true);
							this.displayTC.setDisable(true);
							this.hideTC.setDisable(true);
						} else {
														
							// Enable public transport related fields
							this.lines.setDisable(false);
							this.displayTC.setDisable(false);
							this.hideTC.setDisable(false);
						}
						
						// Fetch, Load and Render the map for this city
//						MainViewController.renderCityMap(MainViewController.CITY);
		    			RenderCity firstRender = new RenderCity(city);
		    			new Thread(firstRender).start();
		    			this.loading.visibleProperty().bind(firstRender.runningProperty());
						
		    			// Load streets
						QueriesLoading.loadStreets();
						
					    
					} catch (Exception e) {
						System.out.println(e);
					}
					
					// Check if not empty
					if (!this.cityName.getSelectionModel().isEmpty()) {
						
						// Check fields
						this.checkCityFields();
					}
					else {
						this.Reset(event);
					}
					
				} else {
					City c = MainViewController.LIST_CITIES_SORTED.get(this.cityName.getSelectionModel().getSelectedIndex());
					MainViewController.addStateBar(c.name + " doesn't exist !");
				}
				
			}
			catch (NullPointerException e) {
				MainViewController.addStateBar("No city found !");
			}
		}
		else {
			System.out.println("Empty field");
		}
	}

	/**
	 * Check the fields when we set the city
	 */
	private void checkCityFields() {
		
		// Reset the fields
		this.fromNumber.clear();
		this.fromName.getSelectionModel().clearSelection();
		this.toNumber.clear();
		this.toName.getSelectionModel().clearSelection();
		
		// Reset the button states
		this.cityButton.setDisable(false);
		this.resetBtn.setDisable(false);
		
		// Departure arrival
		this.fromNumber.setDisable(false);
		this.fromName.setDisable(false);
		this.toNumber.setDisable(false);
		this.toName.setDisable(false);
		
		// Path settings
		this.calculMode.setDisable(false);
		this.transportMode.setDisable(false);
		this.correspondance.setDisable(true);
		this.sens.setDisable(false);
	}

	/**
	 * Fetch, Load and Render the map for this city
	 * @param city {@code String} City to render
	 * @author Yanis Labrak
	 */
	public static void renderCityMap(City city) {
		MainViewController.renderCityMap(city, false);
	}
	
	/**
	 * Fetch, Load and Render the map for this city
	 * @param city {@code String} City to render
	 * @param importing {@code Boolean} Does It's a importation ? True => Yes, False => No
	 * @author Yanis Labrak
	 */
	public static void renderCityMap(City city, boolean importing) {
		
		try {

			// Check if the city change
			boolean sameCity = false;
						
			if (MainViewController.CITY != null) {
				sameCity = MainViewController.CITY.toString().equals(city.toString());				
			} else {
				sameCity = true;
			}
	        	        
	        // If the city change
	        if (importing == true || sameCity != true || MainViewController.FIRST_RENDER == true) {
	        	
	        	MainViewController.FIRST_RENDER = false;
			
				// Sanitize the city name
				city.name = Encode.encodeUrl(city);
				
				// Fetch everything we need to display the map		
				MainViewController.fetchAllContentCity(city);
				
				// Parse the JSON file as Java Objects
				// TODO: 7,25300 seconds - To optimize
				Map.loadCityAsObject(URLEncoder.encode(city.name, "UTF-8").toLowerCase());
			}
	        
	        MainViewController.CITY = city;
	        
			Map.STATE = true;
									
			// Show the progress
		    MainViewController.addStateBar("The loading of the city is ended !");
		
		} catch (Exception | CannotReachServerException e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Render all the objects of the canvas
	 * @author Yanis Labrak
	 */
	public static void renderMap() {
		
		if (RunningThreads.SEARCHING != null && RunningThreads.SEARCHING.isRunning() == true) {
			return;
		}
		
		// Render the canvas
		MainViewController.render();
	}

	/**
	 * Render all the objects of the canvas
	 * @author Yanis Labrak
	 */
	public static void render() {
		MainViewController.render(RunningThreads.SEARCHING.paths);
	}
	
	/**
	 * Render all the objects of the canvas
	 * @param paths {@code List<Path>} The path's to display on the screen
	 * @author Yanis Labrak
	 */
	public static void render(List<Path> paths) {

		// If inside the auto-completion thread
		if (RunningThreads.AUTOCOMPLETE != null && RunningThreads.AUTOCOMPLETE.isRunning() == true) {
			return;					
		}
		
		// If we are allowed to draw
		if (Map.STATE == true) {
			
			// Clear the canvas before draw
			MainViewController.gc.clearRect(0, 0, MainViewController.canvasMapSize.getX(), MainViewController.canvasMapSize.getY());
			MainViewController.gcNodes.clearRect(0, 0, MainViewController.canvasMapSize.getX(), MainViewController.canvasMapSize.getY());
			
			Map.WIDTH = MainViewController.canvasMapSize.getX();
			Map.HEIGHT = MainViewController.canvasMapSize.getY();
			
			// Redefine the bbox of the canvas
			Bbox.bboxScreen = Bbox.calculateBboxScreen();
			
			// Draw all ways
			Draw.drawWays();
			
			// Draw public transport routes
			Draw.drawPublicTransportsRoutesMain();
			
			// Draw the path
			for (Path path : paths) {
				Draw.drawPath(path);
			}
			
			// Draw the names of the streets		
			Draw.drawStreetsName();
			
			// Draw bus stations
			Draw.drawBusStationsMain();
			
			// Draw public transport
			Draw.drawPublicTransportMain();
		}
	}
	
	/**
	 * Display on a secondary {@code Canvas} all the {@code Node}'s around the mouse cursor.
	 * @param event {@code MouseEvent} The mouse event
	 * @author Yanis Labrak
	 */
	@FXML
	void showNodesArround(MouseEvent event) {
		
		// If the map isn't totally loaded
		if (Map.STATE == false) { return; }
		
		// Define the area were the nodes will be shown.
		Bbox bbox = new Bbox(
			event.getX() - 10,
			event.getX() + 10,
			event.getY() - 10,
			event.getY() + 10
		);
		
		// Clear canvas
		this.canvasNodes.getGraphicsContext2D().clearRect(0, 0, MainViewController.canvasMapSize.getX(), MainViewController.canvasMapSize.getY());
		
		// Draw Nodes
		Draw.drawNodes(MainViewController.gcNodes, bbox);
	}
	
	/**
	 * Get all the info's about the closest {@code Node} of the mouse when right click
	 * @param event {@code MouseEvent}
	 * @author Yanis Labrak Initial implementation
	 * @author Zheng Zhiao Improvement
	 */
	@FXML
	void getNodeInformations(MouseEvent event) {
		
		// If the city is currently empty
		if (this.cityName.getSelectionModel().isEmpty()) {
			
			// Add the city to the list of the comboBox
			MainViewController.LIST_CITIES_SORTED.add(Settings.DEFAULT_CITY);
			
			// Select the city
			this.cityName.getSelectionModel().select(Settings.DEFAULT_CITY);
			
			// Check the fields
			this.checkCityFields();
		}
		
		// Get THE closest Node
		Node closest = this.getClosestRoadNodes(event);
		
		// If we don't find node into the 10px perimeter
		if (closest == null) {
			return;
		}
		
		// The closest bus station
		BusStation station = closest.getClosestBusStation();
		
		Road road = closest.getFirstRoad();
		
		// If we don't find road into the 10px perimeter
		if (road == null) {
			return;
		}
				
		final ContextMenu contextMenu = new ContextMenu();	
		
		// Identifier
		MenuItem cut = new MenuItem(closest.id.toString(), Textures.getImageView(Textures.ID));
		cut.setDisable(true);
		cut.getStyleClass().add("context-menu-label");
		
		// Latitude
		MenuItem copy = new MenuItem(closest.lat.toString(), Textures.getImageView(Textures.LAT));
		copy.setDisable(true);
		copy.getStyleClass().add("context-menu-label");
		
		// Longitude
		MenuItem paste = new MenuItem(closest.lon.toString(), Textures.getImageView(Textures.LON));
		paste.setDisable(true);
		paste.getStyleClass().add("context-menu-label");
		
		// Separator
		SeparatorMenuItem sp1 = new SeparatorMenuItem();
		
		// Departure & arrival
		MenuItem from = new MenuItem("Set as departure", Textures.getImageView(Textures.DEP));
		MenuItem to = new MenuItem("Set as arrival", Textures.getImageView(Textures.ARR));

		// Add to the context menu all the previous items
		contextMenu.getItems().addAll(cut, copy, paste, sp1, from, to);
		
		// If we have public transport lines
		if (station.busLines != null && station.busLines.size() > 0) {

			// Separator
			SeparatorMenuItem sp2 = new SeparatorMenuItem();
			contextMenu.getItems().add(sp2);
			
			// The station name
			MenuItem stationName = new MenuItem(station.nomarret, Textures.getImageView(Textures.SIGN));
			stationName.setDisable(true);
			stationName.getStyleClass().add("context-menu-label");
			contextMenu.getItems().add(stationName);
						
			// Add all the bus lines to the context menu
			MenuItem tc;
			for (PublicTransportRoute line : station.busLines) {
				
				// Check empty
				if (line == null) { continue; }
				
				// Get the next schedule
				Date closeDate = station.getNextSchedule(line.getNumber());
				String date;
				
				// Check if empty
				if (closeDate != null) {
					
					// Hours
					String hours = String.valueOf(closeDate.getHours());
					if (closeDate.getHours() < 10) { hours = "0" + hours; }					
					
					// Minutes
					String minutes = String.valueOf(closeDate.getMinutes());
					if (closeDate.getMinutes() < 10) { minutes = "0" + minutes; }
					
					// Check empty
					if (hours == null || minutes == null) {
						
						// If no date is available, it's closed
						date = "Down";	
						
					} else {
						
						// Date
						date = hours + ":" + minutes;						
					}
										
				} else {
					// If no date is available, it's closed
					date = "Down";
				}
				
				// TC line
				tc = new MenuItem(line.getNumber() + ": " + date, Textures.getImageView(Textures.STATION));
								
				/**
				 * Make It clickable
				 * @author Quentin Capedepon
				 */
				tc.setOnAction(new EventHandler<ActionEvent>() {			
					@Override
					public void handle(ActionEvent event) {
						
						// The line number
						String lineNumber = line.getNumber().toUpperCase();
						
						// For each check box
						for (CheckBoxPublicTransport cb : SELECTED_PUBLIC_TRANSPORTS) {
							
							// If this is the line
							if (cb.line.toUpperCase().equals(lineNumber)) {
								cb.setSelected(true); // Check it
								break;
							}
						}						
						
				    	// Allow to display the public transport on the canvas
				    	MainViewController.STATUS_PUBLIC_TRANSPORT_ROUTE.set(true);

				    	boolean alreadyContains = MainViewController.SELECTED_ROUTES.contains(lineNumber);
				    	
				    	// If the line is already selected
				    	if (alreadyContains == true) {
				    		
				    		// Remove it
					    	MainViewController.SELECTED_ROUTES.remove(lineNumber);
						} else {

							// Add It
					    	MainViewController.SELECTED_ROUTES.add(lineNumber);
						}
						
						// Refresh
				    	MainViewController.renderMap();
					}
				});
				contextMenu.getItems().add(tc);
				
			}
			
		}
		
		// List of the closest roads
		List<Road> roads = closest.getRoadsWithNames();
		
		// If we find roads
		if (roads != null && roads.size() > 0) {
			
			// Separator
			contextMenu.getItems().add(new SeparatorMenuItem());	
					
			// Label for roads name
			MenuItem labelRoadNames = new MenuItem("Rues :");
			labelRoadNames.setDisable(true);
			labelRoadNames.getStyleClass().add("context-menu-label");
			contextMenu.getItems().add(labelRoadNames);
			
			/**
			 * Add the street names to the pop-up
			 * @author Yanis Labrak Initial implementation
			 * @author Zheng Zhiao Reduce the complexity
			 */
			MenuItem roadItem;
			for (Road r : roads) {		
				roadItem = new MenuItem(r.name, Textures.getImageView(Textures.ADDR));
				roadItem.setDisable(true);
				roadItem.getStyleClass().add("context-menu-label");
				contextMenu.getItems().add(roadItem);
			}
		}
		
		// Departure
		from.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				
				// Set the arrival node
				MainViewController.CURRENT_DEPARTURE_NODE = closest;
				
				// Add the road to the comboBox
				MainViewController.LIST_STREETS_NAMES_SORTED_FROM.add(road);
				MainViewController.this.fromName.setValue(road);
				checkAllComboBox(event);
				
			}
		});
		
		// Arrival
		to.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				
				// Set the arrival node
				MainViewController.CURRENT_ARRIVAL_NODE = closest;
				
				// Add the road to the comboBox
				MainViewController.LIST_STREETS_NAMES_SORTED_TO.add(road);
				MainViewController.this.toName.setValue(road);
				checkAllComboBox(event);
				
			}
		});
		
		// Add the pop-up to the stack of pop-up
		this.stackContextMenu.push(contextMenu);
		
		// Display the pop-up
        contextMenu.show(this.canvasPane, event.getScreenX(), event.getScreenY());
	}

	/**
	 * Get the closest {@code Node} of the mouse when right click
	 * @param event {@code MouseEvent}
	 * @return {@code Node} The {@code Node}
	 * @author Yanis Labrak
	 */
	@FXML
	Node getClosestNodes(MouseEvent event) {
		
		Bbox bbox = new Bbox(
			event.getX() - 10,
			event.getX() + 10,
			event.getY() - 10,
			event.getY() + 10
		);

		Node closest = null;
		
		// Get all nodes around
		for (Long key : Map.NODES.keySet()) {
		    
		    Node node = Map.NODES.get(key);
		    
    		// Coordinate after processing
    		List<Double> coordinates = Node.toPixel(node.lat, node.lon);
    		
    		Double x = coordinates.get(0);
    		Double y = Map.HEIGHT - coordinates.get(1);
	    		
		    if (x < bbox.topRight && x > bbox.topLeft && y > bbox.bottomLeft && y < bbox.bottomRight) {
		    	
		    	if (closest == null) {
		    		closest = node;
				}
		    	else {
		    		
		    		Double distanceMouseNode = MainViewController.Distance(event.getX(), event.getY(), node.lon, node.lat);
		    		Double distanceMouseClosest = MainViewController.Distance(event.getX(), event.getY(), closest.lon, closest.lat);

					// Mouse.distance(node) < Mouse.distance(closest)
					// closest = node
		    		if (distanceMouseNode < distanceMouseClosest) {
		    			closest = node;						
					}
				}	
			}
		}
		
		return closest;
	}
	

	/**
	 * Get the closest {@code Road} {@code Node} of the mouse when right click
	 * @param event {@code MouseEvent}
	 * @return {@code Node} The {@code Node}
	 * @author Yanis Labrak
	 */
	@FXML
	Node getClosestRoadNodes(MouseEvent event) {
		
		Bbox bbox = new Bbox(
			event.getX() - 10,
			event.getX() + 10,
			event.getY() - 10,
			event.getY() + 10
		);

		Node closest = null;
		
		// Get all nodes around
		for (Node node : Map.getRoadsNodes()) {
		    
    		// Coordinate after processing
    		List<Double> coordinates = Node.toPixel(node.lat, node.lon);
    		
    		Double x = coordinates.get(0);
    		Double y = Map.HEIGHT - coordinates.get(1);
	    		
		    if (x < bbox.topRight && x > bbox.topLeft && y > bbox.bottomLeft && y < bbox.bottomRight) {
		    	
		    	if (closest == null) {
		    		closest = node;
				}
		    	else {
		    		
		    		Double distanceMouseNode = MainViewController.Distance(event.getX(), event.getY(), node.lon, node.lat);
		    		Double distanceMouseClosest = MainViewController.Distance(event.getX(), event.getY(), closest.lon, closest.lat);

					// Mouse.distance(node) < Mouse.distance(closest)
					// closest = node
		    		if (distanceMouseNode < distanceMouseClosest) {
		    			closest = node;						
					}
				}	
			}
		}
		
		return closest;
	}

    /**
     * The distance between the two points
     * @param x1 {@code Double} X coordinate of the first point
     * @param y1 {@code Double} Y coordinate of the first point
     * @param x2 {@code Double} X coordinate of the second point
     * @param y2 {@code Double} Y coordinate of the second point
     * @return {@code Double} The distance
     */
    static public double Distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.sqrt(y2 - y1) + Math.sqrt(x2 - x1));
    }
	
	/**
	 * When a key is pressed on {@code cityName}
	 * @param event {@code KeyEvent} The key pressed
	 * @throws CannotReachServerException Exception thrown when the server cannot be reached
	 * @throws Exception Throw a exception when the city doesn't exists
	 * @author Zheng Zhiao
	 */
	@FXML
	public void KeyPressCity(KeyEvent event) throws Exception, CannotReachServerException {
		
		this.checkInputCity();
					
		// If it's a moving key continue
		if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.ENTER) {
			event.consume();
			return;
		}
		
		// If the field isn't empty
		if (!this.cityName.getEditor().getText().isEmpty()) {
						
			// Clear the current list of city
			if (MainViewController.LIST_CITIES_SORTED != null && MainViewController.LIST_CITIES_SORTED.size() > 0) {
				MainViewController.LIST_CITIES_SORTED.clear();
			}
			
			if (this.cityName.getEditor().getText().length() >= 3) {
				
				// If it's currently running stop it
				if (RunningThreads.AUTOCOMPLETE != null && RunningThreads.AUTOCOMPLETE.isRunning() == true) {
					RunningThreads.AUTOCOMPLETE.cancel();
				}
				
				AutocompleteCity.isCoordinate = this.cityName.getEditor().getText().matches(".*\\d.*");
				
				// If the field contain digits make a reverse city research
				if (AutocompleteCity.isCoordinate) {
					
					// http://photon.komoot.de/reverse?lon=4.8059012&lat=43.9492493
					
					String[] coordinates = this.cityName.getEditor().getText().split(", ");

					// Adapt coordinates to the OSM format
					String lon = coordinates[1].replaceAll(",",".");
					String lat = coordinates[0].replaceAll("\\s+","").replaceAll(",",".");
	
					AutocompleteCity.nominatimUrl = "http://photon.komoot.de/reverse?lon=" + lon + "&lat=" + lat;
					
				} else {						
					AutocompleteCity.nominatimUrl = "http://photon.komoot.de/api/?q=" + URLEncoder.encode("'" + this.cityName.getEditor().getText() + "'", "UTF-8");
				}
				
				// Load the cities
				QueriesLoading.loadCities(AutocompleteCity.nominatimUrl, AutocompleteCity.isCoordinate);
//					ThreadsRunning.AUTOCOMPLETE = new AutocompleteCity();
//					new Thread(ThreadsRunning.AUTOCOMPLETE).start();
		
				// Display the completion pop-up
				if (this.cityName.isShowing() != true) {
					this.cityName.show();
				}
			}
			
			// Hide auto-completions
			this.fromName.hide();
			this.toName.hide();
			this.calculMode.hide();
			this.transportMode.hide();
			this.lines.hide();
			
			this.cityButton.setDisable(false);
			this.resetBtn.setDisable(false);
		}
		else {
			
			this.cityButton.setDisable(true);
			
			this.disableAllUnderCity();
		}
	}
	
	/**
	 * Disable all the fields under the city field
	 */
	void disableAllUnderCity() {
		
		this.fromNumber.clear();
		this.fromName.getSelectionModel().clearSelection();
		this.toNumber.clear();
		this.toName.getSelectionModel().clearSelection();
		this.calculMode.getSelectionModel().clearSelection();
		this.transportMode.getSelectionModel().clearSelection();
		this.correspondance.setSelected(false);
		this.sens.setSelected(false);
		
		this.searchBtn.setDisable(true);
		this.resetBtn.setDisable(true);
	}
	
	/**
	 * Zoom in the map
	 * @param event {@code ActionEvent}
	 * @author Mohammed Benyamna
	 */
	@FXML
	public void zoomIn(ActionEvent event) {
		
		if (Map.SCALE * Settings.ZOOMING_SCALE <= Settings.MAX_SCALE) {		
			
			Map.SCALE *= Settings.ZOOMING_SCALE;
			
			this.slider.setValue(Map.SCALE);
			
			System.out.println(Map.SCALE);
			
			MainViewController.renderMap();			
		}
	}
	
	/**
	 * Zoom out the map
	 * @param event {@code MouseEvent}
	 * @author Mohammed Benyamna
	 */
	@FXML
	private void zoomInSlider(MouseEvent event) {		
		Map.SCALE = Math.pow(2, this.slider.getValue());
		MainViewController.renderMap();
		
	}
	
	/**
	 * Zoom out the map
	 * @param event {@code ActionEvent}
	 * @author Mohammed Benyamna
	 */
	@FXML
	public void zoomOut(ActionEvent event) {
		
		if (Map.SCALE / Settings.ZOOMING_SCALE > Settings.MIN_SCALE) {
			
			Map.SCALE /= Settings.ZOOMING_SCALE;
			
			this.slider.setValue(Map.SCALE);
			
			System.out.println(Map.SCALE);
			
			MainViewController.renderMap();
		}
	}
	
	/**
	 * Zoom in the map with mouse
	 * @param event {@code ActionEvent}
	 * @author Mohammed Benyamna
	 */
	@FXML
	public void canvasScrolling(ScrollEvent event) {
		
        double deltaY = event.getDeltaY();
        
        if (deltaY > 0 ) {
        	this.zoomIn(new ActionEvent());
		}
        else if (deltaY < 0) {
        	this.zoomOut(new ActionEvent());
		}
	}
	
	/**
	 * Move to the left in the map
	 * @param event {@code ActionEvent}
	 * @author Mohammed Benyamna
	 */
	@FXML
	public void left(ActionEvent event) {
		Map.LONGITUDE += Settings.HORI_MOVE_DIST;
		MainViewController.renderMap();
	}
	
	/**
	 * Move to the right in the map
	 * @param event {@code ActionEvent}
	 * @author Mohammed Benyamna
	 */
	@FXML
	public void right(ActionEvent event) {
		Map.LONGITUDE -= Settings.HORI_MOVE_DIST;
		MainViewController.renderMap();
	}
	
	/**
	 * Center the map
	 * @param event {@code ActionEvent}
	 * @author Mohammed Benyamna
	 */
	@FXML
	public void center(ActionEvent event) {
		Map.LATITUDE = 0.0;
		Map.LONGITUDE = 0.0;
		MainViewController.renderMap();
	}
	
	/**
	 * Move to the top in the map
	 * @param event {@code ActionEvent}
	 * @author Mohammed Benyamna
	 */
	@FXML
	public void up(ActionEvent event) {
		Map.LATITUDE += Settings.VERT_MOVE_DIST;
		MainViewController.renderMap();
	}
	
	/**
	 * Move to the bottom in the map
	 * @param event {@code ActionEvent}
	 * @author Mohammed Benyamna
	 */
	@FXML
	public void down(ActionEvent event) {
		Map.LATITUDE -= Settings.VERT_MOVE_DIST;
		MainViewController.renderMap();
	}
	
	/**
	 * Hide the left panel
	 * @param event {@code ActionEvent}
	 * @author Quentin Capdepon
	 */
	@FXML
	public void hideLeft(ActionEvent event)
	{
		this.splitPane.setDividerPositions(0.0);
		this.verticalSplitPane.setDividerPositions(1);
	}
	
	/**
	 * Show the left panel
	 * @param event {@code ActionEvent}
	 * @author Quentin Capdepon
	 */
	@FXML
	public void showLeft(ActionEvent event)
	{
		this.splitPane.setDividerPositions(0.29);
		this.verticalSplitPane.setDividerPositions(0.85);
	}

    /**
     * Handle the drag event on the canvas
     * @param event {@code MouseEvent}
     * @author Labrak Yanis
     */
    @FXML
    void mouseClick(MouseEvent event) {
    	
    	// Drag
    	if (event.getButton() == MouseButton.PRIMARY)
    	{
    		// Hide the previous ContextMenu
    		if (!this.stackContextMenu.isEmpty()) {
        		this.stackContextMenu
        		.pop()
        		.hide();
			}
    		
        	Map.X_DELTA = event.getSceneX();
        	Map.Y_DELTA = event.getSceneY();
		}
    	// Get information on the closest node
    	else if (event.getButton() == MouseButton.SECONDARY)
        {
			// Hide the previous ContextMenu
    		if (!this.stackContextMenu.isEmpty()) {
        		this.stackContextMenu
        		.pop()
        		.hide();
			}
    		
            this.getNodeInformations(event);
        }
    	
    }

    /**
     * Handle the drop event on the canvas
     * @param event {@code MouseEvent}
     */
    @FXML
    void drop(MouseEvent event) {
		
    	// If the left mouse click is dropped
    	if (event.getButton() == MouseButton.PRIMARY) {
    		
    		// Delta between the drag and the drop
    		Map.X_DELTA = event.getSceneX() - Map.X_DELTA;
        	Map.Y_DELTA = event.getSceneY() - Map.Y_DELTA;
        	
        	Map.LONGITUDE += Map.X_DELTA * 3;
        	Map.LATITUDE +=  Map.Y_DELTA / 1000000000d;	        	
        	
    		MainViewController.renderMap();
		}
		
    }
	
	/**
	 * Auto-complete for the {@code FROM} comboBox
	 * @param event {@code KeyEvent}
	 * @author Zihao Zheng
	 */
	@FXML
	public void autoCompleteFrom(KeyEvent event) {
		
		// Current value of the comboBox {@code FROM} street
		String current_value = this.fromName.getEditor().getText().toLowerCase();
		
		// If the user write nothing
		if (!current_value.isEmpty()) {

			// If it's a moving key continue
			if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.ENTER) {
				event.consume();
			}
			// If the last pressed key is TAB
			else if (event.getCode() == KeyCode.CONTROL) {
				
				if (MainViewController.LIST_STREETS_NAMES_SORTED_FROM.size() > 0) {
					
					// Set the current value to the first element of the list which contain the current word
					this.fromName.setValue(MainViewController.LIST_STREETS_NAMES_SORTED_FROM.get(0));
					
					// Set the middle of the road as the current departure node
					MainViewController.CURRENT_DEPARTURE_NODE = MainViewController.LIST_STREETS_NAMES_SORTED_FROM.get(0).getMiddle();
				}
				
				// Don't read this character
				event.consume();
				
			} else {		
				
				MainViewController.LIST_STREETS_NAMES_SORTED_FROM.clear();
				
				// Take the {@code street} which contain inside it name the current input
				for (Road street : MainViewController.LIST_STREETS_NAMES) {
					
					if (street.name.toLowerCase().contains(current_value)) {
						MainViewController.LIST_STREETS_NAMES_SORTED_FROM.add(street);
					}
				}
				
				// If no result
				if (MainViewController.LIST_STREETS_NAMES_SORTED_FROM.size() <= 0) {
					MainViewController.addStateBar("Unknown start street");
				} else {
					this.toName.hide();
					this.fromName.show();
				}
				
			}			
		}
		else {
			MainViewController.LIST_STREETS_NAMES_SORTED_FROM.setAll(MainViewController.LIST_STREETS_NAMES);
		}
	}
	
	/**
	 * Auto-complete for the TO comboBox
	 * @param event {@code KeyEvent}
	 * @author Zihao Zheng
	 */
	@FXML
	public void autoCompleteTo(KeyEvent event) {
		
		// Current value of the comboBox from/to street
		String current_value = this.toName.getEditor().getText();	
		
		// If the user write nothing
		if (!current_value.isEmpty()) {
			
			// If it's a moving key continue
			if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.ENTER) {
				event.consume();
			}
			// If the last pressed key is TAB
			else if (event.getCode() == KeyCode.CONTROL) {
				
				if (MainViewController.LIST_STREETS_NAMES_SORTED_TO.size() > 0) {
					
					// Set the current value to the first element of the list which contain the current word
					this.toName.setValue(MainViewController.LIST_STREETS_NAMES_SORTED_TO.get(0));
					
					// Set the middle of the road as the current arrival node
					MainViewController.CURRENT_ARRIVAL_NODE = MainViewController.LIST_STREETS_NAMES_SORTED_TO.get(0).getMiddle();
				}
				
				// Destroy the entered key stroke
				event.consume();
				
			} else {

				MainViewController.LIST_STREETS_NAMES_SORTED_TO.clear();
				
				// Take the street which contain inside it name the current input
				for (Road street : MainViewController.LIST_STREETS_NAMES) {
					if (street.name.toLowerCase().contains(current_value)) {
						MainViewController.LIST_STREETS_NAMES_SORTED_TO.add(street);
					}
				}
				
				// If no result
				if (MainViewController.LIST_STREETS_NAMES_SORTED_TO.size() <= 0) {
					MainViewController.addStateBar("Unknown destination street");
				} else {
					this.fromName.hide();
					this.toName.show();
				}
				
			}
			
		}
		else {
			MainViewController.LIST_STREETS_NAMES_SORTED_TO.setAll(MainViewController.LIST_STREETS_NAMES);
		}
	}

    /**
     * Export as XML
     * @param event {@code ActionEvent}
     * @author Mohamed Ben Yamna
     */
    @FXML
    void exportXML(ActionEvent event) {
		
    	// Default output directory
		File directory = new File("Routes");
		directory.mkdirs();
	
        final FileChooser fileChooser = new FileChooser();
        
        // Title of the window
        fileChooser.setTitle("Export");
        
        // Default directory
        fileChooser.setInitialDirectory(directory);
        
        // Allowed extensions
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("XML", "*.xml")
        );

        // Current stage
        Stage stage = (Stage) Launcher.my_scene.getWindow();

        // The pop-up & output file name
        Export.OUTPUT_PATH = fileChooser.showSaveDialog(stage);
        
        // If the file chooser has been closed
        if (Export.OUTPUT_PATH == null) {
			return;
		}
        
		// The thread which will process the export
    	Export export = new Export();
    	new Thread(export).start();
    }

    /**
     * Import as XML
     * @param event {@code ActionEvent}
     * @author Mohamed Ben Yamna
     */
    @FXML
    void importXML(ActionEvent event) {
    	
    	FileInputStream fis;
		try {
			
			// The default root
			File directory = new File("Routes");
			directory.mkdirs();

	        final FileChooser fileChooser = new FileChooser();
	        
	        // Title
	        fileChooser.setTitle("Import");
	        
	        // Default root
	        fileChooser.setInitialDirectory(directory);
	        
	        // Allowed file extensions
	        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
            );

	        // Current stage
	        Stage stage = (Stage) Launcher.my_scene.getWindow();

	        // The pop-up & input file name
	        File file = fileChooser.showOpenDialog(stage);
	        
	        // If the file chooser has been closed
	        if (file == null) {
	        	// Quit
				return;
			}
			
	        // Open a stream with the file
			fis = new FileInputStream(file);
			
			// Decode the XML file
	        XMLDecoder decoder = new XMLDecoder(fis);

	        /**
	         * Save
	         */
	        Save save = (Save) decoder.readObject();
	        
	        /**
	         * City
	         */
	        City city = save.getCity();
	        
	        // Same city or not
	        boolean sameCity = MainViewController.CITY.toString().equals(city.toString());
	        
	        MainViewController.CITY = city;
	        
	        // Add the city to the comboBox list
	        MainViewController.LIST_CITIES_SORTED.add(city);
	        
	        // Select the city
	        this.cityName.getSelectionModel().select(0);
	        
	        // Set the comboBox text
	        this.cityName.getEditor().setText(city.toString());	        
			
			// Enable the button
			this.cityButton.setDisable(false);
			
			// Check all fields
			this.checkInputCity();
			
			// If the city doesn't change, don't re-render
			if (sameCity != true) {
				
    			RenderCity importMap = new RenderCity(city,true);
    			new Thread(importMap).start();
    			this.loading.visibleProperty().bind(importMap.runningProperty());
			}
			
			// Fill up streets
			QueriesLoading.loadStreets();
	        
			/**
			 * Arrival
			 */
	        Road arrival = save.getCurrentArrivalRoad();
			MainViewController.CURRENT_ARRIVAL_ROAD = arrival;
			
	        /**
	         * Departure
	         */
	        Road departure = save.getCurrentDepartureRoad();
			MainViewController.CURRENT_DEPARTURE_ROAD = departure;
	        
	        /**
	         * calculMode
	         */
	        CalculMode calculMode = save.getCurrentCalculMode();
			MainViewController.currentCalculMode = calculMode;
	        
	        /**
	         * Get TransportMode
	         */
	        TransportMode tcMode = save.getCurrentTransportMode();
	        MainViewController.CURRENT_TRANSPORT_MODE = tcMode.name;
	        
	        
	        /**
	         * Correspondence
	         */
	        boolean correspondence = save.isCorrespondenceEnabled();
	        MainViewController.CORRESPONDENCE_ENABLED = correspondence;
	        this.correspondance.setSelected(correspondence);
	        System.out.println(correspondence);

	        /**
	         * Get the path
	         */
	        List<Path> paths = save.getPaths();
	        RunningThreads.SEARCHING.paths = paths;
	        System.out.println(paths);
	        
	        /**
	         * One way
	         */
	        boolean oneWay = save.isSensEnabled();
	        MainViewController.SENS_ENABLED = oneWay;	
	        this.sens.setSelected(oneWay);
			System.out.println(oneWay);
	        
	        decoder.close();	        
	        fis.close();
	        
	        // City
	        MainViewController.LIST_CITIES_SORTED.clear();
	        MainViewController.LIST_CITIES_SORTED.add(city);
			this.cityName.setItems(MainViewController.LIST_CITIES_SORTED);	
			System.out.println(city);
			
			// Departure
			this.fromName.setValue(departure);			
			System.out.println(departure);
			
			// Arrival
			this.toName.setValue(arrival);			
			System.out.println(arrival);    
			
			// Calculation mode
			this.calculMode.setValue(calculMode);	 		
			System.out.println(calculMode);
	        
			// Transport mode
			this.transportMode.setValue(tcMode);		
			System.out.println(tcMode);
			
			// Unlock fields
			this.unlockAllPath();
			
			MainViewController.renderMap();
			
			// Set city name
	        this.cityName.getEditor().setText(city.toString());	
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
	 * Unlock all the fields linked to the path searching
	 */
	private void unlockAllPath() {
		
		this.fromNumber.setDisable(false);
		this.fromName.setDisable(false);
		
		this.toNumber.setDisable(false);
		this.toName.setDisable(false);
		
		this.calculMode.setDisable(false);
		this.transportMode.setDisable(false);
		
		this.correspondance.setDisable(false);
		this.sens.setDisable(false);			

		this.searchBtn.setDisable(false);			
	}

	/**
     * Change the state of the correspondence
     * @param event {@code ActionEvent}
     */
    @FXML
    void correspondance(ActionEvent event) {
    	
    	if (this.correspondance.isSelected()) {
			MainViewController.CORRESPONDENCE_ENABLED = true;
		} else {
			MainViewController.CORRESPONDENCE_ENABLED = false;
		}
    }
    
    /**
     * Change the state of the one way
     * @param event {@code ActionEvent}
     */
    @FXML
    void sens(ActionEvent event) {
    	
    	if (this.sens.isSelected()) {
			MainViewController.SENS_ENABLED = true;
		} else {
			MainViewController.SENS_ENABLED = false;
		}

    }
    
    /**
     * When the transport mode change
     * @param event {@code ActionEvent}
	 * @author Zhiao Zheng
     */
    @FXML
    void transportMode(ActionEvent event) {
    	
    	// If not empty
    	if (this.transportMode.getSelectionModel().getSelectedIndex() >= 0 ) {
    		
    		// Get the transport mode
        	MainViewController.CURRENT_TRANSPORT_MODE = this.transportMode.getEditor().getText();
        	
        	// If this is public transport, disable the correspondence
        	if (!MainViewController.CURRENT_TRANSPORT_MODE.equals(TransportModeNames.PUBLIC_TRANSPORT)) {
        		
        		// Disable the check-box
				this.correspondance.setDisable(true);
				
				// Unselect it
				this.correspondance.setSelected(false);
				
				// Change the status
				MainViewController.CORRESPONDENCE_ENABLED = false;
				
			} else {
				this.correspondance.setDisable(false);
			}
        	
        	// If is empty, set foot by default
        	if (MainViewController.CURRENT_TRANSPORT_MODE.isEmpty() == true) {
        		MainViewController.CURRENT_TRANSPORT_MODE = TransportModeNames.FOOT;
			}
        	
        	// Refresh the canvas
        	MainViewController.renderMap();
		}
    }

	/**
     * When the calculation mode change
     * @param event {@code ActionEvent}
     * @author Quentin Capedepon
     */
    @FXML
    void calculationMode(ActionEvent event) {
    	
    	if (this.calculMode.getSelectionModel().getSelectedIndex() >= 0 ) {
    		
    		MainViewController.CURRENT_CALCUL_MODE = this.calculMode.getEditor().getText();
    		System.out.println("calculModeValue: " + MainViewController.CURRENT_CALCUL_MODE);
    	}
    }

	/**
	 * Return the transport mode currently selected
	 * @return {@code String}
	 * @author Quentin Capedepon
	 */
	public static String getTransportMode() {
		
		// Get the current mode
		String mode = MainViewController.CURRENT_TRANSPORT_MODE;
		
		// If not empty
		if (mode != null) {
			return mode;
		}
		
		return TransportModeNames.FOOT;
	}

    /**
     * Display the public transport routes on the {@code Canvas}
     * @param event {@code ActionEvent}
     */
    @FXML
    void display(ActionEvent event) {
    	
    	// If already in enabled cancel the action
    	if (MainViewController.STATUS_PUBLIC_TRANSPORT_ROUTE.get() == true) { return; }
    	
    	// Allow to display the public transport on the canvas
    	MainViewController.STATUS_PUBLIC_TRANSPORT_ROUTE.set(true);
		
		// Refresh
    	MainViewController.renderMap();
    }

    /**
     * Hide the public transport routes on the {@code Canvas}
     * @param event {@code ActionEvent}
     */
    @FXML
    void hide(ActionEvent event) {
    	
    	// If not empty
    	if (MainViewController.SELECTED_ROUTES != null && MainViewController.SELECTED_ROUTES.size() > 0) {
    		
    		// Clear
        	MainViewController.SELECTED_ROUTES.clear();
        	
        	// Unselect all checkBox's
        	for (CheckBoxPublicTransport cb : MainViewController.SELECTED_PUBLIC_TRANSPORTS) { cb.setSelected(false); }
        	
        	// Clear the current selected value
        	this.lines.setValue(null);
        	
        	// Set the prompt text
        	this.lines.setPromptText("Ligne(s) Transport");
		}
    	
    	// Check if the printing of the lines is enabled
    	if (MainViewController.STATUS_PUBLIC_TRANSPORT_ROUTE.get() == true) {
        	
    		// Disable the printing of the lines
        	MainViewController.STATUS_PUBLIC_TRANSPORT_ROUTE.set(false);
        	
        	// Refresh
        	MainViewController.renderMap();
			
		}
    }
}
