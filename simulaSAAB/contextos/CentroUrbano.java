/**
 * 
 */
package simulaSAAB.contextos;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * Representa los centros urbanos presentes en la simulación
 * <p>
 * Delimitación geográfica de los pueblso incluidos en la simulación, usados como puntos de oferta y demanda
 * 
 * @author dampher
 *
 */
public class CentroUrbano extends AmbienteLocal {
	
		
	private Coordinate roadAccess;
	
	private AmbienteLocal municipio;
	
	private List<PlazaDistrital> PlazasDistritales;

	/**
	 * Constructor
	 */
	public CentroUrbano() {
		super();
	}
	/**
	 * Constructor
	 * @param nombre nombre del centro urbano
	 */
	public CentroUrbano(String nombre){
		super(nombre);		
	}		
	/**
	 * Devuelve el centroide de la geometria del centro urbano
	 * @return Point centroide d ela geometria
	 */
	public Point getCentroid(){
		return this.getGeometria().getCentroid();
	}
	
	@Override
	public Coordinate getRoadAccess() {
		return roadAccess;
	}
	@Override
	public void setRoadAccess(Coordinate roadAccess) {
		this.roadAccess = roadAccess;
	}
	/**
	 * Devuelve el municipio al que pertenece el centro urbano
	 * @return AmbienteLocal municipio
	 */
	public AmbienteLocal getMunicipio() {
		return municipio;
	}
	/**
	 * Asigna el municipio al que pertenece el centro urbano
	 * @param municipio AmbienteLocal, municipio del centro urbano
	 */
	public void setMunicipio(AmbienteLocal municipio) {
		this.municipio = municipio;
	}
	

}
