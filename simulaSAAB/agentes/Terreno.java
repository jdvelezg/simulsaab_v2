package simulaSAAB.agentes;

import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.random.RandomHelper;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.AmbienteLocal;
import simulaSAAB.contextos.GeografiaFija;

public class Terreno implements GeografiaFija {
	
	private double Hecatreas;
	
	private Geometry Geometria;
	
	private AmbienteLocal Ambiente;
	
	private String Estado;
	
	/**
	 * Constructor
	 */
	public Terreno() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor
	 * @param hect Numero de hectareas
	 * @param geom Punto geometrico del terreno
	 */
	public Terreno(double hect, Geometry geom){
		this.Hecatreas	=hect;
		this.Geometria	=geom;
	}
	
	/**
	 * Devuelve el recurso producido en el terreno.
	 * Usa el rendimiento promedio por hectarea del producto y genera una cantidad aleatoria
	 * que puede superar el promedio hasta en un 25%.
	 * 
	 * @param p El producto a cosechar.
	 * @return La cantidad de producto en su unidad de medida.
	 */
	public Recurso cosechar(Producto p){
		
		double cantidad = (p.getPromRendimientoHectarea()*RandomHelper.nextDoubleFromTo(0, 1.25))*this.Hecatreas;	
		
		return new Recurso(p,cantidad);
	}
	
	/**getters & setters**/
	
	public void setAmbiente(AmbienteLocal amb){
		this.Ambiente = amb;
	}
	
	public AmbienteLocal getAmbiente(){
		return this.Ambiente;
	}


	@Override
	public Geometry getGeometria() {
		// TODO Auto-generated method stub
		return this.Geometria;
	}

	@Override
	public void setGeometria(Geometry geom) {
		this.Geometria=geom;
		
	}

}
