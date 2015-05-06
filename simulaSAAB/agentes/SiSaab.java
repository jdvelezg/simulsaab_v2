package simulaSAAB.agentes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import bsh.This;
import repast.simphony.context.Context;
import repast.simphony.space.graph.Network;
import repast.simphony.util.collections.IndexedIterable;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.OrdenDeCompra;
import simulaSAAB.comunicacion.OrdenDePedido;
import simulaSAAB.comunicacion.OrdenDeServicio;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.PlazaDistrital;
import simulaSAAB.contextos.SaabContextBuilder;

public abstract class SiSaab implements AgenteReactivo {
	
	/**
	 * Referencia interna a los contextos propios del SISAAB
	 */
	private static final Context<Object> SISAABContext 		= SaabContextBuilder.SISAABContext;
	
	private static final Network<Object> SISAABNetwork 		= SaabContextBuilder.SISAABNetwork;
	
	private static final Context<Object> ComercialContext 	= SaabContextBuilder.ComercialContext;
	
	private static final Network<Object> ComercialNetwork 	= SaabContextBuilder.ComercialNetwork;
	
	private static final Context<Object> TransaccionContext = SaabContextBuilder.TransaccionContext;
	
	private static final Context<Object> OrdenesContext		= SaabContextBuilder.OrdenesContext;
	
	
	/**
	 * Genera orden de pedido que relacina una oferta con una demanda y calcula los costos logísticos asociados al mismo
	 * implica la generación de una orden de servicio logistico y una orden de compra 
	 * 
	 * @param oferta Objeto Oferta relacionado en la orden de pedido
	 * @param demanda Objeto de demanda relacionado en la orden de pedido
	 */
	private static void generarOrdenDePedido(Demanda demanda, List<OrdenDeCompra> ordenescompra, List<OrdenDeServicio> ordenesservicio){
		
		//cambia estado de la demanda como atendida
		demanda.atendida();
		//Crea la orden de pedido Unitaria
		OrdenDePedido ordenPedido = new OrdenDePedido(demanda, ordenescompra, ordenesservicio);
		
		//Guarda la orden de pedido en el contexto de Ordenes, para que se atendida por un agente OperadorLogistico
		OrdenesContext.add(ordenPedido);
	}
	
	/**
	 * Genera la orden de servicio logístico 
	 * 
	 * @param oferta Objeto Oferta relacionado al servicio logistico
	 * @param demanda Objeto de demanda relacionado al servicio logistico
	 */
	private static OrdenDeServicio generarOrdenDeServicioLogistico(CentroUrbano puntoOferta, List<Oferta> ofertas, Demanda demanda){
		
		OrdenDeServicio servicioLogistico = new OrdenDeServicio(puntoOferta, ofertas, demanda);
		
		
		return servicioLogistico;
	}
	
	/**
	 * Genera orden de Compra para una oferta y demanda registradas en el SAAB
	 * 
	 * @param oferta Objeto oferta ligado a la orden de compra
	 * @param demanda Objeto demanda ligado a la orden de compra
	 */
	private static OrdenDeCompra generarOrdenDeCompra(Oferta oferta, Demanda demanda){
		
		OrdenDeCompra orden = new OrdenDeCompra(oferta,demanda);
		//la extrae del contexto comercial y la registra en el contexto de transacciones.
		ComercialContext.remove(oferta);
		TransaccionContext.add(oferta);
		//crea un edge para representar la oferta
		ComercialNetwork.addEdge(oferta,demanda,oferta.getPrecio());
		
		return orden;
	}
	
	/**
	 * Registra una oferta en el sistema para ser tomada en cuenta en las transacciones de comercialziación.
	 * @param oferta Oferta a registrar
	 */
	public synchronized static void registrarOferta(Oferta oferta){
		
		ComercialContext.add(oferta);
		oferta.setVigente();
	}
	
	/**
	 * Registra una demanda en el sistema para ser toamda en cuenta en las transacciones de comercialziación. 
	 * @param demanda
	 */
	public synchronized static void registrarDemanda(Demanda demanda){
		
		ComercialContext.add(demanda);
		demanda.setVigente();
	}
	
	/**
	 * Devuelve las ofertas registradas para el producto pasado como parametro
	 * @param producto
	 * @return List<Ofertas>
	 */
	public synchronized static List<Oferta> ofertasRegistradas(String producto){
		
		//Obtiene ofertas registradas
		List<Oferta> ofertas 			= new ArrayList<Oferta>();				
		IndexedIterable<Object> offers 	= ComercialContext.getObjects(Oferta.class);
		
		//filtra ofertas de interés para la demanda
		for(int i=0; i<offers.size();i++){
			
			Oferta o = (Oferta)offers.get(i);
			
			if(o.getProducto().equals(producto))
				ofertas.add(o);
		}//end for
		
		return ofertas;
	}
	
	/**
	 * Permite la compra de productos relacionando una oferta y una demanda registrada
	 * @param offer Oferta relacinada en la compra
	 * @param demand Demanda relacionada en la compra
	 */
	public synchronized static void realizarCompra(List<Oferta> offer, Demanda demanda){
		
		Map<CentroUrbano,List<Oferta>> OfertasMap	= new ConcurrentHashMap<CentroUrbano,List<Oferta>>();
		List<OrdenDeCompra> ordenesCompra			= new ArrayList<OrdenDeCompra>();
		List<OrdenDeServicio> ordenesServicio		= new ArrayList<OrdenDeServicio>();
		
		for(Oferta of: offer){
			
			//confirma la oferta como vendida.
			of.vendida();
			//genera una orden de compra 			
			ordenesCompra.add(generarOrdenDeCompra(of,demanda));
			
			//Organiza por punto de Oferta
			if(!OfertasMap.containsKey(of.getPuntoOferta())){
				
				ArrayList<Oferta> value = new ArrayList<Oferta>();
				value.add(of);
				OfertasMap.put(of.getPuntoOferta(), value);
				
			}else{
				
				List<Oferta> value = OfertasMap.get(of.getPuntoOferta());
				value.add(of);
			}
				
		}//end for	
		
		//Genera Orden de servicio logistico por punto de Oferta
		Iterator<CentroUrbano> iter = OfertasMap.keySet().iterator();
		while(iter.hasNext()){			
			
			CentroUrbano puntoOferta = iter.next();
			ordenesServicio.add(generarOrdenDeServicioLogistico(puntoOferta, OfertasMap.get(puntoOferta), demanda));
			
		}
				
		//Genera Orden de pedido Unitaria
		generarOrdenDePedido(demanda, ordenesCompra, ordenesServicio);
	}	
	
	

}


/**
 * Esta clase permite la comparación de dos objetos Oferta para su organización de una lista mediante la clase abstracta Collection
 * @author dampher
 *
 */
class OfertaPrecioComparator implements Comparator<Oferta>{

	@Override
	public int compare(Oferta arg0, Oferta arg1) {
		// TODO Auto-generated method stub
		return Double.compare(arg0.getPrecio(), arg1.getPrecio());		
	}

	
	
}
