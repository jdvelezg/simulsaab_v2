package simulaSAAB.agentes;

import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.PlazaDistrital;

public interface Demandante extends AgenteInteligente{
	
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
	
	/**
	 * Devuelve True si el agente tiene pedidos recibidos en cola
	 * Es usado por el sistema de actividad humana para verificar si el ciclo
	 * de compra ha finalizado
	 * @return
	 */
	boolean pedidosRecibidos();
	
	/**
	 * Gestiona los pedidos recibidos.
	 * En caso de ser un operador de red, debe distribuir el pedido en sus agentes
	 * representados. Si es un demandante individual, los almacena en su tienda para
	 * poder ser vendidos.
	 */
	void gestionarPedidos();
	
	/**
	 * Distribuye los productos comprados en sus tiendas
	 * @param productos
	 */
	void distribuirProductosEnTienda(Recurso productos); 
	
	/**
	 * Fija el punto de demanda que el demandante usa en sus registros de demanda
	 * @param plaza
	 */
	void setPuntoDemanda(PlazaDistrital plaza);
	
	/**
	 * Devuelve el punto de demanda del agente
	 * @return
	 */
	PlazaDistrital getPuntoDemanda();
	
	/**
	 * Devuleve el dinero del Demandante
	 * @return
	 */
	Dinero getDinero();

}
