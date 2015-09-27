/**
 * 
 */
package simulaSAAB.contextos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.OrdenDeServicio;
import simulaSAAB.comunicacion.Producto;

/**
 * Representa los nodos logísticos presentes en la simulación
 * <p>
 * Los nodos logísticos hacen parte de la estructura de modernización del sistema de abastecimeinto alimentario propuesto en el PMASAB
 *  
 * @author jdvelezg
 *
 */
public class NodoSaab {
	
	private String Nombre;
	
	private Geometry geometria;
	
	protected Queue<OrdenDeServicio> ordenes;
	
	protected Queue<PlazaDistrital> agendaEntrega;
	
	protected Map<PlazaDistrital,List<OrdenDeServicio>> ordenesServicio;
	
	private Coordinate roadAccess;
	

	/**
	 * Constructor
	 */
	public NodoSaab() {
		
		ordenes		 	= new ConcurrentLinkedQueue<OrdenDeServicio>();
		ordenesServicio	= new ConcurrentHashMap<PlazaDistrital,List<OrdenDeServicio>>();
		agendaEntrega	= new ConcurrentLinkedQueue<PlazaDistrital>();
	}
	/**
	 * Cosntructor
	 * 
	 * @param nombre nombre del nodo logistico
	 */
	public NodoSaab(String nombre){
		
		this.Nombre		= nombre;
		ordenes 		= new ConcurrentLinkedQueue<OrdenDeServicio>();
		ordenesServicio	= new ConcurrentHashMap<PlazaDistrital,List<OrdenDeServicio>>();
		agendaEntrega	= new ConcurrentLinkedQueue<PlazaDistrital>();
	}
	
	/**
	 * Recibe las ordenes de servicio que representan los productos transportados por el operador logístico
	 * 
	 * @param ordenes Ordenes de Servicio entregadas
	 */
	public void AlmacenarProductos(List<OrdenDeServicio> ordenes){
		
		addAllOrdenesServicio(ordenes);
	}
	/**
	 * Extrae el listadod e ordenes de servicio de la lista interna de ordenes pendientes
	 * 
	 * @param ordenes listado de ordenes de servicio
	 */
	public void DespacharProductos(List<OrdenDeServicio> ordenes){
		
		for(OrdenDeServicio orden: ordenes){
			
			if(this.ordenes.contains(orden))
				this.ordenes.remove(orden);			
		}
	}
	/**
	 * Asigna la geometria del nodo
	 * @param g Gerometry, geometría del nodo
	 */
	public void setGeometria(Geometry g){
		this.geometria=g;
	}
	/**
	 * Devuelve la geometría del nodo
	 * @return Geometry
	 */
	public Geometry getGeometria() {
		return geometria;
	}
	/**
	 * Devuelve el nombre del nodo
	 * @return striang
	 */
	public String getNombre() {
		return Nombre;
	}
	/**
	 * Asigna el nombre del nodo
	 * @param nombre string, nombre dle nodo
	 */
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	/**
	 * Devuelve las coordneadas de acceso vial al nodo
	 * @return Coordinate punto de acceso al nodo por vía
	 */
	public Coordinate getRoadAccess() {
		return roadAccess;
	}
	/**
	 * Asigna el punto de acceso al nodo 
	 * @param roadAccess Coordinate, coordenadas de punto de acceso al nodo
	 */
	public void setRoadAccess(Coordinate roadAccess) {
		this.roadAccess = roadAccess;
	}
	/**
	 * Devuelve el centroide de la geometria del nodo
	 * @return
	 */
	public Geometry getCentroid(){
		return this.geometria.getCentroid();
	}

	/**
	 * Devuelve listado de ordenes de servicio agendadados
	 * @return List<OrdenDeServicio>
	 */
	public List<OrdenDeServicio> pollOrdenes() {
		
		if(!agendaEntrega.isEmpty()){
			
			PlazaDistrital key = agendaEntrega.poll();			
			return (ordenesServicio.containsKey(key))?ordenesServicio.remove(key):null;
		}else{
			return null;
		}		
	}
	
	/**
	 * Comprueba si el nodo tiene ordenes pendientes
	 * @return boolean <code>true</code> si tiene ordenes pendientes, <code>false</code> en caso contrario
	 */
	public boolean ordenesPendientes(){
		if(!agendaEntrega.isEmpty()){
			
			PlazaDistrital key = agendaEntrega.peek();			
			return (ordenesServicio.containsKey(key))?true:false;
		}else{
			return false;
		}
	}
	
	/**
	 * Crea una agenda de entrega, mediante una cola FIFO, agrupando por la plaza distrital asociada a un pedido
	 * <p>
	 * Mediante un {@link MAP} agrupa las ordenes de servicio por plaza
	 * 
	 * @param orden Orden de servicio a agregar
	 */
	protected void addOrdenServicio(OrdenDeServicio orden) {						
		ordenes.offer(orden);
		
		PlazaDistrital plazaDestino = orden.getOrdenPedido().getPuntoDemanda();
		agendaEntrega.offer(plazaDestino);
		if(this.ordenesServicio.containsKey(plazaDestino)){			
			ordenesServicio.get(plazaDestino).add(orden);
		}else{
			List<OrdenDeServicio> listaOrdenes = new ArrayList<OrdenDeServicio>();
			listaOrdenes.add(orden);
			this.ordenesServicio.put(plazaDestino, listaOrdenes);
		}
			
	}
	/**
	 * Agrega un listado de ordenes de servicio
	 * @param ordenes listado de ordenes de servicio 
	 */
	protected void addAllOrdenesServicio(List<OrdenDeServicio> ordenes){					
		ordenes.addAll(ordenes);
		
		for(OrdenDeServicio o : ordenes){
			addOrdenServicio(o);
		}
	}
	
	

}
