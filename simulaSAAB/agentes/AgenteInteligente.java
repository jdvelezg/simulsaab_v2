package simulaSAAB.agentes;

import java.util.List;

import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.tareas.SistemaActividadHumana;

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
	
	/**
	 * Genera una nueva experiencia de acuerdo con los resultados obtenidos al ejecutar un sistema de actividad
	 */
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
	
	/**
	 * Devuelve el valor de la mayor utilidad historica obtenida por el agente.
	 * @return
	 */
	public Double getMayorUtilidadObtenida();
	
	/**
	 * Fija el valor de la mayor utilidad obtenida
	 */
	public void setMayorUtilidadObtenida(Double valor);
	
	/**
	 * Devuelve las actividades ejecutables por el agente, resultado de su percepcion selectiva del mundo
	 * @return
	 */
	public List<SistemaActividadHumana> getActividadesEjecutables();
	
	/**
	 * Devuelve la actividad vigente del agente
	 * @return
	 */
	public SistemaActividadHumana getActividadVigente();
	
	/**
	 * Devuelve la ultima utilidad obtenida por el agente
	 * @return Double ultima utilidad obtenida
	 */
	public Double getUltimaUtilidadObtenida();
	
	/**
	 * Agrega una nueva experiencia al agente
	 * @param exp
	 */
	public void addExperiencia(Experiencia exp);

}
