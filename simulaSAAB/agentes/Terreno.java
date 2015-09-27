package simulaSAAB.agentes;

import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.random.RandomHelper;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.AmbienteLocal;
import simulaSAAB.contextos.GeografiaFija;
import simulaSAAB.global.VariablesGlobales;

/**
 * Representa el <code>Terreno</code> cultivable por un agente <code>Productor</code> 
 * 
 * @author jdvelezg
 *
 */
public class Terreno implements GeografiaFija {
	
	/**
	 * numero de hectareas que posee el terreno
	 */
	private double Hectareas;
	/**
	 * Geometria del agente
	 */
	private Geometry Geometria;
	/**
	 * Ambiente en donde se encuentra el agente
	 */
	private AmbienteLocal Ambiente;
	/**
	 * Estado del agente
	 */
	private String Estado;
	
	/**
	 * Constructor d ela clase
	 */
	public Terreno() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor
	 * @param hect Numero de hectareas
	 * @param geom geometria del terreno
	 */
	public Terreno(double hect, Geometry geom){
		this.Hectareas	=hect;
		this.Geometria	=geom;
	}
	
	/**
	 * Devuelve el recurso producido en el terreno.
	 * <p>
	 * Usa el rendimiento promedio por hectarea del producto y genera una cantidad aleatoria
	 * que puede superar el promedio hasta en un 25%.
	 * 
	 * @param p El producto a cosechar.
	 * @return Recurso
	 */
	public Recurso cosechar(Producto p){
		
		double cantidad = Math.floor((p.getPromRendimientoHectarea()*RandomHelper.nextDoubleFromTo(1, 1.25))*this.Hectareas);	
		return new Recurso(p,cantidad);
	}
	
	
	/**
	 * Asigna el ambiente local al agente
	 * @param amb
	 */
	public void setAmbiente(AmbienteLocal amb){
		this.Ambiente = amb;
	}
	/**
	 * Devuelve el ambiente local del agente
	 * @return
	 */
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
	/**
	 * Devuelve el numero de hectareas del terreno
	 * @return
	 */
	public double getHectareas() {
		return Hectareas;
	}
	
	/**
	 * Asigna el número de hectáreas del terreno
	 * @param hecatreas
	 */
	public void setHectareas(double hecatreas) {
		Hectareas = hecatreas;
	}

	/**
	 * Devuelve el estado del terreno
	 * @return
	 */
	public String getEstado() {
		return Estado;
	}

	/**
	 * Asigna el estado del terreno
	 * @param estado
	 */
	public void setEstado(String estado) {
		Estado = estado;
	}
	
	/**
	 * Devuelve el numero de meses que se demora el terreno en tener lista una cosecha
	 * 
	 * @return int numero aleatorio entre 3 y 6
	 * 
	 *  TODO hacer este un parámetro configurable
	 */
	public int tiempoParaCosecha(){
		return RandomHelper.nextIntFromTo(3,6);
	}	

}
