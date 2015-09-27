package simulaSAAB.tareas;

import java.util.Iterator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.collections.IndexedIterable;
import simulaSAAB.agentes.Consumidor;
import simulaSAAB.agentes.Tienda;
import simulaSAAB.comunicacion.Dinero;
import simulaSAAB.comunicacion.OrdenDePedido;
import simulaSAAB.comunicacion.Proposito;
import simulaSAAB.comunicacion.Recurso;
import simulaSAAB.contextos.SaabContextBuilder;
import simulaSAAB.global.VariablesGlobales;
import simulaSAAB.global.persistencia.AgentTrackObservable;
import simulaSAAB.global.persistencia.MPAConfigurado;
/**
 * Representa la compra de productos por parte de un consumidor final en una tienda
 * @author lfgomezm
 *
 */
public class ComprarEnTienda implements SistemaActividadHumana<Consumidor> {
	/**
	 * Identificador de la transacción
	 */
	private final Integer id;
	/**
	 * Enunciado de la tarea
	 */
	private final String Enunciado;
	/**
	 * Propósito de la tarea
	 */
	private final Proposito proposito;
	
	private final Double CostoEjecucion;
	/**
	 * Rango de búsqueda de tiendas para un agente
	 */
	private Double distanciaBusqueda;
	
	private Moverse moverse;
	/**
	 * Objetos de tipo tienda identificados dentro del rango de búsqueda
	 */
	private IndexedIterable<Object> tiendasCercanas;
	/**
	 * Tienda seleccionada para realizar la compra
	 */
	private Tienda destino;
	/**
	 * Punto de ubicación origen del agente
	 */
	private Coordinate origen;
	/**
	 * Estado actual de la tarea
	 */
	private String Estado;
	/**
	 * Paso actual de la tarea
	 */
	private int paso;

	public CompraTrack OBSERVABLE;
	
	/**
	 * Constructor
	 */
	public ComprarEnTienda() {
		
		MPAConfigurado mpa 	=new MPAConfigurado("ComprarEnTienda");
		
		this.id				= mpa.getId();
		this.Enunciado		= mpa.getEnunciado();
		this.proposito 		= mpa.getProposito();
		this.CostoEjecucion	= mpa.getCosto();
		
		distanciaBusqueda= new Double(VariablesGlobales.DISTANCIA_BUSQUEDA_NUTRIRED_ANGULAR);
		
		this.Estado = EstadosActividad.READY.toString();
		OBSERVABLE 	= new CompraTrack();
	}

	@Override
	public synchronized void secuenciaPrincipalDeAcciones(Consumidor actor) {
		
		if(this.Estado.equalsIgnoreCase(EstadosActividad.READY.toString())){						
									
			Envelope actorEnvelope 	= SaabContextBuilder.SAABGeography.getGeometry(actor).getEnvelopeInternal();
			actorEnvelope.expandBy(distanciaBusqueda);			
			tiendasCercanas = SaabContextBuilder.BogotaContext.getObjects(Tienda.class);
			
			Estado	=EstadosActividad.RUNNING.toString();
			paso	=1;			
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.RUNNING.toString())){
			
			switch(paso){			
			case 1:	
				
				/*
				 * Escoge la primera tienda del iterador
				 * 
				 * TODO Actuar como agente inteligente escogiendo la tienda que mayores beneficios le ofrezca.
				 */
				int index = RandomHelper.nextIntFromTo(0, tiendasCercanas.size()-1);
								
				Tienda t 			= (Tienda)tiendasCercanas.get(index);					
				Coordinate destino 	= t.getGeometria().getCoordinate();
				moverse 			= new Moverse(destino);					
				this.destino 		= t;
				
				Geometry geom		= SaabContextBuilder.SAABGeography.getGeometry(actor);
				this.origen	 		= new Coordinate(geom.getCoordinate().x, geom.getCoordinate().y);
				paso++;
					
				
				
			break;			
			case 2:				
				moverse.secuenciaPrincipalDeAcciones(actor);
				
				if(moverse.getEstado().equalsIgnoreCase(EstadosActividad.DONE.toString()))
					paso++;
				
				
			break;			
			case 3: 
				//TODO Actuar como agente inteligente escogiendo los productos que necesita
				
				String producto = "Cebolla de Bulbo";
				/*
				 * La cantidad de Kilos a comprar
				 * TODO la cantidad es irreal para un consumidor promedio.
				 * Se desproporciona, para acelerar el proceso de recuperacion econÃ³mica del agente Vendedor Final
				 */
				Double cantidad = RandomHelper.nextDoubleFromTo(5000, 15000);
				Dinero pago		= actor.getDinero();
				
				Recurso compra 	= this.destino.venderProducto(producto, cantidad, pago);
				//Actualiza el observador				
				OBSERVABLE.compraEfectuada(producto, cantidad, compra);
				paso++;		
						
			break;
			case 4:
							
				Coordinate c = SaabContextBuilder.SAABGeography.getGeometry(actor).getCoordinate();
				c.x = origen.x;
				c.y = origen.y;
				
				paso++;
			break;
			default:								
				this.Estado =EstadosActividad.DONE.toString();
			}			
		}
		else if(this.Estado.equalsIgnoreCase(EstadosActividad.DONE.toString())){			
			//fija estado del actor en IDLE
			actor.setEstado("IDLE"); 				
		}
		
		
	}

	@Override
	public SistemaActividadHumana getInstance() {
		
		return new ComprarEnTienda();
	}

	@Override
	public int getPaso() {
		
		return paso;
	}

	@Override
	public String getEstado() {
		
		return Estado;
	}

	@Override
	public String getEnunciado() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Proposito getProposito() {
		// TODO Auto-generated method stub
		return null;
	}
		
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
	 * de la clase <code>ComprarEnTienda</code>
	 * @author lfgomezm
	 *
	 */
	public class CompraTrack extends AgentTrackObservable{
		
		private String tendero;
		private String Tienda;
		private Double tick;		
		private String vendedorID;
		private String producto;
		private Double precioUnitario;
		private Double cantidad;
		
		
		/**
		 * Constructor
		 */
		public CompraTrack(){
			super();						
		}
		
		/**
		 * Actualiza los valores y reporta a los observadores
		 * 
		 * @param producto nombre del producto comprado
		 * @param precioUnitario precio unitario del producto en la tienda
		 * @param cantidad double, cantidad comprada
		 */
		public void compraEfectuada(String producto, double cantidad, Recurso compra){
			
			this.tick			= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.tendero		= ComprarEnTienda.this.destino.getPropietario().toString();
			this.Tienda 		= ComprarEnTienda.this.destino.toString();
			this.vendedorID		= ComprarEnTienda.this.destino.getPropietario().toString();
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
			
			return tick.toString()+separador+tendero+separador+Tienda.toString()+separador+vendedorID+separador+producto+separador+precioUnitario.toString()+separador+cantidad.toString()+separador;
		}

		@Override
		public String dataLineStringHeader(String separador) {
			
			return "Tick"+separador+"Tendero"+separador+"Tienda"+separador+"Vendedor_ID"+separador+"Producto"+separador+"Precio_Unitario"+separador+"cantidad"+separador;
		}
		
		
	}

}