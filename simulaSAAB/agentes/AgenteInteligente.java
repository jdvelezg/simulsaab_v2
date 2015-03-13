package simulaSAAB.agentes;

import java.util.List;

import simulaSAAB.comunicacion.Experiencia;

public interface AgenteInteligente {
	
	
	/**
	 * Observa y reconoce las restricciones del medio.
	 * 
	 */
	public void percibirMundoSelectivamente();
	
	/**
	 * Define su propósito vigente
	 */
	public void formarIntenciones();
	
	/**
	 * Escoge la actividad con proposito definido con la que pretende obtener su propósito
	 */
	public void tomarDecisiones();
	
	/**
	 * Ejecuta la actividad con proposito definido vigente
	 */
	public void actuar();
	
	public void juzgarMundoSegunEstandares();
	
	/**
	 * Devuelve el estado del agente. EL agente puede estar en estado de ejecución de tareas(RUNNING), preparado (READY) o desocupado (IDLE)
	 * @return String. Descripcion del estado
	 * IDLE
	 * READY
	 * RUNNING
	 */
	public String getEstado();
	
	/**
	 * Devuelve un array con las experiencias acumuladas por el agente
	 * @return
	 */
	public List<Experiencia> getExperiencia();

}
