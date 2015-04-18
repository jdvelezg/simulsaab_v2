package simulaSAAB.inteligencia;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.RandomGenerator;
import org.jgap.UnsupportedRepresentationException;

import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.tareas.SistemaActividadHumana;

public class MPAGene implements Gene {
	
	private SistemaActividadHumana MPA;
		
	private Dinero UtilidadObtenida;
	
	private int NumeroEjecuciones;
	
	private int NumeroEjecucionesExitosas;
	
	/**
	 * Contructor
	 */
	public MPAGene() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor
	 * @param act. MPA ejecutado
	 * @param utl. Utilidad Obtenida
	 * @param ejec. numero de ejecuciones historicas
	 * @param exitos. Numero de ejecuciones exitosas
	 */
	public MPAGene(SistemaActividadHumana act, Dinero utl, int ejec, int exitos){
		this.MPA 						= act;
		this.UtilidadObtenida 			= utl;
		this.NumeroEjecuciones 			= ejec;
		this.NumeroEjecucionesExitosas 	= exitos;
	}
	
	
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		if(MPA.equals(arg0)){
			return 1;
		}else{
			return 0;
		}
		 
	}

	@Override
	public void applyMutation(int arg0, double arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Devuelve un Double con el valor de la utilidad obtenida
	 */
	@Override
	public Object getAllele() {
		// TODO Auto-generated method stub
		return this.UtilidadObtenida.getCantidad();
	}

	@Override
	public String getPersistentRepresentation()
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return this.MPA.getEnunciado();
	}

	@Override
	public Gene newGene(Configuration arg0) {
		// TODO Auto-generated method stub			
		return new MPAGene();
	}
	
	/**
	 * arg0 Objeto de tipo Dinero
	 */
	@Override
	public void setAllele(Object arg0) {
		this.UtilidadObtenida = (Dinero)arg0;

	}

	@Override
	public void setToRandomValue(RandomGenerator arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValueFromPersistentRepresentation(String arg0)
			throws UnsupportedOperationException,
			UnsupportedRepresentationException {
		// TODO Auto-generated method stub
		

	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 1;
	}


	public SistemaActividadHumana getMPA() {
		return MPA;
	}


	public void setMPA(SistemaActividadHumana mPA) {
		MPA = mPA;
	}

	public int getNumeroEjecuciones() {
		return NumeroEjecuciones;
	}

	public void setNumeroEjecuciones(int numeroEjecuciones) {
		NumeroEjecuciones = numeroEjecuciones;
	}

	public int getNumeroEjecucionesExitosas() {
		return NumeroEjecucionesExitosas;
	}

	public void setNumeroEjecucionesExitosas(int numeroEjecucionesExitosas) {
		NumeroEjecucionesExitosas = numeroEjecucionesExitosas;
	}
	
	
	

}
