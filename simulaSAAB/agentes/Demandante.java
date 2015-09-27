package simulaSAAB.agentes;

import java.util.List;

import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.OrdenDeCompra;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.PlazaDistrital;

/**
 * Representa un Agente Demandante
 * <p>
 * Agente con capacidad para generar ofertas de productos
 * 
 * @author jdvelezg
 *
 */
public interface Demandante extends AgenteInteligente{
	
	/**
	 * Genera una {@link Demanda} de un {@link Producto}
	 */
	Demanda generarDemanda();
	
	/**
	 * Compra productos en el SISAAB partiendo de sus demandas registradas 
	 */
	void realizarCompra();
	
	/**
	 * Devuelve <code>true</code> si el agente esta dispuesto a consolidar sus demandas con otro(s) agente(s) <code>Demandante</code>(s)
	 * @return boolean
	 */
	boolean intencionDeConsolidacion();
	
	/**
	 * Modifica el valor que define si el agente esta dispuesto o no a consolidar sus demandas
	 * @param bool valor a asignar al agente
	 */
	void setIntencionConsolidacion(boolean bool);
	
	/**
	 * Devuelve <code>true</code> si el agente tiene pedidos pendientes
	 * <p>
	 * Es usado por varios {@link SistemaActividadHumna} para verificar si el ciclo de compra ha finalizado
	 * 
	 * @return boolean
	 */
	boolean pedidosRecibidos();
	
	/**
	 * Gestiona los pedidos recibidos.
	 * <p>
	 * En caso de ser un operador de red, distribuye el pedido en los agentes representados
	 * <p>
	 * Si es un demandante individual, almacena los <code>Recursos</code> recibidos en su tienda para ser vendidos
	 */
	void gestionarPedidos();
	
	/**
	 * Distribuye los productos {@link Recurso} comprados en sus tiendas
	 * @param productos recurso, productos a distribuir en la tienda
	 * @param precioCompra double precio de compra de los productos
	 */
	void distribuirProductosEnTienda(Recurso productos, double precioCompra); 
	
	/**
	 * Asigna el punto de demanda del <code>Demandante</code>
	 * @param plaza PlazaDistrital que representa el punto de demanda
	 */
	void setPuntoDemanda(PlazaDistrital plaza);
	
	/**
	 * Devuelve el punto de demanda del agente
	 * @return PlazaDistrital
	 */
	PlazaDistrital getPuntoDemanda();
	
	/**
	 * Devuleve el dinero del Demandante
	 * @return Dinero
	 */
	Dinero getDinero();
	
	/**
	 * Recibe las compras realizadas a través del SISAAB
	 * @param compras compras realizadas por el agente
	 */
	void recibirPedido(List<OrdenDeCompra> compras);
	
	/**
	 * Indica si el agente posee algun producto para la venta 
	 * @return booelan <code>true</code> si posee aún productos
	 */
	boolean productosPendientesVenta();

}
