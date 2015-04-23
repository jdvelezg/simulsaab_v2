package simulaSAAB.contextos;

import repast.simphony.context.DefaultContext;
import simulaSAAB.contextos.environment.Junction;
import simulaSAAB.global.VariablesGlobales;

public class JunctionContext extends DefaultContext<Junction> {
	
	/**
	 * Constructor
	 */
	public JunctionContext(){
		super(VariablesGlobales.CONTEXTO_JUNCTIONS);
	}
}
