package simulaSAAB.agentes;

import simulaSAAB.contextos.AmbienteLocal;
import simulaSAAB.contextos.GeografiaFija;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class Tienda implements GeografiaFija{
	
	
	private Geometry Geometria;
	
	private AmbienteLocal Ambiente;
	
	
	/**
	 * Constructor
	 * @param nombre
	 */
	public Tienda(Geometry geom) {	
		
		
	}
	
	
	@Override
	public Geometry getGeometria() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setGeometria(Geometry geom) {
		this.Geometria=geom;
		
	}
}
