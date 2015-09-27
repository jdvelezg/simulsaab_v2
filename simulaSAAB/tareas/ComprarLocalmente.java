package simulaSAAB.tareas;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.agentes.Intermediario;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.global.persistencia.MPAConfigurado;
/**
 * Representa la actividad de compra de productos dentro del centro urbano del productor
 * @author lfgomezm
 *
 */
public class ComprarLocalmente implements SistemaActividadHumana<Intermediario> {
	/**
	 * Identificador de la transacción
	 */
	private final int id;
	/**
	 * Registro de la clase usado para depuración <code>Debugging</code>
	 */
	private static Logger LOGGER = Logger.getLogger(VenderLocalmente.class.getName());
	/**
	 * Propósito de la tarea
	 */
	private final Proposito proposito;
	/**
	 * Enunciado de la tarea
	 */
	private final String Enunciado;
	/**
	 * Punto de venta del producto dento del centro urbano
	 */
	private CentroUrbano lugarVenta;
	
	private double CostoEjecucion;	
	/**
	 * Estado actual de la tarea
	 */
	private String Estado;
	/**
	 * Paso de la tarea
	 */
	private int paso;
	
	private final double Tickinicial;
	
	private double DineroInicial;
	
	private SistemaActividadHumana moverse;
	
	private VenderEnCorabastos venderEnBogota;
	
	private ObjetoMovil transporte;
	/**
	 * Constructor
	 */
	public ComprarLocalmente() {
		
		MPAConfigurado mpa 	=new MPAConfigurado("ComprarLocalmente");
		
		this.id				= mpa.getId();
		this.Enunciado		= mpa.getEnunciado();
		this.proposito 		= mpa.getProposito();
		this.CostoEjecucion	= mpa.getCosto();
		this.paso			= 0;
		this.Estado 		= EstadosActividad.READY.toString();
		
		Tickinicial	= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		
	}

	@Override
	public void secuenciaPrincipalDeAcciones(Intermediario actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
			
			actor.setEstado("RUNNING");
			
			DineroInicial 	= actor.getDinero().getCantidad();			
			lugarVenta 		= actor.getAmbienteLocal();
			venderEnBogota	= new VenderEnCorabastos();
			
			this.Estado = EstadosActividad.RUNNING.toString();
			this.paso	= 1;
			
			//LOGGER.log(Level.INFO,"iniciando..."+this.Estado);
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){
			
			switch(paso){
			
			case 1:
				//LOGGER.log(Level.INFO,"paso1 :");
				/*
				 * si el actor no se encuentra en su centroUrbano
				 * se desplaza al mismo
				 */
				Geometry actorGeom = SaabContextBuilder.SAABGeography.getGeometry(actor);
				if(!lugarVenta.getGeometria().contains(actorGeom)){
					
					Coordinate ubicacionPueblo = lugarVenta.getCentroid().getCoordinate();
					this.moverse = new Moverse(ubicacionPueblo);
					moverse.secuenciaPrincipalDeAcciones(actor);
				}else{
					paso++;
				}
				
			break;
			case 2:
				//LOGGER.info(actor.toString()+" paso2 :capacidad transp "+actor.getTransporte().getCapacidadDisponible());
				/*
				 * escucha y atiende mensajes cfp
				 * hasta que la capacidad de carga de su transporte estÃ¡
				 * completa 
				 */
				actor.atenderMensajes();
				
				Double CurrentTick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
				
				if(actor.getTransporte().getCapacidadDisponible()<=0) // hasta que tenga carga llena
					paso++;
				else if(actor.getTransporte().getCapacidadNeta()!=actor.getTransporte().getCapacidadDisponible() && Tickinicial-CurrentTick > VariablesGlobales.TICKS_UNDIA_DEMORA_MOVIMIENTO*5)//espera una semana con carga incompleta
					paso++;
				
			break;
			case 3:
				//LOGGER.info(actor.toString()+" paso3 :capacidad transp "+actor.getTransporte().getCapacidadDisponible());
				/*
				 * Se desplaza a corabastos-Bogota y vende los productos
				 * (ejecuta la actividad VenderEnCorabastos)
				 */
				this.venderEnBogota.secuenciaPrincipalDeAcciones(actor);
				
				if(venderEnBogota.getEstado().equalsIgnoreCase(EstadosActividad.DONE.toString()))
					paso++;
				
			break;
			default: //LOGGER.log(Level.INFO,"paso-salida :capacidad transp");
				this.Estado = EstadosActividad.DONE.toString();
			}
			
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			
			//substrae el costo de ejecuciÃ³n del MPA del dinero del agente
			actor.getDinero().subtractCantidad(CostoEjecucion);
			//Calcula la utilidad obtenida al ejecutar el MPA
			double valorActual		= new Double(actor.getDinero().getCantidad()).doubleValue();
			double ultimaUtilidad 	= valorActual>DineroInicial?Math.abs(valorActual-DineroInicial):0;	
			actor.setUltimaUtilidadObtenida(ultimaUtilidad);
			//fija estado del actor en IDLE
			actor.setEstado("IDLE");			
		}
		
	}

	@Override
	public SistemaActividadHumana getInstance() {		
		return new ComprarLocalmente();
	}

	@Override
	public int getPaso() {
		return paso;
	}

	@Override
	public String getEstado() {
		return this.Estado;
	}

	@Override
	public String getEnunciado() {
		// TODO Auto-generated method stub
		return this.Enunciado;
	}

	@Override
	public Proposito getProposito() {		
		return this.proposito;
	}

	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public double getCosto() {
		return this.CostoEjecucion;
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