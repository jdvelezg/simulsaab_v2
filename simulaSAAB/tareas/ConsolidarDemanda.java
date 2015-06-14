package simulaSAAB.tareas;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.util.collections.IndexedIterable;
import simulaSAAB.agentes.AgenteInteligente;
import simulaSAAB.agentes.Demandante;
import simulaSAAB.agentes.VendedorFinal;
import simulaSAAB.comunicacion.EjecutarAccionConProposito;
import simulaSAAB.comunicacion.MensajeACL;
import simulaSAAB.comunicacion.Preposicion;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.VariablesGlobales;

public class ConsolidarDemanda implements SistemaActividadHumana<Demandante> {
	
	private static Logger LOGGER = Logger.getLogger(ConsolidarDemanda.class.getName());
	
	private static String ENUNCIADO = "Consolidar demandas";
	
	private static Proposito Proposito = new Proposito("Comprar para vender productos");
	
	private String Estado;
	
	private int paso;
	
	private Double DineroInicial;
	
	private Double CostoEjecucion;
	
	private int numeroSolicitudes;
	

	public ConsolidarDemanda() {
		
		Estado 			= EstadosActividad.READY.toString();
		paso			= 0;
		CostoEjecucion	= new Double(0);
	}

	@Override
	public void secuenciaPrincipalDeAcciones(Demandante actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
			
			this.Estado	=EstadosActividad.RUNNING.toString();			
			DineroInicial = DineroInicial = new Double(actor.getDinero().getCantidad());
			
			//Crea mensaje proposal, Solicitando la consolidacion de demandas
			MensajeACL mensaje 						= new MensajeACL();
			SistemaActividadHumana conformarNutrired= new ConformarNutrired();
			Preposicion contenido 					= new EjecutarAccionConProposito(conformarNutrired);	
			
			mensaje.setContent(contenido);
			mensaje.setPerformative("propose");
			mensaje.setReply_to(actor);
			mensaje.setReply_with(new MensajeACL(mensaje.getConversationID(), "accept-proposal"));
			mensaje.setSender(actor);
			
			//Envia el mensaje a agentes cercanos		
			Envelope actorEnvelope 	= SaabContextBuilder.SAABGeography.getGeometry(actor).getEnvelopeInternal();
			actorEnvelope.expandBy(VariablesGlobales.DISTANCIA_BUSQUEDA_NUTRIRED_ANGULAR);
			
			Iterable<VendedorFinal> vendedores = SaabContextBuilder.SAABGeography.queryInexact(actorEnvelope, VendedorFinal.class);
			numeroSolicitudes = 0;
			
			for(VendedorFinal vendedor: vendedores){				
								
				Coordinate vendGeom = SaabContextBuilder.SAABGeography.getGeometry(vendedor).getCoordinate();
				
				if(actorEnvelope.contains(vendGeom)){					
					
					vendedor.recibirMensaje(mensaje);
					numeroSolicitudes++;
				}				
			}
			
			//Si contacto almenos un agente para la consolidación, crea la nutrired y espera
			if(numeroSolicitudes>0){
				
				this.paso	=1;	
				//Se pone al agente en espera, como metodo de control
				actor.setEstado("LISTENING");
				
			}else{
				/*
				 * No existen agentes con quien asociarse, fija el paso en 100
				 * para inducir la finalización de la actividad
				 * 
				 * TODO Expandir el area de busqueda y buscar nuevamente
				 */
				paso = 100;
			}			
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){
			
			
			switch(this.paso){
			case 1:
				
				/*
				 * Espera a ser incluido en la nutrired 				 
				 */
				try{
					actor.atenderMensajes();
				}catch(NullPointerException e){
					//no hace nada
				}				
				if(actor.getEstado().equalsIgnoreCase("WAITING"))
					paso ++;
			break;
			case 2:
				
				if(!(actor.getEstado().equalsIgnoreCase("WAITING")))
					paso ++;
				
			break;
			default:
				//LOGGER.log(Level.INFO, "TERMINA PROCESO"+actor.toString());
				this.Estado =EstadosActividad.DONE.toString();				
			}
			
				
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			
			//substrae el costo de ejecución del MPA del dinero del agente
			actor.getDinero().subtractCantidad(CostoEjecucion);
			//Calcula la utilidad obtenida al ejecutar el MPA				
			actor.setUltimaUtilidadObtenida(DineroInicial-actor.getDinero().getCantidad());
			//fija estado del actor en IDLE
			actor.setEstado("IDLE");			
		}

		
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		
		return new ConsolidarDemanda() ;
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

	

}
