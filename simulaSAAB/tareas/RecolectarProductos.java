package simulaSAAB.tareas;

import java.util.List;

import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.OrdenDeServicio;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.NodoSaab;
import simulaSAAB.contextos.ObjetoMovil;

import com.vividsolutions.jts.geom.Coordinate;

public class RecolectarProductos extends TransitarCircular {
	
	private NodoSaab nodoOrigen;
	
	private List<OrdenDeServicio> ordenes;
	
	private String Estado;
	
	/**
	 * Constructor
	 */
	public RecolectarProductos() {
		super();
	}

	public RecolectarProductos(NodoSaab nodoOrigen, CentroUrbano puebloDestino, List<OrdenDeServicio> ordenes) {
		
		super(nodoOrigen.getRoadAccess(), puebloDestino.getRoadAccess());
		
		this.nodoOrigen 	= nodoOrigen;
		this.ordenes 		= ordenes;	
		
		this.Estado = EstadosActividad.READY.toString();
	}
	
	@Override
	public void secuenciaPrincipalDeAcciones(ObjetoMovil actor) {
		
		Coordinate actor_coord = actor.getGeometria().getCoordinate();
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){			
			
			this.Estado	=EstadosActividad.RUNNING.toString();
			actor_coord = this.Path.nextStep();
			
			//LOGGER.log(Level.INFO, this.toString() + " Esta listo para iniciar. Actor: " + actor.toString());
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){		
			
			if(actor_coord.equals(Origen)){//Dio la vuelta completa
				
				this.nodoOrigen.AlmacenarProductos(ordenes);				
				this.Estado = EstadosActividad.DONE.toString();
				
			}else if(actor_coord.equals(Destino)){//Llego a su primer destino		
									
				this.Path.forward(false);
				this.Costo +=Costo;
				actor_coord = this.Path.nextStep();
				
				//TODO El agente deberia dezplazarse a cada ubicacion de las ofertas antes de devolverse
				
			}else{//Va en camino
				
				actor_coord = this.Path.nextStep();
				SAABGeography.move(actor, actor.getGeometria());
			}
		}
		
	}

}
