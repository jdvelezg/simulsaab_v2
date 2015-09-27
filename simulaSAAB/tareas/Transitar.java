package simulaSAAB.tareas;

import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Coordinate;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.contextos.environment.Junction;
import simulaSAAB.contextos.environment.Route;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.global.persistencia.MPAConfigurado;
/**
 * Representa el movmiento de un objeto geométrico de un punto origen a un punto destino sobre una geografía
 * 
 * @author lfgomezm
 */
public class Transitar implements SistemaActividadHumana<ObjetoMovil> {
	
	private final int id;
	/**
	 * Registro de la clase usado para depuración <code>Debugging</code>
	 */
	private static Logger LOGGER = Logger.getLogger(Transitar.class.getName());
	/**
	 * Enunciado de la tarea
	 */
	private static String ENUNCIADO = "Sistema de actividad para mover un objeto geométrico de un punto origen a un punto destino sobre una geografía";
	/**
	 * Propósito de la tarea
	 */
	protected Proposito Proposito;	
	/**
	 * Ruta establecida para llegar del punto de origen al punto de destino 
	 */
	protected Route Path;
	/**
	 * Coordenada del punto de origen 
	 */
	protected Coordinate Origen;
	/**
	 * Coordenada del punto de destino 
	 */
	protected Coordinate Destino;
	/**
	 * Estado actual de la tarea
	 */
	private String Estado;
	/**
	 * Paso actual de la tarea
	 */
	private int paso;
	
	protected Double Costo;
	
	
	/**
	 * Constructor
	 */
	public Transitar(){
		
		MPAConfigurado mpa 	=new MPAConfigurado("Transitar");
		this.id				= mpa.getId();
		
		this.Costo 	= new Double(0);		
		this.Estado	= EstadosActividad.UNSET.toString();
	}
	
	/**
	 * Constructor
	 * @param origen
	 * @param destino
	 */
	public Transitar(Coordinate origen, Coordinate destino) {
			
		MPAConfigurado mpa 	=new MPAConfigurado("Transitar");
		this.id				= mpa.getId();
		
		this.Origen		= origen;
		this.Destino	= destino;		
		this.Costo 		= new Double(0);		
		setPath();
	}
	/**
	 * Crea la ruta para llegar del punto de origen al punto de destino. 
	 * Toma como referencia el costo promedio del transporte de la carga por metro
	 */
	public void setPath(){
		
		this.Path = new Route(Origen,Destino);		
		try{			
			this.Costo		= Path.setRoute()*VariablesGlobales.COSTO_PROMEDIO_TRANSPORTE_CARGA_POR_METRO;
			this.Estado		= EstadosActividad.READY.toString();
		}catch(Exception e){			
			LOGGER.severe("ERROR al crear la ruta: "+e.toString());
			e.printStackTrace();
		}		
	}
	
	@Override
	public void secuenciaPrincipalDeAcciones(ObjetoMovil actor) {
		
		Coordinate actor_coord = actor.getGeometria().getCoordinate();
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){			
			
			this.Estado	=EstadosActividad.RUNNING.toString();
			this.paso	=1;
			//LOGGER.log(Level.INFO, this.toString() + " Esta listo para iniciar. Actor: " + actor.toString());
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){		
			
			if(actor_coord.equals(Destino)){
				
				this.Estado = EstadosActividad.DONE.toString();
			}else{
				
				Coordinate nuevaPosicion = Path.nextStep();
				actor_coord.x = new Double(nuevaPosicion.x);
				actor_coord.y = new Double(nuevaPosicion.y);
				SaabContextBuilder.SAABGeography.move(actor, actor.getGeometria());
			}			
		}
	}
	
	/**
	 * Regresa el objeto a su posición inicial 
	 * @param actor  objeto movil que representa al agente
	 */
	public void repeatBackwards(ObjetoMovil actor){
		/**
		 * Verifica que la tarea se encuentre ejecutada
		 */
		if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			
			this.Estado 	= EstadosActividad.READY.toString();
			this.Destino 	= this.Origen;
			this.Path.forward(false);
			this.Costo +=Costo.doubleValue();
			this.secuenciaPrincipalDeAcciones(actor);
		}
			
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		return new Transitar(this.Origen,this.Destino);
	}

	@Override
	public int getPaso() {
		return this.paso;
	}

	@Override
	public String getEstado() {
		return this.Estado;
	}

	@Override
	public String getEnunciado() {
		return this.ENUNCIADO;
	}

	@Override
	public Proposito getProposito() {
		return this.Proposito;
	}
	/**
	 * Devuelve el punto de origen
	 * @return coordenada del punto de origen
	 */
	public Coordinate getOrigen() {
		return Origen;
	}
	/**
	 * Asigna el punto de origen
	 * @param origen coordinate, coordinada del punto de origen 
	 */
	public void setOrigen(Coordinate origen) {
		Origen = origen;
	}
	/**
	 * Devuelve el punto de detino
	 * @return coordenada del punto de destino
	 */
	public Coordinate getDestino() {
		return Destino;
	}
	/**
	 * Asigna el punto de destino
	 * @param destino   coordinate, coordenada del punto de destino
	 */
	public void setDestino(Coordinate destino) {
		Destino = destino;
	}
	/**
	 * Devuelve el costo promedio del transporte de la carga por metro
	 * @return Costo 
	 */
	public double getCosto() {
		return Costo;
	}

	@Override
	public int getId() {
		return 0;
	}
	
	@Override 
	public boolean equals(Object obj){
		
		if(obj instanceof SistemaActividadHumana){
			
			SistemaActividadHumana act = (SistemaActividadHumana)obj;			
			return this.id==act.getId();
		}else{
			return false;
		}
	}
	

}