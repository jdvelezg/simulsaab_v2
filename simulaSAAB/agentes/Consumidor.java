package simulaSAAB.agentes;

import repast.simphony.engine.schedule.ScheduledMethod;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.tareas.ComprarEnTienda;
import simulaSAAB.tareas.EstadosActividad;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Representa un agente <code>Consumidor</code>
 * <p>
 * Agente Reactivo cuyas acciones estan encaminadas a la compra de productos en establecimeintos de venta al detal
 * 
 * @author jdvelezg
 *
 */
public class Consumidor implements AgenteReactivo, ObjetoMovil {
	
	/**
	 * Actividad Vigente del agente
	 */
	private ComprarEnTienda actividadVigente;
	/**
	 * Dinero del agente
	 */
	private Dinero dinero;
	/**
	 * Geometria del agente
	 */
	private Geometry geometria;
	/**
	 * Estado del agente
	 */
	private String Estado;
	
	/**
	 * Constructor de la <code>Clase</code>
	 */
	public Consumidor() {
		
		actividadVigente = new ComprarEnTienda();
		this.dinero		 = new Dinero(100000000);
	}
	
	/**
	 * Ejecuta el comportamiento del agente según el ciclo <code>Tick</code> de ejecución
	 * <p>
	 * Ubica una tienda en el contexto distrital y compra productos en la misma, <code>start = 600, interval = 2</code>
	 */
	@ScheduledMethod (start = 600, interval = 2)
	public void comprarEnTienda(){
		/*
		 * Si la actividad ha sido termianda, la repite
		 */
		if(this.actividadVigente.getEstado().equalsIgnoreCase(EstadosActividad.DONE.toString())){
			
			this.actividadVigente = new ComprarEnTienda();
		}else{
			actividadVigente.secuenciaPrincipalDeAcciones(this);
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
	/**
	 * Devuelve el estado del Agente
	 * @return String
	 */
	public String getEstado() {
		return Estado;
	}
	/**
	 * Asigna el estado al agente
	 * @param estado nuevo estado a asignar al agente
	 */
	public void setEstado(String estado) {
		Estado = estado;
	}
	/**
	 * Devuelve el {@link Dinero} del agente
	 * @return dinero
	 */
	public Dinero getDinero() {
		return dinero;
	}
	/**
	 * Asigna <code>Dinero<code> al agente
	 */
	public void setDinero(Dinero dinero) {
		this.dinero = dinero;
	}		

}
