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
	
		
	private Coordinate roadAccess;
	
	private AmbienteLocal municipio;
	
	private List<PlazaDistrital> PlazasDistritales;

	/**
	 * 
	 */
	public CentroUrbano() {
		super();
	}
	
	public CentroUrbano(String nombre){
		super(nombre);		
	}		
	
	public Point getCentroid(){
		return this.getGeometria().getCentroid();
	}

	public Coordinate getRoadAccess() {
		return roadAccess;
	}

	public void setRoadAccess(Coordinate roadAccess) {
		this.roadAccess = roadAccess;
	}

	public AmbienteLocal getMunicipio() {
		return municipio;
	}

	public void setMunicipio(AmbienteLocal municipio) {
		this.municipio = municipio;
	}
	

}
