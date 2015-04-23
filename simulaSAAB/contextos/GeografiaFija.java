/**
 * 
 */
package simulaSAAB.contextos;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * @author dampher
 *
 */
public interface GeografiaFija {
	
	public void setGeometria(Geometry geom);	
	public Geometry getGeometria();
	

}
