/**
 * 
 */
package simulaSAAB.contextos;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.context.DefaultContext;

/**
 * @author dampher
 *
 */
public class ViaPrincipal extends DefaultContext<Object> implements GeografiaFija  {
	
	private Coordinate Origen;
	private Coordinate Destino;
	
	@Override
	public Geometry getGeometria() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setGeometria(Geometry geom) {
		// TODO Auto-generated method stub
		
	}

}
