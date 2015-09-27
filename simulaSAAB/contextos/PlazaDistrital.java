/**
 * 
 */
package simulaSAAB.contextos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import simulaSAAB.agentes.Demandante;
import simulaSAAB.comunicacion.OrdenDeCompra;
import simulaSAAB.comunicacion.OrdenDeServicio;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * Representa las plazas distritales presentes en la simualción
 * 
 * @author jdvelezg
 *
 */
public class PlazaDistrital extends NodoSaab {
	
	private String Nombre;
	
	private Geometry geometria;
	
	private Coordinate roadAccess;
	
	private Map<Demandante,List<OrdenDeCompra>> ordenesCompraMap;

	/**
	 * Constructor
	 */
	public PlazaDistrital() {
		super();
		this.ordenesCompraMap = new ConcurrentHashMap<Demandante,List<OrdenDeCompra>>();
	}
	
	/**
	 * Constructor
	 * @param nombre string, nombre de la plaza distrital
	 */
	public PlazaDistrital(String nombre){
		this.Nombre=nombre;
	}
		
	@Override
	public void AlmacenarProductos(List<OrdenDeServicio> ordenes){		
		/**
		 * Recibe los productos de un operador logistico y se los entrega al demandante asociado a la orden de servicio
		 * 
		 * @param ordenes Ordenes de Servicio que representan los recursos entregados 
		 */
		//Odd Exception??
		if(this.ordenesCompraMap==null)
			this.ordenesCompraMap = new ConcurrentHashMap<Demandante,List<OrdenDeCompra>>();
		/*
		 * Para cada orden de servicio recibida, la ordena por dmeandante para gestionar la entrega d e productos
		 */
		for(OrdenDeServicio ordenS: ordenes){
			
			List<OrdenDeCompra> ordenesCompra = ordenS.getOrdenesCompra();			
			for(OrdenDeCompra ordenC: ordenesCompra){
				
				Demandante dm = ordenC.getDemanda().getComprador();
				if(ordenesCompraMap.containsKey(dm)){
					ordenesCompraMap.get(dm).add(ordenC);
				}else{
					List<OrdenDeCompra> ordenesDemandante = new ArrayList<OrdenDeCompra>();
					ordenesDemandante.add(ordenC);
					ordenesCompraMap.put(dm, ordenesDemandante);
				}
			}//EndFor OrdenCompra
			
		}//EndFor OrdenServicio	
		
		despacharProductos();
	}
	/**
	 * Entrega los productos mapeados a los demandantes asociados a la orden de compra pendiente
	 */
	public void despacharProductos(){
		
		Set<Demandante> demandantes = ordenesCompraMap.keySet();
		for(Demandante d: demandantes){
			//Entrega el pedido al demandante, representado en las ordenes de compra
			d.recibirPedido(ordenesCompraMap.get(d));
		}
	}
			
	@Override
	public Point getCentroid(){
		return this.geometria.getCentroid();
	}
	@Override
	public String getNombre() {
		return Nombre;
	}
	@Override
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	@Override
	public Geometry getGeometria() {
		return geometria;
	}
	@Override
	public void setGeometria(Geometry geometria) {
		this.geometria = geometria;
	}	
	@Override
	public Coordinate getRoadAccess() {
		return roadAccess;
	}
	@Override
	public void setRoadAccess(Coordinate roadAccess) {
		this.roadAccess = roadAccess;
	}
}
