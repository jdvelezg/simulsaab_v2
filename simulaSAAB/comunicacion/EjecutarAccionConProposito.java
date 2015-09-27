package simulaSAAB.comunicacion;

import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * Representa la <code>preposición "ejecutar acción con propósito"</code> usado en la ontología para comunicación entre los agentes 
 * <p>
 * Expresa la acción de ejecutar una actividad con propósito definido
 * 
 * @author jdvelezg
 *
 */
public class EjecutarAccionConProposito implements Preposicion {
	/**
	 * <code>MPA</code> a ser ejecutado
	 */
	private SistemaActividadHumana actividad;

	/**
	 * Constructor
	 */
	public EjecutarAccionConProposito() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Constructor
	 * @param act SistemaActividadHumana, <code>MPA</code> a ser ejecutado
	 */
	public EjecutarAccionConProposito(SistemaActividadHumana act) {		
		this.actividad = act;
	}
	/**
	 * Devuelve el <code>MPA</code> a ser ejecutado
	 * @return SistemaActividadHumana
	 */
	public SistemaActividadHumana getActividad() {
		return actividad;
	}
	@Override
	public void setActividad(SistemaActividadHumana actividad) {
		this.actividad = actividad;
	}	

}
