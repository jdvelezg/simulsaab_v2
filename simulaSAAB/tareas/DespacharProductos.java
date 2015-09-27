package simulaSAAB.tareas;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import simulaSAAB.agentes.Camioneta;
import simulaSAAB.comunicacion.OrdenDeServicio;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.NodoSaab;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.PlazaDistrital;
import simulaSAAB.contextos.SaabContextBuilder;

import com.vividsolutions.jts.geom.Coordinate;
/**
 * Representa la entrega de los productos a los demandantes
 * @author lfgomezm
 *
 */
public class DespacharProductos extends Transitar {
	/**
	 * Registro de la clase usado para depuración <code>Debugging</code>
	 */
	private static Logger LOGGER = Logger.getLogger(DespacharProductos.class.getName());
	/**
	 * Listado de las ordenes de servicio por entregar
	 */
	private List<OrdenDeServicio> ordenes;
	/**
	 * Nodo que comprende la plaza en la que debe ser entregado el producto
	 */
	private NodoSaab plazaDestino;
	/**
	 * Nodo en el que debe ser recogido el producto
	 */
	private NodoSaab nodoOrigen;
	/**
	 * Estado actual de la tarea
	 */
	private String Estado;
	/**
	 * Paso actual de la tarea
	 */
	private int paso;
	
	
	/**
	 * Constructor
	 */
	public DespacharProductos() {
		super();
	}
	/**
	 * Constructor
	 * 
	 * @param nodoOrigen nodoSaab, nodo en el que debe recogerse el producto a entregar
	 * @param plazaDestino nodoSaab, nodo en el que debe ser entregado el producto
	 * @param ordenes orden de servicio, listado de las ordenes a entregar
	 */
	public DespacharProductos(NodoSaab nodoOrigen, NodoSaab plazaDestino, List<OrdenDeServicio> ordenes) {
		
		super(nodoOrigen.getRoadAccess(), plazaDestino.getRoadAccess());
		this.nodoOrigen		= nodoOrigen;
		this.ordenes 		= ordenes;
		this.plazaDestino	= plazaDestino;
		
		this.paso	= 0;
		this.Estado = EstadosActividad.READY.toString();
	}
	
	
	@Override
	public void secuenciaPrincipalDeAcciones(ObjetoMovil actor) {
		
		Coordinate actor_coord = actor.getGeometria().getCoordinate();
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){			
			
			Estado	=EstadosActividad.RUNNING.toString();
			paso	=1;
			
			//Fija la velocidad de acuerdo al tipo de transporte
			if(actor instanceof Camioneta){
				super.Path.setVelocidad(2);
			}else{
				super.Path.setVelocidad(10);
			}
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){		
			
			if(actor_coord.equals(Destino)){//llego al destino				
				
				/*
				 * Entrega los productos a los Demandantes, representados
				 * en las ordenes de compra
				 */
				plazaDestino.AlmacenarProductos(ordenes);
				Estado = EstadosActividad.DONE.toString();
				
			}else{//Va en camino
				
				Coordinate nuevaPosicion = Path.nextStep();
				actor_coord.x = new Double(nuevaPosicion.x);
				actor_coord.y = new Double(nuevaPosicion.y);
				SaabContextBuilder.SAABGeography.move(actor, actor.getGeometria());
				//LOGGER.log(Level.INFO,actor.toString()+" moved "+actor_coord.toString());
			}			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			/*
			 *Una vez cumple su funcion el objeto movil es removido 
			 */
			SaabContextBuilder.SAABContext.remove(actor);
			//LOGGER.log(Level.INFO,this.toString()+" DONE: -DOING NOTHING");
		}
	}
	
	@Override
	public SistemaActividadHumana getInstance(){
		return new DespacharProductos(this.nodoOrigen, this.plazaDestino, this.ordenes);
	}

}