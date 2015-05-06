package simulaSAAB.tareas;

import java.util.List;

import simulaSAAB.comunicacion.OrdenDeServicio;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.NodoSaab;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.PlazaDistrital;

import com.vividsolutions.jts.geom.Coordinate;

public class DespacharProductos extends Transitar {
	
	private List<OrdenDeServicio> ordenes;
	
	private PlazaDistrital plazaDestino;
	
	private String Estado;
	
	private int paso;
	
	
	/**
	 * Constructor
	 */
	public DespacharProductos() {
		super();
	}

	public DespacharProductos(NodoSaab nodoOrigen, PlazaDistrital plazaDestino, List<OrdenDeServicio> ordenes) {
		
		super(nodoOrigen.getRoadAccess(), plazaDestino.getRoadAccess());
		this.ordenes 		= ordenes;
		this.plazaDestino	= plazaDestino;
	}
	
	
	@Override
	public void secuenciaPrincipalDeAcciones(ObjetoMovil actor) {
		
		Coordinate actor_coord = actor.getGeometria().getCoordinate();
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){			
			
			Estado	=EstadosActividad.RUNNING.toString();
			paso	=1;
			//LOGGER.log(Level.INFO, this.toString() + " Esta listo para iniciar. Actor: " + actor.toString());
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){		
			
			if(actor_coord.equals(Destino)){				
				
				plazaDestino.AlmacenarProductos(ordenes);
				Estado = EstadosActividad.DONE.toString();
			}else{
				
				actor_coord = this.Path.nextStep();
				SAABGeography.move(actor, actor.getGeometria());
			}			
		}
	}

}
