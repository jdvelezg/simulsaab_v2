/**
 * 
 */
package simulaSAAB.contextos;

import com.vividsolutions.jts.geom.Coordinate;

import repast.simphony.context.DefaultContext;

/**
 * @author dampher
 *
 */
public class ViaPrincipal extends DefaultContext<Object> implements GeografiaFija  {
	
	private Coordinate Origen;
	private Coordinate Destino;

}
