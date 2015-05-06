package simulaSAAB.agentes;

import simulaSAAB.comunicacion.Demanda;

public interface Demandante {
	
	/**
	 * Registra demandas de productos en el SAAB
	 */
	Demanda generarDemanda();
	
	/**
	 * Realiza compra de productos mediante el SAAB a partir de las demandas registradas 
	 */
	void realizarCompra();
	
	/**
	 * Devuelve <code>true</code> si el agente esta dispuesto a consolidar sus demandas
	 * con otro(s) agente(s) <code>Demandante</code>(s)
	 * @return
	 */
	boolean intencionDeConsolidacion();
	
	/**
	 * Modifica el valor que define si el agente esta dispuesto a consolidar sus demandas
	 */
	void setIntencionConsolidacion(boolean bool);
	
	

}
