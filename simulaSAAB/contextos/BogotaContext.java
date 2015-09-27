package simulaSAAB.contextos;

import repast.simphony.context.DefaultContext;
import simulaSAAB.global.VariablesGlobales;

/**
 * Representa el contexto geográfico relacionado a la ciudad de Bogotá presente en la simulación 
 * 
 * @author jdvelezg
 *
 */
public class BogotaContext extends DefaultContext<Object> {
	
	/**
	 * Constructor
	 */
	public BogotaContext(){
		super(VariablesGlobales.CONTEXTO_DISTRITAL);
	}

}
