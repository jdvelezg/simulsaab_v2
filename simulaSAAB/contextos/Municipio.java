/**
 * 
 */
package simulaSAAB.contextos;

import java.util.List;

/**
 * Representa el ambiente local presente en la simulación
 * <p>
 * Delimitación geográfica en el que se ubica a los agentes del modelo 
 * 
 * @author jdvelezg
 *
 */
public class Municipio extends AmbienteLocal {
	
	private String Nombre;
	
	private List<CentroUrbano> CentrosUrbanos;
	
	private NodoSaab nodoPrimario;

	/**
	 * Constructor
	 */
	public Municipio() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getNombre() {
		return Nombre;
	}
	@Override
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	@Override
	public List<CentroUrbano> getCentrosUrbanos() {
		return CentrosUrbanos;
	}
	@Override
	public void setCentrosUrbanos(List<CentroUrbano> centrosUrbanos) {
		CentrosUrbanos = centrosUrbanos;
	}
	/**
	 * Devuelve el nodo primario del municipio
	 * @return NodoSaab
	 */
	public NodoSaab getNodoPrimario() {
		return nodoPrimario;
	}
	/**
	 * Asigna el nodo primario del municipio
	 * 
	 * @param nodoPrimario NodoSaab donde se recogen los productos del municipio
	 */
	public void setNodoPrimario(NodoSaab nodoPrimario) {
		this.nodoPrimario = nodoPrimario;
	}
	
	

}
