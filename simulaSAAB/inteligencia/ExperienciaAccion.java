package simulaSAAB.inteligencia;

import org.jgap.Chromosome;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

public class ExperienciaAccion extends FitnessFunction {

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
	 * Devuelve el numero de ejecuciones de la posible solucion
	 * @param solucion Chomosoma solucion potencial
	 * @return int numero de ejecuciones
	 */
	private int getNumeroEjecuciones(IChromosome solucion){
		
		MPAGene mpaSolucion 	= (MPAGene)solucion.getGene(0);
		int ejecuciones 		= mpaSolucion.getNumeroEjecuciones();
		
		return ejecuciones;
	}
	
	/**
	 * Devuelve el numero de ejecuciones exitosas de la posible solucion
	 * @param solucion Chomosoma solucion potencial
	 * @return int numero de ejecuciones
	 */
	private int getNumeroEjecucionesExitosas(IChromosome solucion){
		
		MPAGene mpaSolucion 	= (MPAGene)solucion.getGene(0);
		int exitos 		= mpaSolucion.getNumeroEjecucionesExitosas();
		
		return exitos;
	}


	
	

}
