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

public class ConformarNutrired implements SistemaActividadHumana<Demandante> {
	
	
	private static Logger LOGGER = Logger.getLogger(ConformarNutrired.class.getName());
	
	private static String ENUNCIADO = "Conformar nutrired";
	
	private Proposito Proposito;
	
	private String Estado;
	
	private int paso;
	
	private OperadorRedDemanda operadorNutriRed;

	/**
	 * Contructor
	 */
	public ConformarNutrired() {
		
		this.Estado 			= EstadosActividad.READY.toString();		
		this.operadorNutriRed 	= new OperadorRedDemanda();				
	}
	
	public ConformarNutrired(OperadorRedDemanda operador){
		
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
								
				//Agrega el actor en el Operador como agente representado
				//operadorNutriRed.addAgenteRepresentado(actor); // DEPRECATED, lee los edges del Network
				
				//crea una relacion entre el agente y el operador de demanda				
				SaabContextBuilder.NutriredesNetwork.addEdge(operadorNutriRed, actor);
				Estado	=EstadosActividad.DONE.toString();
				actor.setEstado("WAITING");
				/*
				 * Cambia intencion de consolidacion para que le agente no se asocie con otras redes
				 */
				actor.setIntencionConsolidacion(false);
				
			}else{
				this.Estado	=EstadosActividad.DONE.toString();
				actor.setEstado("IDLE");
				LOGGER.log(Level.SEVERE,this.toString()+" Agente rechazado Nutrired: El agente no se encuentra en el contexto distrital");
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

}
