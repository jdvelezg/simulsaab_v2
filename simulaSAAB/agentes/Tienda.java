package simulaSAAB.agentes;

import java.util.List;

import simulaSAAB.comunicacion.Producto;
import simulaSAAB.contextos.AmbienteLocal;
import simulaSAAB.contextos.GeografiaFija;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class Tienda implements GeografiaFija{
	
	
	private Geometry Geometria;
	
	private AmbienteLocal Ambiente;
	
	private List<Producto> Productos;
	
	
	/**
	 * Constructor
	 * @param nombre
	 */
	public Tienda(Geometry geom) {	
		
		this.Geometria = geom;
	}
	
		
	/**Getters & setters**/
	
	@Override
	public Geometry getGeometria() {
		// TODO Auto-generated method stub
		return Geometria;
	}


	@Override
	public void setGeometria(Geometry geom) {
		this.Geometria=geom;
		
	}


	public AmbienteLocal getAmbiente() {
		return Ambiente;
	}


	public void setAmbiente(AmbienteLocal ambiente) {
		Ambiente = ambiente;
	}
	
	
}
