package simulaSAAB.tareas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import repast.simphony.util.collections.IndexedIterable;
import simulaSAAB.agentes.Demandante;
import simulaSAAB.agentes.SiSaab;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;

public class RegistrarDemandaUnitaria implements
		SistemaActividadHumana<Demandante> {
	
	private static Logger LOGGER = Logger.getLogger(RegistrarDemandaUnitaria.class.getName());

	private static final String ENUNCIADO ="";
	
	private static Proposito proposito = new Proposito("Comprar para vender productos");
	
	private String Estado;
	
	private int paso;
	
	private Demanda demanda;
	
	/**
	 * Constructor
	 */
	public RegistrarDemandaUnitaria() {
				
		this.Estado	=EstadosActividad.READY.toString();
		this.paso 	= 0;
	}

	@Override
	public void secuenciaPrincipalDeAcciones(Demandante actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
			
			this.Estado	=EstadosActividad.RUNNING.toString();
			this.paso	=1;			
			actor.setEstado("RUNNING");
			
			//Registra la demanda
			this.demanda 	= actor.generarDemanda();
			SiSaab.registrarDemanda(demanda);
			LOGGER.log(Level.INFO,actor.toString()+" DEMANDA REGISTRADA");			
			
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){
			
			
			switch(this.paso){
			case 1:
				/**
				 * Lleva a cabo la compra una vez ha sido registrada la demanda
				 */
				List<Oferta> compra = new ArrayList<Oferta>();			
				Double presupuesto	= demanda.getPresupuesto();
				
				//Obtiene ofertas registradas
				List<Oferta> ofertas = SiSaab.ofertasRegistradas(demanda.getNombreproducto());
				
				//filtra ofertas de interés para la demanda
				for(Oferta o: ofertas){		
					
					if(o.getPrecio() <= presupuesto){					
						o.vendida();
						compra.add(o);						
						presupuesto	-= o.getPrecio();					
					}
				}//end for
				
				//verifica que si existan ofertas disponibles para la demanda
				if(compra.size()>0){	
					
					SiSaab.realizarCompra(compra, demanda);
					LOGGER.log(Level.INFO,actor.toString()+" COMPRA REALIZADA");
					paso++;
					
				}else{//No existen ofertas para la demanda 
					
					// cambia sus preferencias de consolidación a verdaderas
					actor.setIntencionConsolidacion(true);
					//Da por terminada la actividad
					this.Estado = EstadosActividad.DONE.toString();
					
					LOGGER.log(Level.INFO,actor.toString()+" No encontro ofertas");
				}				
				
			break;
			case 2:
				//Espera el producto comprado
				if(actor.pedidosRecibidos())
					paso++;
				
			break;
			case 3:
				actor.setEstado("RUNNING");
				actor.gestionarPedidos();
				paso++;
				
			break;			
			default:
				
				this.Estado =EstadosActividad.DONE.toString();
			}
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			actor.setEstado("IDLE");
			//LOGGER.log(Level.INFO,this.toString()+" DONE: -DOING NOTHING");
		}
		
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		// TODO Auto-generated method stub
		return new RegistrarDemandaUnitaria();
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
		// TODO Auto-generated method stub
		return ENUNCIADO;
	}

	@Override
	public Proposito getProposito() {
		return proposito;
	}

}
