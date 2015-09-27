package simulaSAAB.agentes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
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
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.contextos.environment.Junction;
import simulaSAAB.tareas.DespacharProductos;
import simulaSAAB.tareas.ProducirCebollaBulbo;
import simulaSAAB.tareas.RecolectarProductos;
import simulaSAAB.tareas.SistemaActividadHumana;
import simulaSAAB.tareas.Moverse;
import simulaSAAB.tareas.Transitar;
import bsh.This;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


/**
 * Representa al agente Operador Logístico
 * <p>
 * Agente Reactivo que encapsula el comportamiento de los operadores logísticos adscritos al sistema de abastecimiento alimentario
 * 
 * @author jdvelezg
 *
 */
public class OperadorLogistico implements AgenteReactivo {
	/**
	 * Registro de la clase usado para depuración <code>Debugging</code>
	 */
	private static Logger LOGGER = Logger.getLogger(OperadorLogistico.class.getName());
	/**
	 * {@link SistemaActividadHumana} usado por el agente para el transporte de <code>Productos</code>
	 */
	private SistemaActividadHumana TransportarProductos;
	
	private SistemaActividadHumana Moverse;
	
	/**
	 * Constructor de la clase
	 */
	public OperadorLogistico() {
		
	}
	
	/**
	 * Ejecuta el comportamiento por defecto del agente
	 * <p>
	 * Verifica cada 100 ciclos <code>Ticks</code> los pedidos generados en el <code>SISAAB</code>
	 * <p>
	 * <code>start = 1000, interval = 100</code>
	 */
	@ScheduledMethod (start = 1000, interval = 100)
	public void step(){
				
		IndexedIterable<Object> ordenes 	= SaabContextBuilder.OrdenesContext.getObjects(OrdenDeServicio.class);
		IndexedIterable<Object> nodos 		= SaabContextBuilder.SISAABContext.getObjects(NodoSaab.class);
		/*
		 * Revisa el SISAAB, Si se generaron ordenes de servicio, 
		 * las procesa, recogiendo los productos y entregandolos
		 * al nodo logistico más cercano
		 */
		if(ordenes.size()>0){
			//LOGGER.info("ordenes a procesar: "+ordenes.size());
			atenderOrdenDeServicio(ordenes);
		}else{
			//LOGGER.log(Level.INFO,"OPER-DONE-NOTH; ordenes: "+ordenes.size());
		}		
		/*
		 * Revisa cada nodo logístico. Si ha recibido ordenes de servicio,
		 * las procesa, llevandolas a su destino
		 */
		for(Object o: nodos){
			
			NodoSaab nodo = (NodoSaab)o;
			if(nodo.ordenesPendientes()){
				
				List<OrdenDeServicio> servicios = nodo.pollOrdenes();				
				if(servicios.size()>0)				
					despacharPedido(nodo, servicios);				
			}		
		}//EndFor		
				
	}
	
	/**
	 * Remueve del contexto las ordenes atendidas para liberar memoria
	 * 
	 * @param ordenes ordenes a purgar
	 *
	private void purgeOrdenServicio(Iterator<Object> ordenes){
		
		while(ordenes.hasNext()){			
			OrdenDeServicio orden 	= (OrdenDeServicio)ordenes.next();			
			if(!(orden.isPendiente()))
				SaabContextBuilder.OrdenesContext.remove(orden);			
		}
	}*/
	
	/**
	 * Obtiene las ordenes de servicio logistico generadas por el SISAAB y las procesa
	 * @param ordenes IndexedIterable<Object> con las ordenes a procesar
	 */
	public synchronized void atenderOrdenDeServicio(IndexedIterable<Object> ordenes){
		
		Map<CentroUrbano,List<OrdenDeServicio>> AgendaRecoleccion = new ConcurrentHashMap<CentroUrbano,List<OrdenDeServicio>>();
		
		//obtiene las ordenes de serviciologistico
		//IndexedIterable<Object> ordenes = OrdenesContext.getObjects(OrdenDeServicio.class);
		
		int x =0;
		//Agrupa las ordenes por punto de oferta (CentroUrbano)
		for(Object obj: ordenes){
									
			OrdenDeServicio orden 	= (OrdenDeServicio)obj;			
			CentroUrbano origen 	= orden.getOrigen();
			
			if(orden.isPendiente()){
				
				if(AgendaRecoleccion.containsKey(origen)){
					
					List<OrdenDeServicio> list = AgendaRecoleccion.get(origen);
					list.add(orden);	
					
				}else{
					
					List<OrdenDeServicio> list = new ArrayList<OrdenDeServicio>();
					list.add(orden);
					AgendaRecoleccion.put(origen, list);
				}
				orden.setAtendida(); x++;
			}else{
				
			}
		}//end Foreach
	//LOGGER.info(RunEnvironment.getInstance().getCurrentSchedule().getTickCount()+" ordenes procesadas: "+x);
		//Recolecta productos
		Set<CentroUrbano> pueblos = AgendaRecoleccion.keySet();
		
		for(CentroUrbano pueblo: pueblos){
			
			recolectarProductos(pueblo,AgendaRecoleccion.get(pueblo));
		}		
	}
	
	/**
	 * Crea un agente <code>Camion</code> con la finalidad de recoger los productos asociados a las ordenes pasadas como parámetro 
	 * 
	 * @param puntoOferta centroUrbano al que se debe movilizar el agente camion para recoger los productos
	 * @param ordenes ordenes de servicio que describen el producto a transportar
	 */
	private void recolectarProductos(CentroUrbano puntoOferta, List<OrdenDeServicio> ordenes){
		
		Coordinate origen	= puntoOferta.getNodosSaab().get(0).getRoadAccess();
		//LOGGER.log(Level.INFO," punto de Oferta: "+puntoOferta.getNombre()+" acceso: "+origen.toString());
		RecolectarProductos actividad = new RecolectarProductos(puntoOferta.getNodosSaab().get(0),puntoOferta,ordenes);
		
		Camion transporte = new Camion();
		crearTransporte(transporte,origen, actividad);		
	}
	
	/**
	 * Moviliza un agente <code>Camion</code> de un <code>nodo saab</code> a una <code>plaza distrital</code>
	 * 
	 * @param nodoOrigen NodoSaab desde el cual el agente <code>camion</code> debe iniciar el movimiento
	 * @param ordenes arreglo de <code>OrdenDeServicio</code> que describen los productos a transportar
	 */
	private void despacharPedido(NodoSaab nodoOrigen, List<OrdenDeServicio> ordenes){
		
		Map<NodoSaab,List<OrdenDeServicio>> AgendaDespacho = new ConcurrentHashMap<NodoSaab,List<OrdenDeServicio>>();
		
		//Agrupa las ordenes por punto de demanda
		for(OrdenDeServicio orden: ordenes){			
			
			NodoSaab destino = orden.getDestino();
			
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
			
			DespacharProductos actividad = new DespacharProductos(nodoOrigen,plaza,AgendaDespacho.get(plaza));
			
			Camion transporte = new Camioneta();
			crearTransporte(transporte,origen,actividad);
		}
	}

	/**
	 * Configura un agente <code>Camion</code> y lo registra en el contexto y proyeccion GIS.
	 *
	 * @param transporte Camion agente a configurar
	 * @param origen Coordinate de origen del movimiento del agente <code>Camion</code>
	 * @param actividad SistemaActividadHumana que encapsula las acciones de transporte del agente <code>camion</code>
	 */
	private void crearTransporte(Camion transporte, Coordinate origen, SistemaActividadHumana actividad){
			
		GeometryFactory geofact = new GeometryFactory();
		Point geom = geofact.createPoint(new Coordinate(origen.x,origen.y));
		transporte.setGeometria(geom);
				
		transporte.setActividadVigente(actividad.getInstance());
		SaabContextBuilder.SISAABContext.add(transporte);
		SaabContextBuilder.SAABGeography.move(transporte, geom);
		
		/*
		 * Crea un objeto Schedule y agrega el objeto para que su metodo step sea ejecutado 
		 * en cada tick de la simulación
		 */
		double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		double start = tick < 1 ? 1 : tick + 2;
		int interval = 1;
		
		ScheduleParameters params = ScheduleParameters.createRepeating(start,interval);
		ISchedule sch = RunEnvironment.getInstance().getCurrentSchedule();
		sch.schedule(params, transporte, "step");		
	}
	

}
