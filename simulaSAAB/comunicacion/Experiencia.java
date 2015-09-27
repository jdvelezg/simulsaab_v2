package simulaSAAB.comunicacion;

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.agentes.AgenteInteligente;
import simulaSAAB.agentes.Productor.ProductorTrack;
import simulaSAAB.global.persistencia.AgentTrackObservable;
import simulaSAAB.inteligencia.MPAGene;
import simulaSAAB.tareas.SistemaActividadHumana;

import org.jgap.Chromosome;
import org.jgap.Gene;
import org.jgap.impl.DoubleGene;

/**
 * Representa el concepto de <code>experiencia</code> usado en la ontología para comunicación y procesos cognitivos de los agentes inteligentes
 * 
 * @author jdvelezg
 *
 */
public class Experiencia implements Concepto {
	
	/**
	 * agrega funcionalidades <code>observable</code> a la clase
	 */
	public ExperienciaTrack OBSERVABLE = new ExperienciaTrack();
	
	private String Concepto;
	
	private SistemaActividadHumana ActividadEjecutada;
	
	private Dinero UtilidadObtenida;
	
	private int NumeroEjecuciones;
	
	private int NumeroEjecucionesExitosas;
	
	private AgenteInteligente Agente;
	/**
	 * Instancia de {@link MPAGene} a ser usada en los algoritmos cognitivos de los agnetes inteligentes
	 */
	private MPAGene MPAExperienceGene;
	
	/**
	 * Constructor
	 */
	public Experiencia() {
		this.NumeroEjecuciones	= 1;
	}
	/**
	 * Constructor
	 * @param act actividad experimentada
	 * @param utilidad utilidad obtenida
	 */
	public Experiencia(SistemaActividadHumana act, Dinero utilidad){
		
		this.ActividadEjecutada	= act;
		this.UtilidadObtenida	= utilidad;
		this.NumeroEjecuciones	= 1;
		
		MPAExperienceGene 	= new MPAGene(act,utilidad,NumeroEjecuciones, NumeroEjecucionesExitosas);
	}

	/**
	 * Devuelve el concepto asignado a la experiencia
	 * @return string 
	 */
	public String getConcepto() {
		return Concepto;
	}

	/**
	 * Asigna el concepto fijado para la experiencia
	 * @param concepto
	 */
	public void setConcepto(String concepto) {
		Concepto = concepto;
	}

	/**
	 * Devuelve la actividad <code>MPA</code> correspondiente a la experiencia
	 * @return SistemaActividadHumana
	 */
	public SistemaActividadHumana getActividadEjecutada() {
		return ActividadEjecutada;
	}

	/**
	 * Asigna la actividad <code>MPA</code> correspondiente a la experiencia
	 * @param actividadEjecutada
	 */
	public void setActividadEjecutada(SistemaActividadHumana actividadEjecutada) {
		
		ActividadEjecutada = actividadEjecutada;
		if(this.UtilidadObtenida!=null){
			setGene();
		}
	}
	/**
	 * Devuelve la utilidad obtenida en la experiencia
	 * @return Dinero
	 */
	public Dinero getUtilidadObtenida() {
		return UtilidadObtenida;
	}
	/**
	 * Asigna la utilidad obtenida en la experiencia
	 * @param utilidadObtenida utilidad obtenida al ejecutar el <code>MPA</code> configurado en la experiencia
	 */
	public void setUtilidadObtenida(Dinero utilidadObtenida) {
		
		UtilidadObtenida = utilidadObtenida;
		MPAExperienceGene.setUtilidadObtenida(UtilidadObtenida);		
	}
	/**
	 * Devuelve el <code>gen</code> que representa la <code>experiencia</code>
	 * @return MPAGene
	 */
	public MPAGene getGene() {
		return MPAExperienceGene;
	}	
	/**
	 * Devuelve el número de ejecuciones exitosas experimentadas para el <code>MPA</code>
	 * @return int
	 */
	public int getNumeroEjecucionesExitosas() {
		return NumeroEjecucionesExitosas;
	}
	/**
	 * Agrega una ejecución exitosa al total de ejecuciones exitosas experimentadas para el <code>MPA</code>
	 */
	public void addNumeroEjecucionesExitosas() {
		NumeroEjecucionesExitosas++;
		MPAExperienceGene.setNumeroEjecucionesExitosas(NumeroEjecucionesExitosas);
	}

	/**
	 * Crea un gen que representa la <code>experiencia</code> para ser usado en los algoritmos genéticos que hacen parte de los procesos cognitivos de los agentes inteligentes
	 *  
	 */
	private void setGene(){
		 if(this.MPAExperienceGene==null){			 
			 MPAExperienceGene 	= new MPAGene(this.ActividadEjecutada,this.UtilidadObtenida, this.NumeroEjecuciones, this.NumeroEjecucionesExitosas);		 
		 }else{
			 MPAExperienceGene.setUtilidadObtenida(UtilidadObtenida);
			 MPAExperienceGene.setNumeroEjecuciones(NumeroEjecuciones);
			 MPAExperienceGene.setNumeroEjecucionesExitosas(NumeroEjecucionesExitosas);
		 }
	 }
	
	/**
	 * Agrega una unidad al número de ejecuciones asociados en la experiencia
	 */
	public void addNumeroEjecuciones(){
		NumeroEjecuciones++;
		MPAExperienceGene.setNumeroEjecuciones(NumeroEjecuciones);
	}
	/**
	 * Devuelve el agente inteligente al que le corresponde la experiencia
	 * @return AgenteInteligente
	 */
	public AgenteInteligente getAgente() {
		return Agente;
	}
	/**
	 * Asigna el agente al que le corresponde la experiencia
	 * @param agente agente que crea la experiencia
	 */
	public void setAgente(AgenteInteligente agente) {
		Agente = agente;
	}
	
	/**
	 * Clase anidada a la que se le delega la funcionalidad <code>obserbable</code> a la clase <code>Experiencia</code>
	 * @author jdvelezg
	 *
	 */
	public class ExperienciaTrack extends AgentTrackObservable{
		
		private Double tick;
		private String AgenteID;
		private String actividadEjecutada;
		private Double UtilidadObtenida;
		private Integer Ejecuciones;
		private Integer EjecucionesExitosas;
		
		
		public ExperienciaTrack(){
			super();
		}
		/**
		 * reporta cuando los parámetros de la experiencia han sido modificados
		 */
		public void experienciaActualizada(){
			
			this.tick 				= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.AgenteID			= Experiencia.this.Agente.toString();
			this.actividadEjecutada	= Experiencia.this.ActividadEjecutada.toString();
			this.UtilidadObtenida	= Experiencia.this.UtilidadObtenida.getCantidad();
			this.Ejecuciones		= Experiencia.this.NumeroEjecuciones;
			this.EjecucionesExitosas= Experiencia.this.NumeroEjecucionesExitosas;
			
			super.setChanged();
			super.notifyObservers(this);
		}

		@Override
		public String dataLineString(String separador) {
			
			return tick+separador+AgenteID+separador+actividadEjecutada+separador+UtilidadObtenida.toString()+separador+
																		Ejecuciones.toString()+separador+EjecucionesExitosas.toString()+separador;
		}

		@Override
		public String dataLineStringHeader(String separador) {
			
			return "tick"+separador+"AgenteID"+separador+"actividadEjecutada"+separador+"UtilidadObtenida"+separador+"Ejecuciones"+separador+"EjecucionesExitosas"+separador;
		}
		
	}
	

}
