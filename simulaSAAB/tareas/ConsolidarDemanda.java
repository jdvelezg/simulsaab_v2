package simulaSAAB.tareas;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.util.collections.IndexedIterable;
import simulaSAAB.agentes.AgenteInteligente;
import simulaSAAB.agentes.Demandante;
import simulaSAAB.agentes.VendedorFinal;
import simulaSAAB.comunicacion.EjecutarAccionConProposito;
import simulaSAAB.comunicacion.MensajeACL;
import simulaSAAB.comunicacion.Preposicion;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.contextos.PlazaDistrital;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.global.persistencia.MPAConfigurado;
/**
 * Representa la consolidación de las demandas en el SISAAB
 * @author lfgomezm
 *
 */
public class ConsolidarDemanda implements SistemaActividadHumana<Demandante> {
	/**
	 * Registro de la clase usado para depuración <code>Debugging</code>
	 */
	private static Logger LOGGER = Logger.getLogger(ConsolidarDemanda.class.getName());
	/**
	 * Identificador de la demanda consolidada
	 */
	private final Integer id;
	/**
	 * Enunciado de la tarea
	 */
	private final String Enunciado;
	/**
	 * Propósito de la tarea
	 */
	private final Proposito proposito;
	
	private final Double CostoEjecucion;
	/**
	 * Estado actual de la tarea
	 */
	private String Estado;
	/**
	 * Paso actual de la tarea
	 */
	private int paso;
	
	private Double DineroInicial;
	
	private int numeroSolicitudes;
	
	private Integer MensajeConsolidacionId;
	
	private Double Tickinicial;
	
	/**
	 * Constructor
	 */
	public ConsolidarDemanda() {
		
		MPAConfigurado mpa 	=new MPAConfigurado("ConsolidarDemanda");
		
		this.id				= mpa.getId();
		this.Enunciado		= mpa.getEnunciado();
		this.proposito 		= mpa.getProposito();
		this.CostoEjecucion	= mpa.getCosto();
		
		Estado 			= EstadosActividad.READY.toString();
		paso			= 0;
	}

	@Override
	public synchronized void secuenciaPrincipalDeAcciones(Demandante actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
			
						
			DineroInicial = new Double(actor.getDinero().getCantidad()).doubleValue();
						
			//Crea mensaje proposal, Solicitando la consolidacion de demandas
			MensajeACL mensaje						= new MensajeACL();
			SistemaActividadHumana conformarNutrired= new ConformarNutrired();
			Preposicion contenido 					= new EjecutarAccionConProposito(conformarNutrired);			
			this.MensajeConsolidacionId 			= mensaje.getConversationID();
			
			mensaje.setContent(contenido);
			mensaje.setPerformative("propose");
			mensaje.setReply_to(actor);
			
			MensajeACL replyWhit	= new MensajeACL(MensajeConsolidacionId, "accept-proposal");
			replyWhit.setInReply_to(MensajeConsolidacionId);			
			
			mensaje.setReply_with(replyWhit);
			mensaje.setSender(actor);
			
			//Envia el mensaje a agentes cercanos		
			Envelope actorEnvelope 	= SaabContextBuilder.SAABGeography.getGeometry(actor).getEnvelopeInternal();
			actorEnvelope.expandBy(VariablesGlobales.DISTANCIA_BUSQUEDA_NUTRIRED_ANGULAR);
			
			Iterable<VendedorFinal> vendedores = SaabContextBuilder.SAABGeography.queryInexact(actorEnvelope, VendedorFinal.class);
			numeroSolicitudes = 0;
			
			for(VendedorFinal vendedor: vendedores){				
								
				Geometry otherGeom 	= SaabContextBuilder.SAABGeography.getGeometry(vendedor);
				PlazaDistrital plazaActor = actor.getPuntoDemanda();
				PlazaDistrital plazaOtro  = vendedor.getPuntoDemanda();
				
				if(actorEnvelope.intersects(otherGeom.getEnvelopeInternal()) 
						&& !(otherGeom.equals(SaabContextBuilder.SAABGeography.getGeometry(actor)))
						&& plazaActor.equals(plazaOtro) ){		
					
					actor.enviarMensaje(mensaje, vendedor);					
					numeroSolicitudes++;
				}				
			}
			
			//LOGGER.log(Level.INFO,actor.toString()+" numero de vendedores encontrados: "+numeroSolicitudes);
			//Si contacto almenos un agente para la consolidación, crea la nutrired y espera
			if(numeroSolicitudes>0){				
				
				Estado	= EstadosActividad.RUNNING.toString();
				paso	= 1;	
				//Se pone al agente en espera, como metodo de control
				actor.setEstado("LISTENING");
				Tickinicial	= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			}else{
				/*
				 * No existen agentes con quien asociarse, fija el paso en 100
				 * para inducir la finalización de la actividad
				 * 
				 * TODO Expandir el area de busqueda y buscar nuevamente
				 */
				paso = 100;
				Estado	= EstadosActividad.DONE.toString();
			}//CierraElse-numeroSolicitudes
			
						
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){
			
			
			switch(this.paso){
			case 1:
				
				Double current = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
				/*
				 * Espera a ser incluido en la nutrired 				 
				 */
				
				try{
					actor.buscarRespuesta(MensajeConsolidacionId);
				}catch(NullPointerException e){
					System.err.println("NullPointerException: " + e.getMessage());
				}	
				
				/*
				 * Si entra en espera, es porque conformo nutrired
				 */
				if(actor.getEstado().equalsIgnoreCase("WAITING"))
					paso ++;
					
				
				/*
				 * Espera por solo 1000 ticks - fija el paso en 100
				 * para inducir la finalización de la actividad 
				 */
				if(current > this.Tickinicial+1000){
					paso=100; 
					//LOGGER.log(Level.INFO,actor.toString()+" Nunca consolido");
				}
				
			break;
			case 2:
				/*
				 * Si sale de espera es porque la consolidacion termino
				 */
				if(!(actor.getEstado().equalsIgnoreCase("WAITING"))){
					paso ++;
				//LOGGER.log(Level.INFO,actor.toString()+" termina consolidacion exitosa");
			}
				
			break;			
			default:
				//LOGGER.info(actor.toString()+" TERMINA PROCESO "+numeroSolicitudes);
				this.Estado =EstadosActividad.DONE.toString();				
			}
			
				
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			
			//substrae el costo de ejecución del MPA del dinero del agente
			//actor.getDinero().subtractCantidad(CostoEjecucion);			
			//Fija intencion de consolidacion de nuevo en true
			actor.setIntencionConsolidacion(true);
			
			//Los oferentes calculan su utilidadFinal al recibir el producto
			//LOGGER.info(actor.toString()+" termina consolidacion");
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
	public Proposito getProposito() {		
		return this.proposito;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getEnunciado() {
		return this.Enunciado;
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

	@Override
	public double getCosto() {
		// TODO Auto-generated method stub
		return 0;
	}

}