/**
 * 
 */
package simulaSAAB.contextos;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Representa una geografía fije en el espacio  presente en la simulación
 * 
 * @author jdvelezg
 *
 */
public interface GeografiaFija {
	
	/**
	 * Asigna la geometria
	 * @param geom Geometry, geometría correspondiente
	 */
	public void setGeometria(Geometry geom);
	/**
	 * Devuelve la geometría
	 * @return
	 */
	public Geometry getGeometria();
	

}
