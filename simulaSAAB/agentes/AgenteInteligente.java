package simulaSAAB.agentes;

import java.util.List;

import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.comunicacion.MensajeACL;
import simulaSAAB.tareas.SistemaActividadHumana;

public interface AgenteInteligente {
	
	
	/**
	 * Observa y reconoce las restricciones del medio.
	 * 
	 */
	void percibirMundoSelectivamente();
	
	/**
	 * Define su propósito vigente
	 */
	void formarIntenciones();
	
	/**
	 * Escoge la actividad con proposito definido con la que pretende obtener su propósito
	 */
	void tomarDecisiones();
	
	/**
	 * Ejecuta la actividad con proposito definido vigente
	 */
	void actuar();
	
	/**
	 * Genera una nueva experiencia de acuerdo con los resultados obtenidos al ejecutar un sistema de actividad
	 */
	void juzgarMundoSegunEstandares();
	
	/**
	 * Devuelve el estado del agente. EL agente puede estar en estado de ejecución de tareas(RUNNING), preparado (READY) o desocupado (IDLE)
	 * @return String. Descripcion del estado
	 * IDLE
	 * READY
	 * RUNNING
	 * WAITING
	 */
	String getEstado();
	
	/**
	 * Asigna estado al agente. EL agente puede estar en estado de ejecución de tareas(RUNNING), preparado (READY) o desocupado (IDLE)
	 * @return String. Descripcion del estado
	 * IDLE
	 * READY
	 * RUNNING
	 * WAITING
	 */
	void setEstado(String Estado);
	
	/**
	 * Cambia la actividad vigente del agente arbitrariamente
	 */
	void setActividadVigente(SistemaActividadHumana nuevaactividad);
	
	/**
	 * Devuelve un array con las experiencias acumuladas por el agente
	 * @return
	 */	
	List<Experiencia> getExperiencia();
	
	/**
	 * Devuelve el valor de la mayor utilidad historica obtenida por el agente.
	 * @return
	 */
	Double getMayorUtilidadObtenida();
	
	/**
	 * Fija el valor de la mayor utilidad obtenida
	 */
	void setMayorUtilidadObtenida(Double valor);
	
	/**
	 * Devuelve las actividades ejecutables por el agente, resultado de su percepcion selectiva del mundo
	 * @return
	 */
	List<SistemaActividadHumana> getActividadesEjecutables();
	
	/**
	 * Devuelve la actividad vigente del agente
	 * @return
	 */
	SistemaActividadHumana getActividadVigente();
	
	/**
	 * Devuelve el nombre del MPA fijado como vigente
	 */
	String printActividadVigente();
	
	/**
	 * Fija la ultima utilidad obtenida del agente.
	 * Al finalizar la ejecución de un MPA, se calcula la utilidad obtenida y 
	 * se fija el valor para ser usado en la evaluación de la experiencia del agente
	 * @param ultimaUtilidadObtenida
	 */
	void setUltimaUtilidadObtenida(Double ultimaUtilidadObtenida);
	
	/**
	 * Devuelve la ultima utilidad obtenida por el agente
	 * @return Double ultima utilidad obtenida
	 */
	Double getUltimaUtilidadObtenida();
	
	/**
	 * Agrega una nueva experiencia al agente
	 * @param exp
	 */
	void addExperiencia(Experiencia exp);
	
	/**
	 * Recibe mensajes ACL, y los guarda en una cola para ser posteriormente atendidos
	 */
	void recibirMensaje(MensajeACL mssg);
	
	/**
	 * Atiende los mensajes que dejo en cola
	 */
	void atenderMensajes();

}
