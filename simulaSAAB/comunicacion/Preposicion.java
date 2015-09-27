package simulaSAAB.comunicacion;

import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * Representa el concepto de <code>preposición</code> usado en la ontología para comunicación entre los agentes
 * 
 * @author jdvelezg
 *
 */
public interface Preposicion {
	
	/**
	 * Asigna la actividad asociada a la preposición
	 * @param act actividad asociada a la preposición
	 */
	void setActividad(SistemaActividadHumana act);

}
