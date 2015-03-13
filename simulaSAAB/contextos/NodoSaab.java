/**
 * 
 */
package simulaSAAB.contextos;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import simulaSAAB.comunicacion.Producto;

/**
 * @author dampher
 *
 */
public class NodoSaab {
	
	private String Nombre;
	
	private Geometry geometria;
	
	private List<Producto> Productos;
	

	/**
	 * 
	 */
	public NodoSaab() {
		// TODO Auto-generated constructor stub
	}
	
	public NodoSaab(String nombre){
		this.Nombre=nombre;
	}
	
	
	public void AlmacenarProductos(){
		
	}
	
	public void DespacharProductos(){
		
	}
	
	public void setGeometria(Geometry g){
		this.geometria=g;
	}

}
