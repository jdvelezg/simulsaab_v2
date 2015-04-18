package simulaSAAB.agentes;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Oferta;

public class SiSaab implements AgenteReactivo {
	
	private Context<Object> SISAABContext;
	
	public static Network<Object> SISAABNetwork;
	
	public static Context<Object> ComercialContext;
	
	public static Network<Object> ComercialNetwork;
	
	public static Context<Object> OrdenesContext;
	
	
	/**
	 * Constructor
	 */
	public SiSaab() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Genera orden de pedido que relacina una oferta con una demanda y calcula los costos logísticos asociados al mismo
	 * implica la generación de una orden de servicio logistico y una orden de compra 
	 * 
	 * @param oferta Objeto Oferta relacionado en la orden de pedido
	 * @param demanda Objeto de demanda relacionado en la orden de pedido
	 */
	private void generarOrdenDePedido(Oferta oferta, Demanda demanda){
		
	}
	
	/**
	 * Genera la orden de servicio logístico 
	 * 
	 * @param oferta Objeto Oferta relacionado al servicio logistico
	 * @param demanda Objeto de demanda relacionado al servicio logistico
	 */
	private void generarOrdenDeServicioLogistico(Oferta oferta, Demanda demanda){
		
	}
	
	/**
	 * Genera orden de Compra para una oferta y demanda registradas en el SAAB
	 * 
	 * @param oferta Objeto oferta ligado a la orden de compra
	 * @param demanda Objeto demanda ligado a la orden de compra
	 */
	private void generarOrdenDeCompra(Oferta oferta, Demanda demanda){
		
	}
	
	/**
	 * Registra una oferta en el sistema para ser tomada en cuenta en las transacciones de comercialziación.
	 * @param oferta Oferta a registrar
	 */
	public void registrarOferta(Oferta oferta){
		
	}
	
	/**
	 * Reigstra una demanda en el sistema para ser toamda en cuenta en las transacciones de comercialziación. 
	 * @param demanda
	 */
	public void registrarDemanda(Demanda demanda){
		
	}
	
	/**
	 * Permite la compra de productos relacionando una oferta y una demanda registrada
	 * @param offer Oferta relacinada en la compra
	 * @param demand Demanda relacionada en la compra
	 */
	public void RealizarCompra(Oferta offer, Demanda demand){
		
	}


	public Context<Object> getSISAABContext() {
		return SISAABContext;
	}


	public void setSISAABContext(Context<Object> sISAABContext) {
		SISAABContext = sISAABContext;
	}


	public static Network<Object> getSISAABNetwork() {
		return SISAABNetwork;
	}


	public static void setSISAABNetwork(Network<Object> sISAABNetwork) {
		SISAABNetwork = sISAABNetwork;
	}


	public static Context<Object> getComercialContext() {
		return ComercialContext;
	}


	public static void setComercialContext(Context<Object> comercialContext) {
		ComercialContext = comercialContext;
	}


	public static Network<Object> getComercialNetwork() {
		return ComercialNetwork;
	}


	public static void setComercialNetwork(Network<Object> comercialNetwork) {
		ComercialNetwork = comercialNetwork;
	}


	public static Context<Object> getOrdenesContext() {
		return OrdenesContext;
	}


	public static void setOrdenesContext(Context<Object> ordenesContext) {
		OrdenesContext = ordenesContext;
	}
	
	

}
