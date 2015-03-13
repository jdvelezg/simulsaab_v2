package simulaSAAB.agentes;

public interface Demandante {
	
	/**
	 * Registra demandas de productos en el SAAB
	 */
	public void generarDemanda();
	
	/**
	 * Realiza compra de productos mediante el SAAB a partir de las demandas registradas 
	 */
	public void realizarCompra();

}
