package simulaSAAB.inteligencia;

import org.jgap.Chromosome;
import org.jgap.FitnessFunction;

public class ExperienciaAccion extends FitnessFunction {

	public ExperienciaAccion() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int evaluate(Chromosome arg0) {
		// TODO Auto-generated method stub
		
		int ejecucionesExitosas 	= getNumeroEjecucionesExitosas(arg0);
		int totalEjecuciones		= getNumeroEjecuciones(arg0);
		
		int fitness	= (ejecucionesExitosas/totalEjecuciones)*100;
		
		return fitness;
	}
	
	
	/**
	 * Devuelve el numero de ejecuciones de la posible solucion
	 * @param solucion Chomosoma solucion potencial
	 * @return int numero de ejecuciones
	 */
	private int getNumeroEjecuciones(Chromosome solucion){
		
		MPAGene mpaSolucion 	= (MPAGene)solucion.getGene(0);
		int ejecuciones 		= mpaSolucion.getNumeroEjecuciones();
		
		return ejecuciones;
	}
	
	/**
	 * Devuelve el numero de ejecuciones exitosas de la posible solucion
	 * @param solucion Chomosoma solucion potencial
	 * @return int numero de ejecuciones
	 */
	private int getNumeroEjecucionesExitosas(Chromosome solucion){
		
		MPAGene mpaSolucion 	= (MPAGene)solucion.getGene(0);
		int exitos 		= mpaSolucion.getNumeroEjecucionesExitosas();
		
		return exitos;
	}
	

}
