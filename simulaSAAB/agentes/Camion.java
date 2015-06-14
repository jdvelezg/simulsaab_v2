package simulaSAAB.agentes;

import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.engine.schedule.ScheduledMethod;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.tareas.EstadosActividad;
import simulaSAAB.tareas.SistemaActividadHumana;

public class Camion implements ObjetoMovil {
	
	private Geometry geometria;
	
	private SistemaActividadHumana ActividadVigente;
	
	/**
	 * Constructor
	 */
	public Camion() {
		
	}
	
	/**
	 * Metodo que ejecuta el comportamiento del agente en cada ciclo de reloj enviado por repast
	 */
	@ScheduledMethod (start = 1, interval = 1)
	public void step () {
		
		if(ActividadVigente!=null){
			
			if(!(ActividadVigente.getEstado().equalsIgnoreCase(EstadosActividad.DONE.toString()))){
				
				ActividadVigente.secuenciaPrincipalDeAcciones(this);
				
			}else{
				
				SaabContextBuilder.SAABContext.remove(this);				
			}				
		}					
	}

	@Override
	public Object getObject() {
		return this;
	}

	@Override
	public void setGeometria(Geometry geom) {		
		this.geometria = geom;
	}

	@Override
	public Geometry getGeometria() {
		return this.geometria;
	}

	public SistemaActividadHumana getActividadVigente() {
		return ActividadVigente;
	}

	public void setActividadVigente(SistemaActividadHumana actividadVigente) {
		ActividadVigente = actividadVigente;
	}

	
	
	

}
