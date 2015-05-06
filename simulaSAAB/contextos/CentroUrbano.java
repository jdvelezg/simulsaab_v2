/**
 * 
 */
package simulaSAAB.contextos;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * @author dampher
 *
 */
public class CentroUrbano extends AmbienteLocal {
	
	private String Nombre;
	
	private Geometry Geometria;
	
	private Coordinate roadAccess;
	
	private AmbienteLocal municipio;
	
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
	
	public Point getCentroid(){
		return this.Geometria.getCentroid();
	}

	public Coordinate getRoadAccess() {
		return roadAccess;
	}

	public void setRoadAccess(Coordinate roadAccess) {
		this.roadAccess = roadAccess;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public AmbienteLocal getMunicipio() {
		return municipio;
	}

	public void setMunicipio(AmbienteLocal municipio) {
		this.municipio = municipio;
	}
	
	@Override
	public List<NodoSaab> getNodosSaab() {
		return this.municipio.getNodosSaab();
	}
	

}
