package simulaSAAB.comunicacion;


import repast.simphony.engine.environment.RunEnvironment;
import simulaSAAB.comunicacion.OrdenDeServicio.OrdenServicioTrack;
import simulaSAAB.contextos.CentroUrbano;
import simulaSAAB.global.persistencia.AgentTrackObservable;

/**
 * Representa el concepto de <code>orden de compra</code> usado en los registros del SISAAB y en la ontología para comunicación entre los agentes 
 * <p>
 * Un pedido unitario genera tantas órdenes de compra como puntos de oferta tenga el pedido
 * 
 * @author jdvelezg
 *
 */
public class OrdenDeCompra implements Concepto {
	/**
	 * agrega funcionalidad <code>observable</code> a la clase
	 */
	public final OrdenCompraTrack OBSERVABLE = new OrdenCompraTrack(); 
	
	private int id;
	
	private Oferta Oferta;
	
	private Demanda Demanda;
	
	protected Dinero PagoAcordado;
	
	private CentroUrbano PuntoDeOferta;
	
	/**
	 * Constructor
	 */
	public OrdenDeCompra() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor
	 * @param offer Oferta ligada a la orden de compra
	 * @param demand Demanda ligada a la orden de compra
	 */
	public OrdenDeCompra(Oferta offer, Demanda demand) {
		
		
		this.Oferta 		= offer;
		this.Demanda		= demand;
		this.PagoAcordado	= new Dinero(offer.getPrecio());
		this.PuntoDeOferta	= offer.getPuntoOferta();		
	}
	/**
	 * Devuelve la oferta ligada a la orden de compra
	 * @return
	 */
	public Oferta getOferta() {
		return Oferta;
	}
	/**
	 * Asigna la oferta ligada a la orden de compra
	 * @param oferta
	 */
	public void setOferta(Oferta oferta) {
		Oferta = oferta;
	}
	/**
	 * Devuelve la demanda ligada a la orden de compra
	 * @return
	 */
	public Demanda getDemanda() {
		return Demanda;
	}
	/**
	 * Asigna la demanda ligada a la orden de compra
	 * @param demanda demanda ligada a la orden de compra
	 */
	public void setDemanda(Demanda demanda) {
		Demanda = demanda;
	}
	/**
	 * Devuelve el detalle ligado a la orden de compra
	 * @return Recurso
	 */
	public Recurso getDetalleCompra() {
		return Oferta.getProductos();
	}
	/**
	 * Devuelve el pago acordado ligado a la orden de compra
	 * @return Dinero
	 */
	public Dinero getPagoAcordado() {
		return PagoAcordado;
	}
	
	/**
	 * Agrega el valor logistico al valor del pago acordado
	 * @param costoLogistico double valor del costo logistico
	 */
	public void addCostoLogistico(double costoLogistico) {
		PagoAcordado.addCantidad(costoLogistico);
		this.OBSERVABLE.pagoAcordado();
	}
	/**
	 * Devuelve el punto de oferta ligado a la orden de compra
	 * @return CentroUrbano 
	 */
	public CentroUrbano getPuntoDeOferta() {
		return Oferta.getPuntoOferta();
	}
	/**
	 * Devuelve el identificador de la orden	
	 * @return id int
	 */
	public int getId() {
		return id;
	}
	/**
	 * Asigna un identificador a la orden
	 * @param id int
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Clase anidada a la que se le delega la funcionalidad <code>obserbable</code> de la clase <code>Orden de compra</code>
	 * 
	 * @author jdvelezg
	 *
	 */
	public class OrdenCompraTrack extends AgentTrackObservable{		
		
		private String ordenCompraID;
		private String ofertaID;
		private String demandaID;
		private Double tick;
		private String puntoOferta;
		private String producto;
		private Double pago;
		private Double cantidad;
		
		public OrdenCompraTrack(){			
			super();			
		}
		
		/**
		 * Actualiza los valores y reporta a los observadores de la clase
		 * <p>
		 * Se invoca en generarOrdenDeCompra de la clase SISaaB
		 */
		public void pagoAcordado(){
			
			this.tick			= RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			this.ordenCompraID	= OrdenDeCompra.this.toString();
			this.ofertaID		= OrdenDeCompra.this.Oferta.toString();
			this.demandaID		= OrdenDeCompra.this.Demanda.toString();
			this.puntoOferta	= OrdenDeCompra.this.PuntoDeOferta.getNombre();
			this.producto		= OrdenDeCompra.this.getDetalleCompra().getProducto().getNombre();
			this.cantidad		= OrdenDeCompra.this.getDetalleCompra().getCantidad();
			this.pago			= OrdenDeCompra.this.getPagoAcordado().getCantidad();	
			
			super.setChanged();
			super.notifyObservers(this);
		}
		
		@Override
		public String dataLineString(String separador) {
			
			return tick.toString()+separador+ordenCompraID+separador+ofertaID+separador+demandaID+separador+puntoOferta+separador+producto+separador+cantidad.toString()+separador+pago.toString()+separador;
		}
		@Override
		public String dataLineStringHeader(String separador) {
			
			return "tick"+separador+"Orden_Compra_id"+separador+"oferta_ID"+separador+"demanda_ID"+separador+"punto_Oferta"+separador+"producto"+separador+"cantidad"+separador+"pago"+separador;
		}


		public String getOfertaID() {
			return ofertaID;
		}


		public void setOfertaID(String ofertaID) {
			this.ofertaID = ofertaID;
		}


		public String getDemandaID() {
			return demandaID;
		}


		public void setDemandaID(String demandaID) {
			this.demandaID = demandaID;
		}


		public Double getTick() {
			return tick;
		}


		public void setTick(Double tick) {
			this.tick = tick;
		}


		public String getPuntoOferta() {
			return puntoOferta;
		}


		public void setPuntoOferta(String puntoOferta) {
			this.puntoOferta = puntoOferta;
		}


		public String getProducto() {
			return producto;
		}


		public void setProducto(String producto) {
			this.producto = producto;
		}


		public Double getPago() {
			return pago;
		}


		public void setPago(Double pago) {
			this.pago = pago;
		}


		public Double getCantidad() {
			return cantidad;
		}


		public void setCantidad(Double cantidad) {
			this.cantidad = cantidad;
		}		
		
	}

}
