package simulaSAAB.comunicacion;

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
	 * @param enunciado
	 */
	public Proposito (String enunciado){
		this.Enunciado = enunciado;
	}
	
	/**
	 * Compara los elementos del proposito y determina si son iguales
	 * @param p Proposito a comparar
	 * @return boolean true si son iguales, false en caso contrario.
	 */
	public boolean compare(Proposito p){
		if(this.Enunciado.compareToIgnoreCase(p.getEnunciado())==0)
			return true;
		else
			return false;
	}

	public String getEnunciado() {
		return Enunciado;
	}

	public void setEnunciado(String enunciado) {
		Enunciado = enunciado;
	}
	
	

}
