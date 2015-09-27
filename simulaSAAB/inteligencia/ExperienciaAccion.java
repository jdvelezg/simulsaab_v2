package simulaSAAB.inteligencia;

import org.jgap.Chromosome;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;
/**
 * Representa el proceso de evaluación de la experiencia tras la ejecución de un <code>MPA</code> por parte de un agente
 * @author lfgomezm
 *
 */
public class ExperienciaAccion extends FitnessFunction {
	
	/**
	 * Constructor
	 */
	public ExperienciaAccion() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected double evaluate(IChromosome arg0) {		
		
		int ejecucionesExitosas 	= getNumeroEjecucionesExitosas(arg0);
		int totalEjecuciones		= getNumeroEjecuciones(arg0);
		
		int fitness	= (ejecucionesExitosas/totalEjecuciones)*100;
		
		return new Double(fitness);
	}
	
	
	/**
	 * Devuelve el número de ejecuciones de la posible solución
	 * @param solucion Cromosoma solución potencial
	 * @return int número de ejecuciones
	 */
	private int getNumeroEjecuciones(IChromosome solucion){
		
		MPAGene mpaSolucion 	= (MPAGene)solucion.getGene(0);
		int ejecuciones 		= mpaSolucion.getNumeroEjecuciones();
		
		return ejecuciones;
	}
	
	/**
	 * Devuelve el número de ejecuciones exitosas de la posible solución
	 * @param solucion Cromosoma solución potencial
	 * @return int número de ejecuciones
	 */
	private int getNumeroEjecucionesExitosas(IChromosome solucion){
		
		MPAGene mpaSolucion 	= (MPAGene)solucion.getGene(0);
		int exitos 		= mpaSolucion.getNumeroEjecucionesExitosas();
		
		return exitos;
	}


	
	

}