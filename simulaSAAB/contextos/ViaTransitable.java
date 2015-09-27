/**
 * 
 */
package simulaSAAB.contextos;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.context.DefaultContext;
import repast.simphony.space.gis.Geography;
import simulaSAAB.contextos.environment.Junction;
import simulaSAAB.contextos.environment.NetworkEdge;

/**
 * Representa una vía trnasitable por los agentes de la simulación mediante {@link ObjetoMovil}
 * <p>
 * Agrega la capacidad de simular carreteras y autopistas de abastecimiento
 * 
 * @author jdvelezg
 *
 */
public class ViaTransitable extends DefaultContext<Object> implements GeografiaFija  {
	
	private String Identificador;
	
	private String Nombre;
	
	private String Tipo;
	
	private Geography Geografia;
	
	private Geometry Geometria;
	
	private Integer Multiplicador =0;
	
	// The junctions at either end of the road
	transient private ArrayList<Junction> junctions;	
	// The NetworkEdge which represents this Road in the Network
	transient private NetworkEdge<Junction> edge;	
	
	
	/**
	 * Constructor
	 */
	public ViaTransitable(){
		
		this.junctions = new ArrayList<Junction>();
		setMultiplicador();
	}
	
	/**
	 * Calcula un multiplicador de peso por uso de la vía de acuerdo con el tipo de via asignado.
	 * Los posibles tipos se detallan abajo de esta declaración. Es usado para estimar el costo
	 * de transportar mercancia.
	 * 
	 * 'service' -1
	 * 'living_street' -1
	 * 'link_' 1 
	 * 
	 * 'trunk' -2
	 * 'primary' -2
	 * 'secondary' -3
	 * 'tertiary' -4
	 * 'residential' -5
	 * 'road' -6
	 * 'construction' -7
	 * 'track' -8
	 * 'bridleway' -9
	 * 'unclassified' -10  
	 * 'path' -11
	 * 'steps' -12
	 */
	private void setMultiplicador(){
		
		if(this.Tipo!=null){
			
			if(Tipo.contains("link") || Tipo.equalsIgnoreCase("service") || Tipo.equalsIgnoreCase("living_street")){
				
				this.Multiplicador = 1;
			}else if(Tipo.equalsIgnoreCase("primary") || Tipo.equalsIgnoreCase("trunk") ){
				
				this.Multiplicador = 2;
			}else if(Tipo.equalsIgnoreCase("secondary")){
				
				this.Multiplicador = 3;
			}else if(Tipo.equalsIgnoreCase("tertiary")){
				
				this.Multiplicador = 4;
			}else if(Tipo.equalsIgnoreCase("residential")){
				
				this.Multiplicador = 5;
			}else if(Tipo.equalsIgnoreCase("road")){
				
				this.Multiplicador = 6;
			}else if(Tipo.equalsIgnoreCase("construction")){
				
				this.Multiplicador = 7;
			}else if(Tipo.equalsIgnoreCase("track")){
				
				this.Multiplicador = 8;
			}else if(Tipo.equalsIgnoreCase("bridleway")){
				
				this.Multiplicador = 9;
			}else if(Tipo.equalsIgnoreCase("unclassified")){
				
				this.Multiplicador = 10;
			}else if(Tipo.equalsIgnoreCase("path")){
				
				this.Multiplicador = 11;
			}else if(Tipo.equalsIgnoreCase("steps")){
				
				this.Multiplicador = 12;
			}
			
		}
	}
	
		
	@Override
	public Geometry getGeometria() {
		
		return Geografia.getGeometry(this);
	}
	
	@Override
	public void setGeometria(Geometry geom) {
		
		Geometria = geom;
	}
	/**
	 * Devuelve el identificador
	 * @return string identificador
	 */
	public String getIdentificador() {
		return Identificador;
	}
	/**
	 * Asigna el identificador
	 * @param identificador string identificador de la via
	 */
	public void setIdentificador(String identificador) {
		Identificador = identificador;
	}
	/**
	 * Devuelve el nombre de la vía
	 * @return String
	 */
	public String getNombre() {
		return Nombre;
	}
	/**
	 * Asigna el nombre de la vía
	 * @param nombre string, nombre de la vía
	 */
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	/**
	 * Devuelve el tipo de vía
	 * @return String
	 */
	public String getTipo() {
		return Tipo;
	}
	/**
	 * Asigna el tipo de vía
	 * @param tipo string, tipo de vía
	 */
	public void setTipo(String tipo) {
		Tipo = tipo;
		setMultiplicador();
	}
	/**
	 * Devuelve la geografía de la vía
	 * @return Geography
	 */
	public Geography getGeografia() {
		return Geografia;
	}
	/**
	 * Asigna la geografía de la vía
	 * @param geografia  Geography, geografía de la vía
	 */
	public void setGeografia(Geography geografia) {
		Geografia = geografia;
	}
	/**
	 * Asocia una intersección {@link junction} a la vía
	 * @param j Junction intersección de la via con otra
	 */
	public void addJunction(Junction j){
		this.junctions.add(j);
	}
	/**
	 * Devuelve el {@link Edge} asociado a la vía 
	 * @return NetworkEdge<Junction>
	 */
	public NetworkEdge<Junction> getEdge() {
		return edge;
	}

	/**
	 * Asigna el <code>Edge</code> asociado a la vía 
	 * @param edge NetworkEdge<Junction> asociado a al vía
	 */
	public void setEdge(NetworkEdge<Junction> edge) {
		this.edge = edge;
	}
	/**
	 * Devuelve el multiplicador
	 * @return int
	 */
	public Integer getMultiplicador() {
		return Multiplicador;
	}
	/**
	 * Asigna el multiplicador
	 * @param multiplicador int multiplicador de la vía
	 */
	public void setMultiplicador(Integer multiplicador) {
		Multiplicador = multiplicador;
	}
	/**
	 * Devuelve un arreglo con las coordenadas que conforman la geometría de la vía
	 * @return  Coordinate[] 
	 */
	public Coordinate[] getCoordinates(){
		return this.Geometria.getCoordinates();
	}
	

}
