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
 * @author dampher
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
	
	/**Getters & Setters**/
	
	@Override
	public Geometry getGeometria() {
		
		return Geografia.getGeometry(this);
	}
	
	@Override
	public void setGeometria(Geometry geom) {
		
		Geometria = geom;
	}

	public String getIdentificador() {
		return Identificador;
	}

	public void setIdentificador(String identificador) {
		Identificador = identificador;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public String getTipo() {
		return Tipo;
	}

	public void setTipo(String tipo) {
		Tipo = tipo;
		setMultiplicador();
	}

	public Geography getGeografia() {
		return Geografia;
	}

	public void setGeografia(Geography geografia) {
		Geografia = geografia;
	}
	
	public void addJunction(Junction j){
		this.junctions.add(j);
	}


	public NetworkEdge<Junction> getEdge() {
		return edge;
	}


	public void setEdge(NetworkEdge<Junction> edge) {
		this.edge = edge;
	}

	public Integer getMultiplicador() {
		return Multiplicador;
	}

	public void setMultiplicador(Integer multiplicador) {
		Multiplicador = multiplicador;
	}
	
	

}
