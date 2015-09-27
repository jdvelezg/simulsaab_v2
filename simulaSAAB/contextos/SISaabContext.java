package simulaSAAB.contextos;

import repast.simphony.context.DefaultContext;
import simulaSAAB.global.VariablesGlobales;
/**
 * Contexto para la instancia del sistema de información del SAAB
 * 
 * @author dampher
 *
 */
public class SISaabContext extends DefaultContext<Object> {
	
	/**
	 * Constructor
	 */
	public SISaabContext(){
		super(VariablesGlobales.CONTEXTO_SISAAB);
	}

}
