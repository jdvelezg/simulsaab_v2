package simulaSAAB.comunicacion;

import simulaSAAB.inteligencia.MPAGene;
import simulaSAAB.tareas.SistemaActividadHumana;

import org.jgap.Chromosome;
import org.jgap.Gene;
import org.jgap.impl.DoubleGene;

public class Experiencia implements Concepto {
	
	private String Concepto;
	
	private SistemaActividadHumana ActividadEjecutada;
	
	private Dinero UtilidadObtenida;
	
	private int NumeroEjecuciones;
	
	private int NumeroEjecucionesExitosas;
	
	//private int PuntajeUtilidad;
	
	private MPAGene MPAExperienceGene;
	

	public Experiencia() {
		this.NumeroEjecuciones	= 1;
	}
	
	public Experiencia(SistemaActividadHumana act, Dinero utilidad){
		
		this.ActividadEjecutada	= act;
		this.UtilidadObtenida	= utilidad;
		this.NumeroEjecuciones	= 1;
		
		MPAExperienceGene 	= new MPAGene(act,utilidad,NumeroEjecuciones, NumeroEjecucionesExitosas);
	}


	public String getConcepto() {
		return Concepto;
	}


	public void setConcepto(String concepto) {
		Concepto = concepto;
	}


	public SistemaActividadHumana getActividadEjecutada() {
		return ActividadEjecutada;
	}


	public void setActividadEjecutada(SistemaActividadHumana actividadEjecutada) {
		
		ActividadEjecutada = actividadEjecutada;
		if(this.UtilidadObtenida!=null){
			setGene();
		}
	}

	public Dinero getUtilidadObtenida() {
		return UtilidadObtenida;
	}

	public void setUtilidadObtenida(Dinero utilidadObtenida) {
		
		UtilidadObtenida = utilidadObtenida;
		if(this.ActividadEjecutada!=null){
			setGene();
		}
	}

	public Gene getGene() {
		return MPAExperienceGene;
	}	
	
	public int getNumeroEjecucionesExitosas() {
		return NumeroEjecucionesExitosas;
	}

	public void addNumeroEjecucionesExitosas() {
		NumeroEjecucionesExitosas++;
	}

	/**
	 * Crea el cromosoma usable en el GA, en caso de no haber sido inicializado
	 */
	private void setGene(){
		 if(this.MPAExperienceGene==null){			 
			 MPAExperienceGene 	= new MPAGene(this.ActividadEjecutada,this.UtilidadObtenida, this.NumeroEjecuciones, this.NumeroEjecucionesExitosas);		 
		 }		 
	 }
	
	/**
	 * Agrega una unidad al número de ejecuciones asociados en la experiencia
	 */
	public void addNumeroEjecuciones(){
		NumeroEjecuciones++;
	}
	
	

}
