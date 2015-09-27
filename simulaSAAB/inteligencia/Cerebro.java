/**
 * 
 */
package simulaSAAB.inteligencia;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.impl.DefaultConfiguration;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import simulaSAAB.agentes.AgenteInteligente;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.global.persistencia.AgentTrackObservable;
import simulaSAAB.tareas.ProcesoAgenteHumano;
import simulaSAAB.tareas.ProducirCebollaBulbo;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * Representa el proceso de toma de decisión y evaluación de la experiencia por parte de los agentes, al ejecutar un <code>MPA</code>
 * @author lfgomezm
 *
 */
public class Cerebro {
	/**
	 * Registro de la clase usado para depuración <code>Debugging</code>
	 */
	private static Logger LOGGER = Logger.getLogger(Cerebro.class.getName());
	/**
	 * Instancia de <code>AgenteInteligente</code>
	 */
	protected AgenteInteligente Agente;

	public CerebroTrack OBSERVABLE = new CerebroTrack();
	
	private int lastIndex = -1;
	
	/**
	 * Constructor
	 */
	public Cerebro() {
		
	}
	
	public Cerebro(AgenteInteligente agnt){
		this.Agente = agnt;
	}
	
	/**
	 * Haciendo uso de algoritmos genéticos, itera las posibles actividades ejecutables y devuelve una solución.
	 * En caso que el agente no cuente con experiencia previa, escoge una actividad aleatoriamente. 
	 * @param Actividades conjunto de <code>MPA</code> ejecutables.
	 * @return Solucion actividad a ejecutar
	 */
	public SistemaActividadHumana tomarDecision(List<SistemaActividadHumana> Actividades){
		
		List<Experiencia> Experiencias 	= Agente.getExperiencia();
		Double MayorUtilidad			= Agente.getMayorUtilidadObtenida();
		List<MPAGene> Population 		= new ArrayList<MPAGene>();
		SistemaActividadHumana Solucion = null;
		Experiencia nuevaExp 			= null;
		
		//filtra experiencias utilizables para el listado de actividades
		for(Experiencia exp: Experiencias){
			
			SistemaActividadHumana Actividad = exp.getActividadEjecutada();
			
			if(Actividades.contains(Actividad)){
				Population.add(exp.getGene());
			}
		}
		
		/*
		 * si no tiene experiencia previa o solo es posible ejecutar una actividad, escoge la actividad al azar
		 * debe contener el mismo numero de experiencias que actividades viables
		 */
		if(Population.size()==0 || Population.size()!=Actividades.size()){	
			/*
			 * Intenta escoger una actividad al azar diferente a la ultima escogida
			 */
			int index = lastIndex;
			int breaker = 0;
			while(index==lastIndex){
				index = RandomHelper.nextIntFromTo(0, Actividades.size()-1);
				breaker++;
				if(breaker>15){
					LOGGER.info("index: "+index+" & last: "+lastIndex+"break");
					break;					
				}					
			}			 
			
			Solucion = Actividades.get(index);
			OBSERVABLE.tomaDecision(Solucion, false);
			lastIndex = index;
		}else{			
			//usa GA			
			try{
				
				Configuration conf = new DefaultConfiguration();
				conf.reset();//por extraÃ±a configuracion del JGAP
				ExperienciaAccion cicloexperiencia = new ExperienciaAccion();
				
				conf.setPopulationSize(Population.size());
				conf.setFitnessFunction(cicloexperiencia);
												
				/*
				 * Construye la poblacion de cromosomas donde buscar la solucion
				 */			
				Chromosome[] PopulationChromosome	= new Chromosome[Population.size()];
				int j=0;				
				for(Gene gn: Population){					
					Gene[] InitialGene 		= new Gene[1];
					PopulationChromosome[j]	= new Chromosome(conf,gn,1);
					j++;
				}
				/*
				 * asigna un chromosoma de ejemplo al config
				 */
				conf.setSampleChromosome(PopulationChromosome[0]);
				/*
				 * asigna el número de posibles soluciones a generar
				 */
				conf.setPopulationSize(500);
				/*
				 * Crea la poblacion
				 */ 
				Population soluciones		= new Population(conf,PopulationChromosome);
				/*
				 * Crea el genotipo				
				 */ 
				Genotype PosiblesSoluciones = new Genotype(conf,soluciones);
				PosiblesSoluciones.applyGeneticOperators();
				PosiblesSoluciones.evolve();
				/*
				 * Obtiene solucion 
				 */ 
				IChromosome FittestCromosoma= PosiblesSoluciones.getFittestChromosome();
				MPAGene FittestGen			= (MPAGene)FittestCromosoma.getGene(0);			
				Solucion 					= FittestGen.getMPA();							
				
			}catch(InvalidConfigurationException exc){//En caso de error en la ejecucion del GA escoge una actividad al azar
				Solucion = Actividades.get(RandomHelper.nextIntFromTo(0, Actividades.size()-1));
				LOGGER.log(Level.INFO, Agente.toString() + " CATCHED: "+ exc.getMessage());
			}
			OBSERVABLE.tomaDecision(Solucion, true);
		}
		
		return Solucion;
	}
	
	/**
	 * Implementa la funcion de adaptabilidad usada por los algorimos genéticos para establecer un puntaje a cada solución probada (actividad ejecutada)
	 * @return experiencia Experiencia calificada
	 */
	public  Experiencia evaluarExperiencia(){
		
		List<Experiencia> Experiencias 				= Agente.getExperiencia();
		SistemaActividadHumana actividadEjecutada 	= Agente.getActividadVigente();
		
		double UtilidadActual	= Agente.getUltimaUtilidadObtenida();
		double MayorUtilidad	= Agente.getMayorUtilidadObtenida();
		
		Experiencia experiencia	= null;
		
		//Busca experiencias anteriores del MPA.
		for(Experiencia exp: Experiencias){
			
			SistemaActividadHumana actividad = exp.getActividadEjecutada();
			
			if(actividadEjecutada.equals(actividad)){
				experiencia = exp;
				break;
			}
		}
		
		//Corrobora la ultima utilidad obtenida
		if(UtilidadActual>MayorUtilidad){
			Agente.setMayorUtilidadObtenida(UtilidadActual);
		}
		
		//Evalua la experiencia sobre el MPA
		if(experiencia==null){
			experiencia = new Experiencia(actividadEjecutada,new Dinero(UtilidadActual));
			Agente.addExperiencia(experiencia);
			experiencia.setAgente(Agente);
		}else{
			experiencia.addNumeroEjecuciones();
			experiencia.setUtilidadObtenida(new Dinero(UtilidadActual));
		}
		
		double concepto = UtilidadActual/MayorUtilidad;
		
		if(concepto>=0.9){
			experiencia.addNumeroEjecucionesExitosas();
		}
		
		experiencia.OBSERVABLE.experienciaActualizada();
		
		return experiencia;
	}
		
	
	public AgenteInteligente getAgente() {
		return Agente;
	}

	public void setAgente(AgenteInteligente agente) {
		Agente = agente;
	}
	
	/**
	 * Clase anidada a la que se le delega la funcionalidad <code>observable</code> de la clase <code>Cerebro</code>
	 * @author lfgomezm
	 *
	 */
	public class CerebroTrack extends AgentTrackObservable{
		
		private Double tick;
		private String agente;
		private String decision;
		private String usoGA;
		
		public CerebroTrack(){
			super();
		}
		/**
		 * Actualiza los valores y reporta a los observadores
		 * 
		 * @param decision <code>string</code>, decisión tomada por el agente
		 * @param usoGA <code>string</code>, identifica si la decisión fue tomada o no haciendo uso del algortimo genético
		 */
		public void tomaDecision(SistemaActividadHumana decision, boolean usoGA){
			
			this.tick			= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.agente			= Cerebro.this.Agente.toString();
			this.decision		= decision.toString();
			this.usoGA			= usoGA?"TRUE":"FALSE";
			
			super.setChanged();
			super.notifyObservers(this);
		}

		@Override
		public String dataLineString(String separador) {
						
			return tick+separador+agente+separador+decision+separador+usoGA+separador;
		}

		@Override
		public String dataLineStringHeader(String separador) {
			
			return "tick"+separador+"agente"+separador+"decision"+separador+"Uso_GA"+separador;
		}
		
	}

}