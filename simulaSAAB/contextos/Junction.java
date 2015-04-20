package simulaSAAB.contextos;

import com.vividsolutions.jts.geom.Geometry;

public class Junction {
	
	private String Nombre;
	
	private String Tipo;
	
	private Geometry Geometria;
	
	private Integer Weight; 
	
		
	/**
	 * Contructor
	 */
	public Junction() {
		this.Weight=0;
	}
	
	/**
	 * Constructor
	 * @param nom nombre del nodo en el .shp
	 * @param tipo tipo de nodo en el .shp
	 */
	public Junction(String nom, String tipo){
		this.Nombre	= nom;
		this.Tipo	= tipo;
		this.Weight	= 0;
	}
	
	
	/**getters-setters**/

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
	}

	public Geometry getGeometria() {
		return Geometria;
	}

	public void setGeometria(Geometry geometria) {
		Geometria = geometria;
	}

	public Integer getWeight() {
		return Weight;
	}

	public void setWeight(Integer weight) {
		Weight = weight;
	}
	

}
