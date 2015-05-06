/**
 * 
 */
package simulaSAAB.contextos;

import java.util.List;

import simulaSAAB.comunicacion.OrdenDeServicio;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * @author dampher
 *
 */
public class PlazaDistrital extends NodoSaab {
	
	private String Nombre;
	
	private Geometry geometria;
	
	private Coordinate roadAccess;

	/**
	 * Constructor
	 */
	public PlazaDistrital() {
		// TODO Auto-generated constructor stub
	}
	
	public void AlmacenarProductos(List<OrdenDeServicio> ordenes){
		
		this.addAllOrdenesServicio(ordenes);
	}
	
	public void DespacharProductos(List<OrdenDeServicio> ordenes){
		
		for(OrdenDeServicio orden: ordenes){
			
			if(this.ordenes.contains(orden))
				this.ordenes.remove(orden);			
		}
	}
	
	/**getters & setters **/
	
	public PlazaDistrital(String nombre){
		this.Nombre=nombre;
	}

	public Point getCentroid(){
		return this.geometria.getCentroid();
	}
	
	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public Geometry getGeometria() {
		return geometria;
	}

	public void setGeometria(Geometry geometria) {
		this.geometria = geometria;
	}
	
	
	public Coordinate getRoadAccess() {
		return roadAccess;
	}

	public void setRoadAccess(Coordinate roadAccess) {
		this.roadAccess = roadAccess;
	}
	
	
	
	
	
	

}
