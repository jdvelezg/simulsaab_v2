package simulaSAAB.global.persistencia;

import java.util.Observable;

import simulaSAAB.contextos.SaabContextBuilder;
/**
 * Representa un objeto que puede ser observado, hereda de <code>observable</code>  
 * @author lfgomezm
 *
 */
public abstract class AgentTrackObservable extends Observable {
	/**
	 * Constructor
	 */
	public AgentTrackObservable() {
		super();
		addObserver(SaabContextBuilder.OBSERVADOR);
	}
	
	/**
	 * Devuelve una cadena de texto con los datos separados por el caracter pasado como parámetro.
	 * 
	 * @param separador Caracter(es) separador de los datos.
	 * @return Cadena con datos separados por <code>separador</code>
	 */
	public abstract String dataLineString(String separador);
	
	public abstract String dataLineStringHeader(String separador);

}