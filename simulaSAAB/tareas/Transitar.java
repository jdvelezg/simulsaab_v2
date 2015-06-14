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

public class Transitar implements SistemaActividadHumana<ObjetoMovil> {

	private static Logger LOGGER = Logger.getLogger(Transitar.class.getName());
	
	private static String ENUNCIADO = "Sistema de actividad para mover un objeto Geometrico de un punto origen a un punto destino sobre una geografia";
	
	protected Proposito Proposito;	
	
	protected Route Path;
	
	protected Coordinate Origen;
	
	protected Coordinate Destino;
	
	private String Estado;
	
	private int paso;
	
	protected Double Costo;
	
	
	/**
	 * Constructor
	 */
	public Transitar(){
		
		this.Costo 	= new Double(0);		
		this.Estado	= EstadosActividad.UNSET.toString();
	}
	
	/**
	 * Constructor
	 * @param origen
	 * @param destino
	 */
	public Transitar(Coordinate origen, Coordinate destino) {
						
		this.Origen		= origen;
		this.Destino	= destino;		
		this.Costo 		= new Double(0);		
		setPath();
	}
	
	public void setPath(){
		
		this.Path = new Route(Origen,Destino);		
		try{			
			this.Costo		= Path.setRoute();
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
				
				actor_coord = this.Path.nextStep();
				SaabContextBuilder.SAABGeography.move(actor, actor.getGeometria());
			}			
		}
	}
	
	public void repeatBackwards(ObjetoMovil actor){
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			
			this.Estado = EstadosActividad.READY.toString();
			this.Path.forward(false);
			this.Costo +=Costo;
			this.secuenciaPrincipalDeAcciones(actor);
		}
			
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		return new Transitar(this.Origen,this.Destino);
	}

	@Override
	public int getPaso() {
		// TODO Auto-generated method stub
		return this.paso;
	}

	@Override
	public String getEstado() {
		// TODO Auto-generated method stub
		return this.Estado;
	}

	@Override
	public String getEnunciado() {
		// TODO Auto-generated method stub
		return this.ENUNCIADO;
	}

	@Override
	public Proposito getProposito() {
		// TODO Auto-generated method stub
		return this.Proposito;
	}

	public Coordinate getOrigen() {
		return Origen;
	}

	public void setOrigen(Coordinate origen) {
		Origen = origen;
	}

	public Coordinate getDestino() {
		return Destino;
	}

	public void setDestino(Coordinate destino) {
		Destino = destino;
	}
	
	

}
