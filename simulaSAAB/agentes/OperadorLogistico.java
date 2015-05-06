package simulaSAAB.agentes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.graph.ShortestPath;
import repast.simphony.util.collections.IndexedIterable;
import simulaSAAB.comunicacion.OrdenDeServicio;
import simulaSAAB.contextos.AmbienteLocal;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.NodoSaab;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.contextos.environment.Junction;
import simulaSAAB.tareas.RecolectarProductos;
import simulaSAAB.tareas.SistemaActividadHumana;
import simulaSAAB.tareas.Moverse;
import simulaSAAB.tareas.Transitar;
import bsh.This;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class OperadorLogistico implements AgenteReactivo {
	
	private SistemaActividadHumana TransportarProductos;
	
	private SistemaActividadHumana Moverse;
	
	public static Context<Object> SISAABContext = SaabContextBuilder.SISAABContext;
	
	private final Geography<Object> SAABGeography = SaabContextBuilder.SAABGeography;
	
	public final Context<Object> OrdenesContext = SaabContextBuilder.OrdenesContext;
	
	/**
	 * Constructor
	 */
	public OperadorLogistico() {
		
	}
	
	/**
	 * 
	 */
	@ScheduledMethod (start = 1, interval = 24)
	public void step(){
		
		IndexedIterable<Object> ordenes = OrdenesContext.getObjects(OrdenDeServicio.class);
		IndexedIterable<Object> nodos = SISAABContext.getObjects(NodoSaab.class);
		
		if(ordenes.size()>0){
			atenderOrdenDeServicio(ordenes);
		}
		
		for(Object o: nodos){
			
			NodoSaab nodo = (NodoSaab)o;
			List<OrdenDeServicio> servicios = nodo.getOrdenes();
			
			if(servicios.size()>0){
				
				despacharPedido(nodo, servicios);
			}
		}
		
	}
	
	/**
	 * obtiene las ordenes de servicio logistico generadas por el SISAAB y las procesa
	 */
	public synchronized void atenderOrdenDeServicio(IndexedIterable<Object> ordenes){
		
		Map<AmbienteLocal,List<OrdenDeServicio>> AgendaRecoleccion = new ConcurrentHashMap<AmbienteLocal,List<OrdenDeServicio>>();
		
		//obtiene las ordenes de serviciologistico
		//IndexedIterable<Object> ordenes = OrdenesContext.getObjects(OrdenDeServicio.class);
		
		//Agrupa las ordenes por punto de oferta
		for(Object obj: ordenes){
			
			OrdenDeServicio orden 	= (OrdenDeServicio)obj;
			AmbienteLocal origen 	= orden.getOrigen();
			
			if(AgendaRecoleccion.containsKey(origen)){
				
				List<OrdenDeServicio> list = AgendaRecoleccion.get(origen);
				list.add(orden);				
				
			}else{
				
				List<OrdenDeServicio> list = new ArrayList<OrdenDeServicio>();
				list.add(orden);
				AgendaRecoleccion.put(origen, list);
			}
		}//end Foreach
		
		//Recolecta productos
		Set<AmbienteLocal> pueblos = AgendaRecoleccion.keySet();
		
		for(AmbienteLocal pueblo: pueblos){
			
			recolectarProductos(pueblo,AgendaRecoleccion.get(pueblo));
		}		
	}
	
	/**
	 * Moviliza un agente <code>Camion</code> de cada nodo saab correspondiente al centro urbano de la Oferta
	 * El agente recoge los productos y vuelve al nodo logistico.
	 * Transitar ida y vuelta.
	 * @param orden
	 */
	private void recolectarProductos(AmbienteLocal puntoOferta, List<OrdenDeServicio> ordenes){
		
		Coordinate origen	= puntoOferta.getNodosSaab().get(0).getRoadAccess();
	
		CentroUrbano puebloOfertas = (CentroUrbano)puntoOferta;
		RecolectarProductos actividad = new RecolectarProductos(puntoOferta.getNodosSaab().get(0),puebloOfertas,ordenes);
		
		crearTransporte(origen, actividad);		
	}
	
	/**
	 * Moviliza un agente <code>Camion</code> del nodo saab correspondiente a la plaza distrital.
	 * El agente despacha los productos.
	 * @param orden
	 */
	private void despacharPedido(NodoSaab nodoOrigen, List<OrdenDeServicio> ordenes){
		
		Map<NodoSaab,List<OrdenDeServicio>> AgendaDespacho = new ConcurrentHashMap<NodoSaab,List<OrdenDeServicio>>();
		
		//Agrupa las ordenes por punto de demanda
		for(OrdenDeServicio orden: ordenes){			
			
			NodoSaab destino 		= orden.getDestino();
			
			if(AgendaDespacho.containsKey(destino)){
				
				List<OrdenDeServicio> list = AgendaDespacho.get(destino);
				list.add(orden);				
				
			}else{
				
				List<OrdenDeServicio> list = new ArrayList<OrdenDeServicio>();
				list.add(orden);
				AgendaDespacho.put(destino, list);
			}
		}//end Foreach
		
		//Despacha productos
		
		Set<NodoSaab> plazas = AgendaDespacho.keySet();
		
		for(NodoSaab plaza: plazas){
			
			Coordinate origen	= nodoOrigen.getRoadAccess();
			Coordinate destino = plaza.getRoadAccess();	
			
			SistemaActividadHumana actividad = new Transitar(origen,destino);
			
			crearTransporte(origen,actividad);
		}
	}

	/**
	 * Crea un agente <code>Camion</code> y lo registra en el contexto y proyeccion GIS.
	 * El agente ejecutara la tarea de transitar del origen al destino.
	 * @param origen
	 * @param destino
	 * @param circular
	 */
	private void crearTransporte(Coordinate origen, SistemaActividadHumana actividad){
		
		//crea un agente camion
		Camion transporte = new Camion();
		GeometryFactory geofact = new GeometryFactory();
		Point geom = geofact.createPoint(origen);
		transporte.setGeometria(geom);
					
		transporte.setActividadVigente(actividad);
		SISAABContext.add(transporte);
		SAABGeography.move(transporte, geom);		
		
	}
	
	
	
	

}
