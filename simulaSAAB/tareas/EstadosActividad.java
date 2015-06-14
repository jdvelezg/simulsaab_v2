package simulaSAAB.tareas;

/**
 * Enumerador que define los posibles estados de un sistema de actividad
 * con propósito definido.
 * 
 * @author dampher
 *
 */
public enum EstadosActividad {
	
	/**
	 * El sistema de actividad no ha sido inicializado
	 */
	UNSET,
	/**
	 * El sistema de actividad está inicializado y listo para ser ejecutado
	 */
	READY,
	/**
	 * El sistema de actividad se encuentra en algún paso de ejecución.
	 */
	RUNNING,
	/**
	 * El sistema de actividad fué completado
	 */
	DONE

}
