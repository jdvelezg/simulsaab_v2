package simulaSAAB.contextos;

import repast.simphony.context.DefaultContext;
import simulaSAAB.contextos.environment.Junction;
import simulaSAAB.global.VariablesGlobales;
/**
 * Contexto para las uniones de vías presentes en la simulación
 * 
 * @author jdvelezg
 *
 */
public class JunctionContext extends DefaultContext<Junction> {
	
	/**
	 * Constructor
	 */
	public JunctionContext(){
		super(VariablesGlobales.CONTEXTO_JUNCTIONS);
	}
}
