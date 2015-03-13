/**
 * 
 */
package simulaSAAB.inteligencia;

import java.util.List;

import simulaSAAB.comunicacion.Experiencia;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * @author dampher
 *
 */
public abstract class Cerebro {

	/**
	 * 
	 */
	public Cerebro() {
		// TODO Auto-generated constructor stub
	}
	
	
	public abstract SistemaActividadHumana tomarDecision(List<SistemaActividadHumana> Actividades);
	
	
	public abstract Experiencia evaluarExperiencia();

}
