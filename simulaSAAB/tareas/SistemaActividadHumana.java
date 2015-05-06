package simulaSAAB.tareas;

import simulaSAAB.comunicacion.Proposito;

public interface SistemaActividadHumana <T>{
	
	/**
	 * Metodo principal donde se ejecuta la secuencia de acciones necesarias para completar el sistema de actividad humana conceptualizado
	 * @param actor. Agente que ejecuta el sistema de actividad Humana con propósito definido.
	 */
	public void secuenciaPrincipalDeAcciones(T actor);
	
	/**
	 * Devuelve una nueva instancia del Sistema de activiad humana conceptualziado para ser ejecutado por algun agente.
	 * @return
	 */
	public SistemaActividadHumana getInstance();
	
	/**
	 * Devuelve el paso en el que se encuentra la ejecución del sistema de actividad humana. 
	 * Para efectos de visualización, los sistemas de actividad humana son ejecutados en un numero predefinido de pasos y de tal forma controlar el número de ciclos que le tomará al agente ejecutar el sistema de actividad en su totalidad.
	 * @return int. numero del pasos ejecutados en el sistema de actividad.
	 */
	public int getPaso();
	
	/**
	 * Devuelve el estado en que se encuentra la ejecución del sistema de actividad. Pueden ser: UNSET, READY, RUNNING, DONE.
	 * Describe en que estado de ejecución se encuentra el sistema de actividad, por parte del agente que lo instancia.
	 * @return
	 */
	public String getEstado();
	//public boolean isDone();
	
	/**
	 * Devuelve el nombre generico del sistema de actividad humana.
	 * @return
	 */
	public String getEnunciado();
	
	/**
	 * Devuelve el proposito definido para el MPA
	 * @return Proposito del MPA
	 */
	public Proposito getProposito();
	

}
