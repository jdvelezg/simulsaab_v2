package simulaSAAB.tareas;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import simulaSAAB.agentes.Demandante;
import simulaSAAB.agentes.OperadorRedDemanda;
import simulaSAAB.agentes.VendedorFinal;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.persistencia.MPAConfigurado;
/**
 * Representa la conformación de nutriredes en el SISAAB
 * @author lfgomezm
 *
 */
public class ConformarNutrired implements SistemaActividadHumana<Demandante> {
	/**
	 * Identificador de la nutrired
	 */
	private final int id;
	/**
	 * Registro de la clase usado para depuración <code>Debugging</code>
	 */
	private static Logger LOGGER = Logger.getLogger(ConformarNutrired.class.getName());
	/**
	 * Establece el enunciado de la tarea
	 */
	private static String ENUNCIADO = "Conformar nutrired";
	/**
	 * Propósito de la tarea
	 */
	private Proposito Proposito;
	/**
	 * Estado actual de la tarea
	 */
	private String Estado;
	/**
	 * Paso actual de la tarea
	 */
	private int paso;
	
	private OperadorRedDemanda operadorNutriRed;

	/**
	 * Constructor
	 */
	public ConformarNutrired() {
		
		MPAConfigurado mpa 	=new MPAConfigurado("ConsolidarDemanda");
		
		this.id					= mpa.getId();
		this.Estado 			= EstadosActividad.READY.toString();
		int identificador		= SaabContextBuilder.getIdentificadorAgente();
		this.operadorNutriRed 	= new OperadorRedDemanda(identificador);				
	}
	
	/**
	 * Asigna un operador a la nutrired 
	 * @param operador operador de la red de demanda asignado como operador de la nutrired
	 */
	public ConformarNutrired(OperadorRedDemanda operador){
		
		MPAConfigurado mpa 	=new MPAConfigurado("ConsolidarDemanda");
		
		this.id					= mpa.getId();
		this.operadorNutriRed 	= operador;
		this.Estado 			= EstadosActividad.READY.toString();
	}

	
	@Override
	public void secuenciaPrincipalDeAcciones(Demandante actor) {
	
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
									
			//verifica que el agente este en el contexto distrital
			if(SaabContextBuilder.BogotaContext.contains(actor)){
				
				//Fija el punto de demanda del operador de red de demanda, si no posee uno
				if(!operadorNutriRed.isOperable()){
					
					operadorNutriRed.setPuntoDemanda(actor.getPuntoDemanda());
					SaabContextBuilder.NutriredesNetwork.addEdge(operadorNutriRed, actor.getPuntoDemanda());
				}
							
				
				//LOGGER.log(Level.INFO," Agregando Agente "+actor.toString()+" a la nutrired");
				//crea una relacion entre el agente y el operador de demanda				
				SaabContextBuilder.NutriredesNetwork.addEdge(operadorNutriRed, actor);				
				actor.setEstado("WAITING");
				/*
				 * Cambia intencion de consolidacion para que le agente no se asocie con otras redes
				 */
				actor.setIntencionConsolidacion(false);
				
				Estado	=EstadosActividad.DONE.toString();
				
			}else{
				actor.setEstado("IDLE");
				this.Estado	=EstadosActividad.DONE.toString();				
				LOGGER.log(Level.SEVERE," Agente rechazado Nutrired: El agente no se encuentra en el contexto distrital");
			}	
			
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){	
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			//LOGGER.log(Level.INFO,this.toString()+" DONE: -DOING NOTHING");
		}
	}

	@Override
	public SistemaActividadHumana getInstance() {
		return new ConformarNutrired(this.operadorNutriRed) ;
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
		return ENUNCIADO;
	}

	@Override
	public Proposito getProposito() {
		return this.Proposito;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double getCosto() {
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