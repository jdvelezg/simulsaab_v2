/**
 * 
 */
package simulaSAAB.contextos;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author dampher
 *
 */
public class CentroUrbano extends AmbienteLocal {
	
	private String Nombre;
	
	private Geometry Geometria;
	
	private List<PlazaDistrital> PlazasDistritales;

	/**
	 * 
	 */
	public CentroUrbano() {
		// TODO Auto-generated constructor stub
	}
	
	public CentroUrbano(String nombre){
		this.Nombre=nombre;
	}
	
	@Override
	public void setGeometria(Geometry g){
		this.Geometria=g;
	}
	
	@Override
	public Geometry getGeometria(){
		
		return this.Geometria;
	}
	@Override
	public String getNombre(){
		return this.Nombre;
	}

}
