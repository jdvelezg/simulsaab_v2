package simulaSAAB.contextos;

import repast.simphony.context.DefaultContext;
import simulaSAAB.global.VariablesGlobales;

public class TransaccionContext extends DefaultContext<Object> {

	public TransaccionContext() {
		super(VariablesGlobales.CONTEXTO_TRANSACCIONES);
	}


}
