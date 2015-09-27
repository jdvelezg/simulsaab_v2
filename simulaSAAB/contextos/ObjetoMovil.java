package simulaSAAB.contextos;

import com.vividsolutions.jts.geom.Geometry;
/**
 * Representa los objetos móviles presentes en la simulación
 * 
 * @author jdvelezg
 *
 */
public interface ObjetoMovil {
	
	/**
	 * Devuelve el objeto
	 * @return Object
	 */
	public Object getObject();
	/**
	 * Asigna la geometría del objeto
	 * @param geom Geometry, geometría del objeto movil
	 */
	public void setGeometria(Geometry geom);
	/**
	 * Devuelve la geometría del objeto
	 * @return Geometry
	 */
	public Geometry getGeometria();
	
	

}
