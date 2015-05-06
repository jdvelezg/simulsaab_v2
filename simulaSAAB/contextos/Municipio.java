/**
 * 
 */
package simulaSAAB.contextos;

import java.util.List;

/**
 * @author dampher
 *
 */
public class Municipio extends AmbienteLocal {
	
	private String Nombre;
	
	private List<CentroUrbano> CentrosUrbanos;
	
	private NodoSaab nodoPrimario;

	/**
	 * 
	 */
	public Municipio() {
		// TODO Auto-generated constructor stub
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public List<CentroUrbano> getCentrosUrbanos() {
		return CentrosUrbanos;
	}

	public void setCentrosUrbanos(List<CentroUrbano> centrosUrbanos) {
		CentrosUrbanos = centrosUrbanos;
	}

	public NodoSaab getNodoPrimario() {
		return nodoPrimario;
	}

	public void setNodoPrimario(NodoSaab nodoPrimario) {
		this.nodoPrimario = nodoPrimario;
	}
	
	

}
