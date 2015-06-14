/**
 * 
 */
package simulaSAAB.contextos;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import simulaSAAB.comunicacion.OrdenDeServicio;
import simulaSAAB.comunicacion.Producto;

/**
 * @author dampher
 *
 */
public class NodoSaab {
	
	private String Nombre;
	
	private Geometry geometria;
	
	protected Queue<OrdenDeServicio> ordenes;
	
	private Coordinate roadAccess;
	

	/**
	 * 
	 */
	public NodoSaab() {
		
		this.ordenes = new ConcurrentLinkedQueue<OrdenDeServicio>();
	}
	
	public NodoSaab(String nombre){
		
		this.Nombre		= nombre;
		this.ordenes 	= new ConcurrentLinkedQueue<OrdenDeServicio>();
	}
	
	/**
	 * Recibe las ordenes de servicio que representan los productos 
	 * transportados por el operador logístico
	 * 
	 * @param ordenes Ordenes de Servicio entregadas
	 */
	public void AlmacenarProductos(List<OrdenDeServicio> ordenes){
		
		this.addAllOrdenesServicio(ordenes);
	}
	
	public void DespacharProductos(List<OrdenDeServicio> ordenes){
		
		for(OrdenDeServicio orden: ordenes){
			
			if(this.ordenes.contains(orden))
				this.ordenes.remove(orden);			
		}
	}
	
	public void setGeometria(Geometry g){
		this.geometria=g;
	}
	
	public Geometry getGeometria() {
		return geometria;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public Coordinate getRoadAccess() {
		return roadAccess;
	}

	public void setRoadAccess(Coordinate roadAccess) {
		this.roadAccess = roadAccess;
	}

	public Geometry getCentroid(){
		return this.geometria.getCentroid();
	}

	public OrdenDeServicio pollOrden() {
		return ordenes.poll();
	}

	protected void addOrdenServicio(OrdenDeServicio orden) {						
		ordenes.add(orden);
	}
	
	protected void addAllOrdenesServicio(List<OrdenDeServicio> ordenes){					
		ordenes.addAll(ordenes);
	}
	
	

}
