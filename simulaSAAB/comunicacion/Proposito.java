package simulaSAAB.comunicacion;

/**
 * Representa el concepto de <code>propóstito</code> usado en la ontología para comunicación entre los agentes y descripción de actividades <code>MPA</code> {@link SistemaActividadHumana}
 * 
 * @author jdvelezg
 *
 */
public class Proposito implements Concepto {
	
	private String Enunciado;
	
	/**
	 * Constructor
	 */
	public Proposito() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor
	 * @param enunciado string, enunciado del propósito
	 */
	public Proposito (String enunciado){
		this.Enunciado = enunciado;
	}
	
	/**
	 * Compara los elementos del proposito y determina si son iguales
	 * 
	 * @param p Proposito a comparar
	 * @return boolean <code>true</code> si son iguales, <code>false</code> en caso contrario
	 */
	public boolean compare(Proposito p){
		if(this.Enunciado.compareToIgnoreCase(p.getEnunciado())==0)
			return true;
		else
			return false;
	}
	/**
	 * Devuelve el enunciado 
	 * @return
	 */
	public String getEnunciado() {
		return Enunciado;
	}
	/**
	 * Asigna el enunciado
	 * @param enunciado
	 */
	public void setEnunciado(String enunciado) {
		Enunciado = enunciado;
	}
	
	@Override
	public String toString(){
		return this.Enunciado;
	}

}
