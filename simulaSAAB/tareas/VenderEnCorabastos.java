package simulaSAAB.tareas;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.agentes.Intermediario;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.SaabContextBuilder;
/**
 * Representa la venta de productos en Corabastos
 * 
 * @author lfgomezm
 */
public class VenderEnCorabastos implements
		SistemaActividadHumana<Intermediario> {
	
	/**
	 * Propósito de la tarea
	 */
	private Proposito proposito;
	/**
	 * Enunciado de la tarea
	 */
	private String Enunciado;
	
	private double CostoEjecucion;	
	/**
	 * Estado actual de la tarea
	 */
	private String Estado;
	/**
	 * Paso actual de la tarea
	 */
	private int paso;
	
	private final double Tickinicial;
	
	private double DineroInicial;
	
	private Transitar moverse;
	
	private ObjetoMovil transporte;
	
	private Coordinate corabastosRoadAcces;
	
	/**
	 * Constructor
	 */
	public VenderEnCorabastos() {
		
		this.proposito 	= new Proposito("Vender");
		this.Enunciado	= "";
		
		this.Estado 	= EstadosActividad.READY.toString();
		this.paso		= 0;
		
		Tickinicial	= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
	}

	@Override
	public void secuenciaPrincipalDeAcciones(Intermediario actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
			
			DineroInicial 		= new Double(actor.getDinero().getCantidad()).doubleValue();	//se crea nuevo valor para evitar referencia		
			corabastosRoadAcces = SaabContextBuilder.Corabastos.getRoadAccess();			
			Coordinate ubicacion= actor.getAmbienteLocal().getRoadAccess();
			moverse 			= new Transitar(ubicacion,corabastosRoadAcces);
			
			actor.setEstado("RUNNING");			
			Estado 				= EstadosActividad.RUNNING.toString();
			paso				= 1;
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){
			
			//Coordinate ubicacionActual = SaabContextBuilder.SAABGeography.getGeometry(actor).getCoordinate();
			
			switch(paso){
			
			case 1:
				/*
				 *Si no se encuentra en corabastos, se dezplaza hasta alla 
				 */							
				moverse.secuenciaPrincipalDeAcciones(actor);
				
				if(moverse.getEstado().equalsIgnoreCase(EstadosActividad.DONE.toString())){
					 
					paso++;
				}				
				break;
			case 2:
				/*
				 * vende su cargamento de productos
				 */
				actor.fijarPrecioVenta(moverse.getCosto());
				/*
				 * Corabastos::compraProductos realiza la transaccion comercial correspondiente
				 * para la compra del producto
				 */
				SaabContextBuilder.Corabastos.compraProductos(actor.descargarMercancia(), actor);
				paso++;

				break;
			case 3:
				/*
				 * Vuelve a su CentroUrbano de operacion
				 */
				moverse.repeatBackwards(actor);
				paso++;
				
				break;
			case 4:
				
				moverse.secuenciaPrincipalDeAcciones(actor);
				
				if(moverse.getEstado().equalsIgnoreCase(EstadosActividad.DONE.toString())){
					paso++;
					this.CostoEjecucion += moverse.getCosto();
				}
					
					
				break;
			default:
				this.Estado = EstadosActividad.DONE.toString();
			}
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			
			//substrae el costo de ejecuciÃ³n del MPA del dinero del agente
			actor.getDinero().subtractCantidad(CostoEjecucion);
			//Calcula la utilidad obtenida al ejecutar el MPA				
			actor.setUltimaUtilidadObtenida(new Double(actor.getDinero().getCantidad()).doubleValue()-DineroInicial);
			//fija estado del actor en IDLE
			actor.setEstado("IDLE");			
		}		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		return new VenderEnCorabastos();
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
		return this.Enunciado;
	}

	@Override
	public Proposito getProposito() {
		return this.proposito;
	}

	@Override
	public int getId() {
		return 0;
	}
	
	@Override
	public double getCosto() {		
		return this.CostoEjecucion;
	}

}