package simulaSAAB.agentes;

import simulaSAAB.comunicacion.Demanda;

public interface Demandante {
	
	/**
	 * Registra demandas de productos en el SAAB
	 */
	public Demanda generarDemanda();
	
	/**
	 * Realiza compra de productos mediante el SAAB a partir de las demandas registradas 
	 */
	public void realizarCompra();

}
