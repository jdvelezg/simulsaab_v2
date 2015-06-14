package simulaSAAB.comunicacion;

import simulaSAAB.tareas.SistemaActividadHumana;

public class EjecutarAccionConProposito implements Preposicion {
	
	private SistemaActividadHumana actividad;

	public EjecutarAccionConProposito() {
		// TODO Auto-generated constructor stub
	}
	
	public EjecutarAccionConProposito(SistemaActividadHumana act) {
		
		this.actividad = act;
	}

	public SistemaActividadHumana getActividad() {
		return actividad;
	}

	public void setActividad(SistemaActividadHumana actividad) {
		this.actividad = actividad;
	}
	
	

}
