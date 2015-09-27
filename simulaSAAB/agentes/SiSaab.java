package simulaSAAB.agentes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import bsh.This;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.graph.Network;
import repast.simphony.util.collections.IndexedIterable;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.OrdenDeCompra;
import simulaSAAB.comunicacion.OrdenDePedido;
import simulaSAAB.comunicacion.OrdenDeServicio;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.PlazaDistrital;
import simulaSAAB.contextos.SaabContextBuilder;

/**
 * Representa el agente <code>Sistema de Información del SAAB - SISAAB</code>
 * <p>
 * Agente reactivo que representa una instancia del sistema de información del SAAB, el cual permite a los agentes <code>oferentes</code> y <code>demandantes</code> 
 * comercializar productos 
 *   
 * @author jdvelezg
 *
 */
public abstract class SiSaab implements AgenteReactivo {

	/**
	* Registro de la clase usado para depuración <code>Debugging</code>
	*/
	private static Logger LOGGER = Logger.getLogger(SiSaab.class.getName());
	/**
	 * Registro de ordenes de pedido
	 */		
	//Queue<OrdenDeCompra> ordenesPedido;
	
	/**
	 * Devuelve orden de pedido que relaciona una oferta con una demanda y calcula los costos logísticos asociados al mismo, 
	 * implica la generación de una orden de servicio logistico y una orden de compra 
	 * 
	 * @param oferta Objeto Oferta relacionado en la orden de pedido
	 * @param demanda Objeto de demanda relacionado en la orden de pedido
	 */
	private synchronized static void generarOrdenDePedido(Demanda demanda, List<OrdenDeCompra> ordenescompra, List<OrdenDeServicio> ordenesservicio){
		
		//cambia estado de la demanda como atendida
		//demanda.atendida();
		//Crea la orden de pedido Unitaria
		OrdenDePedido ordenPedido = new OrdenDePedido(demanda, ordenescompra, ordenesservicio);
		ordenPedido.OBSERVABLE.addObserver(SaabContextBuilder.OBSERVADOR);
		ordenPedido.calcularCostoLogistico();
		
		//Guarda la orden de pedido en el contexto de Ordenes, para que se atendida por un agente OperadorLogistico
		//SaabContextBuilder.OrdenesContext.add(ordenPedido);
	}
	
	/**
	 * Devuelve una orden de servicio logístico 
	 * 
	 * @param oferta 
	 * @param demanda 
	 * 
	 * @param puntoOferta <code>centroUrbano</code> que representa el punto de oferta al que pertenecen las <code>ofertas</code> relacionadas
	 * @param ofertas <code>Ofertas</code> relacionadas al servicio logistico
	 * @param demanda <code>Demanda</code> relacionada al servicio logistico
	 * @return OrdenDeServicio
	 */
	private synchronized static OrdenDeServicio generarOrdenDeServicioLogistico(CentroUrbano puntoOferta, List<OrdenDeCompra> ofertas, Demanda demanda){
		
		OrdenDeServicio servicioLogistico = new OrdenDeServicio(puntoOferta, ofertas, demanda);	
		//servicioLogistico.OBSERVABLE.addObserver(SaabContextBuilder.OBSERVADOR);
		SaabContextBuilder.OrdenesContext.add(servicioLogistico);

		return servicioLogistico;
	}
	
	/**
	 * Devuelve una orden de Compra para una oferta y demanda registradas en el SAAB
	 * 
	 * @param oferta <code>oferta</code> ligado a la orden de compra
	 * @param demanda <code>demanda</code> ligado a la orden de compra
	 * @return OrdenDeCompra
	 */
	private synchronized static OrdenDeCompra generarOrdenDeCompra(Oferta oferta, Demanda demanda){
		
		OrdenDeCompra orden = new OrdenDeCompra(oferta,demanda);
		//orden.OBSERVABLE.addObserver(SaabContextBuilder.OBSERVADOR);
		//la extrae del contexto comercial y la registra en el contexto de transacciones.
		SaabContextBuilder.ComercialContext.remove(oferta);
		SaabContextBuilder.TransaccionContext.add(oferta);
		//crea un edge para representar la oferta
		//SaabContextBuilder.ComercialNetwork.addEdge(oferta,demanda,oferta.getPrecio());
				
		return orden;
	}
	
	/**
	 * Registra una oferta en el sistema para ser tomada en cuenta en las transacciones de comercialziación.
	 * @param oferta <code>Oferta</code> a registrar
	 */
	public synchronized static void registrarOferta(Oferta oferta){
		
		SaabContextBuilder.ComercialContext.add(oferta);
		oferta.setVigente();
		/*
		 * Crea un objeto Schedule y agrega el objeto para que su metodo step sea ejecutado 
		 * en cada tick de la simulación
		 */
		double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		double start = tick < 1 ? 1 : tick + 2;
		int interval = 1;
		
		ScheduleParameters params = ScheduleParameters.createRepeating(start,interval);
		ISchedule sch = RunEnvironment.getInstance().getCurrentSchedule();
		sch.schedule(params, oferta, "step");
	}
	
	/**
	 * Registra una demanda en el sistema para ser tomada en cuenta en las transacciones de comercialización. 
	 * @param demanda demanda a ser registrada en el sistema
	 */
	public synchronized static void registrarDemanda(Demanda demanda){
		
		if(demanda.getPresupuesto()>0){
			SaabContextBuilder.ComercialContext.add(demanda);
			demanda.setVigente();
		}
		
		/*
		 * Crea un objeto Schedule y agrega el objeto para que su metodo step sea ejecutado 
		 * en cada tick de la simulación
		 */
		/*double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		double start = tick < 1 ? 1 : tick + 2;
		int interval = 1;
		
		ScheduleParameters params = ScheduleParameters.createRepeating(start,interval);
		ISchedule sch = RunEnvironment.getInstance().getCurrentSchedule();
		sch.schedule(params, demanda, "step");*/
	}
	
	/**
	 * Devuelve las ofertas registradas para el producto pasado como parametro
	 * @param producto producto requerido
	 * @param asc booleano de ser <code>true</code> la lista de ofertas es devuelta en forma ascendente, de ser <code>false</code> de forma descendente
	 * @return List<Oferta>
	 */
	public synchronized static List<Oferta> ofertasRegistradas(String producto, boolean asc){
		
		//Obtiene ofertas registradas
		List<Oferta> ofertas 			= new ArrayList<Oferta>();				
		IndexedIterable<Object> offers 	= SaabContextBuilder.ComercialContext.getObjects(Oferta.class);
	
		//filtra ofertas de interés para la demanda
		for(int i=0; i<offers.size();i++){
			
			Oferta o = (Oferta)offers.get(i);
			String p = o.getProducto().getNombre();
			if(o.vigente() && p.equalsIgnoreCase(producto))
				ofertas.add(o); 
		}//end for
		
		//Orden las ofertas en forma ascendente(asc=true)/descentende(asc=false)
		//Collections.sort(ofertas,asc?new OfertaPrecioComparator():Collections.reverseOrder(new OfertaPrecioComparator()));
	
		return ofertas;
	}
	
	/**
	 * Permite la compra de productos relacionando una oferta y una demanda registrada
	 * @param offer listado de <code>Ofertas</code> relacionadas en la compra
	 * @param demanda Demanda relacionada en la compra
	 */
	public synchronized static void realizarCompra(List<Oferta> offer, Demanda demanda){
		
		Queue<Oferta> offersq 							 = new ConcurrentLinkedDeque<Oferta>();
		offersq.addAll(offer);
		Map<CentroUrbano,List<OrdenDeCompra>> OfertasMap = new ConcurrentHashMap<CentroUrbano,List<OrdenDeCompra>>();
		List<OrdenDeCompra> ordenesCompra				 = new ArrayList<OrdenDeCompra>();
		List<OrdenDeServicio> ordenesServicio			 = new ArrayList<OrdenDeServicio>();
		Demandante demandante						 	 = demanda.getComprador();
		Dinero dineroDemandante							 = demandante.getDinero();
		
		
		while(!offersq.isEmpty()){
			
			Oferta of = offersq.poll();
			//realiza la transacción comercial entre el oferente y el demandante
			Dinero dinerOferente 	= of.getVendedor().getDinero();
			Double monto 			= new Double(of.getPrecio());
			
			dineroDemandante.subtractCantidad(monto.doubleValue());
			dinerOferente.addCantidad(monto.doubleValue());			
			//confirma la oferta como vendida.
			of.setVendida();
			demanda.setAtendida();
			//genera una orden de compra por cada Oferta
			OrdenDeCompra ordenCompra = generarOrdenDeCompra(of,demanda);
			ordenesCompra.add(ordenCompra);			
			/*
			 * Organiza por punto de Oferta
			 * El punto de oferta es genérico, referencia el centro urbano
			 * 
			 * TODO los puntos de oferta deben ser las fincas registradas en el SISAAB
			 */
			if(!OfertasMap.containsKey(of.getPuntoOferta())){
				
				ArrayList<OrdenDeCompra> value = new ArrayList<OrdenDeCompra>();
				value.add(ordenCompra);
				OfertasMap.put(of.getPuntoOferta(), value);
				
			}else{
				
				List<OrdenDeCompra> value = OfertasMap.get(of.getPuntoOferta());
				value.add(ordenCompra);
			}
			
			
		}//end for	
		
		//Genera Orden de servicio logistico por punto de Oferta
		Iterator<CentroUrbano> iter = OfertasMap.keySet().iterator();
		while(iter.hasNext()){			
			
			CentroUrbano puntoOferta 	= iter.next();
			ordenesServicio.add(generarOrdenDeServicioLogistico(puntoOferta, OfertasMap.get(puntoOferta), demanda));
			
		}
				
		//Genera Orden de pedido Unitaria
		generarOrdenDePedido(demanda, ordenesCompra, ordenesServicio);
	}
	

}


/**
 * Clase anidada que permite la comparación de dos objetos <code>Oferta</code> para ser organizada al implementar la clase abstracta Collection
 * 
 * @author jdvelezg
 *
 */
class OfertaPrecioComparator implements Comparator<Oferta>{

	@Override
	public int compare(Oferta arg0, Oferta arg1) {
		// TODO Auto-generated method stub
		return Double.compare(arg0.getPrecio(), arg1.getPrecio());		
	}	
}
