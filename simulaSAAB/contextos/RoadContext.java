package simulaSAAB.contextos;

import repast.simphony.context.DefaultContext;
import simulaSAAB.global.VariablesGlobales;

public class RoadContext extends DefaultContext<Object> {
	
	/**
	 * Constructor
	 */
	public RoadContext(){
		super(VariablesGlobales.CONTEXTO_RUTAS);
	}

}
