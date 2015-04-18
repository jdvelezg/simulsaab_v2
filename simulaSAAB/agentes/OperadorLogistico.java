package simulaSAAB.agentes;

import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import simulaSAAB.comunicacion.OrdenDeServicio;
import simulaSAAB.tareas.SistemaActividadHumana;

import com.vividsolutions.jts.geom.Geometry;

public class OperadorLogistico implements AgenteReactivo {
	
	private SistemaActividadHumana TransportarProductos;
	
	private SistemaActividadHumana Moverse;
	
	private Geography<Object> SAABGeography;
	
	/**
	 * Constructor
	 */
	public OperadorLogistico() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * obtiene las ordenes de servicio logistico generadas por el SISAAB y las procesa
	 */
	public void atenderOrdenDeServicio(){
		
	}
	
	/**
	 * 
	 * @param orden
	 */
	private void recolectarProductos(OrdenDeServicio orden){
		
	}
	
	private void despacharPedido(OrdenDeServicio orden){
		
	}
	
	private void calcularRutaLogistica(Geometry origen, Geometry destino, Network<Object> network){
		
	}


	public Geography<Object> getSAABGeography() {
		return SAABGeography;
	}


	public void setSAABGeography(Geography<Object> sAABGeography) {
		SAABGeography = sAABGeography;
	}
	
	
	

}
