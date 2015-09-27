package simulaSAAB.contextos.environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.referencing.GeodeticCalculator;

import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.graph.ShortestPath;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.contextos.ViaTransitable;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;


/**
 * Create routes around a GIS road network. The <code>setRoute</code> function actually finds the route and can be
 * overridden by subclasses to create different types of Route. See the method documentation for details of how routes
 * are calculated.
 * 
 * <p>
 * A "unit of travel" is the distance that an agent can cover in one iteration (one square on a grid environment or the
 * distance covered at walking speed in an iteration on a GIS environment). This will change depending on the type of
 * transport the agent is using. E.g. if they are in a car they will be able to travel faster, similarly if they are
 * travelling along a transort route they will cover more ground.
 * </p>
 * 
 * @author Nick Malleson
 */
public class Route {
	
	private static Logger LOGGER = Logger.getLogger(Route.class.getName());
	
	private Coordinate destination;
	private Coordinate origin;
	private Double totalLenght;
	/*
	 * The route consists of a list of coordinates which describe how to get to the destination. Each coordinate might
	 * have an attached 'speed' which acts as a multiplier and is used to indicate whether or not the agent is
	 * travelling along a transport route (i.e. if a coordinate has an attached speed of '2' the agent will be able to
	 * get to the next coordinate twice as fast as they would do if they were walking). The current position incicate
	 * where in the lists of coords the agent is up to. Other attribute information about the route can be included as
	 * separate arrays with indices that match those of the 'route' array below.
	 */
	private int currentPosition;
	private List<Coordinate> routeX;
	/*
	 * This maps route coordinates to their containing Road, used so that when travelling we know which road/community
	 * the agent is on. private
	 */
	//private List<ViaTransitable> roadsX;
	
	// Record which function has added each coord, useful for debugging
	//private List<String> routeDescriptionX;
	/**
	 * Indica si la ruta debe ser recorrida hacia adelante (Origen -> Destino) o hacia atras (Destino -> Origen)
	 */
	private boolean Forward;
	
	private int velocidad;

	public Route(Coordinate origen, Coordinate destino) {
		
		this.destination 	= destino;
		this.origin			= origen;
		this.currentPosition= 0;
		this.totalLenght	= new Double(0);
		this.routeX			= new ArrayList<Coordinate>();
		Forward				= true;
		velocidad			= 10;
		
	}
	
	/**
	 * Find a route from the origin to the destination. A route is a list of Coordinates which describe the route to a
	 * destination restricted to a road network. The algorithm consists of three major parts:
	 * <ol>
	 * <li>Find out if the agent is on a road already, if not then move to the nearest road segment</li>
	 * <li>Get from the current location (probably mid-point on a road) to the nearest junction</li>
	 * <li>Travel to the junction which is closest to our destination (using Dijkstra's shortest path)</li>
	 * <li>Get from the final junction to the road which is nearest to the destination
	 * <li>
	 * <li>Move from the road to the destination</li>
	 * </ol>
	 * 
	 * @throws Exception
	 */
	public Double setRoute() throws Exception {
		
		ShortestPath<Junction> path 	= new ShortestPath<Junction>(SaabContextBuilder.RoadNetwork);
		List<RepastEdge<Junction>> ruta	= new ArrayList<RepastEdge<Junction>>();
		
		if(SaabContextBuilder.coordMap.containsKey(origin) && SaabContextBuilder.coordMap.containsKey(destination)){
			
			Junction Inicio	= SaabContextBuilder.coordMap.get(origin);
			Junction Final	= SaabContextBuilder.coordMap.get(destination);
			
			ruta = path.getPath(Inicio, Final);
			
			totalLenght = path.getPathLength(Inicio, Final);
		}else{
			throw new Exception("El origen  destino no esta mapeado en las vias del modelo");
		}
		
		for(RepastEdge<Junction> edge:ruta){
			
			NetworkEdge<Junction> roadedge = (NetworkEdge<Junction>)edge;
			this.routeX.addAll(Arrays.asList(roadedge.getRoad().getCoordinates()));	
		}
		return totalLenght;
	}
	
	public void forward(boolean goForward){
		this.Forward = goForward;
	}
	
	/**
	 * Devuelve la coordenada del siguiente paso en la ruta.
	 * Verifica si el movimiento es hacia adelante o hacia atrás, luego
	 * modifica el indicador de posiscion actual sumando o restando un
	 * digito según el caso y devuelve la siguiente posicion de la ruta.
	 * 
	 * @return Coordinate
	 */
	public Coordinate nextStep(){
		
		Coordinate paso = routeX.get(currentPosition);
		if(!(destination.equals(paso)) && Forward){
			
			if((currentPosition+velocidad)>(routeX.size()-1)) 
				return destination;
			
			currentPosition = (currentPosition+velocidad)>(routeX.size()-1)?(routeX.size()-1):currentPosition+velocidad;
			
		}else if(!(origin.equals(paso)) && !(Forward)){
			
			if((currentPosition-velocidad)<0)
				return origin;
			
			currentPosition = (currentPosition-velocidad)<0?0:currentPosition-velocidad;
			
		}else{
			LOGGER.info("Ya esta en el destino");
		}
		
		return this.routeX.get(currentPosition);
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}
	
	/**
	 * Calculate the distance (in meters) between two Coordinates, using the coordinate reference system that the
	 * roadGeography is using. For efficiency it can return the angle as well (in the range -0 to 2PI) if returnVals
	 * passed in as a double[2] (the distance is stored in index 0 and angle stored in index 1).
	 * 
	 * @param c1
	 * @param c2
	 * @param returnVals
	 *            Used to return both the distance and the angle between the two Coordinates. If null then the distance
	 *            is just returned, otherwise this array is populated with the distance at index 0 and the angle at
	 *            index 1.
	 * @return The distance between Coordinates c1 and c2.
	 */
	public static synchronized double distance(Coordinate c1, Coordinate c2, double[] returnVals) {
		// TODO check this now, might be different way of getting distance in new Simphony
		GeodeticCalculator calculator = new GeodeticCalculator(SaabContextBuilder.SAABGeography.getCRS());
		calculator.setStartingGeographicPoint(c1.x, c1.y);
		calculator.setDestinationGeographicPoint(c2.x, c2.y);
		double distance = calculator.getOrthodromicDistance();
		if (returnVals != null && returnVals.length == 2) {
			returnVals[0] = distance;
			double angle = Math.toRadians(calculator.getAzimuth()); // Angle in range -PI to PI
			// Need to transform azimuth (in range -180 -> 180 and where 0 points north)
			// to standard mathematical (range 0 -> 360 and 90 points north)
			if (angle > 0 && angle < 0.5 * Math.PI) { // NE Quadrant
				angle = 0.5 * Math.PI - angle;
			} else if (angle >= 0.5 * Math.PI) { // SE Quadrant
				angle = (-angle) + 2.5 * Math.PI;
			} else if (angle < 0 && angle > -0.5 * Math.PI) { // NW Quadrant
				angle = (-1 * angle) + 0.5 * Math.PI;
			} else { // SW Quadrant
				angle = -angle + 0.5 * Math.PI;
			}
			returnVals[1] = angle;
		}
		return distance;
	}

}
