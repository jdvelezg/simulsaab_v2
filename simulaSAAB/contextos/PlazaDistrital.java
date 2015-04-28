/**
 * 
 */
package simulaSAAB.contextos;

/**
 * @author dampher
 *
 */
public class PlazaDistrital extends NodoSaab {
	
	private String Nombre;

	/**
	 * 
	 */
	public PlazaDistrital() {
		// TODO Auto-generated constructor stub
	}
	
	public PlazaDistrital(String nombre){
		this.Nombre=nombre;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	
	

}
