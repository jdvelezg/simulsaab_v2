package simulaSAAB.agentes;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.tareas.EstadosActividad;
import simulaSAAB.tareas.SistemaActividadHumana;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Recurso;

/**
 * Representa el agente <code>Camion</code>
 * <p>
 * Agente movil con capacidad de almacenar y transportar productos <code>Recursos</code>
 *  
 * @author jdvelezg
 *
 */
public class Camion implements ObjetoMovil {
		
	protected Geometry geometria;
	
	protected SistemaActividadHumana ActividadVigente;
	
	protected boolean operable;
	/**
	 * Capacidad de carga del vehiculo en kilogramos
	 */
	private double capacidadNeta;
	
	private List<Recurso> carga;
	
	/**
	 * Constructor de la clase
	 */
	public Camion() {
		
		carga 		= new ArrayList<Recurso>();
		operable 	= false;
		
		setCapacidadAleatoria();
	}
	
	/**
	 * Ejecuta el comportamiento del agente en cada ciclo <code>Tick</code> de ejecución
	 */
	public void step () {
		
		if(operable){
			if(ActividadVigente!=null){
				
				if(!(ActividadVigente.getEstado().equalsIgnoreCase(EstadosActividad.DONE.toString()))){
					
					ActividadVigente.secuenciaPrincipalDeAcciones(this);
					
				}else{	
					operable=false;
					SaabContextBuilder.SAABContext.remove(this);				
				}				
			}//EnIf-ActVigente
		}//EndIf-Operable
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
	 * Devuelve la actividad vigente del agente
	 * @return SistemaActividadHumana
	 */
	public SistemaActividadHumana getActividadVigente() {
		return ActividadVigente;
	}
	/**
	 * Asigna una actividad vigente {@link SistemaActividadHumana} al agente
	 * 
	 * @param actividadVigente SistemaActividadHumana a asignar como <code>MPA</code> vignete del agente
	 */
	public void setActividadVigente(SistemaActividadHumana actividadVigente) {
		ActividadVigente = actividadVigente;
		operable = true;
	}
	
	/**
	 * Devuelve la capacidad disponible de carga
	 * 
	 * @return double
	 * 
	 * TODO verificar las unidades de medida y hacer conversiones
	 */
	public double getCapacidadDisponible() {
		/*
		 * Evita referenciacion
		 */
		double capacidad = new Double(capacidadNeta).doubleValue();
		
		if(this.carga.size()!=0)			
			capacidad -= pesoCargado();		
		
		return capacidad;	
	}
	
	/**
	 * Devuelve el número de kilogramos cargados en el camion
	 * <p> 
	 * El producto del recurso cargado debe estar en kilogramos
	 *   
	 * @return double 
	 * 
	 * TODO identificar la unidad de medida del cargamento y hacer las conversiones respectivas
	 */
	public double pesoCargado(){
		
		double cargado = 0;
		if(this.carga.size()!=0){
			
			for(Recurso r: carga){
				cargado += r.getCantidad().doubleValue();
			}			
		}
		
		return cargado;
	}
	
	/**
	 * Devuelve la capacidad neta de carga del agente <code>Camion</code>
	 * @return double
	 */
	public double getCapacidadNeta() {
		return capacidadNeta;
	}
	/**
	 * Fija la capacidad neta de carga del agente <code>Camion</code>
	 * @param capacidad double, capacidad de carga a asignar al agente
	 */
	public void setCapacidadNeta(double capacidad) {
		this.capacidadNeta = capacidad;
	}
	/**
	 * Asigna el recurso pasado como parámetro como parte de la carga del <code>Camion</code>
	 * @param carga Recurso a incluir como carga en el <code>Agente</code>
	 */
	public void cargarMercancia(Recurso carga){
		if(this.carga != null)
			this.carga.add(carga);
	}
	
	/**
	 * Devuelve un {@link Recurso} que representa la carga total almacenada en el transporte
	 * @return Recurso
	 * 
	 * TODO Implementar un iteratvo para cuando existan diferentes tipos de productos cargados
	 */
	public Recurso descargarMercancia(){
				
		Producto producto	= new Producto();
		double cantidad		= 0.0;
		double precioTotal	= 0.0;
				
		for(Recurso r: this.carga){				
			cantidad 	+= r.getCantidad();
			precioTotal	+= r.getCostoUnitario()*r.getCantidad();
		}
		
		Recurso ProductoCargado = new Recurso(producto, cantidad);
		ProductoCargado.setCostoUnitario(precioTotal/cantidad);
		this.carga.clear();	
		
		return ProductoCargado;		
	}
	
	/**
	 * Fija la capacidad de carga del camion un valor aleatorio
	 * <p> 
	 * Entre 16000 y 50000 kilogramos (16 a 50 toneladas)
	 * 
	 * TODO parametrizar los valores <code>MIN<code> - <code>MAX</code> de carga
	 */
	public void setCapacidadAleatoria(){
		capacidadNeta = RandomHelper.nextDoubleFromTo(1000, 5000);
	}

}
