package simulaSAAB.contextos;

import repast.simphony.context.DefaultContext;
import simulaSAAB.global.VariablesGlobales;

public class SISaabContext extends DefaultContext<Object> {
	
	/**
	 * Constructor
	 */
	public SISaabContext(){
		super(VariablesGlobales.CONTEXTO_SISAAB);
	}

}
