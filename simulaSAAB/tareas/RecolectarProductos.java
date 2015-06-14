package simulaSAAB.tareas;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.OrdenDeServicio;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.NodoSaab;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.SaabContextBuilder;

import com.vividsolutions.jts.geom.Coordinate;

public class RecolectarProductos extends TransitarCircular {
	
	private static Logger LOGGER = Logger.getLogger(RecolectarProductos.class.getName());
	
	private NodoSaab nodoOrigen;
	
	private CentroUrbano puebloDestino;
	
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
		this.puebloDestino	= puebloDestino;
		this.nodoOrigen 	= nodoOrigen;
		this.ordenes 		= ordenes;	
		
		this.Estado = EstadosActividad.READY.toString();
	}
	
	@Override
	public void secuenciaPrincipalDeAcciones(ObjetoMovil actor) {
		
		Coordinate actor_coord = actor.getGeometria().getCoordinate();
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){			
			
			this.Estado	=EstadosActividad.RUNNING.toString();
			Coordinate nuevaPosicion = Path.nextStep();
			actor_coord.x = new Double(nuevaPosicion.x);
			actor_coord.y = new Double(nuevaPosicion.y);
			
			//LOGGER.log(Level.IRecolectarProductosNFO, this.toString() + " Esta listo para iniciar. Actor: " + actor.toString());
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){		
			
			if(actor_coord.equals(Origen)){//Dio la vuelta completa				
				
				//Entrega los productos en el nodo logistico
				this.nodoOrigen.AlmacenarProductos(ordenes);		
				this.Estado = EstadosActividad.DONE.toString();
				
			}else if(actor_coord.equals(Destino)){//Llego a su primer destino			
				
				this.Path.forward(false);
				this.Costo +=Costo;
				Coordinate nuevaPosicion = Path.nextStep();
				actor_coord.x = new Double(nuevaPosicion.x);
				actor_coord.y = new Double(nuevaPosicion.y);
				
				/*
				 * Recolecta los productos 
				 * (recoleccion simbolica): Los productos fueron pasados a la oferta.
				 * TODO El agente deberia dezplazarse a cada ubicacion de las ofertas
				 * y cargar los productos.
				 */
				/*for(OrdenDeServicio ord: ordenes){
					
					List<Oferta> ofertas = ord.getOfertas();
					for(Oferta o: ofertas){
						Oferente productor = o.getVendedor();
					}
					
				}*/
				
			}else{//Va en camino
				
				Coordinate nuevaPosicion = Path.nextStep();
				actor_coord.x = new Double(nuevaPosicion.x);
				actor_coord.y = new Double(nuevaPosicion.y);
				SaabContextBuilder.SAABGeography.move(actor, actor.getGeometria());
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
		return new RecolectarProductos(this.nodoOrigen, this.puebloDestino, this.ordenes);
	}

}
