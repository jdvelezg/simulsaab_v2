package simulaSAAB.inteligencia;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IGeneConstraintChecker;
import org.jgap.RandomGenerator;
import org.jgap.UnsupportedRepresentationException;
import org.jgap.impl.DoubleGene;
import org.jgap.impl.IntegerGene;

import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.tareas.ProducirCebollaBulbo;
import simulaSAAB.tareas.ProducirCebollaBulbo2;
import simulaSAAB.tareas.SistemaActividadHumana;

/**
 * Implementación de <code>Gene</code> que representa un <code>MPA</code>, para ser usado en un algoritmo genético de busqueda, 
 * como parte del proceso cognitivo d elos agentes inteligentes
 *    
 * @author jdvelezg
 *
 */
public class MPAGene implements Gene {
	
	private static int UniqueIDBase = 0;
	
	private final int UniqueId;
	
	private SistemaActividadHumana MPA;
		
	private Dinero UtilidadObtenida;
	
	private int NumeroEjecuciones;
	
	private int NumeroEjecucionesExitosas;
	
	/**
	 * Contructor
	 */
	public MPAGene(){			
		
		UniqueId = UniqueIDBase + 1;
		UniqueIDBase++;
		UtilidadObtenida = new Dinero(0);
	}
	
	/**
	 * Constructor
	 * @param act MPA ejecutado
	 * @param utl Utilidad Obtenida
	 * @param ejec numero de ejecuciones historicas
	 * @param exitos  Numero de ejecuciones exitosas
	 */
	public MPAGene(SistemaActividadHumana act, Dinero utl, int ejec, int exitos){
				
		this();		
		
		this.MPA 						= act;
		this.UtilidadObtenida 			= utl;
		this.NumeroEjecuciones 			= ejec;
		this.NumeroEjecucionesExitosas 	= exitos;
	}
	
	
	@Override
	public int compareTo(Object arg0) {
		/*
		 * Comparación con null
		 */
		if(arg0 == null)
		{
			return 1;
			
		}
		
		if(this.UtilidadObtenida == null)
		{
			if(((MPAGene)arg0).getAllele() == null)
			{			
				return 0;
				
			}
			else
			{
				return -1;
			}			
		}
		
		/*
		 * Comparacion entre objetos MPAGene
		 */
		if(this.MPA.equals(arg0)){
			
			double ultilidad 	= this.UtilidadObtenida.getCantidad();
			double otrautilidad	= (Double)((MPAGene)arg0).getAllele();
			
			return Double.compare(ultilidad, otrautilidad);
			
		}else{
			return -1;
		}			 
	}

	@Override
	public void applyMutation(int arg0, double arg1) {
		
		
		if(this.MPA instanceof ProducirCebollaBulbo2){
			this.MPA = new ProducirCebollaBulbo();
		}else if(this.MPA instanceof ProducirCebollaBulbo){
			this.MPA = new ProducirCebollaBulbo2();
		}
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
		return this.UtilidadObtenida.getCantidad();
	}

	@Override
	public String getPersistentRepresentation()
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return this.MPA.getEnunciado();
	}

	@Override
	public Gene newGene() {
						 		
		MPAGene clon = new MPAGene();
		
		clon.setMPA(this.MPA);
		clon.setNumeroEjecuciones(this.NumeroEjecuciones);
		clon.setNumeroEjecucionesExitosas(this.NumeroEjecucionesExitosas);
		clon.setUtilidadObtenida(new Dinero(UtilidadObtenida.getCantidad()));
				
		return clon;
	}
	
	/**
	 * arg0 Objeto de tipo Dinero o Double
	 */
	@Override
	public void setAllele(Object arg0) {
		
		if(arg0 instanceof Double){
			UtilidadObtenida.setCantidad((Double)arg0);
		}else if(arg0 instanceof Dinero){
			UtilidadObtenida = (Dinero)arg0;
		}
		

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

	/**
	 * Devuelve el <code>MPA</code> representado por el <code>gen</code>
	 * @return SistemaActividadHumana
	 */
	public SistemaActividadHumana getMPA() {
		return MPA;
	}

	/**
	 * Asigna el <code>MPA</code> representado por el <code>gen</code>
	 * @param mPA <code>MPA</code> representado por el <code>gen</code>
	 */
	public void setMPA(SistemaActividadHumana mPA) {
		MPA = mPA;
	}
	/**
	 * Devuelve la utilidad obtenida configurada en el <code>gen</code>
	 * @return
	 */
	public Dinero getUtilidadObtenida() {
		return UtilidadObtenida;
	}
	/**
	 * Asigna la utilidad obtenida configurada en el <code>gen</code>
	 * @param utilidadObtenida double, utilidad a ser configurada en el <code>gen</code> 
	 */
	public void setUtilidadObtenida(Dinero utilidadObtenida) {
		UtilidadObtenida = utilidadObtenida;
	}
	/**
	 * Devuelve el número de ejecuciones configurados en el <code>gen</code>
	 * @return int
	 */
	public int getNumeroEjecuciones() {
		return NumeroEjecuciones;
	}
	/**
	 * Asigna el número de ejecuciones a configurar en el <code>gen</code>
	 * @param numeroEjecuciones número de ejecuciones a configurar
	 */
	public void setNumeroEjecuciones(int numeroEjecuciones) {
		NumeroEjecuciones = numeroEjecuciones;
	}
	/**
	 * Devuelve el número de ejecuciones exitosas configuradas en el <code>gen</code>
	 * @return
	 */
	public int getNumeroEjecucionesExitosas() {
		return NumeroEjecucionesExitosas;
	}
	/**
	 * Asigna el número de ejecuciones exitosas a configurar en el <code>gen</code>
	 * @param numeroEjecucionesExitosas int, número de ejecuciones exitosas
	 */
	public void setNumeroEjecucionesExitosas(int numeroEjecucionesExitosas) {
		NumeroEjecucionesExitosas = numeroEjecucionesExitosas;
	}
	
	@Override
	public boolean equals(Object obj){
		
		if(obj instanceof MPAGene){			
			MPAGene otroGen = (MPAGene)obj; 
			if(otroGen.getMPA().equals(this.MPA)){			
				return true;
			}else{
				return false;
			}
		}else{ 	
			return false;
		}		
	}

	@Override
	public String getUniqueID() {
		
		return Integer.toString(UniqueId);
	}

	@Override
	public String getUniqueIDTemplate(int arg0) {
		
		return Integer.toString(arg0);
	}

	@Override
	public void setUniqueIDTemplate(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getApplicationData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Configuration getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isCompareApplicationData() {
		// TODO Auto-generated method stub
		return false;
	}	

	@Override
	public void setApplicationData(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCompareApplicationData(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setConstraintChecker(IGeneConstraintChecker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEnergy(double arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
