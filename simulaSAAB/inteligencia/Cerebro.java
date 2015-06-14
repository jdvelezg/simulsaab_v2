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

import repast.simphony.random.RandomHelper;
import simulaSAAB.agentes.AgenteInteligente;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.tareas.ProcesoAgenteHumano;
import simulaSAAB.tareas.ProducirCebollaBulbo;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * @author dampher
 *
 */
public class Cerebro {
	
	private static Logger LOGGER = Logger.getLogger(Cerebro.class.getName());
	
	private AgenteInteligente Agente;
	
	
	/**
	 * Constructor
	 */
	public Cerebro() {
		// TODO Auto-generated constructor stub
	}
	
	public Cerebro(AgenteInteligente agnt){
		this.Agente = agnt;
	}
	
	/**
	 * Haciendo uso de Algoritmos geneticos, itera las posibles actividades ejecutables y devuelve una solución.
	 * En caso que el agente no cuente con experiencia previa, escoge una actividad aleatoriamente. 
	 * @param Actividades Conjunto de actividades ejecutables.
	 * @return
	 */
	public SistemaActividadHumana tomarDecision(List<SistemaActividadHumana> Actividades){
		
		List<Experiencia> Experiencias 	= Agente.getExperiencia();
		Double MayorUtilidad			= Agente.getMayorUtilidadObtenida();
		List<Gene> Population 			= new ArrayList();
		SistemaActividadHumana Solucion = null;
		
		//filtra experiencias utilizables para el listado de actividades
		for(Experiencia exp: Experiencias){
			
			SistemaActividadHumana Actividad = exp.getActividadEjecutada();
			
			if(Actividades.contains(Actividad)){
				Population.add(exp.getGene());
			}
		}
		
		//si no tiene experiencia previa o solo es posible ejecutar una actividad, escoge la actividad al azar
		//if(Population.size()==0 || Actividades.size()==1){
		if(Population.size()==0){
			//LOGGER.log(Level.INFO, this.toString() + " AVOID GA; NO EXPERIENCE");
			Solucion = Actividades.get(RandomHelper.nextIntFromTo(0, Actividades.size()-1));
		}else{	
			//LOGGER.log(Level.INFO, this.toString() + " IMPLEMENTA GA.");
			//usa GA			
			try{
				
				Configuration conf = new DefaultConfiguration();
				conf.reset();//por extraña configuracion del JGAP
				ExperienciaAccion cicloexperiencia = new ExperienciaAccion();
				
				conf.setPopulationSize(Population.size());
				conf.setFitnessFunction(cicloexperiencia);
												
				//Crea array de cromosomas				
				Chromosome[] InitialChromosome	= new Chromosome[Population.size()];
				int j=0;
				for(Gene gn: Population){					
					Gene[] InitialGene 	= new Gene[1];
					InitialGene[0] 		= gn;
					InitialChromosome[j]= new Chromosome(conf,InitialGene);
					j++;
				}				
				conf.setSampleChromosome(InitialChromosome[0]);
				
				Population soluciones			= new Population(conf,InitialChromosome);
								
			
				Genotype PosiblesSoluciones = new Genotype(conf,soluciones);			
				IChromosome FittestCromosoma= PosiblesSoluciones.getFittestChromosome();
				MPAGene FittestGen			= (MPAGene)FittestCromosoma.getGene(0);			
				Solucion 					= FittestGen.getMPA();
				
			}catch(InvalidConfigurationException exc){//En caso de error en la ejecucion del GA escoge una actividad al azar
				Solucion = Actividades.get(RandomHelper.nextIntFromTo(0, Actividades.size()-1));
				LOGGER.log(Level.INFO, this.toString() + " CATCHED: "+ exc.getMessage());
			}			
		}
		LOGGER.log(Level.INFO, "Actor: "+this.Agente.toString()+" ejecuta: "+Solucion.toString());
		return Solucion;
	}
	
	/**
	 * Implementa la funcion de adaptabilidad usada por los algorimos gneticos para establecer un puntaje a cada solución probada (Actividad ejecutada).
	 * @return
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
			experiencia = new Experiencia(actividadEjecutada,new Dinero(Agente.getUltimaUtilidadObtenida()));
			Agente.addExperiencia(experiencia);
		}else{
			experiencia.addNumeroEjecuciones();
			experiencia.setUtilidadObtenida(new Dinero(Agente.getUltimaUtilidadObtenida()));
		}
		
		double concepto = UtilidadActual/MayorUtilidad;
		
		if(concepto>=0.9){
			experiencia.addNumeroEjecucionesExitosas();
		}
		
		
		return experiencia;
	}

	
	
	public AgenteInteligente getAgente() {
		return Agente;
	}

	public void setAgente(AgenteInteligente agente) {
		Agente = agente;
	}
	
	

}
