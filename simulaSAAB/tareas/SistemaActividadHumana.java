package simulaSAAB.tareas;

import simulaSAAB.comunicacion.Proposito;

/**
 * Interfaz para representar los sistemas de actividad humana
 * @author lfgomezm
 *
 * @param <T> agente
 */
public interface SistemaActividadHumana <T>{
	
	/**
	 * Ejecuta la secuencia de acciones necesarias para completar el sistema de actividad humana conceptualizado
	 * @param actor agente que ejecuta el sistema de actividad humana con propósito definido
	 */
	public void secuenciaPrincipalDeAcciones(T actor);
	
	/**
	 * Devuelve una nueva instancia del Sistema de activiad humana conceptualziado para ser ejecutado por algún agente
	 * @return SistemaActividadHumana
	 */
	public SistemaActividadHumana getInstance();
	
	/**
	 * Devuelve el paso en el que se encuentra la ejecución del sistema de actividad humana. 
	 * Para efectos de visualización, los sistemas de actividad humana son ejecutados en un número predefinido de pasos, para de tal forma controlar el número de ciclos que le tomará al agente ejecutar el sistema de actividad en su totalidad.
	 * @return int, número de pasos ejecutados en el sistema de actividad humana
	 */
	public int getPaso();
	
	/**
	 * Devuelve el estado en que se encuentra la ejecución del sistema de actividad. Pueden ser: UNSET, READY, RUNNING, DONE.
	 * Describe en que estado de ejecución se encuentra el sistema de actividad, por parte del agente que lo instancia.
	 * @return string, estado
	 */
	public String getEstado();
	//public boolean isDone();
	
	/**
	 * Devuelve el nombre generico del sistema de actividad humana.
	 * @return enunciado string
	 */
	public String getEnunciado();
	
	/**
	 * Devuelve el propósito definido para el MPA
	 * @return proposito propósito del MPA
	 */
	public Proposito getProposito();
	
	/**
	 * Devuelve el Id del MPA
	 * @return id int 
	 */
	public int getId();
	
	/**
	 * Devuelve el costo de ejecución de la actividad en COP
	 * @return costo double
	 */
	double getCosto();
	

}