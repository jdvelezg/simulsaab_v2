package simulaSAAB.agentes;

import com.vividsolutions.jts.geom.Geometry;

import simulaSAAB.contextos.AmbienteLocal;
import simulaSAAB.contextos.GeografiaFija;

public class Terreno implements GeografiaFija {
	
	private double Hecatreas;
	
	private Geometry Geometria;
	
	private AmbienteLocal Ambiente;
	

	public Terreno() {
		// TODO Auto-generated constructor stub
	}
	
	public Terreno(double hect, Geometry geom){
		this.Hecatreas	=hect;
		this.Geometria	=geom;
	}
	
	public void setAmbiente(AmbienteLocal amb){
		this.Ambiente = amb;
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
