package simulaSAAB.contextos;

import repast.simphony.context.DefaultContext;
import simulaSAAB.global.VariablesGlobales;
/**
 * Contexto para las vías presentes en la simulación
 * 
 * @author jdvelezg
 *
 */
public class RoadContext extends DefaultContext<Object> {
	
	/**
	 * Constructor
	 */
	public RoadContext(){
		super(VariablesGlobales.CONTEXTO_RUTAS);
	}

}
