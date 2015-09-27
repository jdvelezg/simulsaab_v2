package simulaSAAB.tareas;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.agentes.Camioneta;
import simulaSAAB.agentes.Demandante;
import simulaSAAB.agentes.Tienda;
import simulaSAAB.agentes.VendedorFinal;
import simulaSAAB.comunicacion.Demanda;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.OrdenDeCompra;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.ReciboCompra;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.contextos.ObjetoMovil;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.persistencia.AgentTrackObservable;
import simulaSAAB.global.persistencia.MPAConfigurado;
import simulaSAAB.tareas.ComprarEnTienda.CompraTrack;
/**
 * Representa la compra de productos en corabastos
 * @author lfgomezm
 *
 */
public class ComprarEnCorabastos implements
		SistemaActividadHumana<VendedorFinal> {
	/**
	 * Identificador de la transacción
	 */
	private final int id;
	/**
	 * Registro de la clase usado para depuración <code>Debugging</code>
	 */
	private static Logger LOGGER = Logger.getLogger(VenderLocalmente.class.getName());
	/**
	 * <code>Propósito</code> de la tarea
	 */
	private final Proposito proposito;
	/**
	 * Enunciado de la tarea
	 */
	private final String Enunciado;
	
	private final CorabastosTrack OBSERVABLE = new CorabastosTrack();
	/**
	 * Ubicación de venta del producto
	 */
	private CentroUrbano lugarVenta;
	
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
	/**
	 * Instancia del <code>MPA</code> {@link ProcesoAgenteHumano}
	*/
	private SistemaActividadHumana moverse;
	/**
	 * Tipo de transporte para movilizar el producto
	 */
	private ObjetoMovil transporte;
	/**
	 * Instancia de <code>Coordinate</code>, vía de acceso corabastos
	 */
	private Coordinate corabastosRoadAcces;
	/**
	 * Instancia de <code>Demanda</code>, caracteristicas de la demanda propia del agente
	 */
	private Demanda compraDemandada;
	
	/**
	 * Constructor
	 */
	public ComprarEnCorabastos() {
		
		MPAConfigurado mpa 	=new MPAConfigurado("ComprarEnCorabastos");
		
		this.id				= mpa.getId();
		this.Enunciado		= mpa.getEnunciado();
		this.proposito 		= mpa.getProposito();
		this.CostoEjecucion	= mpa.getCosto();
		this.paso			= 0;
		this.Estado 		= EstadosActividad.READY.toString();
		
		Tickinicial	= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
	}

	@Override
	public void secuenciaPrincipalDeAcciones(VendedorFinal actor) {		
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){
			
			DineroInicial 			= new Double(actor.getDinero().getCantidad()).doubleValue();			
			compraDemandada			= actor.generarDemanda();
			
			if(DineroInicial>0){
				
				corabastosRoadAcces 	= SaabContextBuilder.Corabastos.getRoadAccess();
				moverse 				= new Moverse(corabastosRoadAcces);	
				
				Coordinate AgentCoord	= SaabContextBuilder.SAABGeography.getGeometry(actor).getCoordinate();
				GeometryFactory geofact = new GeometryFactory();
				Geometry transGeom		= geofact.createPoint(AgentCoord);
				this.transporte 		= new Camioneta();
				this.transporte.setGeometria(transGeom);
				
				SaabContextBuilder.SAABContext.add(transporte);
				SaabContextBuilder.SAABGeography.move(transporte, transGeom);
				
				this.paso	= 1;
				this.Estado = EstadosActividad.RUNNING.toString();
				actor.setEstado("RUNNING");				
			}else{
				/*
				 * Si no posee dinero, no ejecuta la atividad
				 */
				this.Estado = EstadosActividad.DONE.toString();
			}
							
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){
			
			//Coordinate ubicacionActual = SaabContextBuilder.SAABGeography.getGeometry(actor).getCoordinate();
			
			switch(paso){
			
			case 1: 
				/*
				 *Si no se encuentra en corabastos, se dezplaza hasta alla 
				 */				
				if(!moverse.getEstado().equalsIgnoreCase(EstadosActividad.DONE.toString())){	
					
					moverse.secuenciaPrincipalDeAcciones(transporte);
				}else{
					paso++;
				}				
				break;
			case 2:
				/*
				 * Compra los productos que suplan su demanda
				 */				
				SaabContextBuilder.Corabastos.venderProducto(compraDemandada.getNombreproducto(), compraDemandada.getCantidad(),(Demandante)actor);
				paso++;
				break;
			case 3:
				/*
				 * Vuelve a su ubicacion inicial
				 */				
				Coordinate destino	= actor.getTiendas().get(0).getGeometria().getCoordinate();
				moverse = new Moverse(destino);					
				paso++;
								
				break;				
			case 4:
								
				if(!moverse.getEstado().equalsIgnoreCase(EstadosActividad.DONE.toString())){
					
					moverse.secuenciaPrincipalDeAcciones(transporte);
				}else{
					
					SaabContextBuilder.SAABContext.remove(transporte);
					actor.gestionarPedidos();
					this.CostoEjecucion += moverse.getCosto();
					paso++;
					
				}
				break;			
			default:
				this.Estado = EstadosActividad.DONE.toString();
			}
			
		}else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){
			
			//substrae el costo de ejecuciÃ³n del MPA del dinero del agente
			actor.getDinero().subtractCantidad(CostoEjecucion);			
			//fija estado del actor en IDLE
			actor.setEstado("IDLE");			
		}
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		return new ComprarEnCorabastos();
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
		return id;
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
	
	/**
	 * Clase anidada a la que se le delega la funcionalidad <code>observable</code> 
	 * de la clase <code>ComprarEnCorabastos</code>
	 * @author lfgomezm
	 *
	 */
	public class CorabastosTrack extends AgentTrackObservable{
		
		private String Tienda;
		private Double tick;		
		private String vendedorID;
		private String producto;
		private Double precioUnitario;
		private Double cantidad;
		
		
		/**
		 * Constructor
		 */
		public CorabastosTrack(){
			super();
		}
		
		/**
		 * Actualiza los valores y reporta a los observadores
		 * 
		 * @param producto nombre del producto comprado
		 * @param precioUnitario precio unitario del producto en la tienda
		 * @param cantidad cantidad comprada
		 */
		public void compraEfectuada(String producto, double cantidad, Recurso compra){
			
			this.tick			= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.Tienda 		= "CORABASTOS";
			this.vendedorID		= "CORABASTOS";
			this.producto 		= producto;
			this.cantidad		= compra.getCantidad();
			this.precioUnitario	= compra.getCostoUnitario();
			
			if(this.cantidad !=0){
				super.setChanged();
				super.notifyObservers(this);
			}			
		}
		
		@Override
		public String dataLineString(String separador) {
			
			return tick.toString()+separador+Tienda.toString()+separador+vendedorID+separador+producto+separador+precioUnitario.toString()+separador+cantidad.toString()+separador;
		}

		@Override
		public String dataLineStringHeader(String separador) {
			
			return "Tick"+separador+"Tienda"+separador+"Vendedor_ID"+separador+"Producto"+separador+"Precio_Unitario"+separador+"cantidad"+separador;
		}
		
		
	}

}