package simulaSAAB.agentes;

import repast.simphony.engine.schedule.ScheduledMethod;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.tareas.ComprarEnTienda;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

public class Consumidor implements AgenteReactivo, ObjetoMovil {
	
	private ComprarEnTienda actividadVigente;
	
	private Geometry geometria;
	
	private String Estado;
	
	/**
	 * Constructor
	 */
	public Consumidor() {
		
		actividadVigente = new ComprarEnTienda();
	}
	
	/**
	 * Ubica la tienda mas cercana y compra productos
	 */
	@ScheduledMethod (start = 1, interval = 1)
	public void comprarEnTienda(){
		
		actividadVigente.secuenciaPrincipalDeAcciones(this);				
	}

	@Override
	public Object getObject() {
		// TODO Auto-generated method stub
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

	public String getEstado() {
		return Estado;
	}

	public void setEstado(String estado) {
		Estado = estado;
	}	
	

}
