package simulaSAAB.tareas;

import com.vividsolutions.jts.geom.Coordinate;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.contextos.environment.Junction;
import simulaSAAB.contextos.environment.Route;

public class TransitarCircular extends Transitar {
				
		
	private String Estado;
	
	private int paso;	
	

	/**
	 * Constructor
	 */
	public TransitarCircular() {
		super();
	}
	
	public TransitarCircular(Coordinate origen, Coordinate destino){
		super(origen, destino);
	}

	@Override
	public void secuenciaPrincipalDeAcciones(ObjetoMovil actor) {
		
		Coordinate actor_coord = actor.getGeometria().getCoordinate();
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){			
			
			this.Estado	= EstadosActividad.RUNNING.toString();
			actor_coord = this.Path.nextStep();
			
			//LOGGER.log(Level.INFO, this.toString() + " Esta listo para iniciar. Actor: " + actor.toString());
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){		
			
			if(actor_coord.equals(Origen)){
				
				this.Estado = EstadosActividad.DONE.toString();
				
			}else if(actor_coord.equals(Destino)){
				
				this.Path.forward(false);
				this.Costo +=Costo;
				actor_coord = this.Path.nextStep();
			}else{
				
				actor_coord = this.Path.nextStep();
				SaabContextBuilder.SAABGeography.move(actor, actor.getGeometria());
			}
		}
		
	}	

}
