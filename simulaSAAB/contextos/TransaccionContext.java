package simulaSAAB.contextos;

import repast.simphony.context.DefaultContext;
import simulaSAAB.global.VariablesGlobales;
/**
 * Contexto para las transacciones del sistema de información del SAAB
 * 
 * @author jdvelezg
 *
 */
public class TransaccionContext extends DefaultContext<Object> {

	public TransaccionContext() {
		super(VariablesGlobales.CONTEXTO_TRANSACCIONES);
	}


}
