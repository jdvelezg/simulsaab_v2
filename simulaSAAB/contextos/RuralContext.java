/**
 * 
 */
package simulaSAAB.contextos;

import repast.simphony.context.DefaultContext;
import simulaSAAB.global.VariablesGlobales;

/**
 * Contexto para las ubicaciones rurales presentes en la simulación
 * 
 * @author jdvelezg
 *
 */
public class RuralContext extends DefaultContext<Object> {
	
	/**
	 * Constructor
	 */
	public RuralContext(){
		super(VariablesGlobales.CONTEXTO_RURAL);
	}


}
