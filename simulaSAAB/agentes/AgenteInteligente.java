package simulaSAAB.agentes;

import java.util.List;

import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.comunicacion.MensajeACL;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * Representa agentes inteligentes
 * <p>
 * Agente que posee procesos cognitivos para la toma de decisiones y conocimiento basado en la experiencia
 * 
 * @author jdvelezg
 *
 */
public interface AgenteInteligente {
	
	
	/**
	 * Observa y reconoce las restricciones del medio en que se encuentra el agente.
	 * 
	 */
	void percibirMundoSelectivamente();
	
	/**
	 * Define el propósito vigente del agente
	 */
	void formarIntenciones();
	
	/**
	 * Escoge la actividad con proposito definido ({@link SistemaActividadHumana}) con la que el agente pretende cumplir el propósito vigente
	 */
	void tomarDecisiones();
	
	/**
	 * Ejecuta la actividad (<code>MPA</code>) con proposito definido (SistemaActividadHumana) vigente
	 */
	void actuar();
	
	/**
	 * Genera una nueva {@link Experiencia} acorde a los resultados obtenidos después de ejecutar un sistema de actividad humana con propósito definido (SistemaActividadHumana) 
	 */
	void juzgarMundoSegunEstandares();
	
	/**
	 * Devuelve el estado del agente. 
	 * <p>
	 * EL agente puede estar en estado de ejecución de tareas(RUNNING), preparado (READY) o desocupado (IDLE)
	 * 
	 * @return string, descripcion del estado:
	 * 											IDLE
	 * 											READY
	 * 											RUNNING
	 * 											WAITING
	 */
	String getEstado();
	
	/**
	 *  Asigna estado al agente. 
	 * <p>
	 * EL agente puede estar en estado de ejecución de tareas(RUNNING), preparado (READY) o desocupado (IDLE)
	 * 
	 * @param Estado, descripcion del estado:
	 * 											IDLE
	 * 											READY
	 * 											RUNNING
	 * 											WAITING  
	 */
	void setEstado(String Estado);
	
	/**
	 * Asigna la actividad vigente del agente
	 * 
	 * @param nuevaactividad nueva actividad que se le asigna al agente
	 */
	void setActividadVigente(SistemaActividadHumana nuevaactividad);
	
	/**
	 * Devuelve un arreglo con las experiencias acumuladas por el agente
	 * @return list<Experiencia> experiencias del agente
	 */	
	List<Experiencia> getExperiencia();
	
	/**
	 * Devuelve la mayor utilidad obtenida por el agente
	 * @return double mayor utilidad
	 */
	Double getMayorUtilidadObtenida();
	
	/**
	 * Fija el atributo <code>Mayor Utilidad Obtenida</code>
	 * @param valor valor a asignar
	 */
	void setMayorUtilidadObtenida(Double valor);
	
	/**
	 * Devuelve las actividades (SistemaActividadHumana) ejecutables por el agente.
	 * @return list<SistemaActividadHumana> <code>MPA</code>s ejecutables por el agente
	 */
	List<SistemaActividadHumana> getActividadesEjecutables();
	
	/**
	 * Devuelve la actividad (SistemaActividadHumana) vigente del agente
	 * @return SistemaActividadHumana <code>MPA</code> vigente del agente
	 */
	SistemaActividadHumana getActividadVigente();
	
	/**
	 * Devuelve el nombre del <code>MPA</code> (SistemaActividadHumana) fijado como vigente
	 * @return String nombre del <code>MPA</code> vigente
	 */
	String printActividadVigente();
	
	/**
	 * Fija la ultima utilidad obtenida del agente
	 * <p>
	 * Al finalizar la ejecución de un <code>MPA</code> (SistemaActividadHumana), se calcula la utilidad obtenida y 
	 * se fija el valor para ser usado en la evaluación de la experiencia del agente
	 * 
	 * @param ultimaUtilidadObtenida double, utilidad obtenida en la ejecucion de un <code>MPA</code>
	 */
	void setUltimaUtilidadObtenida(Double ultimaUtilidadObtenida);
	
	/**
	 * Devuelve la ultima utilidad obtenida por el agente
	 * 
	 * @return double, última utilidad obtenida
	 */
	Double getUltimaUtilidadObtenida();
	
	/**
	 * Agrega una nueva experiencia al agente
	 *  
	 * @param exp experiencia a agregar
	 */
	void addExperiencia(Experiencia exp);
	
	/**
	 * Envia un mensaje a un agente inteligente
	 * 
	 * @param mensaje mensaje a enviar
	 * @param receptor receptor del mensaje
	 */
	void enviarMensaje(MensajeACL mensaje, AgenteInteligente receptor);
	
	/**
	 * Recibe mensajes ACL para ser atendidos posteriormente
	 * @param mssg mensaje a recibir
	 */
	void recibirMensaje(MensajeACL mssg);
	
	/**
	 * Procesa las respuestas recibidas de un mensaje enviado
	 * @param mensajeID identificador del mensaje enviado
	 */
	void buscarRespuesta(Integer mensajeID);
	
	/**
	 * Atiende los mensajes pendientes
	 */
	void atenderMensajes();
	
	/**
	 * Devuelve el identificador único del agente
	 * @return int
	 */
	int getId();

}
