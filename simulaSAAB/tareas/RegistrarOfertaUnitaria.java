package simulaSAAB.tareas;

import java.util.logging.Level;
import java.util.logging.Logger;

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.agentes.Oferente;
import simulaSAAB.agentes.Productor;
import simulaSAAB.agentes.SiSaab;
import simulaSAAB.agentes.Terreno;
import simulaSAAB.comunicacion.Producto;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.Oferta;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.global.persistencia.MPAConfigurado;
import simulaSAAB.global.persistencia.ProductoConfigurado;
/**
 * Representa el registro de ofertas unitarias en el SISAAB
 * @author lfgomezm
 *
 */
public class RegistrarOfertaUnitaria implements
		SistemaActividadHumana<Oferente> {
	
	/**
	 * Identificador de la oferta unitaria
	 */
	private final int id;
	/**
	 * Registro de la clase usado para depuración <code>Debugging</code>
	 */
	private static Logger LOGGER = Logger.getLogger(RegistrarOfertaUnitaria.class.getName());
	/**
	 * Establece el propósito de la actividad registro de oferta unitaria
	 */
	private static Proposito proposito = new Proposito("Vender productos");
	/**
	 * Enunciado de la tarea
	 */
	private static String Enunciado;
	
	private double CostoEjecucion;
	
	private double DineroInicial;
	
	private Oferta Oferta;	
	/**
	 * Estado actual de la tarea
	 */
	private String Estado;
	/**
	 * Paso actual de la tarea
	 */
	private int paso;
	
	//private final double Tickinicial;
	
	/**
	 * Constructor
	 */
	public RegistrarOfertaUnitaria() {
		
		MPAConfigurado mpa 	= new MPAConfigurado("RegistrarOfertaUnitaria");
		
		this.id				= mpa.getId();
		this.Enunciado		= mpa.getEnunciado();
		this.CostoEjecucion	= mpa.getCosto();		
				
		this.Estado = EstadosActividad.READY.toString();
	}

	@Override
	public void secuenciaPrincipalDeAcciones(Oferente actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
			
			this.Oferta = actor.generarOferta();
			
			if(Oferta !=null){
				
				Estado			= EstadosActividad.RUNNING.toString();			
				DineroInicial 	= new Double(actor.getDinero().getCantidad());
				paso			= 1;
				
				actor.setEstado("RUNNING");	
			}else{
				//Si no tiene ofertas, no ejecuta la actividad
				Estado	= EstadosActividad.DONE.toString();		
			}
					
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){			
			
			switch(this.paso){
			case 1:				
								
				SiSaab.registrarOferta(Oferta);			
				this.paso++;
				
				break;
			case 2:	
				//LOGGER.log(Level.INFO,this.toString()+"paso2 "+actor.getEstado());
				
				/**
				 * Monitorea la Oferta registrada. El agente espera durante la vigencia de la oferta registrada.
				 * La espera es necesaria, dado que el agente debe evaluar su experiencia despuÃ©s de realizada la venta
				 * 
				 * TODO Buscar una forma que el agente ejecute diferentes actividades y luego sea capaz de evaluarlas separadamente.
				 */
				if((!(Oferta.vigente())) || Oferta.vendida())
					paso++;
				
				break;			
			default:				
				//substrae el costo de ejecuciÃ³n del MPA del dinero del agente
				actor.getDinero().subtractCantidad(CostoEjecucion);
				//Calcula la utilidad obtenida al ejecutar el MPA				
				actor.setUltimaUtilidadObtenida(new Double(actor.getDinero().getCantidad()).doubleValue()-DineroInicial);
				this.Estado ="DONE";				
			}
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){			
			//fija estado del actor en IDLE
			actor.setEstado("IDLE");
		}
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		return new RegistrarOfertaUnitaria();
	}

	@Override
	public int getPaso() {
		return paso ;
	}

	@Override
	public String getEstado() {
		return Estado;
	}

	@Override
	public String getEnunciado() {
		return Enunciado;
	}

	@Override
	public Proposito getProposito() {
		return proposito;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public double getCosto() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override 
	public boolean equals(Object obj){
		
		if(obj instanceof SistemaActividadHumana){
			
			SistemaActividadHumana act = (SistemaActividadHumana)obj;			
			return this.id==act.getId();
		}else{
			return false;
		}
	}

}