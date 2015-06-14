package simulaSAAB.inteligencia;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IGeneConstraintChecker;
import org.jgap.RandomGenerator;
import org.jgap.UnsupportedRepresentationException;

import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.tareas.SistemaActividadHumana;

public class MPAGene implements Gene {
	
	private static int UniqueIDBase;
	
	private final int UniqueId;
	
	private SistemaActividadHumana MPA;
		
	private Dinero UtilidadObtenida;
	
	private int NumeroEjecuciones;
	
	private int NumeroEjecucionesExitosas;
	
	/**
	 * Contructor
	 */
	public MPAGene() {
		UniqueId = UniqueIDBase + 1;
		UniqueIDBase++;
		UtilidadObtenida = new Dinero(0);
	}
	
	/**
	 * Constructor
	 * @param act. MPA ejecutado
	 * @param utl. Utilidad Obtenida
	 * @param ejec. numero de ejecuciones historicas
	 * @param exitos. Numero de ejecuciones exitosas
	 */
	public MPAGene(SistemaActividadHumana act, Dinero utl, int ejec, int exitos){
		UniqueId = UniqueIDBase + 1;
		UniqueIDBase++;
		
		this.MPA 						= act;
		this.UtilidadObtenida 			= utl;
		this.NumeroEjecuciones 			= ejec;
		this.NumeroEjecucionesExitosas 	= exitos;
	}
	
	
	@Override
	public int compareTo(Object arg0) {
		
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
		
		double ultilidad 	= this.UtilidadObtenida.getCantidad();
		double otrautilidad	= (Double)((MPAGene)arg0).getAllele();

		return Double.compare(ultilidad, otrautilidad);		 
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
	public Gene newGene() {
		
		MPAGene clon = new MPAGene();
				
		clon.setMPA(this.MPA);
		clon.setNumeroEjecuciones(this.NumeroEjecuciones);
		clon.setNumeroEjecucionesExitosas(this.NumeroEjecucionesExitosas);		
		
		return clon;
	}
	
	/**
	 * arg0 Objeto de tipo Dinero o Double
	 */
	@Override
	public void setAllele(Object arg0) {
		
		if(arg0 instanceof Double){
			UtilidadObtenida.addCantidad((Double)arg0);
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
